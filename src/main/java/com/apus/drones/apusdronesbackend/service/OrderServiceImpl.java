package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.config.CustomUserDetails;
import com.apus.drones.apusdronesbackend.mapper.OrderDTOMapper;
import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.OrderItemEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.*;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;
import com.apus.drones.apusdronesbackend.service.dto.OrderItemDto;
import com.apus.drones.apusdronesbackend.service.dto.UpdateCartDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final BigDecimal defaultDeliveryPrice;
    private final Double weightLimitGrams;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductRepository productRepository;

    private final AddressRepository addressRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            UserRepository userRepository, ProductRepository productRepository,
                            @Value("${application.vars.default-delivery-price}") String deliveryPrice,
                            @Value("${application.vars.grams-weight-limit}") String weightLimit,
                            AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;

        this.defaultDeliveryPrice = new BigDecimal(deliveryPrice);
        this.weightLimitGrams = Double.parseDouble(weightLimit);
    }

    @Override
    public List<OrderDTO> getByCustomerId(OrderStatus status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
            var ignoredStatuses = List.of(OrderStatus.IN_CART);
            List<OrderEntity> orders = status == null
                ? orderRepository.findAllByCustomer_IdAndStatusIsNotIn(details.getUserID(),
                ignoredStatuses)
                : orderRepository.findAllByCustomer_IdAndStatus(details.getUserID(), status);

            for (OrderEntity o : orders) {
                List<OrderItemEntity> items = orderItemRepository.findAllByOrder_Id(o.getId());
                o.setOrderItems(items);
            }
            return orders.stream().map(OrderDTOMapper::fromOrderEntity).collect(Collectors.toList());
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usu??rio n??o autenticado.");
        }
    }

    public List<OrderDTO> findAllByPartnerIdAndFilterByStatus(OrderStatus status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
            List<OrderEntity> orders;
            if (status == null) { //sem filtro
                orders = orderRepository.findAllByPartner_Id(details.getUserID()).stream()
                    .filter(it -> it.getStatus() != OrderStatus.IN_CART)
                    .collect(Collectors.toList());
            } else if (status == OrderStatus.WAITING_FOR_PARTNER) { //Se esta esperando por parceiro aceitar
                orders = checkForExpiredOrder(details.getUserID());
            } else {
                orders = orderRepository.findAllByPartner_IdAndStatus(details.getUserID(), status); // com filtro
            }

            for (OrderEntity o : orders) {
                List<OrderItemEntity> items = orderItemRepository.findAllByOrder_Id(o.getId());
                o.setOrderItems(items);
            }

            return orders.stream().map(OrderDTOMapper::fromOrderEntity).collect(Collectors.toList());
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usu??rio n??o autenticado.");
        }
    }

    @Override
    public List<OrderDTO> findAllWaitingForPilot() {
        List<OrderEntity> orders = orderRepository.findAllByStatus(OrderStatus.WAITING_FOR_PILOT);
        return orders.stream().map(OrderDTOMapper::fromOrderEntity).collect(Collectors.toList());
    }

    @Override
    public OrderDTO update(OrderDTO orderDto) {
        double totalWeight = this.calcTotalWeight(orderDto.getItems());
        checkTotalWeight(totalWeight);

        OrderEntity savedEntity = this.updateOrder(orderDto);
        List<OrderItemEntity> updatedItems = this.updateItems(orderDto.getItems(), savedEntity.getId());
        List<OrderItemEntity> savedItems = this.saveUpdatedItems(updatedItems);

        savedEntity.getOrderItems().clear();
        savedEntity.getOrderItems().addAll(savedItems);

        if (savedEntity.getStatus().equals(OrderStatus.IN_CART) && savedEntity.getOrderItems().isEmpty()) {
            orderRepository.delete(savedEntity);
            return null;
        }

        return OrderDTOMapper.fromOrderEntity(savedEntity);
    }

    @Override
    public OrderDTO getById(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Pedido n??o encontrado"));
        List<OrderItemEntity> items = orderItemRepository.findAllByOrder_Id(orderId);
        order.setOrderItems(items);

        return OrderDTOMapper.fromOrderEntity(order);
    }

    @Override
    public void addToCart(UpdateCartDTO updateCartDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();

        if (auth.isAuthenticated()) {
            if (details.getRole() != Role.CUSTOMER) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "O usu??rio n??o possui privil??gios para adicionar itens ao carrinho.");
            }
            OrderDTO cart = this.getByCustomerId(OrderStatus.IN_CART).stream().findFirst().orElse(null);
            if (!Objects.isNull(cart)) {
                cart.getItems().addAll(updateCartDTO.getItems());
                this.update(cart);
            } else {
                this.createCart(updateCartDTO, details.getUserID());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usu??rio n??o autenticado.");
        }
    }

    @Override
    public OrderDTO getCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();

        if (auth.isAuthenticated()) {
            if (details.getRole() != Role.CUSTOMER) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "O usu??rio n??o possui privil??gios para visualizar os itens de um "
                        + "carrinho.");
            }
            return this.getByCustomerId(OrderStatus.IN_CART).stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrinho n??o encontrado"));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usu??rio n??o autenticado.");
        }
    }

    @Override
    public OrderDTO createCart(UpdateCartDTO updateCartDTO, Long customerId) {
        double totalWeight = this.calcTotalWeight(updateCartDTO.getItems());
        checkTotalWeight(totalWeight);

        OrderEntity savedEntity = this.createCartOrder(updateCartDTO, customerId);
        List<OrderItemEntity> updatedItems = this.updateItems(updateCartDTO.getItems(), savedEntity.getId());
        List<OrderItemEntity> savedItems = this.saveUpdatedItems(updatedItems);

        savedEntity.getOrderItems().clear();
        savedEntity.getOrderItems().addAll(savedItems);

        return OrderDTOMapper.fromOrderEntity(savedEntity);
    }

    private void checkTotalWeight(double totalWeight) {
        if (totalWeight > weightLimitGrams) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Falha ao adicionar item. O carrinho passou do limite de peso de "
                    + weightLimitGrams + "g.");
        }
    }

    private double calcTotalWeight(List<OrderItemDto> items) {
        double sum = 0;

        for (OrderItemDto item : items) {
            sum += item.getProduct().getWeight() * item.getQuantity();
        }

        return sum;
    }

    private OrderEntity createCartOrder(UpdateCartDTO updateCartDTO, Long customerId) {
        UserEntity customer = userRepository.getById(customerId);

        UserEntity partner = userRepository.getById(updateCartDTO.getPartner().getId());

        OrderEntity entity = OrderEntity.builder()
            .deliveryPrice(defaultDeliveryPrice)
            .orderPrice(this.calcOrderPrice(updateCartDTO.getItems(), defaultDeliveryPrice))
            .status(OrderStatus.IN_CART)
            .customer(customer)
            .partner(partner)
            .deliveryAddress(addressRepository.findAllByUser_Id(customer.getId()).get(0))
            .shopAddress(addressRepository.findAllByUser_Id(partner.getId()).get(0))
            .createdAt(LocalDateTime.now())
            .build();

        return orderRepository.save(entity);
    }

    private OrderEntity updateOrder(OrderDTO orderDto) {
        UserEntity customer = Optional.ofNullable(orderDto.getCustomer())
            .map(user -> userRepository.getById(user.getId()))
            .orElseGet(() -> userRepository.findAllByRole(Role.CUSTOMER).get(0));

        UserEntity partner = userRepository.getById(orderDto.getPartner().getId());

        OrderEntity order = orderDto.getId() != null ? orderRepository.getById(orderDto.getId()) : null;

        OrderEntity entity = OrderEntity.builder()
            .id(orderDto.getId())
            .deliveryPrice(defaultDeliveryPrice)
            .orderPrice(this.calcOrderPrice(orderDto.getItems(), defaultDeliveryPrice))
            .status(
                orderDto.getStatus() == null ? OrderStatus.IN_CART : orderDto.getStatus())
            .customer(customer)
            .partner(partner)
            .deliveryAddress(addressRepository.findAllByUser_Id(customer.getId()).get(0))
            .shopAddress(addressRepository.findAllByUser_Id(partner.getId()).get(0))
            .createdAt(orderDto.getCreatedAt() == null ? LocalDateTime.now() :
                orderDto.getCreatedAt())
            .build();

        return orderRepository.save(entity);
    }

    private List<OrderItemEntity> updateItems(List<OrderItemDto> items, Long orderId) {
        items.stream()
            .filter(item -> item.getQuantity() < 1)
            .forEach(item -> orderItemRepository.deleteById(item.getId()));

        List<OrderItemEntity> itemsEntities = items.stream()
            .filter(item -> item.getQuantity() >= 1)
            .map(item -> {
                Double orderItemWeight =
                    item.getProduct().getWeight() * item.getQuantity();
                BigDecimal orderItemPrice = item.getProduct().getPrice().multiply(
                    BigDecimal.valueOf(item.getQuantity()));
                OrderItemEntity existingItem = orderItemRepository
                    .findOneByOrder_IdAndProduct_Id(orderId,
                        item.getProduct().getId())
                    .stream().findFirst().orElse(null);
                if (existingItem != null && item.getId() == 0) {
                    existingItem.setQuantity(
                        existingItem.getQuantity() + item.getQuantity());
                    orderItemRepository.save(existingItem);
                    return existingItem;
                } else if (existingItem != null) {
                    existingItem.setQuantity(item.getQuantity());
                    return existingItem;
                }

                return OrderItemEntity.builder()
                    .id(item.getId())
                    .order(OrderEntity.builder().id(orderId)
                        .build())
                    .product(ProductEntity.builder()
                        .id(item.getProduct()
                            .getId())
                        .build())
                    .quantity(item.getQuantity())
                    .price(orderItemPrice)
                    .weight(orderItemWeight)
                    .build();
            })
            .collect(Collectors.toList());

        return itemsEntities;
    }

    private List<OrderItemEntity> saveUpdatedItems(List<OrderItemEntity> items) {
        return orderItemRepository.saveAllAndFlush(items).stream()
            .peek(item -> item.setProduct(productRepository.getById(item.getProduct().getId())))
            .collect(Collectors.toList());
    }

    private BigDecimal calcOrderPrice(List<OrderItemDto> items, BigDecimal deliveryPrice) {
        BigDecimal sum = BigDecimal.ZERO;

        for (OrderItemDto item : items) {
            sum = sum.add(item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        sum = sum.add(deliveryPrice);

        return sum;
    }

    private List<OrderEntity> checkForExpiredOrder(Long userId) {
        var ordersResult = orderRepository.findAllByPartner_IdAndStatus(userId, OrderStatus.WAITING_FOR_PARTNER);
        var waitingOrderResult = new ArrayList<OrderEntity>();

        for (OrderEntity order : ordersResult) {
            if (LocalDateTime.now().isAfter(order.getExpiresAt())) {
                order.setStatus(OrderStatus.REFUSED);
                orderRepository.save(order);
            } else {
                waitingOrderResult.add(order);
            }
        }

        return waitingOrderResult;
    }
}

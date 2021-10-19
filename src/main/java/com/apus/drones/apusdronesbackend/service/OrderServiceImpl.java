package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.mapper.OrderDTOMapper;
import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.OrderItemEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.OrderItemRepository;
import com.apus.drones.apusdronesbackend.repository.OrderRepository;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;
import com.apus.drones.apusdronesbackend.service.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            UserRepository userRepository, ProductRepository productRepository,
                            @Value("${application.vars.default-delivery-price}") String deliveryPrice,
                            @Value("${application.vars.grams-weight-limit}") String weightLimit) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.defaultDeliveryPrice = new BigDecimal(deliveryPrice);
        this.weightLimitGrams = Double.parseDouble(weightLimit);
    }

    @Override
    public void addToCart(Long userId, OrderDTO orderDTO) {
        OrderDTO cart = this.getByCustomerId(userId, OrderStatus.IN_CART).stream().findFirst().orElse(null);
        if (!Objects.isNull(cart)) {
            cart.getItems().addAll(orderDTO.getItems());
            this.update(cart);
        } else {
            this.update(orderDTO);
        }
    }

    @Override
    public List<OrderDTO> getByCustomerId(Long userId, OrderStatus status) {
        var ignoredStatuses = List.of(OrderStatus.IN_CART);
        List<OrderEntity> orders = status == null
                ? orderRepository.findAllByCustomer_IdAndStatusIsNotIn(userId, ignoredStatuses)
                : orderRepository.findAllByCustomer_IdAndStatus(userId, status);

        for (OrderEntity o : orders) {
            List<OrderItemEntity> items = orderItemRepository.findAllByOrder_Id(o.getId());
            o.setOrderItems(items);
        }
        return orders.stream().map(OrderDTOMapper::fromOrderEntity).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getById(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        List<OrderItemEntity> items = orderItemRepository.findAllByOrder_Id(orderId);
        order.setOrderItems(items);

        return OrderDTOMapper.fromOrderEntity(order);
    }

    @Override
    public OrderDTO update(OrderDTO orderDto) {
        double totalWeight = this.calcTotalWeight(orderDto.getItems());

        if (totalWeight > weightLimitGrams) {
            String message = "Falha ao adicionar item. O carrinho passou do limite de peso de "
                    + weightLimitGrams + "g.";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        } else {
            OrderEntity savedEntity = this.updateOrder(orderDto);
            List<OrderItemEntity> savedItems = this.updateItems(orderDto.getItems(), savedEntity.getId());
            savedEntity.setOrderItems(savedItems);

            return OrderDTOMapper.fromOrderEntity(savedEntity);
        }
    }

    private double calcTotalWeight(List<OrderItemDto> items) {
        double sum = 0;

        for (OrderItemDto item : items) {
            sum += item.getProduct().getWeight() * item.getQuantity();
        }

        return sum;
    }

    private OrderEntity updateOrder(OrderDTO orderDto) {
        UserEntity customer = Optional.ofNullable(orderDto.getCustomer())
                .map(user -> userRepository.getById(user.getId()))
                .orElseGet(() -> userRepository.findAllByRole(Role.CUSTOMER).get(0));

        UserEntity partner = userRepository.getById(orderDto.getPartner().getId());

        OrderEntity entity = OrderEntity.builder()
                .id(orderDto.getId())
                .deliveryPrice(defaultDeliveryPrice)
                .orderPrice(this.calcOrderPrice(orderDto.getItems()))
                .status(orderDto.getStatus() == null ? OrderStatus.IN_CART : orderDto.getStatus())
                .customer(customer)
                .partner(partner)
                .deliveryAddress(orderDto.getDeliveryAddress())
                .shopAddress(orderDto.getShopAddress())
                .createdAt(orderDto.getCreatedAt() == null ? LocalDateTime.now() : orderDto.getCreatedAt())
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
                    Double orderItemWeight = item.getProduct().getWeight() * item.getQuantity();
                    BigDecimal orderItemPrice = item.getProduct()
                            .getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

                    return OrderItemEntity.builder()
                            .id(item.getId())
                            .order(OrderEntity.builder().id(orderId).build())
                            .product(ProductEntity.builder().id(item.getProduct().getId()).build())
                            .quantity(item.getQuantity())
                            .price(orderItemPrice)
                            .weight(orderItemWeight)
                            .build();
                })
                .collect(Collectors.toList());

        return orderItemRepository.saveAllAndFlush(itemsEntities).stream()
                .peek(item -> item.setProduct(productRepository.getById(item.getProduct().getId())))
                .collect(Collectors.toList());
    }

    private BigDecimal calcOrderPrice(List<OrderItemDto> items) {
        BigDecimal sum = BigDecimal.ZERO;

        for (OrderItemDto item : items) {
            sum = sum.add(item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return sum;
    }

    public List<OrderDTO> findAllByPartnerIdAndFilterByStatus(Long userId, OrderStatus status) {

        List<OrderEntity> orders;
        if (status == null) { //sem filtro
            orders = orderRepository.findAllByPartner_Id(userId).stream()
                    .filter(it -> it.getStatus() != OrderStatus.IN_CART)
                    .collect(Collectors.toList());
        } else if (status == OrderStatus.WAITING_FOR_PARTNER) { //Se esta esperando por parceiro aceitar
            orders = checkForExpiredOrder(userId);
        } else {
            orders = orderRepository.findAllByPartner_IdAndStatus(userId, status); // com filtro
        }

        for (OrderEntity o : orders) {
            List<OrderItemEntity> items = orderItemRepository.findAllByOrder_Id(o.getId());
            o.setOrderItems(items);
        }

        return orders.stream().map(OrderDTOMapper::fromOrderEntity).collect(Collectors.toList());
    }

    private List<OrderEntity> checkForExpiredOrder(Long userId) {
        var ordersResult = orderRepository.findAllByPartner_IdAndStatus(userId,
                OrderStatus.WAITING_FOR_PARTNER);
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

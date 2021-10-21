package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.config.CustomUserDetails;
import com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper;
import com.apus.drones.apusdronesbackend.mapper.ProductDtoMapper;
import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.ProductImageRepository;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreateProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.apus.drones.apusdronesbackend.mapper.ProductDtoMapper.fromProductEntityList;

@Transactional
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final PartnerService partnerService;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository, PartnerService partnerService, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.partnerService = partnerService;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Void> create(CreateProductDTO productDTO) {
        // TODO obter o parceiro da autenticação
        // partnerService.get may throw a ResponseStatusException
        this.partnerService.get(productDTO.getPartner());

        UserEntity partner = UserEntity.builder().id(productDTO.getPartner()).build();

        ProductEntity entity = ProductEntity.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .status(ProductStatus.ACTIVE)
                .weight(productDTO.getWeight())
                .quantity(productDTO.getQuantity())
                .deleted(Boolean.FALSE)
                .createDate(LocalDateTime.now())
                .user(partner)
                .build();

        Long generatedId = productRepository.save(entity).getId();
        entity.setId(generatedId);

        List<ProductImage> productImages = productDTO.getImagesUrls().stream().map(
                url -> ProductImage.builder().url(url).isMain(false).product(entity).build()
        ).collect(Collectors.toList());

        if (!productImages.isEmpty())
            productImages.get(0).setIsMain(true);

        productImageRepository.saveAll(productImages);

        log.info("Saved new product entity with id [{}]", generatedId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ProductDTO get(Long id) {
        return productRepository.findByIdAndDeletedFalse(id).map(ProductDtoMapper::fromProductEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não foi possível encontrar o produto com ID " + id));
    }

    @Override
    public List<ProductDTO> findAllActiveProductsByUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
            var resultFromDB = productRepository.findAllByUserIdAndStatusAndDeletedFalse(details.getUserID(), ProductStatus.ACTIVE);
            return fromProductEntityList(resultFromDB);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }

    }

    @Override
    public ResponseEntity<Void> update(Long id, ProductDTO productDTO) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não foi possível encontrar o produto com ID " + id));

        updateProduct(productDTO, entity);

        productRepository.save(entity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não foi possível encontrar o produto com ID " + id));

        entity.setDeleted(Boolean.TRUE);

        productRepository.save(entity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void updateProduct(ProductDTO productDTO, ProductEntity entity) {
        if (productDTO.getDescription() != null)
            entity.setDescription(productDTO.getDescription());

        if (productDTO.getPrice() != null)
            entity.setPrice(productDTO.getPrice());

        if (productDTO.getStatus() != null)
            entity.setStatus(productDTO.getStatus());

        if (productDTO.getWeight() != null)
            entity.setWeight(productDTO.getWeight());

        if (productDTO.getQuantity() != null)
            entity.setQuantity(productDTO.getQuantity());

        if (productDTO.getImagesUrls() != null) {
            List<ProductImage> productImages = productDTO.getImagesUrls().stream().map(
                    url -> {
                        ProductImage productImage = productImageRepository
                                .findProductImageByProductAndUrl(entity, url);
                        return productImage != null
                                ? productImage
                                : ProductImage.builder()
                                    .url(url)
                                    .isMain(false)
                                    .product(entity)
                                    .build();
                    }
            ).collect(Collectors.toList());

            if (!productImages.isEmpty())
                productImages.get(0).setIsMain(true);

            productImageRepository.deleteAllByProduct(entity);
            entity.setProductImages(productImages);
        }
    }
}

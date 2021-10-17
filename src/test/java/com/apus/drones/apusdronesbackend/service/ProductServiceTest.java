package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.repository.ProductImageRepository;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreateProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper.fromUserEntity;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductImageRepository productImageRepository;
    @Mock
    private PartnerServiceImpl partnerService;

    @Test
    public void testCreateProduct() {
        UserEntity partner = UserEntity.builder().name("Parceiro").build();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        ProductImage productImage = new ProductImage();
        productImage.setId(1L);

        CreateProductDTO productDTO = CreateProductDTO.builder()
                .name("Produto teste")
                .description("Descrição teste")
                .price(new BigDecimal(1))
                .status(ProductStatus.ACTIVE)
                .weight(100.0)
                .imagesUrls(List.of("url"))
                .quantity(1)
                .partner(1L)
                .build();

        when(productRepository.save(Mockito.any())).thenReturn(productEntity);
        when(productImageRepository.saveAll(Mockito.any())).thenReturn(List.of(productImage));
        var result = productService.create(productDTO);

        assertThat(result).isNotNull();
    }

    @Test
    public void testFindProductById() {
        var date = LocalDateTime.now();
        UserEntity user = UserEntity.builder().id(1L).name("asdkfask").avatarUrl("www.www.www").build();
        ProductImage productImage = ProductImage.builder().isMain(true).url("www.www.www").build();
        ProductEntity entity = ProductEntity.builder()
                .user(user)
                .name("Produto test")
                .price(new BigDecimal(1))
                .status(ProductStatus.ACTIVE)
                .productImages(List.of(productImage))
                .createDate(date)
                .quantity(1)
                .weight(100.0)
                .deleted(Boolean.FALSE)
                .build();

        entity.setId(12345L);

        when(productRepository.findByIdAndDeletedFalse(Mockito.any())).thenReturn(Optional.of(entity));

        var result = productService.get(12345L);

        var expected = ProductDTO.builder()
                .id(12345L)
                .partner(fromUserEntity(user))
                .name("Produto test")
                .price(new BigDecimal(1))
                .status(ProductStatus.ACTIVE)
                .quantity(1)
                .createdAt(date)
                .imageUrl("www.www.www")
                .imagesUrls(List.of("www.www.www"))
                .weight(100.0)
                .deleted(Boolean.FALSE)
                .build();

        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void testUpdateProduct() {
        Long id = 12345l;
        ProductEntity entity = new ProductEntity("Produto test", new BigDecimal(1), ProductStatus.ACTIVE, 5);
        ProductDTO productDTO = ProductDTO.builder()
                .name("Produto test")
                .price(new BigDecimal(1))
                .status(ProductStatus.ACTIVE)
                .weight(1.0)
                .quantity(1)
                .build();

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(entity));

        var result = productService.update(id, productDTO);

        assertThat(result).isNotNull();
    }

    @Test
    public void testDeleteProduct() {
        ProductEntity entity = new ProductEntity("Produto test", new BigDecimal(1), ProductStatus.ACTIVE, 5);
        entity.setId(12345L);

        when(productRepository.findByIdAndDeletedFalse(Mockito.any())).thenReturn(Optional.of(entity));

        var result = productService.delete(12345L);

        assertThat(result).isNotNull();
    }
}

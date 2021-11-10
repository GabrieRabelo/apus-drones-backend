package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.config.CustomUserDetails;
import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.repository.ProductImageRepository;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreateProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.FileDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper.fromUserEntity;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
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
    private UserRepository userRepository;
    @Mock
    private ImageUploadService imageUploadService;

    @Test
    public void testCreateProduct() throws SizeLimitExceededException {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(new CustomUserDetails("user", "pass", Collections.emptyList(), 1L, any()));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UserEntity partner = UserEntity.builder().name("Parceiro").build();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        ProductImage productImage = new ProductImage();
        productImage.setId(1L);
        List<FileDTO> images = List.of(FileDTO.builder().fileName("file.png").base64("base64").mainFile(true).build());

        CreateProductDTO productDTO = CreateProductDTO.builder()
                .name("Produto teste")
                .description("Descrição teste")
                .price(new BigDecimal(1))
                .status(ProductStatus.ACTIVE)
                .weight(100.0)
                .quantity(1)
                .partner(1L)
                .files(images)
                .build();

        when(productRepository.save(Mockito.any())).thenReturn(productEntity);
        when(productImageRepository.saveAll(Mockito.any())).thenReturn(List.of(productImage));
        when(imageUploadService.upload(Mockito.any())).thenReturn("imageUrl");
        when(userRepository.findAllByIdAndRole(Mockito.any(), Mockito.any())).thenReturn(Optional.of(partner));

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
//    @WithMockUser(roles = "PARTNER")
    public void testUpdateProduct() {
        Long id = 12345l;
        ProductEntity entity = ProductEntity.builder()
                .name("Produto test")
                .price(new BigDecimal(1))
                .weight(5.0)
                .status(ProductStatus.ACTIVE)
                .quantity(1)
                .productImages(Collections.emptyList())
                .build();
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

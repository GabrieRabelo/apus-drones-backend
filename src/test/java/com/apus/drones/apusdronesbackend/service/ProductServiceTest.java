package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.config.CustomUserDetails;
import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.ProductImageRepository;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreateProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.FileDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.UpdateProductDTO;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper.fromUserEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

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
        when(authentication.getPrincipal()).thenReturn(
            new CustomUserDetails("user", "pass", Collections.emptyList(), 1L, Role.PARTNER));
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
        ProductImage productImage = ProductImage.builder().id(1L).isMain(true).url("www.www.www").build();
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
        Long id = 12345L;
        UserEntity user = UserEntity.builder().id(1L).name("asdkfask").avatarUrl("www.www.www").build();

        ProductEntity entity = ProductEntity.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .weight(5.0)
            .status(ProductStatus.ACTIVE)
            .quantity(1)
            .productImages(Collections.emptyList())
            .user(user)
            .build();

        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .status(ProductStatus.ACTIVE)
            .weight(1.0)
            .quantity(1)
            .build();

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(entity));

        var result = productService.update(id, updateProductDTO);

        assertThat(result).isNotNull();
    }

    @Test
    public void testUpdateProductMainImage() {
        Long id = 12345L;
        String newMainImageUrl = "url2";
        UserEntity user = UserEntity.builder().id(1L).name("asdkfask").avatarUrl("www.www.www").build();

        ProductEntity entity = ProductEntity.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .weight(5.0)
            .status(ProductStatus.ACTIVE)
            .quantity(1)
            .productImages(
                List.of(
                    ProductImage.builder().id(1L).isMain(true).url("url").build(),
                    ProductImage.builder().id(2L).isMain(false).url("url2").build()
                )
            )
            .user(user)
            .build();

        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .status(ProductStatus.ACTIVE)
            .weight(1.0)
            .quantity(1)
            .mainImageUrl(newMainImageUrl)
            .build();

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(entity));

        var result = productService.update(id, updateProductDTO);

        assertThat(result.getImageUrl().equals(newMainImageUrl));
    }

    @Test
    public void testUpdateProductMainImageWithLocalImage() throws SizeLimitExceededException {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(
            new CustomUserDetails("user", "pass", Collections.emptyList(), 1L, Role.PARTNER));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long id = 12345L;
        UserEntity user = UserEntity.builder().id(1L).name("asdkfask").avatarUrl("www.www.www").build();

        List<FileDTO> newFiles = List.of(
            FileDTO.builder().fileName("f1.img").base64("base64").mainFile(true).build(),
            FileDTO.builder().fileName("f2.img").base64("base64").mainFile(false).build()
        );

        ProductEntity entity = ProductEntity.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .weight(5.0)
            .status(ProductStatus.ACTIVE)
            .quantity(1)
            .productImages(
                Stream.of(
                    ProductImage.builder().id(1L).isMain(true).url("url").build()
                ).collect(Collectors.toList())
            )
            .user(user)
            .build();

        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .status(ProductStatus.ACTIVE)
            .weight(1.0)
            .quantity(1)
            .files(newFiles)
            .build();

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(entity));
        when(imageUploadService.upload(newFiles.get(0))).thenReturn("url3");
        when(imageUploadService.upload(newFiles.get(1))).thenReturn("url4");

        var result = productService.update(id, updateProductDTO);

        assertThat(result.getImageUrl().equals("url3"));
    }

    @Test
    public void testDeleteProductMainImage() {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(
            new CustomUserDetails("user", "pass", Collections.emptyList(), 1L, Role.PARTNER));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long id = 12345L;
        UserEntity user = UserEntity.builder().id(1L).name("asdkfask").avatarUrl("www.www.www").build();

        ProductEntity entity = ProductEntity.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .weight(5.0)
            .status(ProductStatus.ACTIVE)
            .quantity(1)
            .productImages(
                Stream.of(
                    ProductImage.builder().id(1L).isMain(true).url("url").build(),
                    ProductImage.builder().id(2L).isMain(false).url("url2").build()
                ).collect(Collectors.toList())
            )
            .user(user)
            .build();

        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .status(ProductStatus.ACTIVE)
            .weight(1.0)
            .quantity(1)
            .removedImagesUrls(List.of("url"))
            .build();

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(entity));

        var result = productService.update(id, updateProductDTO);

        assertThat(result.getImageUrl().equals("url2"));
    }

    @Test
    public void testUpdateProductWithTwoMainImages() {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(
            new CustomUserDetails("user", "pass", Collections.emptyList(), 1L, Role.PARTNER));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long id = 12345L;
        UserEntity user = UserEntity.builder().id(1L).name("asdkfask").avatarUrl("www.www.www").build();

        ProductEntity entity = ProductEntity.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .weight(5.0)
            .status(ProductStatus.ACTIVE)
            .quantity(1)
            .productImages(
                Stream.of(
                    ProductImage.builder().id(1L).isMain(true).url("url").build(),
                    ProductImage.builder().id(2L).isMain(false).url("url2").build()
                ).collect(Collectors.toList())
            )
            .user(user)
            .build();

        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .status(ProductStatus.ACTIVE)
            .weight(1.0)
            .quantity(1)
            .files(List.of(
                FileDTO.builder().fileName("f1.img").base64("base64")
                    .mainFile(true).build(),
                FileDTO.builder().fileName("f2.img").base64("base64")
                    .mainFile(true).build()
            ))
            .build();

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> productService.update(id, updateProductDTO))
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testUpdateProductWithMainImageLocalAndMainImageUrl() {
        Long id = 12345L;
        UserEntity user = UserEntity.builder().id(1L).name("asdkfask").avatarUrl("www.www.www").build();

        ProductEntity entity = ProductEntity.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .weight(5.0)
            .status(ProductStatus.ACTIVE)
            .quantity(1)
            .productImages(
                Stream.of(
                    ProductImage.builder().id(1L).isMain(true).url("url").build(),
                    ProductImage.builder().id(2L).isMain(false).url("url2").build()
                ).collect(Collectors.toList())
            )
            .user(user)
            .build();

        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
            .name("Produto test")
            .price(new BigDecimal(1))
            .status(ProductStatus.ACTIVE)
            .weight(1.0)
            .quantity(1)
            .files(List.of(
                FileDTO.builder().fileName("f1.img").base64("base64")
                    .mainFile(true).build(),
                FileDTO.builder().fileName("f2.img").base64("base64")
                    .mainFile(false).build()
            ))
            .mainImageUrl("url")
            .build();

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> productService.update(id, updateProductDTO))
            .isInstanceOf(ResponseStatusException.class)
            .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST);
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

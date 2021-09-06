package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.request.product.CreateProductRequest;
import com.apus.drones.apusdronesbackend.model.request.product.UpdateProductRequest;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;

    @Test
    public void testCreateProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Produto test");
        request.setPrice(new BigDecimal(1));
        request.setStatus(ProductStatus.ACTIVE);
        request.setWeight(5);

        ProductEntity entity = new ProductEntity();
        entity.setId(1L);

        when(productRepository.save(Mockito.any())).thenReturn(entity);

        var result = productService.create(request);

        assertThat(result).isNotNull();
    }

    @Test
    public void testFindProductById() {
        ProductEntity entity = new ProductEntity("Produto test", new BigDecimal(1), ProductStatus.ACTIVE, 5);

        entity.setId(12345L);

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(entity));

        var result = productService.get(12345L);

        assertThat(result.getBody()).isEqualToComparingFieldByFieldRecursively(entity);
    }

    @Test
    public void testUpdateProduct() {
        ProductEntity entity = new ProductEntity("Produto test", new BigDecimal(1), ProductStatus.ACTIVE, 5);
        entity.setId(12345L);

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Update novo");

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(entity));

        var result = productService.update(request);

        assertThat(result).isNotNull();
    }

    @Test
    public void testDeleteProduct() {
        ProductEntity entity = new ProductEntity("Produto test", new BigDecimal(1), ProductStatus.ACTIVE, 5);
        entity.setId(12345L);

        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(entity));

        var result = productService.delete(12345L);

        assertThat(result).isNotNull();
    }
}

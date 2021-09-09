package com.apus.drones.apusdronesbackend.service.converter;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductConverter {

    public List<ProductDTO> toDTO(List<ProductEntity> entity) {
        var response = new ArrayList<ProductDTO>();

        for (ProductEntity product : entity) {

            var url = "https://nayemdevs.com/wp-content/uploads/2020/03/default-product-image.png";
            var optionalImage = product.getProductImages()
                    .stream()
                    .filter(ProductImage::isMain)
                    .findFirst();

            if (optionalImage.isPresent()) {
                url = optionalImage.get().getUrl();
            }

            var dto = ProductDTO.builder()
                    .name(product.getName())
                    .price(product.getPrice())
                    .partnerName(product.getUser().getName())
                    .imageUrl(url)
                    .build();
            response.add(dto);

        }
        return response;
    }
}

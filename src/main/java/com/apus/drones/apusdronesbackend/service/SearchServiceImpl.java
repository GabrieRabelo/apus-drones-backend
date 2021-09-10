package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.converter.PartnerConverter;
import com.apus.drones.apusdronesbackend.service.converter.ProductConverter;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.SearchResultDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final UserRepository userRepository;
    private final PartnerConverter partnerConverter;

    public SearchServiceImpl(ProductRepository productRepository,
                             ProductConverter productConverter,
                             UserRepository userRepository,
                             PartnerConverter partnerConverter) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
        this.userRepository = userRepository;
        this.partnerConverter = partnerConverter;
    }

    @Override
    public SearchResultDTO searchProductsAndPartnersByName(String name) {
        var products = findAllActiveProductsByName(name);
        var partners = findAllPartnersByName(name);
        return SearchResultDTO.builder()
                .partners(partners)
                .products(products)
                .build();
    }

    private List<PartnerDTO> findAllPartnersByName(String name) {

        var resultFromDB = userRepository.findAllByRoleAndNameContainingIgnoreCase(Role.PARTNER, name);
        return partnerConverter.toDTOList(resultFromDB);
    }

    private List<ProductDTO> findAllActiveProductsByName(String name) {

        var resultFromDB = productRepository.findAllByNameContainingIgnoreCaseAndStatus(name, ProductStatus.ACTIVE);
        return productConverter.toDTO(resultFromDB);
    }


}

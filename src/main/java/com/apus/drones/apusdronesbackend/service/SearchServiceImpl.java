package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.SearchResultDTO;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper.fromUserEntityList;
import static com.apus.drones.apusdronesbackend.mapper.ProductDtoMapper.fromProductEntityList;

@Service
public class SearchServiceImpl implements SearchService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public SearchServiceImpl(ProductRepository productRepository,
                             UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
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

    private List<ProductDTO> findAllActiveProductsByName(String name) {

        var resultFromDB = productRepository.findAllByNameContainingIgnoreCaseAndStatus(name, ProductStatus.ACTIVE);
        return fromProductEntityList(resultFromDB);
    }

    private List<PartnerDTO> findAllPartnersByName(String name) {

        var resultFromDB = userRepository.findAllByRoleAndNameContainingIgnoreCase(Role.PARTNER, name);
        return fromUserEntityList(resultFromDB);
    }


}

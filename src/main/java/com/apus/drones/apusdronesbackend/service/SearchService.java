package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.service.dto.SearchResultDTO;

public interface SearchService {

    SearchResultDTO searchProductsAndPartnersByName(String name);
}


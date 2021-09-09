package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.SearchService;
import com.apus.drones.apusdronesbackend.service.dto.SearchResultDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
public class SearchAPI {

    private final SearchService searchService;

    public SearchAPI(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/{name}")
    public SearchResultDTO search(@PathVariable String name) {
        return searchService.searchProductsAndPartnersByName(name);
    }
}

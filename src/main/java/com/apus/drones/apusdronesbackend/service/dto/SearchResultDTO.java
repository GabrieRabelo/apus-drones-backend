package com.apus.drones.apusdronesbackend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDTO {

    private List<ProductDTO> products;
    private List<PartnerDTO> partners;
}

package com.apus.drones.apusdronesbackend.service.dto;

import lombok.*;

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

package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.PartnerServiceImpl;
import com.apus.drones.apusdronesbackend.service.ProductService;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartnerAPI.class)
class PartnerAPITest {

    @Autowired
    private MockMvc client;
    @MockBean
    private ProductService productService;
    @MockBean
    private PartnerServiceImpl partnerService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetPartners() throws Exception {

        var partner = PartnerDTO.builder().id(1L).build();

        when(partnerService.findAllPartners()).thenReturn(List.of(partner));

        var resultString = client.perform(get("/api/v1/partners"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var resultPartner = objectMapper.readValue(resultString, PartnerDTO[].class)[0];

        var expectedPartner = PartnerDTO.builder().id(1L).build();

        assertThat(resultPartner).isEqualToComparingFieldByFieldRecursively(expectedPartner);

        verify(partnerService).findAllPartners();
    }
}

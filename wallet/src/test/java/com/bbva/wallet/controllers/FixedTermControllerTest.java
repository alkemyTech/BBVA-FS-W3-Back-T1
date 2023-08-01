package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.FixedTermCreateRequestDTO;
import com.bbva.wallet.dtos.FixedTermSimulateResponseDTO;
import com.bbva.wallet.services.FixedTermService;
import com.bbva.wallet.utils.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FixedTermControllerTest {

    private MockMvc mockMvc;

    //InjectMock crea la instancia de la clase a probar.
    @InjectMocks
    private FixedTermController fixedTermController;

    //Con mock, instancio las dependencias de la clase a probar.
    @Mock
    private FixedTermService fixedTermService;

    @Before
    public void setUp() {
        initMocks(this);

        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(fixedTermController).build();
    }

    @Test
    public void FixedTermControler_SimulateFixedTerm_ReturnCreated() throws Exception {
        //Simulo lo que devuelve el servicio.

        Timestamp fechaCreacion = new Timestamp(System.currentTimeMillis());
        Timestamp fechaFinalizacion = new Timestamp(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        Double montoInvertido = 1000.0;
        Double interes = 50.0;
        Double montoTotal = montoInvertido + interes;

        FixedTermSimulateResponseDTO responseDTO =
                new FixedTermSimulateResponseDTO(fechaCreacion, fechaFinalizacion, montoInvertido, interes, montoTotal);

        given(fixedTermService.simulateFixedTerm(ArgumentMatchers.any())).willReturn(responseDTO);

        // Realizar la solicitud a través de MockMvc

        mockMvc.perform(post("/fixedTerm/simulate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 2000, \"cantDias\": 30 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fechaCreacion").value(fechaCreacion.getTime()))
                .andExpect(jsonPath("$.data.fechaFinalizacion").value(fechaFinalizacion.getTime()))
                .andExpect(jsonPath("$.data.montoInverito").value(montoInvertido))
                .andExpect(jsonPath("$.data.interes").value(interes))
                .andExpect(jsonPath("$.data.montoTotal").value(montoTotal));
    }

}
package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.FixedTermCreateRequestDTO;
import com.bbva.wallet.dtos.FixedTermSimulateResponseDTO;
import com.bbva.wallet.services.FixedTermService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FixedTermControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FixedTermService fixedTermService;

    @InjectMocks
    private FixedTermController fixedTermController;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        initMocks(this);

        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(fixedTermController).build();
    }

    @Test
    public void FixedTermControler_SimulateFixedTerm_ReturnCreated() throws Exception {
        // Simular el comportamiento del servicio
//        FixedTermCreateRequestDTO requestDTO = new FixedTermCreateRequestDTO(100D, 30);
//
//        Timestamp fechaCreacion = new Timestamp(System.currentTimeMillis());
//        Timestamp fechaFinalizacion = new Timestamp(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)); // Agregar 30 días al tiempo actual
//        Double montoInvertido = 1000.0;
//        Double interes = 50.0;
//        Double montoTotal = montoInvertido + interes;
//
//        FixedTermSimulateResponseDTO responseDTO =
//                new FixedTermSimulateResponseDTO(fechaCreacion, fechaFinalizacion, montoInvertido, interes, montoTotal);
//
//        given(fixedTermService.simulateFixedTerm(ArgumentMatchers.any())).willReturn(responseDTO);

        // Realizar la solicitud a través de MockMvc
        ResultActions response = mockMvc.perform(post("/fixedTerm/simulate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 2000, \"cantDias\": 30 }"));

        // Verificar el resultado esperado
        response.andExpect(status().isOk());
    }

    @Test
    public void FixedTermControler_SimulateFixedTerm_Return400() throws Exception {

        // Realizar la solicitud a través de MockMvc
        ResultActions response = mockMvc.perform(post("/fixedTerm/simulate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"amount\": 2000, \"cantDias\": 29 }"));

        // Verificar el resultado esperado
        response.andExpect(status().isBadRequest());
    }
}
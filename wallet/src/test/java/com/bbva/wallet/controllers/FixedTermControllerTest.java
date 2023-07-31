package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.FixedTermCreateRequestDTO;
import com.bbva.wallet.services.FixedTermService;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FixedTermControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FixedTermService fixedTermService;

    @Test
    @WithAnonymousUser
    public void testSimulateFixedTerm() throws Exception {
        FixedTermCreateRequestDTO fixedTermCreateRequestDTO =
                new FixedTermCreateRequestDTO(100D, 30);

        mockMvc.perform(post("http://localhost:8080/fixedTerm/simulate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"amount\": 2000, \"cantDias\": 2000 }"))
                .andExpect(status().isForbidden());
    }
}

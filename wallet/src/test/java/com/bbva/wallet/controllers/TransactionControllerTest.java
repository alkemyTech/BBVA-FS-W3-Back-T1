package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.TransactionSendMoneyRequestDTO;
import com.bbva.wallet.services.TransactionService;
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
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;


    @Test
    @WithAnonymousUser
    public void testSendPesosUnauthenticatedUser() throws Exception {

        TransactionSendMoneyRequestDTO transactionDto =
                new TransactionSendMoneyRequestDTO(1L,10.0);


        mockMvc.perform(post("/sendArs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 18, \"amount\": 2000 }"))
                .andExpect(status().isForbidden());
    }
}

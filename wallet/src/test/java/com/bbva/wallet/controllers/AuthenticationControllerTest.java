package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.JwtAuthenticationResponse;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.ErrorCodes;
import com.bbva.wallet.exceptions.ExceptionUserNotFound;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.services.AuthenticationService;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.utils.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bbva.wallet.dtos.SingInRequestDTO;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    @Test
    public void testSignIn_status200() throws Exception {
        JwtAuthenticationResponse mockResponse = new JwtAuthenticationResponse("mockJwtToken",new User());
        when(authenticationService.singIn(any(SingInRequestDTO.class))).thenReturn(mockResponse);

        // Create a SignInRequestDTO instance with your desired data
        SingInRequestDTO signInRequestDTO = new SingInRequestDTO("username@example.com", "password");

        // Perform the POST request to the /login endpoint with the SignInRequestDTO as the request body
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signInRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").value(mockResponse.token()));
    }

    @Test
    public void testSignIn_status400_mal_formato_email() throws Exception {

        // Create a SignInRequestDTO instance with your desired data
        SingInRequestDTO signInRequestDTO = new SingInRequestDTO("username", "password");

        // Perform the POST request to the /login endpoint with the SignInRequestDTO as the request body
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signInRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("VALOR_INVALIDO"));
    }

    @Test
    public void testSignIn_status400_user_not_found() throws Exception {

        when(authenticationService.singIn(any(SingInRequestDTO.class))).thenThrow(new ExceptionUserNotFound());

        // Create a SignInRequestDTO instance with your desired data
        SingInRequestDTO signInRequestDTO = new SingInRequestDTO("username@example", "password");

        // Perform the POST request to the /login endpoint with the SignInRequestDTO as the request body
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signInRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value("USUARIO_NO_ENCONTRADO"));
    }


    // Utility method to convert objects to JSON string
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}

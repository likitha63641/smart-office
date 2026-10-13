package com.smartoffice.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class IssueControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void login_shouldReturnToken_whenAdminCredentialsAreValid() throws Exception {
        String body = "{\"email\":\"admin@test.local\",\"password\":\"Admin@12345\"}";
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.token").exists()).andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void login_shouldRejectInvalidCredentials() throws Exception {
        String body = "{\"email\":\"admin@test.local\",\"password\":\"wrongPassword\"}";
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_shouldRejectUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get("/issues")).andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_shouldRejectInvalidToken() throws Exception {
        mockMvc.perform(get("/issues").header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }
}

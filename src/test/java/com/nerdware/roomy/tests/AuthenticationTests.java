package com.nerdware.roomy.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdware.roomy.domain.entities.UserRole;
import com.nerdware.roomy.features.auth.dtos.requests.LoginDto;
import com.nerdware.roomy.features.auth.dtos.responses.TokenDto;
import com.nerdware.roomy.features.auth.services.JwtService;
import com.nerdware.roomy.framework.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthenticationTests extends IntegrationTestBase
{
    private static String Api = "/api/auth/login";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Test
    void CorrectCredentials() throws Exception
    {
        var request = new LoginDto("default_admin@gmail.com", "abcd123");
        var result = client.perform(post(Api)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn();

        var responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDto.class);
        assertNotNull(responseDto.AccessToken());
    }

    @Test
    void EncodedClaimsAreCorrect() throws Exception
    {
        var request = new LoginDto("default_admin@gmail.com", "abcd123");
        var result = client.perform(post(Api)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn();

        var responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDto.class);
        assertNotNull(responseDto.AccessToken());

        var claims = jwtService.extractAllClaims(responseDto.AccessToken());

        assertTrue(claims.containsKey("role"));
        var role = claims.get("role", String.class);
        var subject = claims.getSubject();

        assertEquals(UserRole.Admin, UserRole.valueOf(role));
        assertEquals("default_admin@gmail.com", subject);
    }

    @Test
    void WrongCredentials() throws Exception {
        var request = new LoginDto("unknown_user@gmail.com", "abcd123");
        var result = client.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is4xxClientError())
            .andReturn();

        var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        assertEquals(401, response.getStatus());
        assertEquals("Unauthorized", response.getTitle());
        assertEquals("Bad credentials", response.getDetail());
    }
}
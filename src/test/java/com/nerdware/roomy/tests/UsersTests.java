package com.nerdware.roomy.tests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdware.roomy.domain.entities.User;
import com.nerdware.roomy.domain.entities.UserRole;
import com.nerdware.roomy.features.users.dtos.requests.CreateUserDto;
import com.nerdware.roomy.features.users.dtos.responses.UserDto;
import com.nerdware.roomy.features.users.repositories.UserRepository;
import com.nerdware.roomy.framework.IntegrationTestBase;
import com.nerdware.roomy.framework.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCustomUser()
class UsersTests extends IntegrationTestBase
{
    private static String Api = "/api/users";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void CreateUser_AdminCanCreate() throws Exception
    {
        var request = new CreateUserDto(
            "Other employee",
            "other.employee@gmail",
            "abcd123",
            UserRole.Employee);

        var result = client.perform(post(Api)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn();

        var responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);

        assertEquals(UserRole.Employee, responseDto.role());
        assertEquals("other.employee@gmail", responseDto.email());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    void CreateUser_InvalidEmailAddress(String email) throws Exception
    {
        var request = new CreateUserDto(
            "Other employee",
            email,
            "abcd123",
            UserRole.Employee);

        var result = client.perform(post(Api)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is4xxClientError())
            .andReturn();

        var responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        assertEquals(400, responseDto.getStatus());
        assertEquals("Bad Request", responseDto.getTitle());
    }

    @Test
    void CreateUser_EmailAddressAlreadyTaken() throws Exception
    {
        var request = new CreateUserDto(
            "Other employee",
            "default_admin@gmail.com",
            "abcd123",
            UserRole.Admin);

        var result = client.perform(post(Api)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is4xxClientError())
            .andReturn();

        var responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        assertEquals(400, responseDto.getStatus());
        assertEquals("Bad Request", responseDto.getTitle());
        assertEquals("Email already exists", responseDto.getDetail());
    }

    @Test
    @WithMockCustomUser(email = "normal.employee@gmail.com")
    void CreateUser_EmployeeCanNotCreate() throws Exception
    {
        var request = new CreateUserDto(
            "Other employee",
            "other.employee@gmail",
            "abcd123",
            UserRole.Employee);

        var result = client.perform(post(Api)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is4xxClientError())
            .andReturn();

        var responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        assertEquals("Unauthorized", responseDto.getTitle());
        assertEquals(401, responseDto.getStatus());
        assertEquals("You do not have permission to add user", responseDto.getDetail());
    }

    @Test
    void GetUserById_UserDoesNotExist() throws Exception
    {

        var result = client.perform(get(Api + "/5"))
            .andExpect(status().is4xxClientError())
            .andReturn();

        var responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        assertEquals("Not Found", responseDto.getTitle());
        assertEquals(404, responseDto.getStatus());
    }

    @Test
    void GetUserById_UserExist() throws Exception
    {

        var result = client.perform(get(Api + "/1"))
            .andExpect(status().isOk())
            .andReturn();

        var responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals("default_admin@gmail.com", responseDto.email());
        assertEquals(UserRole.Admin, responseDto.role());
    }

    @Test
    void DeleteUser_AdminCanDeleteOtherUser() throws Exception
    {
        var otherUser = new User();
        otherUser.setRole(UserRole.Employee);
        otherUser.setEmail("other.employee@gmail.com");
        otherUser.setFullName("other.employee");
        otherUser.setPassword("password");

        otherUser = userRepository.save(otherUser);

        var result = client.perform(delete(Api + "/" + otherUser.getId().toString()))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @WithMockCustomUser(email = "normal.employee@gmail.com")
    void DeleteUser_EmployeeCanNotDeleteOtherUser() throws Exception
    {
        var otherUser = new User();
        otherUser.setRole(UserRole.Employee);
        otherUser.setEmail("other.employee@gmail.com");
        otherUser.setFullName("other.employee");
        otherUser.setPassword("password");

        otherUser = userRepository.save(otherUser);

        var result = client.perform(delete(Api + "/" + otherUser.getId().toString()))
            .andExpect(status().is4xxClientError())
            .andReturn();

        var responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        assertEquals("Unauthorized", responseDto.getTitle());
        assertEquals(401, responseDto.getStatus());
        assertEquals("You do not have permission to delete a user", responseDto.getDetail());
    }

    @Test
    void DeleteUser_UserDoesNotExist() throws Exception
    {
        var result = client.perform(delete(Api + "/5"))
            .andExpect(status().is4xxClientError())
            .andReturn();

        var responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        assertEquals("Not Found", responseDto.getTitle());
        assertEquals(404, responseDto.getStatus());
    }

}
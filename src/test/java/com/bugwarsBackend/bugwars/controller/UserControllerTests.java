package com.bugwarsBackend.bugwars.controller;//package com.bugwarsBackend.bugwars.controller;
//import com.bugwarsBackend.bugwars.controller.UserController;
//import com.bugwarsBackend.bugwars.model.User;
//import com.bugwarsBackend.bugwars.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//public class UserControllerTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @Test
//    public void getAllUsers_ReturnsListOfUsers() throws Exception {
//        // mocking the behavior of the userService
//        when(userService.getAllUsers()).thenReturn(List.of(
//                new User("john.doe", "john.doe@test.com","password123"),
//                new User("jane.doe", "jane.doe@test.com","password456")
//        ));
//
//        // performing a GET request and verifying the response
//        mockMvc.perform(get("/users/all"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.length()").value(2)) // Assuming your JSON response is an array
//                .andExpect(jsonPath("$[0].username").value("john.doe"))
//                .andExpect(jsonPath("$[1].username").value("jane.doe"));
//    }
//
//    @Test
//    public void getUserById_UserExists_ReturnsUser() throws Exception {
//        // mocking the behavior of the userService
//        when(userService.getUserById(1L)).thenReturn(Optional.of(new User("john.doe", "john.doe@test.com","password123")));
//
//        // performing a GET request and verifying the response
//        mockMvc.perform(get("/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.username").value("john.doe"));
//    }
//}
//
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        // Populate users as needed

        when(userService.getAllUsers()).thenReturn(users);

        List<User> result = userController.getAllUsers();

        assertEquals(users, result);
    }

    @Test
    void testGetUserById() {
        Long id = 1L;
        Optional<User> user = Optional.of(new User());
        // Populate user as needed

        when(userService.getUserById(id)).thenReturn(user);

        Optional<User> result = userController.getUserById(id);

        assertEquals(user, result);
    }

//    @Test
//    void testGetUserByIdNotFound() {
//        Long id = 1L;
//
//        when(userService.getUserById(id)).thenReturn(Optional.empty());
//
//        assertThrows(ResponseStatusException.class, () -> {
//            userController.getUserById(id);
//        });
//    }

    @Test
    void testCreateUser() {
        User user = new User();
        // Populate user as needed

        when(userService.createUser(user)).thenReturn(user);

        User result = userController.createUser(user);

        assertEquals(user, result);
    }

    @Test
    void testUpdateUser() {
        Long id = 1L;
        User user = new User();
        // Populate user as needed

        when(userService.updateUser(id, user)).thenReturn(user);

        User result = userController.updateUser(id, user);

        assertEquals(user, result);
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;

        userController.deleteUser(id);

        verify(userService, times(1)).deleteUser(id);
    }
}

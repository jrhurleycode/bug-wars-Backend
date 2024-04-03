package com.bugwarsBackend.bugwars.controller;

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

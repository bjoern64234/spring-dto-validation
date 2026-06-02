package org.example.springdtovalidation.service;

import lombok.Value;
import org.example.springdtovalidation.model.User;
import org.example.springdtovalidation.model.UserDTO;
import org.example.springdtovalidation.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepo mockUserRepo = Mockito.mock(UserRepo.class);
    private final IdService mockIdService = Mockito.mock(IdService.class);
    private UserService userService;
    private UserDTO userDTO;
    private User newUser;

    @BeforeEach
    void setUp() {
        // Given
        this.userService = new UserService(mockUserRepo, mockIdService);
        this.userDTO = UserDTO.builder().build()
                .withUsername("mustermann")
                .withAge(33)
                .withEmail("musterman@mail.de")
                .withPassword("cdkslwofddfsfs1A");
        this.newUser = User.builder().build()
                .withId("1")
                .withUsername(this.userDTO.username())
                .withAge(this.userDTO.age())
                .withEmail(this.userDTO.email())
                .withPassword(this.userDTO.password());
    }

    @Test
    void saveUser_shouldSaveUser_byGivenValidUserDTO() {
        // Given
        String id = "1";
        when(this.mockIdService.generateId()).thenReturn(id);
        when(this.mockUserRepo.save(this.newUser)).thenReturn(this.newUser);
        // When
        UserDTO actual = this.userService.saveUser(this.userDTO);
        // Then
        assertEquals(this.userDTO, actual);
        verify(this.mockUserRepo).save(newUser);
        verify(this.mockIdService).generateId();
    }

    @Test
    void getUserByUsername_shouldReturnUser_byGivenUsername() {
        // Given
        when(this.mockUserRepo.getUserByUsername(this.newUser.username())).thenReturn(this.userDTO);
        // When
        UserDTO actual = this.userService.getUserByUsername(this.newUser.username());
        // Then
        assertEquals(this.userDTO, actual);
        verify(this.mockUserRepo).getUserByUsername(this.newUser.username());
    }

    @Test
    void getUsersByAge_shouldReturnUsers_byGivenAge() {
        // Given
        User newUser2 = User.builder().build()
                .withId("2")
                .withUsername(this.userDTO.username())
                .withAge(this.userDTO.age())
                .withEmail(this.userDTO.email())
                .withPassword(this.userDTO.password());
        User newUser3 = User.builder().build()
                .withId("3")
                .withUsername(this.userDTO.username())
                .withAge(this.userDTO.age())
                .withEmail(this.userDTO.email())
                .withPassword(this.userDTO.password());
        List<User> repoResult = List.of(this.newUser, newUser2, newUser3);
        when(this.mockUserRepo.getUsersByAge(this.newUser.age())).thenReturn(repoResult);
        // When
        List<UserDTO> actual = this.userService.getUsersByAge(this.newUser.age());
        // Then
        assertEquals(3, actual.size());
        assertEquals(this.newUser.age(), actual.getFirst().age());
        verify(this.mockUserRepo).getUsersByAge(this.newUser.age());
    }

    @Test
    void getUserByPassword_shouldReturnUser_byGivenPassword() {
        // Given
        when(this.mockUserRepo.getUserByPassword(this.newUser.password())).thenReturn(this.newUser);
        // When
        UserDTO actual = this.userService.getUserByPassword(this.newUser.password());
        // Then
        assertEquals(this.userDTO, actual);
        verify(this.mockUserRepo).getUserByPassword(this.newUser.password());
    }
}
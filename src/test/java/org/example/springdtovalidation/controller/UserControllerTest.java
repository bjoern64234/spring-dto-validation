package org.example.springdtovalidation.controller;

import org.example.springdtovalidation.model.User;
import org.example.springdtovalidation.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Test
    void create_shouldReturnCreated() throws Exception {
        this.mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content("""
                    {
                      "username": "mustermann",
                      "age": 37,
                      "email": "mustermann@email.de",
                      "password": "cdkslwofddfsfs1A"
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("mustermann"))
                .andExpect(jsonPath("$.age").value(37))
                .andExpect(jsonPath("$.email").value("mustermann@email.de"))
                .andExpect(jsonPath("$.password").value("cdkslwofddfsfs1A"));
    }

    @Test
    void create_shouldReturnException_whenPayloadIsInvalid() throws Exception {
        this.mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content("""
                    {
                      "username": "",
                      "age": 5,
                      "email": "mustermannemail.de",
                      "password": "cdkslwofddfsfs"
                    }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(jsonPath("$.username").value("The username can not be blank."))
                .andExpect(jsonPath("$.age").value("You must be oder than 15"))
                .andExpect(jsonPath("$.email").value("The email must be valid."))
                .andExpect(jsonPath("$.password").value("The password must be valid."));;
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {
        User user = User.builder().build().withId("1").withUsername("mustermann").withAge(37).withEmail("mustermann@email.de").withPassword("cdkslwofddfsfs1A");
        this.userRepo.save(user);
        this.mockMvc.perform(get("/api/users?name=" + user.username()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                  {
                      "username": "mustermann",
                      "age": 37,
                      "email": "mustermann@email.de",
                      "password": "cdkslwofddfsfs1A"
                  }
                """));
    }

    @Test
    void getUser_shouldReturnException_whenNameIsInvalid() throws Exception {
        User user = User.builder().build().withId("1").withUsername("mustermann").withAge(37).withEmail("mustermann@email.de").withPassword("cdkslwofddfsfs1A");
        this.userRepo.save(user);
        this.mockMvc.perform(get("/api/users?name="))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                  {
                      "name": "The requested name can not be blank."
                  }
                """));
    }

    @Test
    void getUsersByAge() throws Exception {
        User user1 = User.builder().build().withId("1").withUsername("mustermann").withAge(37).withEmail("mustermann@email.de").withPassword("cdkslwofddfsfs1A");
        User user2 = User.builder().build().withId("2").withUsername("musterfrau").withAge(37).withEmail("musterfrau@email.de").withPassword("cdkslwofddfsfs1A");
        User user3 = User.builder().build().withId("3").withUsername("mueller").withAge(37).withEmail("mueller@email.de").withPassword("cdkslwofddfsfs1A");
        this.userRepo.save(user1);
        this.userRepo.save(user2);
        this.userRepo.save(user3);

        this.mockMvc.perform(get("/api/users/age?age=" + user1.age()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                  [
                      {
                          "username": "mustermann",
                          "age": 37,
                          "email": "mustermann@email.de",
                          "password": "cdkslwofddfsfs1A"
                      },
                      {
                          "username": "musterfrau",
                          "age": 37,
                          "email": "musterfrau@email.de",
                          "password": "cdkslwofddfsfs1A"
                      },
                      {
                          "username": "mueller",
                          "age": 37,
                          "email": "mueller@email.de",
                          "password": "cdkslwofddfsfs1A"
                      }
                  ]
                """));
    }

    @Test
    void getUserByPassword_shouldReturnUser() throws Exception {
        User user = User.builder().build().withId("1").withUsername("mustermann").withAge(37).withEmail("mustermann@email.de").withPassword("cdkslwofddfsfs1A");
        this.userRepo.save(user);
        this.mockMvc.perform(get("/api/users/pass?password=" + user.password()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                  {
                      "username": "mustermann",
                      "age": 37,
                      "email": "mustermann@email.de",
                      "password": "cdkslwofddfsfs1A"
                  }
                """));
    }

    @Test
    void getUserByPassword_shouldReturnException_whenPasswordIsInvalid() throws Exception {
        User user = User.builder().build().withId("1").withUsername("mustermann").withAge(37).withEmail("mustermann@email.de").withPassword("cdkslwofddfsfs1A");
        this.userRepo.save(user);
        this.mockMvc.perform(get("/api/users/pass?password=cdkslwofddfsfs1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                  {
                    "password": "The given password is invalid."
                  }
                """));
    }

    @Test
    void getUserByPassword_shouldReturnException_whenPasswordIsLessThen8Characters() throws Exception {
        User user = User.builder().build().withId("1").withUsername("mustermann").withAge(37).withEmail("mustermann@email.de").withPassword("cdkslwofddfsfs1A");
        this.userRepo.save(user);
        this.mockMvc.perform(get("/api/users/pass?password=45"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                  {
                    "password": "The password must be at least 8 character long."
                  }
                """));
    }
}
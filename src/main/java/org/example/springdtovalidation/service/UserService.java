package org.example.springdtovalidation.service;

import org.example.springdtovalidation.model.User;
import org.example.springdtovalidation.model.UserDTO;
import org.example.springdtovalidation.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final IdService idService;

    public UserService(UserRepo userRepo, IdService idService) {
        this.userRepo = userRepo;
        this.idService = idService;
    }

    public UserDTO saveUser(UserDTO userDTO){
        User newUser = User.builder().build()
                .withId(this.idService.generateId())
                .withUsername(userDTO.username())
                .withAge(userDTO.age())
                .withEmail(userDTO.email())
                .withPassword(userDTO.password());

        this.userRepo.save(newUser);

        return userDTO;
    }

    public UserDTO getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }

    public List<UserDTO> getUsersByAge(int age) {
        List<User> users = this.userRepo.getUsersByAge(age);
        List<UserDTO> responseUsers = new ArrayList<>();

        users.forEach(user -> {
            responseUsers.add(UserDTO.builder().build().withUsername(user.username()).withAge(user.age()).withEmail(user.email()).withPassword(user.password()));
        });

        return responseUsers;
    }

    public UserDTO getUserByPassword(String password) {
        User user = this.userRepo.getUserByPassword(password);
        return UserDTO.builder().build().withUsername(user.username()).withAge(user.age()).withEmail(user.email()).withPassword(user.password());
    }
}

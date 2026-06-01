package org.example.springdtovalidation.repository;

import org.example.springdtovalidation.model.User;
import org.example.springdtovalidation.model.UserDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    UserDTO getUserByUsername(String username);

    List<User> getUsersByAge(int age);

    User getUserByPassword(String password);
}

package org.example.springdtovalidation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.springdtovalidation.model.UserDTO;
import org.example.springdtovalidation.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody @Valid UserDTO userDTO) {
        return this.userService.saveUser(userDTO);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@RequestParam @NotBlank(message = "The requested name can not be blank.") String name) {
        return this.userService.getUserByUsername(name);
    }

    @GetMapping("/age")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUsersByAge(@RequestParam @Min(value = 8, message = "You must be at least 18 year old") int age) {
        return this.userService.getUsersByAge(age);
    }

    @GetMapping("/pass")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserByPassword(@RequestParam @NotBlank(message = "The requested name can not be blank.") @Size(min = 8, message = "The password must be at least 8 character long.") @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).+$", message = "The given password is invalid.") String password) {
        return this.userService.getUserByPassword(password);
    }
}

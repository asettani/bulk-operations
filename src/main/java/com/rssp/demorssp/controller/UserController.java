package com.rssp.demorssp.controller;

import com.rssp.demorssp.models.UserDocument;
import com.rssp.demorssp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Flux<UserDocument> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public Mono<UserDocument> insertUser(@RequestBody @NonNull UserDocument user) {
        return userService.addUser(user);
    }

    @DeleteMapping
    public Mono<Void> deleteUser(@RequestParam @NonNull String email) {
        return userService.deleteUser(email);
    }

    @PostMapping("/insert/many")
    public Flux<UserDocument> insertManyUsers(@RequestBody @NonNull Set<UserDocument> users) {
        return userService.saveAll(users);
    }

    @PostMapping("/delete/many")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteManyUsers(@RequestBody @NonNull Set<String> emails) {
        return userService.deleteMany(emails);
    }
}

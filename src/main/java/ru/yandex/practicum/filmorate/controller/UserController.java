package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmsAndUsersService.UserService;
import ru.yandex.practicum.filmorate.service.storage.InMemoryUserStorage;

import java.util.Collection;


@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        return inMemoryUserStorage.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addToFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public User deleteFromFriendList(@PathVariable int id, @PathVariable int friendId) {
        return userService.deleteFriendFromUser(id, friendId);
    }
}

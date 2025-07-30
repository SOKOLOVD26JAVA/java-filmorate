package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    protected int id = 1;
    protected Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity.ok(users.values());
    }

    @PostMapping
    public ResponseEntity<String> addUser(@Valid @RequestBody User user) {
        user.setId(generateID());
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Данный пользователь уже добавлен");
        }
        userValidation(user);
        users.put(user.getId(), user);
        return ResponseEntity.ok("Пользователь " + user.getName() + " успешно добавлен");
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@Valid @RequestBody User newUser) {
        newUser.setId(generateID());
        userValidation(newUser);
        if (users.containsKey(newUser.getId())) {
            users.put(newUser.getId(), newUser);
            return ResponseEntity.ok("Пользователь успешно обновлен");
        } else {
            return ResponseEntity.badRequest().body("Обновление не удалось");
        }

    }

    private int generateID() {
        return id++;
    }

    private void userValidation(User user) {
        if (user.getId() <= 0) {
            throw new ValidationException("Id должен быть указан");
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}

package ru.yandex.practicum.filmorate.controller;

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

    Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        if (users.isEmpty()) {
            throw new ValidationException("Список пользователей пуст.");
        } else {
            return ResponseEntity.ok(users.values());
        }
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Данный пользователь уже добавлен");
        }
        userValidation(user);
        users.put(user.getId(), user);
        return ResponseEntity.ok("Пользователь " + user.getName() + " успешно добавлен");
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody User newUser) {
        userValidation(newUser);
        if (users.containsKey(newUser.getId())){
            users.put(newUser.getId(), newUser);
            return ResponseEntity.ok("Пользователь успешно обновлен");
        }else {
            return ResponseEntity.badRequest().body("Обновление не удалось");
        }

    }

    private void userValidation(User user) {
        if (user.getId() <= 0) {
            throw new ValidationException("Id должен быть указан");
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}

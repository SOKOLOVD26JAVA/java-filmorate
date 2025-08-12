package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
    public User addUser(@Valid @RequestBody User user) {
        log.info("Попытка добавления нового пользователя, login: {} ", user.getLogin());
        user.setId(generateID());
        log.info("Для пользователя сгенерирован ID: {}.", user.getId());
        userValidation(user);
        users.put(user.getId(), user);
        log.info("Пользователь с именем: {} успешно добавлен.", user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        log.info("Попытка обновления пользователя, login: {}", newUser.getLogin());
        userValidation(newUser);
        if (users.containsKey(newUser.getId())) {
            users.put(newUser.getId(), newUser);
            log.info("Пользователь с логином: {} успешно обновлен", newUser.getLogin());
            return newUser;
        } else {
            log.warn("Пользователь: {} с ID: {} отсутствует в списке.", newUser.getName(), newUser.getId());
            throw new NotFoundException("Такого пользователя нет.");
        }

    }

    private int generateID() {
        return id++;
    }

    private void userValidation(User user) {

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.warn("При добавлении у пользователя с ID: {} отсутствует поле name. ", user.getId());
            user.setName(user.getLogin());
            log.info("В поле name записано поле login. Новое значение: {}", user.getName());
        }
    }
}

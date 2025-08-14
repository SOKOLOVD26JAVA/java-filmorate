package ru.yandex.practicum.filmorate.service.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    protected int id = 1;
    @Getter
    protected Map<Integer, User> users = new HashMap<>();


    @Override
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity.ok(users.values());
    }

    @Override
    public User addUser(User user) {
        log.info("Попытка добавления нового пользователя, login: {} ", user.getLogin());
        user.setId(generateID());
        log.info("Для пользователя сгенерирован ID: {}.", user.getId());
        userValidation(user);
        users.put(user.getId(), user);
        log.info("Пользователь с именем: {} успешно добавлен.", user.getName());
        return user;
    }

    @Override
    public User updateUser(User newUser) {
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

    @Override
    public ResponseEntity<String> delete(int id) {
        return null;
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

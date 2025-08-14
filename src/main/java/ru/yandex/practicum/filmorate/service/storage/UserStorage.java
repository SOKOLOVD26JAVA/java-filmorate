package ru.yandex.practicum.filmorate.service.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    public ResponseEntity<Collection<User>> getAllUsers();

    public User addUser(User user);

    public User updateUser(User newUser);

    public ResponseEntity<String> delete(int id);
}

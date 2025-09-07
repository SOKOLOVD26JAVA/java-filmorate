package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;

    public List<User> findAll() {
        String query = "SELECT * FROM users";
        List<User> users = jdbc.query(query, mapper);
        log.info("Список пользователй возвращен.");
        return users;
    }

    public User findById(int userId) {
        try {
            String query = "SELECT * FROM users WHERE user_id = ?";
            User user = jdbc.queryForObject(query, mapper, userId);
            log.info("Пользователь возвращен.");
            return user;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Пользователь не найден");
            throw new NotFoundException("Ошибка поиска пользователя.");
        }
    }

    public User saveUser(User user) {
        String query = "INSERT INTO users (email, login, user_name, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int updatedRows = jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));

            return ps;
        }, keyHolder);

        if (updatedRows == 0) {
            log.warn("Пользователь не сохранен");
            throw new RuntimeException("Ошибка сохранения пользователя.");
        }

        user.setId(keyHolder.getKey().intValue());
        log.info("Пользователь сохранен");
        return user;
    }

    public User updateUser(User user) {
        String query = "UPDATE users SET email = ?, login = ?, user_name = ?, birthday = ? WHERE user_id = ?";

        int updatedRows = jdbc.update(query, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Пользователь обновлен.");
        return user;
    }

    public void addToFriends(int userId, int friendId) {

        String query = "INSERT INTO friendships (user_id, friend_id) VALUES (?,?)";

        int updatedRows = jdbc.update(query, userId, friendId);
        log.info("Успешное добавление в друзья.");
    }

    public void deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";

        int updatedRows = jdbc.update(query, userId);
        log.info("Успешное удаление пользователя.");
    }

    public void deleteFromFriendList(int userId, int friendId) {
        String query = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";

        int updatedRows = jdbc.update(query, userId, friendId);
        log.info("Успешное удаление из друзей.");
    }

    public List<User> getFriends(int userId) {
        String query = "SELECT u.* " +
                "FROM users AS u " +
                "JOIN friendships AS f ON u.user_id = f.friend_id " +
                "WHERE f.user_id = ?";

        List<User> friends = jdbc.query(query, mapper, userId);
        log.info("Список друзей возвращен");
        return friends;
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        String query = "SELECT u.* " +
                "FROM users u " +
                "INNER JOIN friendships f1 ON u.user_id = f1.friend_id AND f1.user_id = ? " +
                "INNER JOIN friendships f2 ON u.user_id = f2.friend_id AND f2.user_id = ? " +
                "WHERE f1.user_id != f2.user_id";

        List<User> commonFriends = jdbc.query(query, mapper, userId, friendId);
        if (commonFriends.isEmpty()) {
            log.warn("Не удалось получить список общих друзей.");
            throw new NotFoundException("Ошибка получения общих друзей.");
        }
        log.info("Список общих друзей возвращен.");
        return commonFriends;
    }

}

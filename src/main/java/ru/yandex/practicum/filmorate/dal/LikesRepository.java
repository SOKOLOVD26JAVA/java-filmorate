package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;


@Slf4j
@Repository
@RequiredArgsConstructor
public class LikesRepository {

    private final JdbcTemplate jdbc;

    public void addLike(int filmId, int userId) {
        String query = "INSERT INTO likes (film_id, user_id) VALUES (?,?)";
        int updatedRows = jdbc.update(query, filmId, userId);

        if (updatedRows == 0) {
            log.warn("Не удалось поставить лайк от пользователя с ID: {} к фильму с ID: {}", userId, filmId);
            throw new NotFoundException("Не удалось поставить лайк.");
        }
        log.info("Добавлен лайк от пользователя с ID: {} к фильму с ID: {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        String query = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        int updatedRows = jdbc.update(query, filmId, userId);

        if (updatedRows == 0) {
            log.warn("Не удалось удалить лайк от пользователя с ID: {} у фильма с ID: {}", userId, filmId);
            throw new NotFoundException("Не удалось удалить лайк.");
        }
        log.info("Удален лайк от пользователя с ID: {} у фильма с ID: {}", userId, filmId);
    }

}

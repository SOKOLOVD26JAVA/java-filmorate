package ru.yandex.practicum.filmorate.dal;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class GenreRepository {

    private final JdbcTemplate jdbc;
    private final GenreRowMapper genreRowMapper;

    public List<Genre> getAllGenres() {
        String query = "SELECT * FROM genres";
        List<Genre> genres = jdbc.query(query, genreRowMapper);
        if (genres.isEmpty()) {
            log.warn("Получить жанры не удалось.");
            throw new NotFoundException("Ошибка получения жанров");
        }
        log.info("Жанры возвращены");
        return genres;
    }

    public Genre getGenreById(int genreId) {
        String query = "SELECT * FROM genres WHERE genre_id = ?";
        try {
            Genre genre = jdbc.queryForObject(query, genreRowMapper, genreId);
            log.info("Жанр возвращен.");
            return genre;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Жанр с ID: {} отсутствует.", genreId);
            throw new NotFoundException("Жанр не найден");
        }
    }
}

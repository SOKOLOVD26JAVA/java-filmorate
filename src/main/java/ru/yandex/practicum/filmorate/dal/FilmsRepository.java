package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmGenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmsRepository {

    private final JdbcTemplate jdbc;
    private final FilmRowMapper mapper;
    private final MpaRowMapper mpaMapper;
    private final GenreRowMapper genreMapper;
    private final FilmGenreRowMapper filmGenreMapper;

    String allFilmsQuery = "SELECT f.*, m.mpa_id, m.mpa_name " +
            "FROM films AS f " +
            "JOIN mpa AS m ON f.mpa_id = m.mpa_id";

    String filmQuery = "SELECT * FROM films WHERE film_id = ?";

    String mpaQuery = "SELECT * FROM mpa WHERE mpa_id = ?";

    String genreQuery = "SELECT g.* FROM genres AS g " +
            "JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
            "WHERE fg.film_id = ?";

    String checkGenreQuery = "SELECT * FROM genres WHERE genre_id = ?";

    String allFilmGenreQuery = "SELECT fg.film_id, g.genre_id, g.genre_name " +
            "FROM film_genre AS fg " +
            "JOIN genres AS g ON fg.genre_id = g.genre_id";

    public List<Film> findAll() {
        try {
            List<Film> films = jdbc.query(allFilmsQuery, mapper);
            List<FilmGenre> fg = jdbc.query(allFilmGenreQuery, filmGenreMapper);
            for (Film film : films) {
                List<Genre> genre = fg.stream().filter(filmGenre -> filmGenre.getFilmId() == film.getId())
                        .map(FilmGenre::getGenre).collect(Collectors.toList());
                film.setGenres(genre);
            }
            log.info("Список фильмов возвращен.");
            return films;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Ошибка получения MPA при получении всех фильмов.");
            throw new NotFoundException("Ошибка получения MPA");
        }
    }

    public Film getFilm(int filmId) {
        try {
            Film film = jdbc.queryForObject(filmQuery, mapper, filmId);
            Mpa mpa = getMpa(film);
            film.setMpa(mpa);
            List<Genre> genres = getGenres(film);
            film.setGenres(genres);
            log.info("Фильм с ID: {} есть в базе", filmId);
            return film;

        } catch (EmptyResultDataAccessException e) {
            log.warn("Ошибка получения фильма с ID: {}", filmId);
            throw new NotFoundException("Ошибка получения фильма.");
        }
    }

    public Film saveFilm(Film film) {

        String query = "INSERT INTO films (film_name, description, release_date, duration, mpa_id) VALUES (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (film.getMpa() == null) {
            Mpa mpa = new Mpa();
            mpa.setId(1);
            mpa.setName("G");
            film.setMpa(mpa);
        }

        jdbc.update(con -> {
            PreparedStatement pr = con.prepareStatement(query, new String[]{"film_id"});
            pr.setString(1, film.getName());
            pr.setString(2, film.getDescription());
            pr.setDate(3, Date.valueOf(film.getReleaseDate()));
            pr.setInt(4, film.getDuration());
            pr.setInt(5, film.getMpa().getId());
            return pr;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());
        saveGenres(film, film.getGenres());
        log.info("Фильм сохранен.");
        return film;
    }

    public void deleteFilm(int filmID) {
        String query = "DELETE FROM films WHERE film_id = ?";

        int updatedRows = jdbc.update(query, filmID);
        if (updatedRows == 0) {
            throw new RuntimeException("удалить фильм не удалось.");
        }
    }

    public Film updateFilm(Film film) {
        try {
            Film findFilm = jdbc.queryForObject(filmQuery, mapper, film.getId());

            findFilm.setName(film.getName());
            findFilm.setDescription(film.getDescription());
            findFilm.setReleaseDate(film.getReleaseDate());
            findFilm.setDuration(film.getDuration());

            Mpa mpa = getMpa(film);
            findFilm.setMpa(mpa);


            String query = "UPDATE films SET film_name = ?, description = ?, release_date = ?, " +
                    "duration = ?, mpa_id = ? WHERE film_id = ?";

            jdbc.update(query,
                    findFilm.getName(), findFilm.getDescription(), findFilm.getReleaseDate(),
                    findFilm.getDuration(), findFilm.getMpa().getId(), findFilm.getId()
            );


            removeGenres(findFilm);
            saveGenres(findFilm, film.getGenres());

            List<Genre> genres = getGenres(findFilm);
            findFilm.setGenres(genres);
            log.info("Фильм успешно обновлен.");
            return findFilm;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Ошибка обновления фильма, фильм не найден.");
            throw new NotFoundException("Ошибка обновления фильма.");
        }
    }

    public Mpa getMpa(Film film) {
        log.info("Попытка получения MPA.");
        try {
            if (film.getMpa() == null) {
                log.info("MPA обнаружен через ID");
                return jdbc.queryForObject(mpaQuery, mpaMapper, film.getMpaId());
            } else {
                log.info("MPA обнаружен через фильм.");
                return jdbc.queryForObject(mpaQuery, mpaMapper, film.getMpa().getId());
            }
        } catch (EmptyResultDataAccessException e) {
            log.warn("MPA не обнаружен.");
            throw new NotFoundException("Выбран не корректный MPA.");
        }
    }

    public void checkGenre(List<GenreDto> genres) {
        try {
            log.info("Проверка жанров.");
            for (GenreDto genreDto : genres) {
                jdbc.queryForObject(checkGenreQuery, genreMapper, genreDto.getId());
            }
            log.info("Жанры обнаружены.");
        } catch (EmptyResultDataAccessException e) {
            log.warn("Не корректные жанры.");
            throw new NotFoundException("Выбран не корректный жанр.");
        }
    }

    public List<Genre> getGenres(Film film) {
        log.info("Получение жанров по фильму.");
        List<Genre> genres = jdbc.query(genreQuery, genreMapper, film.getId());
        log.info("Список жанров возвращен.");
        return genres;

    }

    public List<Film> getMostPopularFilm(int count) {
        try {
            String query = "SELECT f.*, m.mpa_id, m.mpa_name,  COUNT(l.user_id) AS likes_count " +
                    "FROM films AS f " +
                    "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                    "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    "GROUP BY f.film_id " +
                    "ORDER BY likes_count DESC " +
                    "LIMIT ?";

            List<Film> popularFilms = jdbc.query(query, mapper, count);
            List<FilmGenre> fg = jdbc.query(allFilmGenreQuery, filmGenreMapper);
            for (Film film : popularFilms) {
                List<Genre> genre = fg.stream().filter(filmGenre -> filmGenre.getFilmId() == film.getId())
                        .map(FilmGenre::getGenre).collect(Collectors.toList());
                film.setGenres(genre);
            }
            log.info("Наиболее популярные фильмы возвращены.");
            return popularFilms;
        } catch (EmptyResultDataAccessException e) {
            log.warn("Ошибка получения MPA при получении наиболее популярных фильмов.");
            throw new NotFoundException("Ошибка получения MPA.");
        }
    }

    private void saveGenres(Film film, List<Genre> genres) {
        log.info("Сохранение жанров.");
        if (genres == null || genres.isEmpty()) {
            return;
        }
        Set<Genre> noDuplicateGenres = new HashSet<>(genres);
        String query = "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)";
        for (Genre genre : noDuplicateGenres) {
            jdbc.update(query, film.getId(), genre.getId());
        }

    }

    private void removeGenres(Film film) {
        log.info("Удаление жанров.");
        String query = "DELETE FROM film_genre WHERE film_id = ?";
        jdbc.update(query, film.getId());
    }


}


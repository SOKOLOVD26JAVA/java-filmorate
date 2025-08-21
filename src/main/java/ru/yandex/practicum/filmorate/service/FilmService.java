package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> deleteFilm(Film film) {
        return filmStorage.delete(film);
    }

    public Film addLikeToFilm(int filmId, int userId) {
        log.info("Попытка добавления лайка к фильму с ID: {} от пользователя с ID: {}", filmId, userId);
        if (checkUser(userId) && checkFilm(filmId)) {
            Film filmForLike = filmStorage.getFilm(filmId);
            filmForLike.getLikeSet().add(userId);
            log.info("Фильму с ID: {} успешно добавлен лайк от пользователя  c ID: {}", filmId, userId);
            return filmForLike;
        } else {
            throw new NotFoundException("Проверьте вводимые данные.");
        }
    }

    public Collection<Film> getMostPopularFilms(int count) {
        log.info("Попытка получения наиболее популярных фильмов.");
        log.info("Передан параметр count: {}", count);
        return filmStorage.getFilms().stream().filter(film -> !film.getLikeSet().isEmpty())
                .sorted(Comparator.comparing(Film::getLikesCount).reversed()).limit(count).collect(Collectors.toList());
    }

    public Film deleteLikeFromFilm(int filmId, int userId) {
        log.info("Попытка удаления лайка у фильма с ID: {} от пользователя с ID: {}", filmId, userId);
        if (checkUser(userId) && checkFilm(filmId)) {
            Film filmForDeleteLike = filmStorage.getFilm(filmId);
            if (!filmForDeleteLike.getLikeSet().contains(userId)) {
                log.warn("Фильм с ID: {} не был оценен пользователем с ID: {}", filmId, userId);
                throw new NotFoundException("Данный пользователь не оценивал этот фильм.");
            } else {
                filmForDeleteLike.getLikeSet().remove(userId);
                log.info("У фильма с ID: {} удален лайк от пользователя с ID: {}", filmId, userId);
                return filmForDeleteLike;
            }
        } else {
            throw new NotFoundException("Проверьте вводимые данные.");
        }


    }

    private boolean checkUser(int userId) {
        log.info("Проверка пользователя по спискам.");
        if (userStorage.getUser(userId) == null) {
            log.warn("Пользователь с ID: {} не найден.", userId);
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        } else {
            return true;
        }
    }

    private boolean checkFilm(int filmId) {
        log.info("Проверка фильма по спискам.");
        if (filmStorage.getFilm(filmId) == null) {
            log.warn("Фильм с ID: {} не найден.", filmId);
            throw new NotFoundException("Фильм с ID " + filmId + " не найден.");
        } else {
            return true;
        }
    }

    public static void filmValidation(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Фильм: {} с ID: {} не прошел валидацию.", film.getName(), film.getId());
            throw new ValidationException("Дата релиза фильмы не может быть раньше 28 декабря 1895 года");
        }
    }


}

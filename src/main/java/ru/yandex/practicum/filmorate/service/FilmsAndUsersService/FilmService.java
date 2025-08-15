package ru.yandex.practicum.filmorate.service.FilmsAndUsersService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.service.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public Film addLikeToFilm(int filmId, int userId) {
        log.info("Попытка добавления лайка к фильму с ID: {} от пользователя с ID: {}", filmId, userId);
        if (checkUserAndFilm(filmId, userId)) {
            Film filmForLike = filmStorage.getFilms().get(filmId);
            filmForLike.getLikeList().add(userId);
            log.info("Фильму с ID: {} успешно добавлен лайк от пользователя  c ID: {}", filmId, userId);
            return filmForLike;
        } else {
            throw new NotFoundException("Проверьте вводимые данные.");
        }
    }

    public Collection<Film> getMostPopularFilms(int count) {
        log.info("Попытка получения наиболее популярных фильмов.");
        if (count == 10) {
            log.info("Передан дефолтный параметр count: 10.");
            return filmStorage.getFilms().values().stream().filter(film -> film.getLikeList() != null)
                    .sorted(Comparator.comparing(Film::getLikesCount).reversed()).limit(10).collect(Collectors.toList());
        } else {
            log.info("Передан параметр count: {}", count);
            return filmStorage.getFilms().values().stream().filter(film -> film.getLikeList() != null)
                    .sorted(Comparator.comparing(Film::getLikesCount).reversed()).limit(count).collect(Collectors.toList());
        }
    }

    public Film deleteLikeFromFilm(int filmId, int userId) {
        log.info("Попытка удаления лайка у фильма с ID: {} от пользователя с ID: {}", filmId, userId);
        if (checkUserAndFilm(filmId, userId)) {
            Film filmForDeleteLike = filmStorage.getFilms().get(filmId);
            if (!filmForDeleteLike.getLikeList().contains(userId)) {
                log.warn("Фильм с ID: {} не был оценен пользователем с ID: {}", filmId, userId);
                throw new NotFoundException("Данный пользователь не оценивал этот фильм.");
            } else {
                filmForDeleteLike.getLikeList().remove(userId);
                log.info("E фильма с ID: {} удален лайк от пользователя с ID: {}", filmId, userId);
                return filmForDeleteLike;
            }
        } else {
            throw new NotFoundException("Проверьте вводимые данные.");
        }


    }

    private boolean checkUserAndFilm(int filmId, int userId) {
        log.info("Проверка пользователя и фильма по спискам.");
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.warn("Фильм с ID: {} не найден.", filmId);
            throw new NotFoundException("Фильм с ID " + filmId + " не найден.");
        } else if (!userStorage.getUsers().containsKey(userId)) {
            log.warn("Пользователь с ID: {} не найден.", userId);
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        } else {
            return true;
        }
    }
}

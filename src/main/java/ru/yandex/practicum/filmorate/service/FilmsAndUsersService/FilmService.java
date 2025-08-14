package ru.yandex.practicum.filmorate.service.FilmsAndUsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.service.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

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
        if (checkUserAndFilm(filmId, userId)) {
            Film filmForLike = filmStorage.getFilms().get(filmId);
            filmForLike.getLikeList().add(userId);
            return filmForLike;
        }else {
            throw new NotFoundException("Проверьте вводимые данные.");
        }
    }

    public Collection<Film> getMostPopularFilms(int count) {
        if (count == 10) {
            return filmStorage.getFilms().values().stream().filter(film -> film.getLikeList() != null)
                    .sorted(Comparator.comparing(Film::getLikesCount).reversed()).limit(10).collect(Collectors.toList());
        } else {
            return filmStorage.getFilms().values().stream().filter(film -> film.getLikeList() != null)
                    .sorted(Comparator.comparing(Film::getLikesCount).reversed()).limit(count).collect(Collectors.toList());
        }
    }

    public Film deleteLikeFromFilm(int filmId, int userId) {
        if(checkUserAndFilm(filmId,userId)){
            Film filmForDeleteLike = filmStorage.getFilms().get(filmId);
            if (!filmForDeleteLike.getLikeList().contains(userId)){
                throw new NotFoundException("Данный пользователь не оценивал этот фильм.");
            }else {
                filmForDeleteLike.getLikeList().remove(userId);
                return filmForDeleteLike;
            }
        } else {
            throw new NotFoundException("Проверьте вводимые данные.");
        }


    }

    private boolean checkUserAndFilm(int filmId, int userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Фильм с ID " + filmId + " не найден.");
        } else if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        } else {
            return true;
        }
    }
}

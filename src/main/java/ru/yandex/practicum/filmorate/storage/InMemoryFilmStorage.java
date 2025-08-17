package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.FilmService.filmValidation;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    protected int id = 1;
    protected Map<Integer, Film> films = new HashMap<>();


    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Попытка добавления фильма {}", film.getName());
        film.setId(generateID());
        log.info("Для фильма сгенерирован ID: {}.", film.getId());
        filmValidation(film);
        log.info("При добавлении фильм: {} с ID: {} прошел валидацию по дате релиза.", film.getName(), film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("Попытка обновления фильма: {}", newFilm.getName());
        filmValidation(newFilm);
        log.info("При обновлении фильм: {} с ID: {} прошел валидацию по дате релиза.", newFilm.getName(), newFilm.getId());
        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            log.info("Фильм: {} успешно обновлен.", newFilm.getName());
            return newFilm;
        } else {
            log.warn("Фильм: {} с ID: {} отсутствует в списке.", newFilm.getName(), newFilm.getId());
            throw new NotFoundException("Такого фильма нет.");
        }

    }

    @Override
    public Collection<Film> delete(Film film) {
        log.info("Попытка удаления фильма с ID: {}", film.getId());
        filmValidation(film);
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            log.info("Фильм с ID: {} удален.", film.getId());
            return films.values();
        } else {
            throw new NotFoundException("Такого фильма нет");
        }

    }

    private int generateID() {
        return id++;
    }

}

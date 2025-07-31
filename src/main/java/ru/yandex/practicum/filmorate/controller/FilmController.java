package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    protected int id = 1;
    protected Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {

        return ResponseEntity.ok(films.values());

    }


    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        film.setId(generateID());
        filmValidation(film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        filmValidation(newFilm);
        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            return newFilm;
        } else {
            throw new NotFoundException("Такого пользователя нет");
        }

    }

    private int generateID() {
        return id++;
    }


    private void filmValidation(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильмы не может быть раньше 28 декабря 1895 года");
        }
    }
}

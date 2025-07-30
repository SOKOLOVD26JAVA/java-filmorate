package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<String> postFilm(@Valid @RequestBody Film film) {
        film.setId(generateID());

        if (films.containsKey(film.getId())) {
            throw new ValidationException("Данный фильм уже добавлен");
        }
        filmValidation(film);
        films.put(film.getId(), film);
        return ResponseEntity.ok("Фильм " + film.getName() + " yспешно добавлен");
    }

    @PutMapping
    public ResponseEntity<String> updateFilm(@Valid @RequestBody Film newFilm) {
        newFilm.setId(generateID());
        filmValidation(newFilm);
        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            return ResponseEntity.ok("Фильм успешно обновлен");
        } else {
            return ResponseEntity.badRequest().body("Обновление не удалось");
        }

    }

    private int generateID(){
       return id++;
    }


    private void filmValidation(Film film) {
        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильмы не может быть раньше 28 декабря 1895 года");
        } else if (film.getId() <= 0) {
            throw new ValidationException("Id должен быть указан");
        }
    }
}

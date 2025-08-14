package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.storage.InMemoryFilmStorage;

import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }


    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return inMemoryFilmStorage.updateFilm(newFilm);
    }


}

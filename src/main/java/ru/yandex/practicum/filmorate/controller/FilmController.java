package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getFilms();
    }


    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @DeleteMapping
    public Collection<Film> removeFilm(@Valid @RequestBody Film film) {
        return filmService.deleteFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable int filmId, @PathVariable int userId) {
        return ResponseEntity.ok(filmService.addLikeToFilm(filmId, userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        Collection<Film> mostPopularFilms = filmService.getMostPopularFilms(count);
        return ResponseEntity.ok(mostPopularFilms);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> deleteLikeFromFilm(@PathVariable int filmId, @PathVariable int userId) {
        Film filmForDeleteLike = filmService.deleteLikeFromFilm(filmId, userId);
        return ResponseEntity.ok(filmForDeleteLike);
    }


}

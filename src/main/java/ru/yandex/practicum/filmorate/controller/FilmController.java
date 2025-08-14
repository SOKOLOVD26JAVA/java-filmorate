package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmsAndUsersService.FilmService;
import ru.yandex.practicum.filmorate.service.storage.InMemoryFilmStorage;

import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;
//Насколько я понял, если у нас единственный конструктор, то можно убрать эту аннотацию, но для себя оставил, если критично - уберу.))
    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
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

    @PutMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable int filmId, @PathVariable int userId) {
         return ResponseEntity.ok(filmService.addLikeToFilm(filmId,userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        Collection<Film> mostPopularFilms = filmService.getMostPopularFilms(count);
        return ResponseEntity.ok(mostPopularFilms);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public ResponseEntity<Film> deleteLikeFromFilm(@PathVariable int filmId, @PathVariable int userId){
        Film filmForDeleteLike = filmService.deleteLikeFromFilm(filmId,userId);
        return ResponseEntity.ok(filmForDeleteLike);
    }


}

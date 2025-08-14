package ru.yandex.practicum.filmorate.service.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    public ResponseEntity<Collection<Film>> getAllFilms();

    public Film createFilm(Film film);

    public Film updateFilm(Film newFilm);

    public ResponseEntity<String> delete(int id);


}

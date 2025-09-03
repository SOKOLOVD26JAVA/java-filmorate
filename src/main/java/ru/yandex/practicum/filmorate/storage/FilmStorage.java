package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    public Collection<Film> getFilms();

    public Film createFilm(Film film);

    public Film updateFilm(Film newFilm);

    public Collection<Film> delete(Film film);

    public Film getFilm(int id);


}

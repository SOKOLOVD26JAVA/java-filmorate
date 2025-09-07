package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();

        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        filmDto.setMpa(MpaMapper.mapToMpaDto(film.getMpa()));
        filmDto.setGenres(GenreMapper.mapToGenresDto(film.getGenres()));

        return filmDto;
    }

    public static Film mapToFilm(FilmDto filmDto) {
        Film film = new Film();

        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDuration(filmDto.getDuration());
        film.setMpa(MpaMapper.mapToMpa(filmDto.getMpa()));
        film.setGenres(GenreMapper.mapToGenres(filmDto.getGenres()));

        return film;
    }
}

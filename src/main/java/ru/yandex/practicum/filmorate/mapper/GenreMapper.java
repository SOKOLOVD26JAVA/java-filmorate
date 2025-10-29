package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenreMapper {

    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();

        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());
        return genreDto;
    }

    public static Genre mapToGenre(GenreDto genreDto) {
        Genre genre = new Genre();

        genre.setId(genreDto.getId());
        genre.setName(genreDto.getName());
        return genre;

    }

    public static List<GenreDto> mapToGenresDto(List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return new ArrayList<>();
        }
        return genres.stream().map(GenreMapper::mapToGenreDto).collect(Collectors.toList());
    }

    public static List<Genre> mapToGenres(List<GenreDto> genreDto) {
        if (genreDto == null || genreDto.isEmpty()) {
            return new ArrayList<>();
        }
        return genreDto.stream().map(GenreMapper::mapToGenre).collect(Collectors.toList());
    }
}

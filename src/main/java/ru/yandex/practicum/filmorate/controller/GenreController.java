package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {

    private final FilmService filmService;

    @GetMapping
    public List<GenreDto> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("{id}")
    public GenreDto getGenreById(@PathVariable int id) {
        return filmService.getGenreById(id);
    }
}

package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.*;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmsRepository filmsRepository;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;


    public static void filmValidation(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Фильм: {} с ID: {} не прошел валидацию.", film.getName(), film.getId());
            throw new ValidationException("Дата релиза фильмы не может быть раньше 28 декабря 1895 года");
        }
    }

    public List<FilmDto> getAll() {
        log.info("Попытка получения всех фильмов.");
        return filmsRepository.findAll().stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public FilmDto getFilm(int filmId) {
        log.info("Попытка получений фильма с ID: {}", filmId);
        return FilmMapper.mapToFilmDto(filmsRepository.getFilm(filmId));
    }

    public FilmDto saveFilm(FilmDto filmDto) {
        log.info("Попытка сохранения фильма.");
        Film film = FilmMapper.mapToFilm(filmDto);
        filmValidation(film);
        if (filmDto.getMpa() != null) {
            log.info("Проверка MPA.");
            Mpa mpa = filmsRepository.getMpa(film);
        }
        if (filmDto.getGenres() != null) {
            log.info("Проверка жанров.");
            filmsRepository.checkGenre(filmDto.getGenres());
        }

        return FilmMapper.mapToFilmDto(filmsRepository.saveFilm(film));
    }

    public void deleteFilm(int filmId) {
        log.info("Попытка удаления фильма.");
        filmsRepository.deleteFilm(filmId);
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        log.info("Попытка обновления фильма.");
        Film film = FilmMapper.mapToFilm(filmDto);
        return FilmMapper.mapToFilmDto(filmsRepository.updateFilm(film));

    }

    public void addLike(int filmId, int userId) {
        log.info("Попытка добавления лайка фильму с ID: {} от пользователя с ID: {}", filmId, userId);
        log.info("Проверка наличия фильма для добавления с ID: {}", filmId);
        log.info("Проверка наличия пользователя для добавления с ID: {}", userId);
        filmsRepository.getFilm(filmId);
        userRepository.findById(userId);

        likesRepository.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        log.info("Попытка удаления лайка у фильма с ID: {} от пользователя с ID: {}", filmId, userId);
        log.info("Проверка наличия фильма для удаления с ID: {}", filmId);
        log.info("Проверка наличия пользователя для удаления с ID: {}", userId);
        filmsRepository.getFilm(filmId);
        userRepository.findById(userId);

        likesRepository.removeLike(filmId, userId);
    }

    public List<FilmDto> getMostPopularFilm(int count) {
        log.info("Попытка получения наиболее популярных фильмов, параметр COUNT: {}", count);
        return filmsRepository.getMostPopularFilm(count).stream().map(FilmMapper::mapToFilmDto).collect(Collectors.toList());
    }

    public List<MpaDto> getAllMpa() {
        log.info("Попытка получения всех MPA.");
        return mpaRepository.getMpa().stream().map(MpaMapper::mapToMpaDto).collect(Collectors.toList());
    }

    public MpaDto getMpaById(int mpaId) {
        log.info("Попытка получения MPA с ID: {}", mpaId);
        return MpaMapper.mapToMpaDto(mpaRepository.getMpaById(mpaId));
    }

    public List<GenreDto> getAllGenres() {
        log.info("Попытка получения всех жанров.");
        return genreRepository.getAllGenres().stream().map(GenreMapper::mapToGenreDto).collect(Collectors.toList());
    }

    public GenreDto getGenreById(int genreId) {
        log.info("Попытка получения жанра с ID: {}", genreId);
        return GenreMapper.mapToGenreDto(genreRepository.getGenreById(genreId));
    }


}

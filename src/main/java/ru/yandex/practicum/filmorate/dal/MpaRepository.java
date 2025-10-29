package ru.yandex.practicum.filmorate.dal;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class MpaRepository {
    private final JdbcTemplate jdbc;
    private final MpaRowMapper mpaRowMapper;

    public List<Mpa> getMpa() {
        String query = "SELECT * FROM mpa";
        List<Mpa> mpas = jdbc.query(query, mpaRowMapper);
        if (mpas.isEmpty()) {
            log.warn("Ошибка получения всех MPA.");
            throw new NotFoundException("Ошибка получения MPA.");
        }
        log.info("Все MPA возвращены.");
        return mpas;
    }

    public Mpa getMpaById(int mpaId) {
        String query = "SELECT * FROM mpa WHERE mpa_id = ?";
        try {
            Mpa mpa = jdbc.queryForObject(query, mpaRowMapper, mpaId);
            log.info("МPA возвращен.");
            return mpa;
        } catch (EmptyResultDataAccessException e) {
            log.warn("MPA не обнаружен.");
            throw new NotFoundException("MPA не найден.");
        }
    }
}

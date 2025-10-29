package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

public class MpaMapper {

    public static MpaDto mapToMpaDto(Mpa mpa) {
        MpaDto mpaDto = new MpaDto();
        if (mpa != null) {
            mpaDto.setId(mpa.getId());
            mpaDto.setName(mpa.getName());
            return mpaDto;
        }
        return null;
    }

    public static Mpa mapToMpa(MpaDto mpaDto) {
        Mpa mpa = new Mpa();
        if (mpaDto != null) {
            mpa.setId(mpaDto.getId());
            mpa.setName(mpaDto.getName());
            return mpa;
        }
        return null;
    }
}

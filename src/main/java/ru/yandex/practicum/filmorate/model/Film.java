package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import jakarta.validation.constraints.Size;
import lombok.Data;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    private int id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Длинна описания не более 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Integer> likeSet = new HashSet<>();
    private List<Genre> genres;
    MPA mpa;

    public int getLikesCount() {
        return likeSet.size();
    }
}

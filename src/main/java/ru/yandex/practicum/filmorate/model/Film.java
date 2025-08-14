package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import jakarta.validation.constraints.Size;
import lombok.Data;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    private Set<Integer> likeList = new HashSet<>();
    private int id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Длинна описания не более 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;

    public int getLikesCount(){
        return likeList.size();
    }
}

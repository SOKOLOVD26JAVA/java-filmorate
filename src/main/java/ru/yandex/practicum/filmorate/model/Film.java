package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    int id;
    @NotBlank(message = "Название не может быть пустым")
    String name;
    @Size(max = 200, message = "Длинна описания не более 200 символов")
    String description;
    LocalDate releaseDate;
    Duration duration;
}

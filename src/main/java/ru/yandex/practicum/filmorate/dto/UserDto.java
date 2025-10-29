package ru.yandex.practicum.filmorate.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private int id;
    @Email
    private String email;
    @Pattern(regexp = "\\S+", message = "Поле login не допускает наличие пробелов")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}

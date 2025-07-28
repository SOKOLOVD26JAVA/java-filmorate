package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    int id;
    @Email
    String email;
    @Pattern(regexp = "\\S+", message = "Поле login не допускает наличие пробелов")
    String login;
    String name;
    @Past(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;
}

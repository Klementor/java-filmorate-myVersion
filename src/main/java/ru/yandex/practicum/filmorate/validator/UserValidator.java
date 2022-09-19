package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class UserValidator {

    public static void validate(@NotNull User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new RuntimeException();
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new RuntimeException();
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new RuntimeException();
        }
    }
}

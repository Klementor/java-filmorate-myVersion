package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exceptions.UserValidateException;
import ru.yandex.practicum.filmorate.model.user.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class UserValidator {

    public static void validate(@NotNull User user) throws UserValidateException {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new UserValidateException("Введено некорректное имя пользователя");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new UserValidateException("Введен некорректный Email");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserValidateException("Дата рождения не может быть в будущем");
        }
    }
}

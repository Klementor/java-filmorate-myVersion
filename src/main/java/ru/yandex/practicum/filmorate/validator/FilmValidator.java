package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exceptions.FilmValidateException;
import ru.yandex.practicum.filmorate.model.film.Film;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class FilmValidator {
    public static void validate(@NotNull Film film) throws FilmValidateException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new FilmValidateException("Введено некорректное название фильма");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() || film.getDescription().length() > 200) {
            throw new FilmValidateException("Введено неверное описание фильма");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmValidateException("Дата выхода не может быть раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            throw new FilmValidateException("Длительность фильма не может быть отрицательной");
        }
    }
}

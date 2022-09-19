package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class FilmValidator {
    public static void validate (@NotNull Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new RuntimeException();
        }
        if (film.getDescription() == null || film.getDescription().isBlank() || film.getDescription().length() > 200) {
            throw new RuntimeException();
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new RuntimeException();
        }
        if (film.getDuration() <= 0) {
            throw new RuntimeException();
        }
    }
}

package ru.yandex.practicum.filmorate.exceptions;

public class FilmValidateException extends RuntimeException {

    public FilmValidateException(final String message) {
        super(message);
    }
}
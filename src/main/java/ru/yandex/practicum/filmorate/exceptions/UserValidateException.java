package ru.yandex.practicum.filmorate.exceptions;

public class UserValidateException extends RuntimeException {
    public UserValidateException(final String message) {
        super(message);
    }
}

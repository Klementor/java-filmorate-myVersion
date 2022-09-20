package ru.yandex.practicum.filmorate.exceptions;

public class UserUpdateException extends Exception{

    public UserUpdateException(final String message){
        super(message);
    }
    public UserUpdateException() {
    }
}

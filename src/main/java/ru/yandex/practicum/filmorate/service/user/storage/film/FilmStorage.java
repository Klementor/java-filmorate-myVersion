package ru.yandex.practicum.filmorate.service.user.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Map<Long, Film> getAll();

    Film getFilm(long id);
}

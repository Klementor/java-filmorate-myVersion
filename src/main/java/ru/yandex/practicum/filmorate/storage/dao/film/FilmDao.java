package ru.yandex.practicum.filmorate.storage.dao.film;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;
import java.util.Set;

public interface FilmDao {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAll();

    Film getFilm(long id);

    void addGenres(Film film);

    void updateGenres(Film film);

    Set<Genre> getGenres(Film film);
}

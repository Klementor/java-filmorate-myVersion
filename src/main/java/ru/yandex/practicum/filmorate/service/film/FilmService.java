package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmService {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Map<Long, Film> getAll();

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> getPopularFilms(int countFilms);

    Film getFilm(long id);
}

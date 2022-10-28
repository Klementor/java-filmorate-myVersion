package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getGenres();

    Genre getGenre(int id);
}

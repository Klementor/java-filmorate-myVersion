package ru.yandex.practicum.filmorate.storage.dao.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreDao {
    Genre getGenre(int id);

    Set<Genre> getAll();
}

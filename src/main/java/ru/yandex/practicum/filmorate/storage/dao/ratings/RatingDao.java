package ru.yandex.practicum.filmorate.storage.dao.ratings;

import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.Collection;

public interface RatingDao {
    Collection<Mpa> getAll();

    Mpa getRating(int id);
}

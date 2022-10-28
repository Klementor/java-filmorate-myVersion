package ru.yandex.practicum.filmorate.storage.dao.ratings;

import ru.yandex.practicum.filmorate.model.film.Rating;

import java.util.Collection;

public interface RatingDao {
    Collection<Rating> getAll();

    Rating getRating(int id);
}

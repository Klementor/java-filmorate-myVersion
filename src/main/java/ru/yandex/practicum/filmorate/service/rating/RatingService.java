package ru.yandex.practicum.filmorate.service.rating;

import ru.yandex.practicum.filmorate.model.film.Rating;

import java.util.List;

public interface RatingService {
    List<Rating> getRatings();

    Rating getRating(int id);
}

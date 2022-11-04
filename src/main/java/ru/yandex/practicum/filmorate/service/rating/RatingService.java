package ru.yandex.practicum.filmorate.service.rating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface RatingService {
    List<Mpa> getRatings();

    Mpa getRating(int id);
}

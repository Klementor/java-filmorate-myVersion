package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.rating.RatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public List<Mpa> getRating() {
        return ratingService.getRatings();
    }

    @GetMapping("/{id}")
    public Mpa getRating(@PathVariable int id) {
        return ratingService.getRating(id);
    }
}

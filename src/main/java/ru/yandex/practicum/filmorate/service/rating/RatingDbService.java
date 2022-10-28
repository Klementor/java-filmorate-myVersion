package ru.yandex.practicum.filmorate.service.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Rating;
import ru.yandex.practicum.filmorate.storage.dao.ratings.RatingDao;

import java.util.ArrayList;
import java.util.List;

@Service("RatingDbService")
public class RatingDbService implements RatingService{

    private final RatingDao ratingDao;

    @Autowired
    public RatingDbService(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    @Override
    public List<Rating> getRatings() {
        return new ArrayList<>(ratingDao.getAll());
    }

    @Override
    public Rating getRating(int id) {
        return ratingDao.getRating(id);
    }
}

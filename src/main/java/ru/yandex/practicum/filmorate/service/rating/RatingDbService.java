package ru.yandex.practicum.filmorate.service.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.ratings.RatingDao;

import java.util.ArrayList;
import java.util.List;

@Service("RatingDbService")
@RequiredArgsConstructor
public class RatingDbService implements RatingService {

    private final RatingDao ratingDao;

    @Override
    public List<Mpa> getRatings() {
        return new ArrayList<>(ratingDao.getAll());
    }

    @Override
    public Mpa getRating(int id) {
        try {
            return ratingDao.getRating(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинг не найден");
        }

    }
}

package ru.yandex.practicum.filmorate.storage.dao.ratings;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.RatingMapper;

import java.util.Collection;

@Component("RatingDaoImpl")
@RequiredArgsConstructor
public class RatingDaoImpl implements RatingDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> getAll() {
        return jdbcTemplate.query(""
                + "SELECT rating_id, name "
                + "FROM ratings "
                + "ORDER BY rating_id", new RatingMapper());
    }

    @Override
    public Mpa getRating(int id) {
        return jdbcTemplate.queryForObject(String.format(""
                + "SELECT rating_id, name "
                + "FROM ratings "
                + "WHERE RATING_ID=%d", id), new RatingMapper());
    }
}

package ru.yandex.practicum.filmorate.storage.dao.ratings;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component("RatingDaoImpl")
public class RatingDaoImpl implements RatingDao {

    private final JdbcTemplate jdbcTemplate;

    public RatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Rating> getAll() {
        return jdbcTemplate.query(""
                + "SELECT rating_id, name "
                + "FROM ratings "
                + "ORDER BY rating_id", new RatingMapper());
    }

    @Override
    public Rating getRating(int id) {
        return jdbcTemplate.queryForObject(String.format(""
                + "SELECT rating_id, name "
                + "FROM ratings "
                + "WHERE RATING_ID=%d", id), new RatingMapper());
    }

    static private class RatingMapper implements RowMapper<Rating> {
        @Override
        public Rating mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rating rating = new Rating();
            rating.setRatingId(rs.getInt("rating_id"));
            rating.setName(rs.getString("name"));
            return rating;
        }
    }
}

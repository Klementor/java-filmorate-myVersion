package ru.yandex.practicum.filmorate.storage.dao.ratings;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Mpa;

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

    static private class RatingMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("rating_id"));
            mpa.setName(rs.getString("name"));
            return mpa;
        }
    }
}

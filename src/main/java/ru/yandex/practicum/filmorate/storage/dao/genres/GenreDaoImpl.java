package ru.yandex.practicum.filmorate.storage.dao.genres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

@Component("GenreDaoImpl")
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(int id) {
        return jdbcTemplate.queryForObject(format(""
                + "SELECT genre_id, name "
                + "FROM genres "
                + "WHERE genre_id=%d", id), new GenreMapper());
    }

    @Override
    public Set<Genre> getAll() {
        return new HashSet<>(jdbcTemplate.query(""
                + "SELECT genre_id, name "
                + "FROM genres "
                + "ORDER BY genre_id", new GenreMapper()));
    }

    static public class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre();
            genre.setGenreId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        }
    }
}

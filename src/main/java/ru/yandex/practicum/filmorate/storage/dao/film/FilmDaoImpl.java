package ru.yandex.practicum.filmorate.storage.dao.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.genres.GenreDaoImpl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.lang.String.format;

@Component("FilmDbStorage")
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update(""
                        + "INSERT INTO films(name, description, release_date, duration, rating_id) "
                        + "VALUES (?, ?, ?, ?, ?)",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        return jdbcTemplate.queryForObject(format(""
                        + "SELECT film_id, name, description, release_date, duration, rating_id "
                        + "FROM films "
                        + "WHERE name='%s' "
                        + "AND description='%s' "
                        + "AND release_date='%s' "
                        + "AND duration=%d "
                        + "AND rating_id=%d",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()), new FilmMapper());
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(""
                        + "UPDATE films "
                        + "SET name=?, description=?, release_date=?, duration=?, rating_id=? "
                        + "WHERE film_id=?",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return getFilm(film.getId());
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(""
                + "SELECT film_id, name, description, release_date, duration, rating_id "
                + "FROM films", new FilmMapper());
    }

    @Override
    public Film getFilm(long id) {
        return jdbcTemplate.queryForObject(format(""
                + "SELECT film_id, name, description, release_date, duration, rating_id "
                + "FROM films "
                + "WHERE film_id=%d", id), new FilmMapper());
    }

    @Override
    public void addGenres(long id, Set<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update(""
                    + "INSERT INTO film_genres(film_id, genre_id) "
                    + "VALUES (?, ?)", id, genre.getId());
        }
    }

    @Override
    public void updateGenres(Film film) {
        deleteGenres(film.getId());
        addGenres(film.getId(), film.getGenres());
    }

    @Override
    public Set<Genre> getGenres(long id) {
        return new HashSet<>(jdbcTemplate.query(String.format(""
                + "SELECT film_genres.genre_id, genres.name "
                + "FROM film_genres "
                + "JOIN genres ON film_genres.genre_id=genres.genre_id "
                + "WHERE film_genres.film_id=%d "
                + "ORDER BY film_genres.genre_id", id), new GenreDaoImpl.GenreMapper()));
    }

    private void deleteGenres(long id) {
        jdbcTemplate.update(""
                + "DELETE "
                + "FROM film_genres "
                + "WHERE film_id=?", id);
    }

    private static class FilmMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("rating_id"));

            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setDuration(rs.getInt("duration"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setMpa(mpa);
            return film;
        }
    }
}

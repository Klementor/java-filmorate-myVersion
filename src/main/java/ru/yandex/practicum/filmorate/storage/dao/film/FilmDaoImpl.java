package ru.yandex.practicum.filmorate.storage.dao.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

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
                + "ORDER BY film_genres.genre_id", id), new GenreMapper()));
    }

    private void deleteGenres(long id) {
        jdbcTemplate.update(""
                + "DELETE "
                + "FROM film_genres "
                + "WHERE film_id=?", id);
    }
}

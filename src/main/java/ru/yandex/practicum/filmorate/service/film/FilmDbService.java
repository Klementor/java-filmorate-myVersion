package ru.yandex.practicum.filmorate.service.film;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.genres.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.ratings.RatingDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDao;
import ru.yandex.practicum.filmorate.storage.mappers.LikeMapper;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("FilmDbService")
@RequiredArgsConstructor
public class FilmDbService implements FilmService {

    private final FilmDao filmDao;
    private final UserDao userDao;
    private final RatingDao ratingDao;
    private final GenreDao genreDao;
    private final LikeDao likeDao;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(@NonNull Film film) {
        FilmValidator.validate(film);
        checkFilmToAdd(film);
        Film filmResult = filmDao.addFilm(film);
        filmDao.addGenres(filmResult.getId(), film.getGenres());
        Set<Genre> genre = filmDao.getGenres(filmResult.getId());
        filmResult.setGenres(genre);
        filmResult.setMpa(ratingDao.getRating(film.getMpa().getId()));
        return filmResult;
    }

    @Override
    public Film updateFilm(Film film) {
        FilmValidator.validate(film);
        checkFilmToUpdate(film);
        filmDao.updateGenres(film);
        Film filmResult = filmDao.updateFilm(film);
        filmResult.setGenres(filmDao.getGenres(film.getId()));
        filmResult.setMpa(ratingDao.getRating(film.getMpa().getId()));
        return filmResult;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = filmDao.getAll();
        for (Film film : films) {
            film.setGenres(filmDao.getGenres(film.getId()));
            film.setMpa(ratingDao.getRating(film.getMpa().getId()));
        }
        return films;
    }

    @Override
    public void addLike(long filmId, long userId) {
        checkLikeToAdd(filmId, userId);
        likeDao.addLike(userId, filmId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        checkLikeToRemove(filmId, userId);
        likeDao.deleteLike(userId, filmId);
    }

    @Override
    public List<Film> getPopularFilms(int countFilms) {
        var films = filmDao.getAll();
        return films.stream().sorted(Comparator.comparingLong(this::getLikeCount).reversed())
                .limit(countFilms).collect(Collectors.toList());
    }

    @Override
    public Film getFilm(long id) {
        try {
            Film film = filmDao.getFilm(id);
            film.setGenres(filmDao.getGenres(film.getId()));
            film.setMpa(ratingDao.getRating(film.getMpa().getId()));
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Данного фильма не существует");
        }
    }

    private long getLikeCount(Film film) {
        return likeDao.countLikes(film.getId());
    }

    private void checkFilmToAdd(Film film) {
        if (film.getId() != 0) {
            throw new AlreadyExistsException("Данный фильм уже создан");
        }
        try {
            ratingDao.getRating(film.getMpa().getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Неправильно задан рейтинг");
        }
        try {
            for (Genre genre : film.getGenres()) {
                genreDao.getGenre(genre.getId());
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Неправильно задан жанр");
        }
    }

    private void checkFilmToUpdate(Film film) {
        try {
            filmDao.getFilm(film.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Невозможно обновить несуществующий фильм");
        }
        try {
            ratingDao.getRating(film.getMpa().getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Неправильно задан рейтинг");
        }
        try {
            for (Genre genre : film.getGenres()) {
                genreDao.getGenre(genre.getId());
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Неправильно задан жанр");
        }
    }

    private void checkLikeToAdd(long filmId, long userId) {
        try {
            filmDao.getFilm(filmId);
            userDao.getUser(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Введенные данные неверны");
        }
        if (likeDao.getUserWhoLikes(filmId).contains(userId)) {
            throw new AlreadyExistsException("Лайк от данного пользователя уже поставлен");
        }
    }

    private void checkLikeToRemove(long filmId, long userId) {
        try {
            filmDao.getFilm(filmId);
            userDao.getUser(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Введены неверные данные");
        }
        try {
            jdbcTemplate.queryForObject(String.format("SELECT film_id, user_id " +
                    "FROM likes " +
                    "WHERE film_id=%d AND user_id=%d", filmId, userId), new LikeMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Попытка удалить несуществующий лайк");
        }
    }
}


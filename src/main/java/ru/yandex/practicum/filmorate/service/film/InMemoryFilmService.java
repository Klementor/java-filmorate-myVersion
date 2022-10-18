package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.service.user.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InMemoryFilmService implements FilmService {

    public FilmStorage inMemoryFilmStorage;
    public UserStorage inMemoryUserStorage;

    @Autowired
    public InMemoryFilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public Film addFilm(Film film) throws FilmValidateException {
        FilmValidator.validate(film);
        if (inMemoryFilmStorage.getAll().get(film.getId()) == null) {
            return inMemoryFilmStorage.addFilm(film);
        } else {
            throw new AlreadyExistsException("Данный фильм уже создан");
        }
    }

    @Override
    public Film updateFilm(Film film) throws FilmValidateException {
        FilmValidator.validate(film);
        if (inMemoryFilmStorage.getAll().get(film.getId()) == null) {
            throw new NotFoundException("Невозможно обновить несуществующий фильм");
        }
        return inMemoryFilmStorage.updateFilm(film);
    }

    @Override
    public Map<Long, Film> getAll() {
        return inMemoryFilmStorage.getAll();
    }

    @Override
    public void addLike(long filmId, long userId) {
        if (inMemoryFilmStorage.getAll().get(filmId) == null) {
            throw new NotFoundException("Данного фильма не существует");
        } else if (inMemoryUserStorage.getAll().get(userId) == null) {
            throw new NotFoundException("Пользователя не существует");
        }
        inMemoryFilmStorage.getFilm(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        if (inMemoryFilmStorage.getAll().get(filmId) == null) {
            throw new NotFoundException("Данного фильма не существует");
        } else if (inMemoryUserStorage.getAll().get(userId) == null) {
            throw new NotFoundException("Пользователя не существует");
        }
        inMemoryFilmStorage.getFilm(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(int countFilms) {
        var films = inMemoryFilmStorage.getAll().values();
        return films.stream().sorted(Comparator.comparingInt(this::getLikeCount).reversed())
                .limit(countFilms).collect(Collectors.toList());
    }

    @Override
    public Film getFilm(long id) {
        if (inMemoryFilmStorage.getAll().get(id) == null) {
            throw new NotFoundException("Данного фильма не существует");
        }
        return inMemoryFilmStorage.getFilm(id);
    }

    private int getLikeCount(Film film) {
        return film.getLikes().size();
    }
}

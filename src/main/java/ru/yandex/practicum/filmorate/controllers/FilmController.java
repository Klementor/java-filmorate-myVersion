package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmUpdateException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();
    private long id = 1;

    @PostMapping
    public Film create(@RequestBody Film film) throws FilmValidateException {
        FilmValidator.validate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.debug("Добавлен фильм. Количество фильмов: {}", films.size());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws FilmUpdateException, FilmValidateException {
        FilmValidator.validate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Фильм обновлен. Количество фильмов: {}", films.size());
            return film;
        } else {
            throw new FilmUpdateException("Попытка обновить несуществующий фильм");
        }
    }

    @GetMapping
    public List<Film> getAll() {
        log.debug("Вывод списка из всех фильмов");
        return new ArrayList<>(films.values());
    }
}

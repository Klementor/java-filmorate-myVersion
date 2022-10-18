package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService inMemoryFilmService;

    @Autowired
    public FilmController(FilmService inMemoryFilmService) {
        this.inMemoryFilmService = inMemoryFilmService;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Добавлен фильм");
        return inMemoryFilmService.addFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.debug("Фильм обновлен. Количество фильмов: {}", inMemoryFilmService.updateFilm(film));
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        log.debug("Вывод списка из всех фильмов");
        return new ArrayList<>(inMemoryFilmService.getAll().values());
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        log.debug("Получение фильма по id");
        return inMemoryFilmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Добавление пользователем лайка фильму");
        inMemoryFilmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Удаление пользователем лайка у фильма");
        inMemoryFilmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("Вывод самых популярных фильмов");
        return inMemoryFilmService.getPopularFilms(count);
    }
}

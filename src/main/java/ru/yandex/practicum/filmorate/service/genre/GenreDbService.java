package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.genres.GenreDao;

import java.util.ArrayList;
import java.util.List;

@Service("GenreDbService")
@RequiredArgsConstructor
public class GenreDbService implements GenreService {

    private final GenreDao genreDao;

    @Override
    public List<Genre> getGenres() {
        return new ArrayList<>(genreDao.getAll());
    }

    @Override
    public Genre getGenre(int id) {
        try {
            return genreDao.getGenre(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанра с таким id не существует");
        }
    }
}

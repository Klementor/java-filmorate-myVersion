package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.dao.genres.GenreDao;

import java.util.ArrayList;
import java.util.List;

@Service("GenreDbService")
public class GenreDbService implements GenreService {

    private final GenreDao genreDao;

    @Autowired
    public GenreDbService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> getGenres() {
        return new ArrayList<>(genreDao.getAll());
    }

    @Override
    public Genre getGenre(int id) {
        try{
            return genreDao.getGenre(id);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Жанра с таким id не существует");
        }
    }
}

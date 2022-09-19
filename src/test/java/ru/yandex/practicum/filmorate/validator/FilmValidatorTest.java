package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmValidatorTest {
    Film film;

    @BeforeEach
    void createFilm() {
        film = new Film();
        film.setId(0);
        film.setName("Титаник");
        film.setDescription("Любовная драма");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2000, 06, 5));
    }

    @Test
    void validate() {
        assertDoesNotThrow(() -> FilmValidator.validate(film));
    }

    @Test
    void validateNullName() {
        film.setName(null);
        assertThrows(RuntimeException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void validateSpaceName() {
        film.setName("  ");
        assertThrows(RuntimeException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void validateDescriptionNull() {
        film.setDescription(null);
        assertThrows(RuntimeException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void validateSpaceDescription() {
        film.setDescription("  ");
        assertThrows(RuntimeException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void validateDescriptionMoreThen200() {
        film.setDescription("Contained terminated times formerly pleasant need offer. Grave discretion wooded though " +
                "winter resolving tore face other cold left now curiosity for inquiry enjoyment. Seems were concluded " +
                "discretion aware stuff that husband pursuit truth suffer. Desirous sending law delivered spirit " +
                "admitting himself warrant peculiar shed made doubt felicity many unpacked. Improved over earnestly " +
                "twenty sentiments lively. His widen mistress marriage. ");
        assertThrows(RuntimeException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void validateReleaseDate() {
        film.setReleaseDate(LocalDate.of(1700, 3, 4));
        assertThrows(RuntimeException.class, () -> FilmValidator.validate(film));
    }

    @Test
    void validateFilmDuration() {
        film.setDuration(-20);
        assertThrows(RuntimeException.class, () -> FilmValidator.validate(film));
    }
}
package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.UserUpdateException;
import ru.yandex.practicum.filmorate.exceptions.UserValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    private User user;

    @BeforeEach
    void createUser() {
        user = new User();
        user.setId(0);
        user.setName("Mikhail");
        user.setBirthday(LocalDate.of(2001,12,21));
        user.setEmail("lala@mail.ru");
        user.setLogin("MiKrylov");
    }

    @Test
    void validate() {
        assertDoesNotThrow(() -> UserValidator.validate(user));
    }

    @Test
    void validateNullLogin() {
        user.setLogin(null);
        assertThrows(UserValidateException.class, () -> UserValidator.validate(user));
    }

    @Test
    void validateSpaceLogin() {
        user.setLogin("  ");
        assertThrows(UserValidateException.class, () -> UserValidator.validate(user));
    }

    @Test
    void validateNullEmail() {
        user.setEmail(null);
        assertThrows(UserValidateException.class, () -> UserValidator.validate(user));
    }

    @Test
    void validateEmailWithoutTheSignAt() {
        user.setEmail("lala");
        assertThrows(UserValidateException.class, () -> UserValidator.validate(user));
    }

    @Test
    void validateNUllName() throws UserValidateException {
        user.setName(null);
        UserValidator.validate(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void validateSpaceName() throws UserValidateException {
        user.setName("  ");
        UserValidator.validate(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void validateImpossibleBirthday () {
        user.setBirthday(LocalDate.of(2023, 12,21));
        assertThrows(UserValidateException.class, () -> UserValidator.validate(user));
    }
}
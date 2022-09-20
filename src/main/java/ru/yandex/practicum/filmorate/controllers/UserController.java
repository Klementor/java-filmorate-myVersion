package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserUpdateException;
import ru.yandex.practicum.filmorate.exceptions.UserValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @PostMapping
    public User create(@RequestBody User user) throws UserValidateException {
        UserValidator.validate(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.debug("Добавлен пользователь");
        log.debug("Количество пользователей: {}", users.size());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws UserUpdateException, UserValidateException {
        UserValidator.validate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Пользователь обновлен");
            log.debug("Количество пользователей: {}", users.size());
            return user;
        } else {
            log.warn("Попытка обновить информацию о пользователе которого не существует");
            throw new UserUpdateException("Попытка обновить информацию о пользователе которого не существует");
        }
    }

    @GetMapping
    public List<User> getAll() {
        log.debug("Вывод списка из всех пользователей");
        return new ArrayList<>(users.values());
    }

}

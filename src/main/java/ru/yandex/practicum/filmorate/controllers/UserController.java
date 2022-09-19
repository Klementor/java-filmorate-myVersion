package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
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

    Map<Long, User> users = new HashMap<>();
    private long id = 1;
    @PostMapping
    public User create(@RequestBody User user) {
        UserValidator.validate(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.trace("Добавлен пользователь");
        log.debug("Количество пользователей: {}", users.size());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        UserValidator.validate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.trace("Пользователь обновлен");
            log.debug("Количество пользователей: {}", users.size());
            return user;
        } else {
            log.warn("Попытка обновить информацию о пользователе которого не существует");
            throw new RuntimeException();
        }
    }

    @GetMapping
    public List<User> get() {
        log.trace("Вывод списка из всех пользователей");
        return new ArrayList<>(users.values());
    }

}

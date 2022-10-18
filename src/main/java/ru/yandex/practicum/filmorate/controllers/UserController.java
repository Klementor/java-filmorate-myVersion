package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService inMemoryUserService;

    @Autowired
    public UserController(UserService inMemoryUserService) {
        this.inMemoryUserService = inMemoryUserService;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.debug("Добавлен пользователь. Количество пользователей: {}", inMemoryUserService.addUser(user));
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.debug("Пользователь обновлен. Количество пользователей: {}", inMemoryUserService.updateUser(user));
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        log.debug("Вывод списка из всех пользователей");
        return new ArrayList<>(inMemoryUserService.getAll().values());
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.debug("Получение пользователя по id");
        return inMemoryUserService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Добавление пользователя в друзья");
        inMemoryUserService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Удаление пользователя из друзей");
        inMemoryUserService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendByIdUser(@PathVariable long id) {
        log.debug("Получение всех друзей пользователя");
        return inMemoryUserService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.debug("Получение списка друзей общих с другим пользователем");
        return inMemoryUserService.getMutualFriends(id, otherId);
    }
}

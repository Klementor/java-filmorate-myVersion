package ru.yandex.practicum.filmorate.service.user.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    Map<Long, User> getAll();

    User getUser(long id);
}
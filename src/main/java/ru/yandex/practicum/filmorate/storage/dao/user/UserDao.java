package ru.yandex.practicum.filmorate.storage.dao.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserDao {
    User addUser(User user);

    User updateUser(User user);

    List<User> getAll();

    User getUser(long id);
}

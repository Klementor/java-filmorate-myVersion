package ru.yandex.practicum.filmorate.service.user.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public User addUser(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) throws UserValidateException{
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException("Попытка обновить информацию о пользователе которого не существует");
        }
    }

    public User getUser(long id) {
        return users.get(id);
    }

    @Override
    public Map<Long, User> getAll() {
        return users;
    }
}

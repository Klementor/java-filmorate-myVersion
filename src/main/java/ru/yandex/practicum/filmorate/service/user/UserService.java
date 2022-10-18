package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User addUser(User user);

    User updateUser(User user);

    Map<Long, User> getAll();

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    List<User> getFriends(long id);

    User getUser(long id);

    List<User> getMutualFriends(long id, long otherId);
}

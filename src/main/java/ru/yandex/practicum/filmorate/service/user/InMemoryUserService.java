package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InMemoryUserService implements UserService {

    public final UserStorage inMemoryUserStorage;

    public InMemoryUserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public User addUser(User user) throws UserValidateException {
        UserValidator.validate(user);
        if (inMemoryUserStorage.getAll().get(user.getId()) != null) {
            throw new AlreadyExistsException("Данный пользователь уже существует");
        }
        return inMemoryUserStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) throws UserValidateException {
        UserValidator.validate(user);
        if (inMemoryUserStorage.getAll().get(user.getId()) == null) {
            throw new NotFoundException("Невозможно обновить несуществующего пользователя");
        }
        return inMemoryUserStorage.updateUser(user);
    }

    @Override
    public Map<Long, User> getAll() {
        return inMemoryUserStorage.getAll();
    }

    @Override
    public void addFriend(long id, long friendId) {
        if (inMemoryUserStorage.getAll().get(id) == null || inMemoryUserStorage.getAll().get(friendId) == null) {
            throw new NotFoundException("Пользователь не существует");
        }
        inMemoryUserStorage.getUser(id).getFriendsId().add(friendId);
        inMemoryUserStorage.getUser(friendId).getFriendsId().add(id);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        if (inMemoryUserStorage.getAll().get(id) == null || inMemoryUserStorage.getAll().get(friendId) == null) {
            throw new NotFoundException("Пользователь не существует");
        }
        inMemoryUserStorage.getUser(id).getFriendsId().remove(friendId);
        inMemoryUserStorage.getUser(friendId).getFriendsId().remove(id);
    }

    @Override
    public List<User> getFriends(long id) {
       return inMemoryUserStorage.getUser(id).getFriendsId().stream()
               .map(this::getUser)
               .collect(Collectors.toList());
    }

    @Override
    public User getUser(long id) {
        if (inMemoryUserStorage.getAll().get(id) == null) {
            throw new NotFoundException("Пользователь не существует");
        }
        return inMemoryUserStorage.getUser(id);
    }

    public List<User> getMutualFriends(long id, long otherId) {
        Set<Long> friendsById = inMemoryUserStorage.getAll().get(id).getFriendsId();
        Set<Long> friendsByOtherId = inMemoryUserStorage.getAll().get(otherId).getFriendsId();
        List<Long> mutualFriends = new ArrayList<>();
        for (Long friendId: friendsById) {
            if (friendsByOtherId.contains(friendId)){
                mutualFriends.add(friendId);
            }
        }
        return mutualFriends.stream().map(this::getUser).collect(Collectors.toList());
    }
}

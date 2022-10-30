package ru.yandex.practicum.filmorate.service.user;

import lombok.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.dao.friendship.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDao;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDbService implements UserService {

    private final UserDao userDao;

    private final FriendshipDao friendshipDao;

    public UserDbService(UserDao userDao, FriendshipDao friendshipDao) {
        this.userDao = userDao;
        this.friendshipDao = friendshipDao;
    }

    @Override
    public User addUser(@NonNull User user) throws UserValidateException {
        UserValidator.validate(user);
        checkUserToAdd(user);
        return userDao.addUser(user);
    }

    @Override
    public User updateUser(@NonNull User user) throws UserValidateException {
        UserValidator.validate(user);
        checkUserToUpdate(user);
        return userDao.updateUser(user);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void addFriend(long fromUserId, long toUserId) {
        checkFriendshipToAdd(fromUserId, toUserId);
        boolean mutual = false;
        if (friendshipDao.getFriends(toUserId).contains(fromUserId)) {
            mutual = true;
        }
        friendshipDao.addFriends(fromUserId, toUserId, mutual);
    }

    @Override
    public void deleteFriend(long toUserid, long fromUserId) {
        checkFriendshipToDelete(toUserid, fromUserId);
        boolean mutual = friendshipDao.getFriends(toUserid).contains(fromUserId);
        friendshipDao.removeFriend(fromUserId, toUserid, mutual);
    }

    @Override
    public List<User> getFriends(long id) {
        return friendshipDao.getFriends(id).stream()
                .map(this::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public User getUser(long id) {
        try {
            userDao.getUser(id);
            return userDao.getUser(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не существует");
        }
    }

    public List<User> getMutualFriends(long id, long otherId) {
        checkMutualFriends(id, otherId);
        Set<Long> friends1 = friendshipDao.getFriends(id);
        Set<Long> friends2 = friendshipDao.getFriends(otherId);
        List<Long> friendsResult = new ArrayList<>();
        for (Long friend1Id : friends1) {
            for (Long friend2Id : friends2) {
                if (friend1Id.equals(friend2Id)) {
                    friendsResult.add(friend1Id);
                    break;
                }
            }
        }
        return friendsResult.stream()
                .map(userDao::getUser)
                .collect(Collectors.toList());
    }

    private void checkUserToAdd(User user) {
        if (user.getId() != 0) {
            throw new AlreadyExistsException("Данный пользователь уже существует");
        }
    }

    private void checkUserToUpdate(User user) {
        try {
            userDao.getUser(user.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Невозможно обновить несуществующего пользователя");
        }
    }

    private void checkFriendshipToAdd(long fromUserId, long toUserId) {
        try {
            userDao.getUser(fromUserId);
            userDao.getUser(toUserId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не существует");
        }
        if (friendshipDao.getFriends(toUserId).contains(fromUserId)) {
            throw new AlreadyExistsException("Данные пользователи уже в друзьях");
        }
        if (fromUserId == toUserId) {
            throw new RuntimeException("Невозможно добавить самого себя в друзья");
        }
    }

    private void checkFriendshipToDelete(long fromUserId, long toUserId) {
        try {
            userDao.getUser(fromUserId);
            userDao.getUser(toUserId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не существует");
        }
        if (friendshipDao.getFriends(toUserId).contains(fromUserId)) {
            throw new NotFoundException("Данные пользователи не в друзьях");
        }
        if (fromUserId == toUserId) {
            throw new RuntimeException("Невозможно добавить самого себя в друзья");
        }
    }

    private void checkMutualFriends(long id, long otherId) {
        try {
            userDao.getUser(id);
            userDao.getUser(otherId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не существует");
        }
        if (id == otherId) {
            throw new RuntimeException("Нельзя запрашивать общих друзей у 1 пользователя");
        }
    }
}

package ru.yandex.practicum.filmorate.storage.dao.friendship;

import java.util.Set;

public interface FriendshipDao {
    void addFriends(long from_user_id, long to_user_id, boolean mutual);

    void removeFriend(long from_user_id, long to_user_id, boolean mutual);

    Set<Long> getFriends(long to_user_id);
}

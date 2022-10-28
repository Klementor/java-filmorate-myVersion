package ru.yandex.practicum.filmorate.storage.dao.like;

import java.util.List;

public interface LikeDao {
    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);

    long countLikes(long filmId);

    List<Long> getUserWhoLikes(long filmId);
}

package ru.yandex.practicum.filmorate.storage.dao.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.mappers.LikeMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component("LikeDaoImpl")
@RequiredArgsConstructor
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(long userId, long filmId) {
        jdbcTemplate.update(""
                + "INSERT INTO likes(user_id, film_id) "
                + "VALUES (?, ?)", userId, filmId);
    }

    @Override
    public void deleteLike(long userId, long filmId) {
        jdbcTemplate.update(""
                + "DELETE FROM likes "
                + "WHERE film_id=? AND user_id=?", filmId, userId);
    }

    @Override
    public long countLikes(long filmId) {
        Long count = jdbcTemplate.queryForObject(String.format(""
                + "SELECT COUNT(user_id) "
                + "FROM likes "
                + "WHERE film_id=%d", filmId), Long.class);
        return (count == null) ? 0 : count;
    }

    @Override
    public List<Long> getUserWhoLikes(long filmId) {
        return jdbcTemplate.query(String.format(""
                                + "SELECT user_id "
                                + "FROM likes "
                                + "WHERE film_id=%d", filmId),
                        new LikeMapper()).stream()
                .map(Like::getUserId)
                .collect(Collectors.toList());
    }
}

package ru.yandex.practicum.filmorate.storage.dao.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component("LikeDaoImpl")
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    static private class LikeMapper implements RowMapper<Like> {

        @Override
        public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
            Like like = new Like();
            like.setFilmId(rs.getLong("film_id"));
            like.setUserId(rs.getLong("user_id"));
            return like;
        }
    }
}

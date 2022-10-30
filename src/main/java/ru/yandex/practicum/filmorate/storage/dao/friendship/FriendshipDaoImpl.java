package ru.yandex.practicum.filmorate.storage.dao.friendship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

@Component("FriendshipDaoImpl")
public class FriendshipDaoImpl implements FriendshipDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriends(long to_user_id, long from_user_id, boolean mutual) {
        jdbcTemplate.update(""
                + "INSERT INTO friendships(from_user_id, to_user_id, mutual) "
                + "VALUES (?, ?, ?)", from_user_id, to_user_id, mutual);
    }

    @Override
    public void removeFriend(long from_user_id, long to_user_id, boolean mutual) {
        jdbcTemplate.update(""
                + "DELETE FROM friendships "
                + "WHERE from_user_id=? AND to_user_id=?", from_user_id, to_user_id);
        if (mutual) {
            jdbcTemplate.update(""
                    + "UPDATE FRIENDSHIPS "
                    + "SET mutual=FALSE "
                    + "WHERE from_user_id=? AND to_user_id=?", to_user_id, from_user_id);
        }
    }

    @Override
    public Set<Long> getFriends(long to_user_id) {
        return jdbcTemplate.query(String.format(""
                        + "SELECT from_user_id, to_user_id, mutual "
                        + "FROM friendships "
                        + "WHERE to_user_id=%d", to_user_id), new FriendshipsMapper()
                ).stream()
                .map(Friendship::getFromUserId)
                .collect(Collectors.toSet());
    }

    static private class FriendshipsMapper implements RowMapper<Friendship> {
        @Override
        public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
            Friendship friendship = new Friendship();
            friendship.setFromUserId(rs.getLong("from_user_id"));
            friendship.setToUserId(rs.getLong("to_user_id"));
            friendship.setMutual(rs.getBoolean("mutual"));
            return friendship;
        }
    }
}

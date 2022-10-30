package ru.yandex.practicum.filmorate.storage.dao.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;

import java.sql.Date;
import java.util.List;

import static java.lang.String.format;

@Component("UserDbStorage")
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        jdbcTemplate.update(""
                        + "INSERT INTO users(email, login, name, birthday) "
                        + "VALUES (?,?,?,?)",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()));
        return jdbcTemplate.queryForObject(format(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users "
                + "WHERE login='%s'", user.getLogin()), new UserMapper());
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(""
                        + "UPDATE users "
                        + "SET email=?, login=?, name=?, birthday=? "
                        + "WHERE user_id=?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        return getUser(user.getId());
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users", new UserMapper());
    }

    @Override
    public User getUser(long id) {
        return jdbcTemplate.queryForObject(format(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users "
                + "WHERE user_id=%d", id), new UserMapper());
    }
}

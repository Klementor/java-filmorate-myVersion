package ru.yandex.practicum.filmorate.model.user;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friendsId = new HashSet<>();
}
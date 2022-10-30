package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {
    private long id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    @NonNull
    private String name;
    @NonNull
    private LocalDate birthday;
    @NonNull
    private Set<Long> friendsId = new HashSet<>();
}

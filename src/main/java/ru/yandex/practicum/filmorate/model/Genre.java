package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
}

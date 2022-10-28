package ru.yandex.practicum.filmorate.model.film;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @NonNull
    private Integer genreId;
    @NonNull
    private String name;
}

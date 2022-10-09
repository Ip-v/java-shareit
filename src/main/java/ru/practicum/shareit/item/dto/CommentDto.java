package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.utils.Create;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class CommentDto {

    private Long id;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String text;
    private String authorName;
    private LocalDateTime created;
}


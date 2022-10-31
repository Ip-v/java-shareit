package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * ItemRequest DTO
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private Long requesterId;
    private LocalDateTime created;
    private List<ItemInfoRequestDto> items;

    /**
     * Item DTO
     */
    @Getter
    @AllArgsConstructor
    public static class ItemInfoRequestDto {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long owner;
        private Long requestId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ItemInfoRequestDto itemDto = (ItemInfoRequestDto) o;
            return Objects.equals(id, itemDto.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}

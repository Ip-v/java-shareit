package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Репозиторий предмет араенды.
 */
@Component
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select item from Item item " +
            "where item.isAvailable = true " +
            "and (lower(item.name) like lower(CONCAT('%',?1,'%')) " +
            "or lower(item.description) like lower(CONCAT('%',?1,'%')))")
    List<Item> search(String text, Pageable pageRequest);

    @Query("select item from Item item " +
            "where item.request.id = ?1")
    List<Item> findItemByRequestId(Long id);
}

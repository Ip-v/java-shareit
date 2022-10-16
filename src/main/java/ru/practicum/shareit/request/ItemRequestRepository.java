package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

/**
 * Репозиторий запросов
 */
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query("select r from ItemRequest r where r.requester.id = ?1")
    List<ItemRequest> findUserRequests(Long userId);

    @Query("select r from ItemRequest r where r.requester.id <> ?1 ")
    List<ItemRequest> findRequests(Long userId, PageRequest pageRequest);
}

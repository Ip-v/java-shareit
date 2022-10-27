package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService service;
    @MockBean
    private CommentService commentService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private final User user = new User(1L, "owner", "owner@mail.ru");
    private final Item item = new Item(1L, "name", "description", true, user, null);
    private final ItemDto itemDto = ItemMapper.toItemDto(item);
    private final ItemInfoDto itemInfoDto = ItemMapper.itemInfoDto(item);
    private final User commentator = new User(2L, "commentator", "commentator@mail.ru");
    private final Comment comment = new Comment(1L, "comment", item, commentator,
            LocalDateTime.of(2022, Month.OCTOBER, 22, 11, 11, 11));
    private final CommentDto commentDto = CommentMapper.toCommentDto(comment);

    @Test
    void create() throws Exception {
        when(service.create(anyLong(), any())).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", nullValue()));
    }

    @Test
    void update() throws Exception {
        when(service.update(anyLong(), anyLong(), any())).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", String.valueOf(item.getId()))
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", nullValue()));
    }

    @Test
    void getTest() throws Exception {
        when(service.get(anyLong(), anyLong())).thenReturn(itemInfoDto);

        mockMvc.perform(get("/items/{itemId}", String.valueOf(item.getId()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemInfoDto.getName())))
                .andExpect(jsonPath("$.description", is(itemInfoDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemInfoDto.getAvailable())))
                .andExpect(jsonPath("$.nextBooking", nullValue()))
                .andExpect(jsonPath("$.lastBooking", nullValue()));
    }

    @Test
    void getAll() throws Exception {
        when(service.getAll(1L, PageRequest.ofSize(10))).thenReturn(List.of(itemInfoDto));

        mockMvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemInfoDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemInfoDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemInfoDto.getAvailable())))
                .andExpect(jsonPath("$.[0].nextBooking", nullValue()))
                .andExpect(jsonPath("$.[0].lastBooking", nullValue()));
    }

    @Test
    void search() throws Exception {
        when(service.search(1L, "search string", PageRequest.ofSize(10)))
                .thenReturn(List.of(itemDto));
        mockMvc.perform(get("/items/search")
                        .param("text", "search string")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.[0].requestId", nullValue()));
    }

    @Test
    void createComment() throws Exception {
        when(commentService.createComment(any(), anyLong(), anyLong())).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", String.valueOf(item.getId()))
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthor().getName())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated().toString())));
    }
}
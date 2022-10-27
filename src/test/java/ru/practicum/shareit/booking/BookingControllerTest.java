package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private BookingInfoDto bookingInfoDto;
    private BookingDto bookingDto;

    @BeforeEach
    void beforeEach() {
        User booker = new User(1L, "owner", "owner@mail.ru");
        Item item = new Item(1L, "name", "description", true, booker, null);
        BookingInfoDto.ItemInfo itemInfo = new BookingInfoDto.ItemInfo(item);
        bookingInfoDto = new BookingInfoDto(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, booker,
                itemInfo, BookingStatus.WAITING);
        bookingDto = new BookingDto(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, 1L,
                1L, BookingStatus.WAITING);
    }

    @Test
    void create() throws Exception {
        when(service.create(anyLong(), any())).thenReturn(bookingInfoDto);

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingInfoDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingInfoDto.getStatus().toString())))
                .andExpect(jsonPath("$.item.name", is(bookingInfoDto.getItem().getName())));
    }

    @Test
    void approveBooking() throws Exception {
        when(service.approveBooking(1L, 1L, true)).thenReturn(bookingInfoDto);

        mockMvc.perform(patch("/bookings/" + bookingInfoDto.getId())
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingInfoDto.getStatus()
                        .toString())));
    }

    @Test
    void getOwnerBookings() throws Exception {
        when(service.getOwnerBookings(anyLong(), any(), any()))
                .thenReturn(List.of(bookingInfoDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "")
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getBooking() throws Exception {
        when(service.getAll(anyLong(), any(), any()))
                .thenReturn(List.of(bookingInfoDto));

        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "")
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getBookingWrongState() throws Exception {
        when(service.getAll(anyLong(), any(), any()))
                .thenReturn(List.of(bookingInfoDto));

        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "WRONG")
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getBookingById() throws Exception {
        when(service.getBookingById(any(), any())).thenReturn(bookingInfoDto);

        mockMvc.perform(get("/bookings/" + bookingInfoDto.getId())
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingInfoDto.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingInfoDto.getStatus().toString())))
                .andExpect(jsonPath("$.item.name", is(bookingInfoDto.getItem().getName())));
    }
}
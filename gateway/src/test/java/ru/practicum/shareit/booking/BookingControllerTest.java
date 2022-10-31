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
import ru.practicum.shareit.booking.model.BookingStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingClient client;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private BookingDto bookingDto;

    @BeforeEach
    void beforeEach() {
        bookingDto = new BookingDto(1L, LocalDateTime.now().plusMinutes(10), LocalDateTime.MAX, 1L,
                1L, BookingStatus.WAITING);
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createBookingWithEndInPastError() throws Exception {
        BookingDto booking = new BookingDto();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().minusHours(1));

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createBookingWithStartInPastError() throws Exception {
        BookingDto booking = new BookingDto();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().minusHours(1));
        booking.setEnd(LocalDateTime.now().plusMinutes(10));

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void approveBooking() throws Exception {
        mockMvc.perform(patch("/bookings/" + bookingDto.getId())
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getOwnerBookings() throws Exception {
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "")
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().isOk());
    }

    @Test
    void getBooking() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "")
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingWrongState() throws Exception {
        mockMvc.perform(get("/bookings/")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "WRONG")
                        .param("from", "")
                        .param("size", ""))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getBookingById() throws Exception {
        mockMvc.perform(get("/bookings/" + bookingDto.getId())
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getWithNegativeFromError() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "")
                        .param("from", "-1")
                        .param("size", ""))
                .andExpect(status().isBadRequest());
    }
}
package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext
public class UserServiceIntegrationTest {
    private final EntityManager em;
    private final UserService service;

    @Test
    void getAllUsers() {
        em.createNativeQuery("insert into users (user_name, user_email) values (?,?)")
                .setParameter(1, "user1")
                .setParameter(2, "user1@mail.ru")
                .executeUpdate();
        em.createNativeQuery("insert into users (user_name, user_email) values (?,?)")
                .setParameter(1, "user2")
                .setParameter(2, "user2@mail.ru")
                .executeUpdate();
        User userOwner = new User(1L, "user1", "user1@mail.ru");
        User userBooker = new User(2L, "user2", "user2@mail.ru");
        assertThat(service.getAll(), equalTo(List.of(UserMapper.toUserDto(userOwner), UserMapper.toUserDto(userBooker))));
    }
}


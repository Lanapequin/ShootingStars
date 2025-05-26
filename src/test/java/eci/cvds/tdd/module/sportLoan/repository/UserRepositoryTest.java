package eci.cvds.tdd.module.sportLoan.repository;

import eci.cvds.tdd.module.sportLoan.enums.Role;
import eci.cvds.tdd.module.sportLoan.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        user = new User();
        user.setId("123");
        user.setRole(Role.STUDENT);
        userRepository.save(user);
    }

    @Test
    public void testFindUserById() {
        User found = userRepository.findUserById("123");
        assertNotNull(found);
        assertEquals("123", found.getId());
    }

    @Test
    public void testFindUserByIdNotFound() {
        User notFound = userRepository.findUserById("999");
        assertNull(notFound);
    }
}

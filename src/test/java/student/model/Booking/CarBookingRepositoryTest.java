package student.model.Booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.model.Car.Brand;
import student.model.Car.Car;
import student.model.User.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CarBookingRepository.
 */
class CarBookingRepositoryTest {

    private CarBookingRepository repository;
    private CarBooking booking1;
    private CarBooking booking2;

    @BeforeEach
    void setUp() {
        repository = new CarBookingRepository();

        booking1 = new CarBooking(
                UUID.randomUUID(),
                new User(UUID.randomUUID(), "Alice"),
                new Car("ABC123", new BigDecimal(50), Brand.AUDI, false),
                LocalDateTime.now()
        );

        booking2 = new CarBooking(
                UUID.randomUUID(),
                new User(UUID.randomUUID(), "Bob"),
                new Car("XYZ789", new BigDecimal(55), Brand.VW, false),
                LocalDateTime.now()
        );
    }

    @Test
    void testBookAndGetCarBookings() {
        assertTrue(repository.getCarBookings().isEmpty(), "Should start empty");

        repository.book(booking1);
        repository.book(booking2);

        List<CarBooking> all = repository.getCarBookings();
        assertEquals(2, all.size());
        assertTrue(all.contains(booking1));
        assertTrue(all.contains(booking2));
    }

    @Test
    void testCancelCarBooking() {
        repository.book(booking1);
        repository.book(booking2);

        repository.cancelCarBooking(booking1.getBookingId());
        List<CarBooking> all = repository.getCarBookings();

        // booking1 removed
        assertFalse(all.contains(booking1));
        // booking2 remains
        assertTrue(all.contains(booking2));
    }
}
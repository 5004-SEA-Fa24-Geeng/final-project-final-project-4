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

class CarBookingRepositoryTest {

    private CarBookingRepository repository;
    private CarBooking booking;
    private UUID bookingId;
    private Car car;
    private User user;

    @BeforeEach
    void setUp() {
        repository = new CarBookingRepository();
        bookingId = UUID.randomUUID();
        user = new User(UUID.randomUUID(), "Alice");
        car = new Car("CAR001", new BigDecimal("88.88"), Brand.TESLA, true, "Model X");
        booking = new CarBooking(bookingId, user, car, LocalDateTime.now());
    }

    @Test
    void shouldAddBookingSuccessfully() {
        User user = new User(UUID.randomUUID(), "testUser");
        Car car = new Car("CAR1000", new BigDecimal("100"), Brand.TESLA, true, "Model X");

        CarBookingRepository repo = new CarBookingRepository();
        UUID bookingId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        CarBooking booking = new CarBooking(bookingId, user, car, now);

        repo.book(booking);

        CarBooking found = repo.getCarBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);

        assertNotNull(found);
        assertEquals(user.getId(), found.getUser().getId());
        assertEquals(car.getRegNumber(), found.getCar().getRegNumber());
    }

    @Test
    void shouldCancelBookingSuccessfully() {
        repository.book(booking);
        assertFalse(booking.isCanceled());

        repository.cancelCarBooking(bookingId);

        // 从仓库中重新取出
        CarBooking canceled = repository.getCarBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow();

        assertTrue(canceled.isCanceled());
    }

    @Test
    void shouldThrowExceptionWhenCancelNonexistentBooking() {
        UUID fakeId = UUID.randomUUID();

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            repository.cancelCarBooking(fakeId);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }
}
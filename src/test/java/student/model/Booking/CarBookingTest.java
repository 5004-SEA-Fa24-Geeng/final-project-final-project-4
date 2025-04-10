package student.model.Booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.model.Car.Brand;
import student.model.Car.Car;
import student.model.User.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarBookingTest {

    private UUID bookingId;
    private User user;
    private Car car;
    private LocalDateTime bookingTime;
    private CarBooking booking;

    @BeforeEach
    void setUp() {
        bookingId = UUID.randomUUID();
        user = new User(UUID.randomUUID(), "Test User");
        car = new Car("ABC123", new BigDecimal(91), Brand.TESLA, true);
        bookingTime = LocalDateTime.now();

        booking = new CarBooking(bookingId, user, car, bookingTime);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(bookingId, booking.getBookingId());
        assertEquals(user, booking.getUser());
        assertEquals(car, booking.getCar());
        assertEquals(bookingTime, booking.getBookingTime());
        assertFalse(booking.isCanceled());
    }

    @Test
    void testSetCanceled() {
        booking.setCanceled(true);
        assertTrue(booking.isCanceled());

        booking.setCanceled(false);
        assertFalse(booking.isCanceled());
    }

    @Test
    void testToStringContainsValues() {
        String str = booking.toString();
        assertTrue(str.contains("CarBooking"));
        assertTrue(str.contains(user.getName()));
        assertTrue(str.contains(car.getRegNumber()));
    }

    @Test
    void testEqualsAndHashCode() {
        CarBooking booking2 = new CarBooking(bookingId, user, car, bookingTime);
        assertEquals(booking, booking2);
        assertEquals(booking.hashCode(), booking2.hashCode());
    }

    @Test
    void testNotEqualsWithDifferentBookingId() {
        CarBooking different = new CarBooking(UUID.randomUUID(), user, car, bookingTime);
        assertNotEquals(booking, different);
    }
}
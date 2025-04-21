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

    private CarBooking booking;
    private UUID bookingId;
    private User user;
    private Car car;
    private LocalDateTime time;

    @BeforeEach
    void setUp() {
        bookingId = UUID.randomUUID();
        user = new User(UUID.randomUUID(), "Alice");
        car = new Car("REG888", new BigDecimal("120.00"), Brand.TESLA, true, "Model 3");
        time = LocalDateTime.now();

        booking = new CarBooking(bookingId, user, car, time);
    }

    @Test
    void getBookingId() {
        assertEquals(bookingId, booking.getBookingId());
    }

    @Test
    void getUser() {
        assertEquals(user, booking.getUser());
    }

    @Test
    void getCar() {
        assertEquals(car, booking.getCar());
    }

    @Test
    void getBookingTime() {
        assertEquals(time, booking.getBookingTime());
    }

    @Test
    void isCanceled() {
        assertFalse(booking.isCanceled());
    }

    @Test
    void setCanceled() {
        booking.setCanceled(true);
        assertTrue(booking.isCanceled());
    }

    @Test
    void testToString() {
        String str = booking.toString();
        assertTrue(str.contains("CarBooking"));
        assertTrue(str.contains("bookingId=" + bookingId));
        assertTrue(str.contains("user="));
        assertTrue(str.contains("car="));
    }

    @Test
    void testEquals() {
        CarBooking another = new CarBooking(bookingId, user, car, time);
        assertEquals(booking, another);

        CarBooking different = new CarBooking(UUID.randomUUID(), user, car, time);
        assertNotEquals(booking, different);
    }

    @Test
    void testHashCode() {
        CarBooking another = new CarBooking(bookingId, user, car, time);
        assertEquals(booking.hashCode(), another.hashCode());
    }
}
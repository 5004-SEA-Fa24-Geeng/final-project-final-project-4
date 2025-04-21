package student.model.Booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.model.Car.*;
import student.model.User.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarBookingServiceTest {

    private CarBookingService bookingService;
    private CarBookingRepository bookingRepository;
    private CarService carService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        bookingRepository = new CarBookingRepository();
        carService = new CarService(new CarFileRepository());
        bookingService = new CarBookingService(bookingRepository, carService);
        userService = new UserService(new UserArrayRepository());
    }

    @Test
    void bookCar() {
        User user = userService.register("Tom");
        Car car = bookingService.getAvailableCars().get(0);

        UUID bookingId = bookingService.bookCar(user, car.getRegNumber());
        assertNotNull(bookingId);

        CarBooking booking = bookingService.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow();

        assertEquals(user.getId(), booking.getUser().getId());
        assertEquals(car.getRegNumber(), booking.getCar().getRegNumber());
        assertFalse(booking.isCanceled());
    }

    @Test
    void getUserBookedCars() {
        User user = userService.register("Anna");
        Car car = bookingService.getAvailableCars().get(0);
        bookingService.bookCar(user, car.getRegNumber());

        List<Car> cars = bookingService.getUserBookedCars(user.getId());
        assertEquals(1, cars.size());
        assertEquals(car.getRegNumber(), cars.get(0).getRegNumber());
    }

    @Test
    void getAvailableCars() {
        User user = userService.register("Tom");
        Car car = bookingService.getAvailableCars().get(0);
        bookingService.bookCar(user, car.getRegNumber());

        List<Car> available = bookingService.getAvailableCars();
        assertFalse(available.contains(car));
    }

    @Test
    void getAvailableElectricCars() {
        List<Car> allElectric = carService.getAllCars().stream()
                .filter(Car::isElectric)
                .toList();

        List<Car> available = bookingService.getAvailableElectricCars();

        assertTrue(allElectric.containsAll(available));
    }

    @Test
    void getBookings() {
        User user = userService.register("Mark");
        Car car = bookingService.getAvailableCars().get(0);
        bookingService.bookCar(user, car.getRegNumber());

        List<CarBooking> bookings = bookingService.getBookings();

        boolean hasBooking = bookings.stream()
                .anyMatch(b -> b.getUser().getId().equals(user.getId()) &&
                        b.getCar().getRegNumber().equals(car.getRegNumber()));

        assertTrue(hasBooking);
    }

    @Test
    void cancelBooking() {
        User user = userService.register("John");
        Car car = bookingService.getAvailableCars().get(0);
        UUID bookingId = bookingService.bookCar(user, car.getRegNumber());

        bookingService.cancelBooking(bookingId);

        CarBooking booking = bookingService.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow();

        assertTrue(booking.isCanceled());
    }
}
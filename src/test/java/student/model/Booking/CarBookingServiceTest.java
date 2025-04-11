package student.model.Booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.model.Car.Car;
import student.model.Car.CarService;
import student.model.User.User;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests CarBookingService using only real (in-memory) repositories and services.
 * No Mockito or mocking is used.
 */
class CarBookingServiceTest {

    private CarBookingRepository bookingRepository;
    private CarRepository carRepository;
    private CarService carService;
    private CarBookingService bookingService;

    private Car testCar;
    private Car electricCar;
    private User testUser;

    @BeforeEach
    void setUp() {
        // 1) Create real in-memory repositories
        bookingRepository = new CarBookingRepository();
        carRepository = new CarRepository();

        // 2) Create a real CarService using carRepository
        carService = new CarService(carRepository);

        // 3) Create the CarBookingService
        bookingService = new CarBookingService(bookingRepository, carService);

        // 5) Sample user
        testUser = new User(UUID.randomUUID(), "Alice");
    }

    @Test
    void testBookCar_Success() {
        // Book a car that's available
        UUID bookingId = bookingService.bookCar(testUser, "1234");

        // Check results
        var bookings = bookingService.getBookings();
        assertEquals(1, bookings.size(), "One booking should exist");
        assertEquals(bookingId, bookings.get(0).getBookingId());
        assertEquals(testUser, bookings.get(0).getUser());
    }

    @Test
    void testBookCar_AlreadyBooked() {
        // First booking
        bookingService.bookCar(testUser, "1234");

        // Try booking the same car again => should fail
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> bookingService.bookCar(testUser, "1234")
        );
        assertTrue(ex.getMessage().contains("not available"));
    }

    @Test
    void testCancelBooking() {
        // Book a car, then cancel it
        UUID bookingId = bookingService.bookCar(testUser, "1234");
        bookingService.cancelBooking(bookingId);

        // The booking is removed => check the repository
        assertTrue(bookingService.getBookings().isEmpty(),
                "Booking should be removed after cancellation");

        // Car is now available again
        List<Car> availableCars = bookingService.getAvailableCars();
        assertTrue(availableCars.size() == 4,
                "Car should be back in the available list");
    }

    @Test
    void testGetUserBookedCars() {
        // Book the electric car
        bookingService.bookCar(testUser, "1234");

        // Should be exactly one booked car for this user
        List<Car> userCars = bookingService.getUserBookedCars(testUser.getId());
        assertEquals(1, userCars.size());
        assertEquals("1234", userCars.get(0).getRegNumber());
    }

    @Test
    void testGetAvailableCars() {
        // Book the non-electric car
        bookingService.bookCar(testUser, "1234");

        // So only the electric car is left
        List<Car> available = bookingService.getAvailableCars();
        assertEquals(3, available.size());
        assertEquals("5678", available.get(0).getRegNumber());
    }

    @Test
    void testGetAvailableElectricCars() {
        // Book the electric car
        bookingService.bookCar(testUser, "1234");

        // Then none electric left
        List<Car> electrics = bookingService.getAvailableElectricCars();
        assertTrue(electrics.size() == 1);
    }
}
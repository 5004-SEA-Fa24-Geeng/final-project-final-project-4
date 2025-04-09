package student.model.Booking;

import student.model.Car.Car;
import student.model.Car.CarService;
import student.model.User.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Service class to handle car booking logic.
 * Provides methods for booking, cancellation, and availability checking.
 */
public class CarBookingService {

    private final CarBookingRepository carBookingRepository;
    private final CarService carService;

    /**
     * Constructs a CarBookingService.
     *
     * @param carBookingRepository Repository to store bookings
     * @param carService           Car service to retrieve cars
     */
    public CarBookingService(CarBookingRepository carBookingRepository, CarService carService) {
        this.carBookingRepository = carBookingRepository;
        this.carService = carService;
    }

    /**
     * Books a car for a user by registration number.
     *
     * @param user       The user making the booking
     * @param regNumber  Registration number of the car
     * @return Booking ID if successful
     * @throws IllegalStateException if car is already booked or not available
     */
    public UUID bookCar(User user, String regNumber) {
        List<Car> availableCars = getAvailableCars();

        for (Car car : availableCars) {
            if (car.getRegNumber().equals(regNumber)) {
                UUID bookingId = UUID.randomUUID();
                CarBooking booking = new CarBooking(bookingId, user, car, LocalDateTime.now());
                carBookingRepository.book(booking);
                return bookingId;
            }
        }

        throw new IllegalStateException("Already booked or car with regNumber " + regNumber + " not available.");
    }

    /**
     * Gets a list of cars booked by a specific user.
     * @param userId The user's ID
     * @return List of cars the user has booked (not canceled)
     */
    public List<Car> getUserBookedCars(UUID userId) {
        List<Car> result = new ArrayList<>();
        for (CarBooking booking : carBookingRepository.getCarBookings()) {
            if (!booking.isCanceled() && booking.getUser().getId().equals(userId)) {
                result.add(booking.getCar());
            }
        }
        return result;
    }

    /**
     * Returns all currently available (not booked) cars.
     * @return List of available cars
     */
    public List<Car> getAvailableCars() {
        return getAvailableCars(carService.getAllCars());
    }

    /**
     * Returns all available electric cars.
     * @return List of available electric cars
     */
    public List<Car> getAvailableElectricCars() {
        return getAvailableCars(carService.getAllElectricCars());
    }

    /**
     * Returns a filtered list of cars that are not currently booked.
     *
     * @param cars The full list of cars to filter from
     * @return List of available (not booked) cars
     */
    private List<Car> getAvailableCars(List<Car> cars) {
        if (cars.isEmpty()) return Collections.emptyList();

        List<CarBooking> bookings = carBookingRepository.getCarBookings();
        List<Car> available = new ArrayList<>();

        for (Car car : cars) {
            boolean booked = bookings.stream()
                    .anyMatch(b -> !b.isCanceled() && b.getCar().equals(car));
            if (!booked) available.add(car);
        }

        return available;
    }

    /**
     * Returns all car bookings in the system.
     * @return List of all car bookings
     */
    public List<CarBooking> getBookings() {
        return carBookingRepository.getCarBookings();
    }

    /**
     * Cancels a car booking based on its ID.
     * @param bookingId The booking ID to cancel
     */
    public void cancelBooking(UUID bookingId) {
        carBookingRepository.cancelCarBooking(bookingId);
    }
}
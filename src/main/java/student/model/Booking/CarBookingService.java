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
 * <p>
 * Provides methods for booking, cancellation, and availability checking
 * by interacting with both {@link CarBookingRepository} and
 * {@link CarService}.
 * </p>
 */
public class CarBookingService {

    private final CarBookingRepository carBookingRepository;
    private final CarService carService;

    /**
     * Constructs a CarBookingService with a specified repository and car service.
     *
     * @param carBookingRepository Repository to store car bookings
     * @param carService           Service to retrieve car information
     */
    public CarBookingService(CarBookingRepository carBookingRepository, CarService carService) {
        this.carBookingRepository = carBookingRepository;
        this.carService = carService;
    }

    /**
     * Books a car for a given user by registration number.
     * <p>
     * It first fetches a list of all currently available cars, then checks
     * if the given registration number matches one of them. If so, it
     * creates a new {@link CarBooking} and saves it.
     * </p>
     *
     * @param user      The user who wants to book the car
     * @param regNumber The car's registration number
     * @return The unique booking ID if booking is successful
     * @throws IllegalStateException If the car is already booked or not available
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
     * Retrieves a list of cars booked by a specific user (that are not canceled).
     *
     * @param userId The unique ID of the user
     * @return A list of cars the user has booked and not canceled
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
     *
     * @return A list of all available cars
     */
    public List<Car> getAvailableCars() {
        return getAvailableCars(carService.getAllCars());
    }

    /**
     * Returns all available electric cars (i.e., not booked and is electric).
     *
     * @return A list of available electric cars
     */
    public List<Car> getAvailableElectricCars() {
        return getAvailableCars(carService.getAllElectricCars());
    }

    /**
     * Internal method to filter out cars that are currently booked (not canceled).
     *
     * @param cars The full list of cars to filter
     * @return A list of cars that are not currently booked
     */
    private List<Car> getAvailableCars(List<Car> cars) {
        if (cars.isEmpty()) return Collections.emptyList();

        List<CarBooking> bookings = carBookingRepository.getCarBookings();
        List<Car> available = new ArrayList<>();

        for (Car car : cars) {
            boolean booked = bookings.stream()
                    .anyMatch(b -> !b.isCanceled() && b.getCar().equals(car));
            if (!booked) {
                available.add(car);
            }
        }
        return available;
    }

    /**
     * Returns all car bookings in the system (including canceled ones).
     *
     * @return A list of all car bookings
     */
    public List<CarBooking> getBookings() {
        return carBookingRepository.getCarBookings();
    }

    /**
     * Cancels a car booking identified by its unique booking ID.
     * <p>
     * This method first looks up the booking. If the booking is found and
     * not yet canceled, it calls {@code carBookingRepository.cancelCarBooking(bookingId)}
     * to finalize the cancellation. If the booking is already canceled,
     * or not found at all, an exception is thrown.
     * </p>
     *
     * @param bookingId The unique booking ID to cancel
     * @throws IllegalStateException If booking is already canceled or not found
     */
    public void cancelBooking(UUID bookingId) {
        for (CarBooking booking : carBookingRepository.getCarBookings()) {
            if (booking.getBookingId().equals(bookingId)) {
                if (booking.isCanceled()) {
                    throw new IllegalStateException("Booking already canceled.");
                }
                carBookingRepository.cancelCarBooking(bookingId);
                return;
            }
        }
        throw new IllegalStateException("Booking not found.");
    }
}
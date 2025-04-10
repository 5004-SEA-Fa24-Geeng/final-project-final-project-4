package student.model.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository class to manage car booking records in memory.
 * Acts as a simple storage for booking data.
 */
public class CarBookingRepository {

    private final List<CarBooking> carBookings = new ArrayList<>();

    /**
     * Returns all current bookings.
     * @return List of all car bookings
     */
    public List<CarBooking> getCarBookings() {
        return carBookings;
    }

    /**
     * Adds a new booking to the repository.
     * @param carBooking The car booking to be added
     */
    public void book(CarBooking carBooking) {
        carBookings.add(carBooking);
    }

    /**
     * Cancels a booking by removing it from the list.
     * @param bookingId The ID of the booking to be canceled
     */
    public void cancelCarBooking(UUID bookingId) {
        carBookings.removeIf(b -> b.getBookingId().equals(bookingId));
    }
}
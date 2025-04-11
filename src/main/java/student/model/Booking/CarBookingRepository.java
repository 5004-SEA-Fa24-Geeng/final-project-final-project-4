package student.model.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Repository class for managing {@link CarBooking} records in memory.
 * <p>
 * This class provides methods to add new bookings and mark existing
 * bookings as canceled. All bookings are stored in a static list,
 * which serves as an in-memory storage.
 * </p>
 *
 * <p><strong>Note:</strong> Since a static list is used, this data
 * is shared across the entire application lifetime and will be lost
 * once the application stops.</p>
 */
public class CarBookingRepository {

    /**
     * Static list to store all {@link CarBooking} records.
     * Acts as an in-memory database for car bookings.
     */
    private static final List<CarBooking> carBookings = new ArrayList<>();

    /**
     * Retrieves all existing {@link CarBooking} records.
     *
     * @return a {@link List} of all current car bookings
     */
    public List<CarBooking> getCarBookings() {
        return carBookings;
    }

    /**
     * Adds a new {@link CarBooking} record to the repository.
     *
     * @param carBooking the booking to be stored
     */
    public void book(CarBooking carBooking) {
        carBookings.add(carBooking);
    }

    /**
     * Marks an existing booking as canceled, identified by its unique ID.
     * If the booking cannot be found, throws an exception.
     *
     * @param bookingId the unique identifier of the booking to cancel
     * @throws IllegalStateException if no booking with the given ID is found
     */
    public void cancelCarBooking(UUID bookingId) {
        for (CarBooking booking : carBookings) {
            if (booking.getBookingId().equals(bookingId)) {
                booking.setCanceled(true);
                return;
            }
        }
        throw new IllegalStateException("Booking with ID " + bookingId + " not found.");
    }
}
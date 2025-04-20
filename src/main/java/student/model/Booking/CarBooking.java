package student.model.Booking;

import student.model.Car.Car;
import student.model.User.User;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a car booking made by a user.
 * Contains booking details such as user, car, time, and cancellation status.
 */
public class CarBooking {

    /**
     * A unique identifier for the booking.
     */
    private UUID bookingId;

    /**
     * The user who made the booking.
     */
    private User user;

    /**
     * The car that was booked.
     */
    private Car car;

    /**
     * The timestamp indicating when the booking was created.
     */
    private LocalDateTime bookingTime;

    /**
     * Indicates whether the booking has been canceled.
     */
    private boolean isCanceled;

    /**
     * Creates a new car booking.
     *
     * @param bookingId    Unique ID of the booking
     * @param user         The user who made the booking
     * @param car          The car being booked
     * @param bookingTime  Time of booking
     */
    public CarBooking(UUID bookingId, User user, Car car, LocalDateTime bookingTime) {
        this.bookingId = bookingId;
        this.user = user;
        this.car = car;
        this.bookingTime = bookingTime;
        this.isCanceled = false;
    }

    /** @return The booking ID */
    public UUID getBookingId() {
        return bookingId;
    }

    /** @return The user who made the booking */
    public User getUser() {
        return user;
    }

    /** @return The car that was booked */
    public Car getCar() {
        return car;
    }

    /** @return The date and time of the booking */
    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    /** @return Whether the booking has been canceled */
    public boolean isCanceled() {
        return isCanceled;
    }

    /**
     * Sets the cancellation status of the booking.
     * @param canceled True if the booking is canceled, false otherwise
     */
    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    /**
     * Returns a string representation of the booking, including user, car, time, and status.
     *
     * @return A string summarizing the booking.
     */
    @Override
    public String toString() {
        return "CarBooking{" +
                "bookingId=" + bookingId +
                ", user=" + user +
                ", car=" + car +
                ", bookingTime=" + bookingTime +
                ", isCanceled=" + isCanceled +
                '}';
    }

    /**
     * Checks if two bookings are equal based on ID, user, car, time, and cancellation status.
     *
     * @param o The object to compare with.
     * @return {@code true} if equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarBooking that)) return false;
        return isCanceled == that.isCanceled &&
                Objects.equals(bookingId, that.bookingId) &&
                Objects.equals(user, that.user) &&
                Objects.equals(car, that.car) &&
                Objects.equals(bookingTime, that.bookingTime);
    }

    /**
     * Computes a hash code based on booking ID, user, car, time, and status.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(bookingId, user, car, bookingTime, isCanceled);
    }
}
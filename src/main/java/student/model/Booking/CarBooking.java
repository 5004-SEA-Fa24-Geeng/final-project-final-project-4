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

    private UUID bookingId;
    private User user;
    private Car car;
    private LocalDateTime bookingTime;
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

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, user, car, bookingTime, isCanceled);
    }
}
package student.view;

import student.model.Car.CarService;
import student.model.User.UserService;
import student.model.Booking.CarBookingService;

/**
 * Defines the common interface for all views (CLI or GUI) in the Car Rental System.
 * <p>
 * Implementing classes must provide user interaction logic such as displaying menus,
 * capturing user inputs, rendering car listings, and invoking booking or user-related operations.
 * </p>
 *
 * <p>
 * This interface enables decoupling of the controller from specific view implementations,
 * supporting interchangeable CLI and GUI views via dependency injection.
 * </p>
 */
public interface CarRentalViewInterface {

    /**
     * Displays the main menu with options.
     */
    void displayMenu();

    /**
     * Prompts the user to select a menu option.
     *
     * @return the selected option as an integer
     */
    int getUserOption();

    /**
     * Displays a message for an invalid menu selection.
     */
    void displayInvalidOption();

    /**
     * Handles the car booking process.
     *
     * @param userService    the service used for retrieving users
     * @param bookingService the service used for managing bookings
     */
    void bookCar(UserService userService, CarBookingService bookingService);

    /**
     * Books a car and exports the resulting booking to a CSV file.
     *
     * @param userService    the user service
     * @param bookingService the booking service
     */
    void bookCarAndExport(UserService userService, CarBookingService bookingService);

    /**
     * Exports the currently available cars to a CSV file.
     *
     * @param bookingService the service used to retrieve available cars
     */
    void exportAvailableCarsToCSV(CarBookingService bookingService);

    /**
     * Displays all cars booked by a specific user.
     *
     * @param userService    the user service
     * @param bookingService the booking service
     */
    void displayUserBookings(UserService userService, CarBookingService bookingService);

    /**
     * Displays all bookings in the system.
     *
     * @param bookingService the booking service
     */
    void displayAllBookings(CarBookingService bookingService);

    /**
     * Displays a list of all registered users.
     *
     * @param userService the user service
     */
    void displayAllUsers(UserService userService);

    /**
     * Displays available cars.
     *
     * @param bookingService the booking service
     * @param isElectric     if true, filters for electric cars only
     */
    void displayAvailableCars(CarBookingService bookingService, boolean isElectric);

    /**
     * Displays all cars sorted by rental price (ascending).
     *
     * @param carService the car service used to retrieve cars
     */
    void displayCarsSortedByPrice(CarService carService);

    /**
     * Filters and displays cars within a price range.
     *
     * @param carService the car service
     */
    void displayCarsByPriceRange(CarService carService);

    /**
     * Filters and displays cars within a price range.
     *
     * @param carService the car service
     */
    void displayCarsByKeyword(CarService carService);

    /**
     * Cancels a booking selected by the user.
     *
     * @param bookingService the booking service
     */
    void cancelBooking(CarBookingService bookingService);

    /**
     * Registers a new user via input.
     *
     * @param userService the user service
     */
    void registerUser(UserService userService);

    /**
     * Logs in an existing user by name.
     *
     * @param userService the user service
     */
    void loginUser(UserService userService);

}

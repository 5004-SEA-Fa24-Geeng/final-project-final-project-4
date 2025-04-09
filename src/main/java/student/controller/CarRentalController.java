package student.controller;

import student.model.Car.CarService;
import student.model.Booking.CarBookingService;
import student.model.User.UserService;
import student.view.CarRentalView;

import java.util.Scanner;

/**
 * Controller class for the car rental system.
 * <p>
 * This class acts as the main coordinator between the user interface (view)
 * and the application logic (services). It provides a command-line menu-driven
 * interface for users to interact with the system, allowing them to book cars,
 * view bookings, filter cars, and manage users.
 * </p>
 *
 * <p>
 * Responsibilities include:
 * <ul>
 *     <li>Displaying the main menu</li>
 *     <li>Interpreting user input</li>
 *     <li>Delegating logic to appropriate services and view methods</li>
 * </ul>
 * </p>
 */
public class CarRentalController {
    private final CarService carService;
    private final CarBookingService bookingService;
    private final UserService userService;
    private final CarRentalView view;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a new CarRentalController with the required service and view components.
     *
     * @param carService     The car service to handle car-related operations
     * @param bookingService The booking service to handle car booking logic
     * @param userService    The user service to handle user-related operations
     * @param view           The view used to interact with the user
     */
    public CarRentalController(CarService carService,
                               CarBookingService bookingService,
                               UserService userService,
                               CarRentalView view) {
        this.carService = carService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.view = view;
    }

    /**
     * Starts and runs the car rental application.
     * <p>
     * Continuously displays the menu and processes user-selected options
     * until the user chooses to exit the program.
     * </p>
     *
     * <p>Supported operations:</p>
     * <ul>
     *     <li>1 - Book a car</li>
     *     <li>2 - View user's own bookings</li>
     *     <li>3 - View all bookings</li>
     *     <li>4 - View all available cars</li>
     *     <li>5 - View available electric cars</li>
     *     <li>6 - View all users</li>
     *     <li>7 - Sort and display cars by price</li>
     *     <li>8 - Filter and display cars by price range</li>
     *     <li>9 - Search cars by keyword</li>
     *     <li>10 - Export available cars to a CSV file</li>
     *     <li>11 - Book a car and export booking to CSV</li>
     *     <li>0 - Exit the application</li>
     * </ul>
     */
    public void run() {
        boolean running = true;
        while (running) {
            view.displayMenu();
            int option = view.getUserOption();
            switch (option) {
                case 1 -> view.bookCar(userService, bookingService);
                case 2 -> view.displayUserBookings(userService, bookingService);
                case 3 -> view.displayAllBookings(bookingService);
                case 4 -> view.displayAvailableCars(bookingService, false);
                case 5 -> view.displayAvailableCars(bookingService, true);
                case 6 -> view.displayAllUsers(userService);
                case 7 -> view.displayCarsSortedByPrice(carService);
                case 8 -> view.displayCarsByPriceRange(carService);
                case 9 -> view.displayCarsByKeyword(carService);
                case 10 -> view.exportAvailableCarsToCSV(bookingService);
                case 11 -> view.bookCarAndExport(userService, bookingService);
                case 0 -> running = false;
                default -> view.displayInvalidOption();
            }
        }
    }
}
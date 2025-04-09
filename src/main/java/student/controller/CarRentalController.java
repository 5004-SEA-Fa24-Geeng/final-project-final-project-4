package student.controller;

import student.model.Car.CarService;
import student.model.Booking.CarBookingService;
import student.model.User.UserService;
import student.view.CarRentalView;

import java.util.Scanner;

/**
 * Controller class for the car rental application.
 * <p>
 * This class connects the view with the model and service layers. It manages
 * user interaction through a menu-driven interface, allowing users to book cars,
 * view bookings, search/filter available cars, and interact with user data.
 * </p>
 *
 * Responsibilities include:
 * <ul>
 *   <li>Displaying the main menu</li>
 *   <li>Reading user input</li>
 *   <li>Delegating actions to the appropriate service or view layer</li>
 * </ul>
 */
public class CarRentalController {

    private final CarService carService;
    private final CarBookingService bookingService;
    private final UserService userService;
    private final CarRentalView view;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a CarRentalController with required service and view components.
     *
     * @param carService      The service responsible for car-related logic
     * @param bookingService  The service managing car bookings
     * @param userService     The service managing user accounts
     * @param view            The view layer responsible for user I/O
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
     * Runs the car rental system.
     * Displays a command-line menu and handles user interaction
     * until the user chooses to exit.
     *
     * Menu options include:
     * <ul>
     *     <li>Booking a car</li>
     *     <li>Viewing user-specific or all bookings</li>
     *     <li>Displaying available or electric cars</li>
     *     <li>Searching cars by price or keyword</li>
     *     <li>Displaying all users</li>
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
                case 0 -> running = false;
                default -> view.displayInvalidOption();
            }
        }
    }
}
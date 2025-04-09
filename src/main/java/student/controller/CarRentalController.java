package student.controller;

import student.model.Car.CarService;
import student.model.Booking.CarBookingService;
import student.model.User.UserService;
import student.view.CarRentalView;

import java.util.Scanner;

/**
 * Controller class for the car rental system.
 * Coordinates interactions between the user interface (view) and backend logic (services).
 * This class acts as the entry point of the application and handles user commands.
 */
public class CarRentalController {
    private final CarService carService;
    private final CarBookingService bookingService;
    private final UserService userService;
    private final CarRentalView view;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a new CarRentalController with the specified services and view.
     *
     * @param carService     The car service to use for car operations
     * @param bookingService The booking service to use for booking operations
     * @param userService    The user service to use for user operations
     * @param view           The view to use for user interaction
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
     * Runs the car rental system application.
     * Displays the menu and processes user input until the user chooses to exit.
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
                case 0 -> running = false;
                default -> view.displayInvalidOption();
            }
        }
    }
}
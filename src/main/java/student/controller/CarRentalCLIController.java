package student.controller;

import student.model.Car.CarService;
import student.model.Booking.CarBookingService;
import student.model.User.UserService;
import student.view.CarRentalViewInterface;

import java.util.Scanner;

/**
 * Controller class for handling command-line interface (CLI) interactions
 * in the Car Rental System.
 * <p>
 * This class serves as the coordinator between the CLI view and the
 * underlying service layer. It listens for user commands, delegates
 * operations to the appropriate service classes, and ensures proper
 * application flow during runtime.
 * </p>
 */
public class CarRentalCLIController implements CarRentalControllerInterface {

    /** Service for car-related operations such as sorting and filtering. */
    private final CarService carService;

    /** Service for managing bookings including availability and cancellation. */
    private final CarBookingService bookingService;

    /** Service for user management such as registration and login. */
    private final UserService userService;

    /** CLI view for displaying menus and interacting with the user. */
    private final CarRentalViewInterface view;

    /** Scanner for reading console input (passed to view if needed). */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a CLI controller with the given services and view.
     *
     * @param carService      The car service used for vehicle-related logic.
     * @param bookingService  The booking service used for managing reservations.
     * @param userService     The user service used for handling user accounts.
     * @param view            The CLI view interface for displaying output and reading input.
     */
    public CarRentalCLIController(CarService carService,
                                  CarBookingService bookingService,
                                  UserService userService,
                                  CarRentalViewInterface view) {
        this.carService = carService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.view = view;
    }

    /**
     * Runs the main application loop for the CLI.
     * <p>
     * Continuously displays the main menu, accepts user input,
     * and routes commands to corresponding service methods.
     * </p>
     */
    @Override
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
                case 12 -> view.cancelBooking(bookingService);
                case 13 -> view.registerUser(userService);
                case 14 -> view.loginUser(userService);     
                case 0 -> running = false;
                default -> view.displayInvalidOption();
            }
        }
    }
}

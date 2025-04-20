package student;

import student.controller.CarRentalCLIController;
import student.controller.CarRentalControllerInterface;
import student.controller.CarRentalGUIController;
import student.model.Booking.CarBookingRepository;
import student.model.Booking.CarBookingService;
import student.model.Car.CarFileRepository;
import student.model.Car.CarRepository;
import student.model.Car.CarService;
import student.model.User.UserArrayRepository;
import student.model.User.UserFileRepository;
import student.model.User.UserRepository;
import student.model.User.UserService;
import student.view.CarRentalViewInterface;
import student.view.cli.CarRentalCLIView;
import student.view.gui.CarRentalGUIView;

import java.util.Scanner;

/**
 * Entry point of the Car Rental System application.
 * <p>This class is responsible for initializing the repositories, services, and controllers,
 * and launching the application in either CLI or GUI mode based on user input.</p>
 *
 * <p>Supported modes:</p>
 * <ul>
 *   <li><b>cli</b> - launches a command-line interface using {@link student.view.cli.CarRentalCLIView}</li>
 *   <li><b>gui</b> - launches a Swing-based GUI using {@link student.view.gui.CarRentalGUIView}</li>
 * </ul>
 */
public class Main {

    /**
     * Initializes the application components and launches either CLI or GUI mode.
     *
     * <p>Steps performed:</p>
     * <ol>
     *   <li>Instantiates {@link CarRepository}, {@link CarService}</li>
     *   <li>Creates {@link CarBookingService} and {@link UserService}</li>
     *   <li>Prompts the user to select interface mode</li>
     *   <li>Creates corresponding view and controller based on the mode</li>
     *   <li>Starts the controller's main loop via {@code run()}</li>
     * </ol>
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {

        // Initialize core services
        CarRepository carRepo = new CarFileRepository();
        CarService carService = new CarService(carRepo);

        CarBookingRepository bookingRepo = new CarBookingRepository();
        CarBookingService bookingService = new CarBookingService(bookingRepo, carService);

        UserRepository userRepo = new UserFileRepository(); // UserFileRepository() / UserArrayRepository
        UserService userService = new UserService(userRepo);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Launch in CLI or GUI mode? (cli/gui): ");
        String mode = scanner.nextLine().trim().toLowerCase();

        CarRentalControllerInterface controller;

        if (mode.equals("cli")) {
            CarRentalViewInterface view = new CarRentalCLIView();
            controller = new CarRentalCLIController(carService, bookingService, userService, view);
        } else {
            CarRentalGUIView guiView = new CarRentalGUIView(carService, bookingService, userService);
            controller = new CarRentalGUIController(carService, bookingService, userService, guiView);
        }

        controller.run();
    }
}

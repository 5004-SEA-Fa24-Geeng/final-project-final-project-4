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
import student.model.User.UserRepository;
import student.model.User.UserService;
import student.view.CarRentalViewInterface;
import student.view.cli.CarRentalCLIView;
import student.view.gui.CarRentalGUIView;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Initialize core services
        CarRepository carRepo = new CarFileRepository();
        CarService carService = new CarService(carRepo);

        CarBookingRepository bookingRepo = new CarBookingRepository();
        CarBookingService bookingService = new CarBookingService(bookingRepo, carService);

        UserRepository userRepo = new UserArrayRepository(); // or new UserFileRepository()
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

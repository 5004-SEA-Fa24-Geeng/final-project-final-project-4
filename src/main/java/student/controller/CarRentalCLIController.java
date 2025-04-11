package student.controller;

import student.model.Car.CarService;
import student.model.Booking.CarBookingService;
import student.model.User.UserService;
import student.view.CarRentalViewInterface;

import java.util.Scanner;

public class CarRentalCLIController implements CarRentalControllerInterface {
    private final CarService carService;
    private final CarBookingService bookingService;
    private final UserService userService;
    private final CarRentalViewInterface view;
    private final Scanner scanner = new Scanner(System.in);

    public CarRentalCLIController(CarService carService,
                                  CarBookingService bookingService,
                                  UserService userService,
                                  CarRentalViewInterface view) {
        this.carService = carService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.view = view;
    }

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
                case 0 -> running = false;
                default -> view.displayInvalidOption();
            }
        }
    }
}

package student;

import student.controller.CarRentalController;
import student.model.Booking.CarBookingRepository;
import student.model.Booking.CarBookingService;
import student.model.Car.CarRepository;
import student.model.Car.CarService;
import student.model.User.UserArrayRepository;
import student.model.User.UserRepository;
import student.model.User.UserService;
import student.view.CarRentalView;

public class Main {
    public static void main(String[] args) {
        CarRepository carRepo = new CarRepository();
        CarService carService = new CarService(carRepo);

        CarBookingRepository bookingRepo = new CarBookingRepository();
        CarBookingService bookingService = new CarBookingService(bookingRepo, carService);

        UserRepository userRepo = new UserArrayRepository();
        UserService userService = new UserService(userRepo);

        CarRentalView view = new CarRentalView();

        CarRentalController controller = new CarRentalController(carService, bookingService, userService, view);
        controller.run();
    }
}

package student.view;

import student.model.Car.CarService;
import student.model.User.UserService;
import student.model.Booking.CarBookingService;

public interface CarRentalViewInterface {
    void displayMenu();
    int getUserOption();
    void displayInvalidOption();

    void bookCar(UserService userService, CarBookingService bookingService);
    void bookCarAndExport(UserService userService, CarBookingService bookingService);
    void exportAvailableCarsToCSV(CarBookingService bookingService);
    void displayUserBookings(UserService userService, CarBookingService bookingService);
    void displayAllBookings(CarBookingService bookingService);
    void displayAllUsers(UserService userService);
    void displayAvailableCars(CarBookingService bookingService, boolean isElectric);
    void displayCarsSortedByPrice(CarService carService);
    void displayCarsByPriceRange(CarService carService);
    void displayCarsByKeyword(CarService carService);
    void cancelBooking(CarBookingService bookingService);
    void registerUser(UserService userService);
    void loginUser(UserService userService);

}

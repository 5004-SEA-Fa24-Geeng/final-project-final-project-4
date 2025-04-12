package student.view.cli;

import student.model.Booking.CarBookingService;
import student.model.User.UserService;
import student.model.Car.CarService;
import student.view.CarRentalViewInterface;

import java.util.LinkedList;
import java.util.Queue;

public class MockCLIView implements CarRentalViewInterface {

    public final Queue<Integer> userInputs = new LinkedList<>();
    public String lastCalledMethod = "";

    @Override
    public void displayMenu() {
        // no-op
    }

    @Override
    public int getUserOption() {
        return userInputs.isEmpty() ? 0 : userInputs.poll();
    }

    @Override
    public void displayInvalidOption() {
        lastCalledMethod = "displayInvalidOption";
    }

    @Override
    public void bookCar(UserService userService, CarBookingService bookingService) {
        lastCalledMethod = "bookCar";
    }

    @Override
    public void displayUserBookings(UserService userService, CarBookingService bookingService) {
        lastCalledMethod = "displayUserBookings";
    }

    @Override
    public void displayAllBookings(CarBookingService bookingService) {
        lastCalledMethod = "displayAllBookings";
    }

    @Override
    public void displayAvailableCars(CarBookingService bookingService, boolean electricOnly) {
        lastCalledMethod = electricOnly ? "displayElectricCars" : "displayAvailableCars";
    }

    @Override
    public void displayAllUsers(UserService userService) {
        lastCalledMethod = "displayAllUsers";
    }

    @Override
    public void displayCarsSortedByPrice(CarService carService) {
        lastCalledMethod = "displayCarsSortedByPrice";
    }

    @Override
    public void displayCarsByPriceRange(CarService carService) {
        lastCalledMethod = "displayCarsByPriceRange";
    }

    @Override
    public void displayCarsByKeyword(CarService carService) {
        lastCalledMethod = "displayCarsByKeyword";
    }

    @Override
    public void exportAvailableCarsToCSV(CarBookingService bookingService) {
        lastCalledMethod = "exportAvailableCarsToCSV";
    }

    @Override
    public void bookCarAndExport(UserService userService, CarBookingService bookingService) {
        lastCalledMethod = "bookCarAndExport";
    }

    @Override
    public void cancelBooking(CarBookingService bookingService) {
        lastCalledMethod = "cancelBooking";
    }

    @Override
    public void registerUser(UserService userService) {
        lastCalledMethod = "registerUser";
    }

    @Override
    public void loginUser(UserService userService) {
        lastCalledMethod = "loginUser";
    }
}

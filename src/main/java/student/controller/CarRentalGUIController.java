package student.controller;

import student.model.Booking.CarBooking;
import student.model.Booking.CarBookingService;
import student.model.Car.Car;
import student.model.Car.CarService;
import student.model.User.User;
import student.model.User.UserService;
import student.view.gui.CarRentalGUIView;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Controller class for handling GUI-based interactions in the Car Rental System.
 * <p>
 * This controller coordinates user actions triggered from the GUI
 * (e.g., button clicks, dialogs) with underlying business logic
 * provided by service classes.
 * </p>
 */
public class CarRentalGUIController implements CarRentalControllerInterface {

    /**
     * Provides car-related operations such as retrieval, filtering, and sorting.
     */
    private final CarService carService;

    /**
     * Handles booking operations, including creation, cancellation, and availability checks.
     */
    private final CarBookingService bookingService;

    /**
     * Manages user-related logic such as registration and login.
     */
    private final UserService userService;

    /**
     * The graphical user interface used to display data and capture user interactions.
     */
    private final CarRentalGUIView guiView;

    /**
     * Constructs a GUI controller with required service and view components.
     *
     * @param carService      The service for car-related operations.
     * @param bookingService  The service for booking-related operations.
     * @param userService     The service for user account operations.
     * @param guiView         The GUI view for user interaction.
     */
    public CarRentalGUIController(CarService carService,
                                  CarBookingService bookingService,
                                  UserService userService,
                                  CarRentalGUIView guiView) {
        this.carService = carService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.guiView = guiView;
    }

    /**
     * Launches the GUI application.
     * Sets the controller as event listener and makes the GUI visible.
     */
    @Override
    public void run() {
        guiView.setViewListener(this);
        guiView.setVisible(true);
    }

    /**
     * Displays all currently available cars.
     */
    public void handleViewAvailableCars() {
        guiView.showCars(bookingService.getAvailableCars());
    }

    /**
     * Displays only available electric cars.
     */
    public void handleViewElectricCars() {
        guiView.showCars(bookingService.getAvailableElectricCars());
    }

    /**
     * Displays cars sorted by rental price (ascending).
     */
    public void handleSortByPrice() {
        guiView.showCars(carService.sortCarsByPrice());
    }

    /**
     * Filters cars by user-specified minimum and maximum rental price.
     */
    public void handleFilterByPrice() {
        try {
            String minStr = JOptionPane.showInputDialog(guiView, "Minimum price:");
            String maxStr = JOptionPane.showInputDialog(guiView, "Maximum price:");
            if (minStr == null || maxStr == null) return;

            BigDecimal min = new BigDecimal(minStr.trim());
            BigDecimal max = new BigDecimal(maxStr.trim());

            List<Car> filtered = carService.getCarsByPriceRange(min, max);
            guiView.showCars(filtered);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(guiView, "❌ Invalid input.");
        }
    }

    /**
     * Searches cars based on a keyword entered by the user.
     */
    public void handleSearchByKeyword() {
        String keyword = JOptionPane.showInputDialog(guiView, "Enter keyword:");
        if (keyword == null || keyword.isBlank()) return;

        List<Car> result = carService.searchCars(keyword.trim().toLowerCase());
        guiView.showCars(result);
    }

    /**
     * Displays all registered users in the system.
     */
    public void handleViewUsers() {
        guiView.showUsers(userService.getUsers());
    }

    /**
     * Displays all bookings in the system.
     */
    public void handleViewBookings() {
        guiView.showBookings(bookingService.getBookings());
    }

    /**
     * Handles the booking process for a selected car by prompting the user to choose a user.
     *
     * @param selectedCar The car to be booked.
     */
    public void handleBookCar(Car selectedCar) {
        if (selectedCar == null) {
            JOptionPane.showMessageDialog(guiView, "Please select a car to book.");
            return;
        }

        List<User> users = userService.getUsers();
        String[] userNames = users.stream().map(User::getName).toArray(String[]::new);
        String selectedUser = (String) JOptionPane.showInputDialog(guiView, "Select user:", "Booking",
                JOptionPane.PLAIN_MESSAGE, null, userNames, userNames[0]);

        if (selectedUser == null) return;

        User user = users.stream()
                .filter(u -> u.getName().equals(selectedUser))
                .findFirst().orElse(null);

        if (user == null) {
            JOptionPane.showMessageDialog(guiView, "❌ User not found.");
            return;
        }

        try {
            UUID bookingId = bookingService.bookCar(user, selectedCar.getRegNumber());
            JOptionPane.showMessageDialog(guiView, "✅ Booking successful! ID: " + bookingId);
            guiView.showCars(bookingService.getAvailableCars());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(guiView, "❌ Booking failed: " + e.getMessage());
        }
    }

    /**
     * Cancels a selected booking.
     *
     * @param booking The booking to cancel.
     */
    public void handleCancelBooking(CarBooking booking) {
        if (booking == null) {
            JOptionPane.showMessageDialog(guiView, "Please select a booking to cancel.");
            return;
        }

        if (booking.isCanceled()) {
            JOptionPane.showMessageDialog(guiView, "Already canceled.");
            return;
        }

        try {
            bookingService.cancelBooking(booking.getBookingId());
            JOptionPane.showMessageDialog(guiView, "✅ Booking canceled.");
            guiView.showBookings(bookingService.getBookings());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(guiView, "❌ Failed to cancel: " + e.getMessage());
        }
    }

    /**
     * Exports all bookings in the system to a CSV file.
     */
    public void handleExportBookings() {
        List<CarBooking> bookings = bookingService.getBookings();
        try (FileWriter writer = new FileWriter("bookings_export.csv")) {
            writer.write("BookingID,User,Brand,Model,CarReg,Price,Time,Canceled\n");
            for (CarBooking b : bookings) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
                        b.getBookingId(),
                        b.getUser().getName(),
                        b.getCar().getBrand(),
                        b.getCar().getModel(),
                        b.getCar().getRegNumber(),
                        b.getCar().getRentalPricePerDay(),
                        b.getBookingTime(),
                        b.isCanceled()));
            }
            JOptionPane.showMessageDialog(guiView, "✅ Exported to bookings_export.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(guiView, "❌ Export failed: " + e.getMessage());
        }
    }

    /**
     * Registers a new user and sets them as the current user in the GUI.
     *
     * @param name The name of the user to register.
     */
    public void handleRegisterUser(String name) {
        try {
            User user = userService.register(name);
            guiView.setCurrentUser(user);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(guiView, "❌ " + e.getMessage());
        }
    }

    /**
     * Logs in an existing user by name.
     *
     * @param name The name of the user to log in.
     */
    public void handleLoginUser(String name) {
        User user = userService.login(name);
        if (user == null) {
            JOptionPane.showMessageDialog(guiView, "❌ User not found.");
        } else {
            guiView.setCurrentUser(user);
        }
    }

    /**
     * Books a selected car on behalf of a specific user.
     *
     * @param selectedCar The car to book.
     * @param user        The user making the booking.
     */
    public void handleBookCarAs(Car selectedCar, User user) {
        if (selectedCar == null) {
            JOptionPane.showMessageDialog(guiView, "Please select a car to book.");
            return;
        }

        try {
            UUID bookingId = bookingService.bookCar(user, selectedCar.getRegNumber());
            JOptionPane.showMessageDialog(guiView, "✅ Booking successful! ID: " + bookingId);
            guiView.showCars(bookingService.getAvailableCars());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(guiView, "❌ Booking failed: " + e.getMessage());
        }
    }

    /**
     * Displays all active (non-canceled) bookings for the currently logged-in user.
     *
     * @param user The user whose bookings are to be shown.
     */
    public void handleViewMyBookings(User user) {
        List<CarBooking> all = bookingService.getBookings();
        List<CarBooking> mine = all.stream()
                .filter(b -> b.getUser().getId().equals(user.getId()) && !b.isCanceled())
                .toList();

        if (mine.isEmpty()) {
            JOptionPane.showMessageDialog(guiView, "❌ You have no active bookings.");
            return;
        }

        guiView.showBookings(mine);
    }

    /**
     * Searches for cars based on a provided keyword.
     *
     * @param keyword The search keyword to apply (brand, model, reg number).
     */
    public void handleSearchByKeyword(String keyword) {
        List<Car> result = carService.searchCars(keyword.toLowerCase());
        guiView.showCars(result);
    }


}

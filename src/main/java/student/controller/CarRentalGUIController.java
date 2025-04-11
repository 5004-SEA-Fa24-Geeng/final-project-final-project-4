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

public class CarRentalGUIController implements CarRentalControllerInterface {

    private final CarService carService;
    private final CarBookingService bookingService;
    private final UserService userService;
    private final CarRentalGUIView guiView;

    public CarRentalGUIController(CarService carService,
                                  CarBookingService bookingService,
                                  UserService userService,
                                  CarRentalGUIView guiView) {
        this.carService = carService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.guiView = guiView;
    }

    @Override
    public void run() {
        guiView.setViewListener(this);
        guiView.setVisible(true);
    }

    public void handleViewAvailableCars() {
        guiView.showCars(bookingService.getAvailableCars());
    }

    public void handleViewElectricCars() {
        guiView.showCars(bookingService.getAvailableElectricCars());
    }

    public void handleSortByPrice() {
        guiView.showCars(carService.sortCarsByPrice());
    }

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

    public void handleSearchByKeyword() {
        String keyword = JOptionPane.showInputDialog(guiView, "Enter keyword:");
        if (keyword == null || keyword.isBlank()) return;

        List<Car> result = carService.searchCars(keyword.trim().toLowerCase());
        guiView.showCars(result);
    }

    public void handleViewUsers() {
        guiView.showUsers(userService.getUsers());
    }

    public void handleViewBookings() {
        guiView.showBookings(bookingService.getBookings());
    }

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

    public void handleRegisterUser(String name) {
        try {
            User user = userService.register(name);
            guiView.setCurrentUser(user);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(guiView, "❌ " + e.getMessage());
        }
    }

    public void handleLoginUser(String name) {
        User user = userService.login(name);
        if (user == null) {
            JOptionPane.showMessageDialog(guiView, "❌ User not found.");
        } else {
            guiView.setCurrentUser(user);
        }
    }

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

    public void handleSearchByKeyword(String keyword) {
        List<Car> result = carService.searchCars(keyword.toLowerCase());
        guiView.showCars(result);
    }


}

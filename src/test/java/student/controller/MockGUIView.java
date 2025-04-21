package student.controller;

import student.controller.CarRentalGUIController;
import student.model.Booking.CarBooking;
import student.model.Car.Car;
import student.model.User.User;
import student.view.gui.CarRentalGUIView;

import javax.swing.*;
import java.util.List;

public class MockGUIView extends CarRentalGUIView {
    public User lastUserSet;
    public String lastMessage = "";
    public List<CarBooking> lastBookingsShown;
    public List<Car> lastCarsShown;
    public List<User> lastUsersShown;

    public MockGUIView() {
        super(null, null, null);
    }

    @Override
    public void setCurrentUser(User user) {
        lastUserSet = user;
    }

    @Override
    public void showCars(List<Car> cars) {
        this.lastCarsShown = cars;
    }

    @Override
    public void showBookings(List<CarBooking> bookings) {
        this.lastBookingsShown = bookings;
    }

    @Override
    public void showUsers(List<User> users) {
        this.lastUsersShown = users;
    }

    @Override
    public void setStatus(String message) {
        lastMessage = message;
    }

    @Override
    public void setViewListener(CarRentalGUIController controller) {
        // Do nothing
    }

    @Override
    public void setVisible(boolean b) {
        // Do nothing
    }

    public void showMessageDialog(String message) {
        lastMessage = message;
    }
}

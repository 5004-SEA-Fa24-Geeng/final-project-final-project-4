package student.view.gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.controller.CarRentalGUIController;
import student.model.Booking.CarBookingRepository;
import student.model.Booking.CarBookingService;
import student.model.Car.*;
import student.model.User.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarRentalGUIViewTest {

    private CarRentalGUIView guiView;
    private CarRentalGUIController controller;
    private UserService userService;
    private CarBookingService bookingService;
    private CarService carService;

    @BeforeEach
    void setUp() {
        CarRepository carRepo = new CarFileRepository();
        carService = new CarService(carRepo);

        bookingService = new CarBookingService(new CarBookingRepository(), carService);
        userService = new UserService(new UserArrayRepository());

        guiView = new CarRentalGUIView(carService, bookingService, userService);
        controller = new CarRentalGUIController(carService, bookingService, userService, guiView);
        guiView.setViewListener(controller);
    }

    @Test
    void shouldSetAndDisplayCurrentUser() {
        User user = new User(UUID.randomUUID(), "TestUser");
        guiView.setCurrentUser(user);

        JLabel statusLabel = (JLabel) TestUtils.getPrivateField(guiView, "statusLabel");
        assertNotNull(statusLabel);
        assertTrue(statusLabel.getText().contains("Logged in"));
    }

    @Test
    void shouldShowCarsPaginated() {
        List<Car> allCars = carService.getAllCars();
        guiView.showCars(allCars);

        assertFalse(allCars.isEmpty());
        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertTrue(table.getRowCount() <= 18);
    }

    @Test
    void shouldSearchKeyword() {
        controller.handleSearchByKeyword("Tesla");
        JTable table = (JTable) TestUtils.getPrivateField(guiView, "table");
        assertTrue(table.getRowCount() > 0);
    }

    @Test
    void shouldRegisterAndLoginUser() {
        String newName = "User_" + System.currentTimeMillis();
        controller.handleRegisterUser(newName);
        controller.handleLoginUser(newName);
        JLabel statusLabel = (JLabel) TestUtils.getPrivateField(guiView, "statusLabel");
        assertTrue(statusLabel.getText().contains(newName));
    }


}

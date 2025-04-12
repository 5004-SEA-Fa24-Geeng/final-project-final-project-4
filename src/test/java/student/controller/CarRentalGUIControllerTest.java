package student.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.model.Booking.CarBookingService;
import student.model.Booking.CarBookingRepository;
import student.model.Car.*;
import student.model.User.*;
import student.view.gui.CarRentalGUIView;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarRentalGUIControllerTest {

    private CarRentalGUIController controller;
    private MockGUIView guiView;
    private UserService userService;
    private CarService carService;
    private CarBookingService bookingService;

    @BeforeEach
    void setUp() {
        carService = new CarService(new CarFileRepository());
        bookingService = new CarBookingService(new CarBookingRepository(), carService);
        userService = new UserService(new UserArrayRepository());

        guiView = new MockGUIView();
        controller = new CarRentalGUIController(carService, bookingService, userService, guiView);
    }

    @Test
    void shouldLoginValidUser() {
        controller.handleLoginUser("James");
        assertNotNull(guiView.lastUserSet);
        assertEquals("James", guiView.lastUserSet.getName());
    }


    @Test
    void shouldRegisterAndSetUser() {
        String name = "NewUser_" + UUID.randomUUID();
        controller.handleRegisterUser(name);
        assertNotNull(guiView.lastUserSet);
        assertEquals(name, guiView.lastUserSet.getName());
    }

    @Test
    void shouldShowUserBookingsOnly() {
        User user = userService.getUsers().get(0);
        Car car = bookingService.getAvailableCars().get(0);
        controller.handleBookCarAs(car, user);

        controller.handleViewMyBookings(user);
        assertNotNull(guiView.lastBookingsShown);
        assertFalse(guiView.lastBookingsShown.isEmpty());
    }

}

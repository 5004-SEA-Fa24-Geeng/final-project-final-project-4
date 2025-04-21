package student.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.model.Booking.CarBookingService;
import student.model.Booking.CarBookingRepository;
import student.model.Car.*;
import student.model.User.*;
import student.view.cli.MockCLIView;

import static org.junit.jupiter.api.Assertions.*;

class CarRentalCLIControllerTest {

    private CarRentalCLIController controller;
    private MockCLIView mockView;

    @BeforeEach
    void setUp() {
        CarService carService = new CarService(new CarFileRepository());
        CarBookingService bookingService = new CarBookingService(new CarBookingRepository(), carService);
        UserService userService = new UserService(new UserArrayRepository());

        mockView = new MockCLIView();
        controller = new CarRentalCLIController(carService, bookingService, userService, mockView);
    }

    @Test
    void shouldCallBookCar_whenOption1() {
        mockView.userInputs.add(1);  // Book car
        mockView.userInputs.add(0);  // Exit

        controller.run();

        assertEquals("bookCar", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallDisplayUserBookings_whenOption2() {
        mockView.userInputs.add(2);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("displayUserBookings", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallDisplayAllBookings_whenOption3() {
        mockView.userInputs.add(3);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("displayAllBookings", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallDisplayElectricCars_whenOption5() {
        mockView.userInputs.add(5);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("displayElectricCars", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallDisplayAllUsers_whenOption6() {
        mockView.userInputs.add(6);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("displayAllUsers", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallSortByPrice_whenOption7() {
        mockView.userInputs.add(7);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("displayCarsSortedByPrice", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallFilterByPrice_whenOption8() {
        mockView.userInputs.add(8);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("displayCarsByPriceRange", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallSearchByKeyword_whenOption9() {
        mockView.userInputs.add(9);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("displayCarsByKeyword", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallExportAvailableCars_whenOption10() {
        mockView.userInputs.add(10);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("exportAvailableCarsToCSV", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallBookAndExport_whenOption11() {
        mockView.userInputs.add(11);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("bookCarAndExport", mockView.lastCalledMethod);
    }

    @Test
    void shouldExit_whenOption0() {
        mockView.userInputs.add(0);  // Directly exit
        controller.run();

        assertEquals("", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallInvalidOption_whenUnknownInput() {
        mockView.userInputs.add(999);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("displayInvalidOption", mockView.lastCalledMethod);
    }

    @Test
    void shouldCallRegisterLoginCancel_whenOptions13To12() {
        mockView.userInputs.add(13); // register
        mockView.userInputs.add(14); // login
        mockView.userInputs.add(12); // cancel
        mockView.userInputs.add(0);  // exit

        controller.run();

        assertEquals("cancelBooking", mockView.lastCalledMethod);
    }
}

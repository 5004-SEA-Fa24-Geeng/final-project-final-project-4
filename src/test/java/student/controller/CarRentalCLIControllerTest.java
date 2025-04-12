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
    void shouldCallDisplayElectricCars_whenOption5() {
        mockView.userInputs.add(5);
        mockView.userInputs.add(0);

        controller.run();

        assertEquals("displayElectricCars", mockView.lastCalledMethod);
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

        // 最后一次操作是 cancel
        assertEquals("cancelBooking", mockView.lastCalledMethod);
    }
}

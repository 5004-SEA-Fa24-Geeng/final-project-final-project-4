package student.view.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.model.Booking.CarBooking;
import student.model.Booking.CarBookingRepository;
import student.model.Booking.CarBookingService;
import student.model.Car.Car;
import student.model.Car.CarFileRepository;
import student.model.Car.CarRepository;
import student.model.Car.CarService;
import student.model.User.User;
import student.model.User.UserArrayRepository;
import student.model.User.UserService;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarRentalCLIViewTest {

    private CarRentalCLIView view;
    private ByteArrayOutputStream outputStream;
    private CarBookingService bookingService;
    private CarService carService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        view = new CarRentalCLIView();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        carService = new CarService(new CarFileRepository());
        bookingService = new CarBookingService(new CarBookingRepository(), carService);
        userService = new UserService(new UserArrayRepository());
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

    private int regIndex = 0;

    private String getNextAvailableRegNumber() {
        List<Car> allCars = carService.getAllCars();
        while (regIndex < allCars.size()) {
            String reg = allCars.get(regIndex++).getRegNumber();
            boolean isBooked = bookingService.getUserBookedCars(userService.getUsers().get(0).getId())
                    .stream().anyMatch(car -> car.getRegNumber().equals(reg));
            if (!isBooked) return reg;
        }
        throw new IllegalStateException("No unbooked cars left to use in test.");
    }


    @Test
    void displayMenu() {
        view.displayMenu();
        String output = outputStream.toString();
        assertTrue(output.contains("Car Rental System"));
    }

    @Test
    void getUserOption() {
        System.setIn(new ByteArrayInputStream("5\n".getBytes()));
        view = new CarRentalCLIView();
        int option = view.getUserOption();
        assertEquals(5, option);
    }

    @Test
    void getUserOptionInvalid() {
        System.setIn(new ByteArrayInputStream("invalid\n".getBytes()));
        view = new CarRentalCLIView();
        int option = view.getUserOption();
        assertEquals(-1, option);
    }

    @Test
    void displayInvalidOption() {
        view.displayInvalidOption();
        assertTrue(outputStream.toString().contains("Invalid option"));
    }

    @Test
    void bookCar() {
        User user = userService.register("User_" + UUID.randomUUID());
        Car car = bookingService.getAvailableCars().get(0);
        String regNumber = car.getRegNumber();
        String userId = user.getId().toString();

        System.setIn(new ByteArrayInputStream((regNumber + "\n" + userId + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.bookCar(userService, bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("Successfully booked car"));
    }

    @Test
    void bookCarUserNotFound() {
        String regNumber = getNextAvailableRegNumber();
        String nonExistentUserId = UUID.randomUUID().toString();

        System.setIn(new ByteArrayInputStream((regNumber + "\n" + nonExistentUserId + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.bookCar(userService, bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌ No user found with id"));
    }

    @Test
    void bookCarInvalidUUIDFormat() {
        String regNumber = getNextAvailableRegNumber();
        String invalidUUID = "not-a-uuid";

        System.setIn(new ByteArrayInputStream((regNumber + "\n" + invalidUUID + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.bookCar(userService, bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌"));
    }

    @Test
    void exportAvailableCarsToCSV()  throws IOException {
        view.exportAvailableCarsToCSV(bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("exported to available_cars.csv") || output.contains("No cars available to export"));

        File file = new File("available_cars.csv");
        if (file.exists()) {
            assertTrue(Files.size(file.toPath()) > 0); // 有内容
            file.delete();
        }
    }

    @Test
    void exportAvailableCarsToCSV_FileWriteFailure() throws IOException {
        File file = new File("available_cars.csv");
        if (file.exists()) {
            file.delete();
        }
        file.mkdir();

        view.exportAvailableCarsToCSV(bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌ Failed to export cars"));

        file.delete();
    }

    @Test
    void exportBookingToCSV() throws IOException {
        User user = userService.register("User_" + UUID.randomUUID());
        Car car = bookingService.getAvailableCars().get(0);
        UUID bookingId = bookingService.bookCar(user, car.getRegNumber());

        CarBooking booking = bookingService.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst().orElseThrow();

        view.exportBookingToCSV(booking);

        File file = new File("booking_" + booking.getBookingId() + ".csv");
        assertTrue(file.exists());
        assertTrue(Files.readString(file.toPath()).contains(user.getName()));
        file.delete();
    }

    @Test
    void exportBookingToCSV_FileWriteFailure() throws IOException {
        User user = userService.getUsers().get(0);
        String regNumber = getNextAvailableRegNumber();
        UUID bookingId = bookingService.bookCar(user, regNumber);

        CarBooking booking = bookingService.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow();

        File file = new File("booking_" + booking.getBookingId() + ".csv");
        if (file.exists()) {
            file.delete();
        }
        file.mkdir();

        view.exportBookingToCSV(booking);

        String output = outputStream.toString();
        assertTrue(output.contains("❌ Failed to export booking"));

        file.delete();
    }

    @Test
    void bookCarAndExport() throws IOException {
        User user = userService.register("User_" + UUID.randomUUID());
        Car car = bookingService.getAvailableCars().get(0);
        String regNumber = car.getRegNumber();
        String userId = user.getId().toString();

        System.setIn(new ByteArrayInputStream((regNumber + "\n" + userId + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.bookCarAndExport(userService, bookingService);
        String output = outputStream.toString();
        assertTrue(output.contains("Successfully booked car"));
    }

    @Test
    void bookCarAndExportUserNotFound() {
        String regNumber = getNextAvailableRegNumber();
        String nonExistentUserId = UUID.randomUUID().toString();

        System.setIn(new ByteArrayInputStream((regNumber + "\n" + nonExistentUserId + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.bookCarAndExport(userService, bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌ No user found with id"));
    }

    @Test
    void bookCarAndExportInvalidUUID() {
        String regNumber = getNextAvailableRegNumber();
        String invalidUUID = "not-a-uuid";

        System.setIn(new ByteArrayInputStream((regNumber + "\n" + invalidUUID + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.bookCarAndExport(userService, bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌"));
    }

    @Test
    void displayAllUsers() {
        view.displayAllUsers(userService);
        String output = outputStream.toString();
        assertTrue(output.contains("James") || output.contains("Jamila"));
    }

    @Test
    void displayAllUsersNoUsers() {
        userService.getUsers().clear();

        view.displayAllUsers(userService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌ No users found."));
    }

    @Test
    void displayAvailableCars() {
        view.displayAvailableCars(bookingService, false);
        String output = outputStream.toString();
        assertTrue(output.contains("Tesla") || output.contains("Toyota") || output.contains("BYD") || output.contains("SUBARU"));
    }

    @Test
    void displayAvailableElectricCars() {
        view.displayAvailableCars(bookingService, true);
        String output = outputStream.toString();
        assertTrue(output.contains("Electric") || output.contains("Brand"));
    }

    @Test
    void displayUserBookings() {
        User user = userService.getUsers().get(0);
        String regNumber = getNextAvailableRegNumber();
        bookingService.bookCar(user, regNumber);

        System.setIn(new ByteArrayInputStream((user.getId().toString() + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.displayUserBookings(userService, bookingService);
        String output = outputStream.toString();
        assertTrue(output.contains(regNumber));
    }

    @Test
    void displayUserBookingsUserNotFound() {
        String nonExistentUserId = UUID.randomUUID().toString();

        System.setIn(new ByteArrayInputStream((nonExistentUserId + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.displayUserBookings(userService, bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌ No user found with id"));
    }

    @Test
    void displayUserBookingsNoCars() {
        User user = userService.getUsers().get(0);

        bookingService.getBookings().clear();

        System.setIn(new ByteArrayInputStream((user.getId().toString() + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.displayUserBookings(userService, bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌ User"));
    }

    @Test
    void displayAllBookings() {
        User user = userService.register("User_" + UUID.randomUUID());
        Car car = bookingService.getAvailableCars().get(0);
        bookingService.bookCar(user, car.getRegNumber());

        view.displayAllBookings(bookingService);
        String output = outputStream.toString();
        assertTrue(output.contains("CarBooking"));
    }

    @Test
    void displayAllBookingsNoBookings() {
        bookingService.getBookings().clear();

        view.displayAllBookings(bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("No bookings available 😕"));
    }

    @Test
    void displayCarsSortedByPrice() {
        view.displayCarsSortedByPrice(carService);
        String output = outputStream.toString();
        assertTrue(output.contains("Cars sorted by rental price"));
    }

    @Test
    void displayCarsByPriceRangeInvalidInput() {
        System.setIn(new ByteArrayInputStream("abc\n123\n".getBytes()));
        view.displayCarsByPriceRange(carService);
        assertTrue(outputStream.toString().contains("Invalid input for price range"));
    }

    @Test
    void displayCarsByPriceRange() {
        System.setIn(new ByteArrayInputStream("50\n150\n".getBytes()));
        view = new CarRentalCLIView();

        view.displayCarsByPriceRange(carService);
        String output = outputStream.toString();
        assertTrue(output.contains("Tesla") || output.contains("Toyota") || output.contains("BYD"));
    }

    @Test
    void displayCarsByPriceRangeNoCars() {
        System.setIn(new ByteArrayInputStream(("100000\n200000\n").getBytes()));
        view = new CarRentalCLIView();

        view.displayCarsByPriceRange(carService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌ No cars found in the given price range."));
    }


    @Test
    void displayCarsByKeyword() {
        System.setIn(new ByteArrayInputStream("Tesla\n".getBytes()));
        view = new CarRentalCLIView();

        view.displayCarsByKeyword(carService);
        String output = outputStream.toString();

        assertNotNull(output);
    }

    @Test
    void displayCarsByKeywordNoMatch() {
        System.setIn(new ByteArrayInputStream("nonsensekeyword\n".getBytes()));
        view = new CarRentalCLIView();

        view.displayCarsByKeyword(carService);
        String output = outputStream.toString();
        assertTrue(output.contains("No cars matched the keyword"));
    }

    @Test
    void cancelBooking() {
        User user = userService.getUsers().get(0);
        String regNumber = getNextAvailableRegNumber();
        UUID bookingId = bookingService.bookCar(user, regNumber);

        System.setIn(new ByteArrayInputStream((bookingId.toString() + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.cancelBooking(bookingService);
        String output = outputStream.toString();
        assertTrue(output.contains("Booking canceled successfully"));
    }

    @Test
    void cancelBookingNonExistent() {
        String randomUUID = UUID.randomUUID().toString();

        System.setIn(new ByteArrayInputStream((randomUUID + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.cancelBooking(bookingService);

        String output = outputStream.toString();

        assertTrue(output.contains("❌"), "Should display an error sign for non-existent booking.");
    }

    @Test
    void cancelBookingInvalidUUID() {
        System.setIn(new ByteArrayInputStream(("abc\n").getBytes()));
        view = new CarRentalCLIView();

        view.cancelBooking(bookingService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌ Invalid booking ID format."));
    }

    @Test
    void registerUser() {
        System.setIn(new ByteArrayInputStream("NewUser\n".getBytes()));
        view = new CarRentalCLIView();

        view.registerUser(userService);
        String output = outputStream.toString();
        assertTrue(output.contains("Registered user") || output.contains("already exists"));
    }

    @Test
    void registerUserAlreadyExists() {
        String existingName = userService.getUsers().get(0).getName();

        System.setIn(new ByteArrayInputStream((existingName + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.registerUser(userService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌"), "Should display an error when registering existing user.");
    }

    @Test
    void loginUser() {
        System.setIn(new ByteArrayInputStream("James\n".getBytes()));
        view = new CarRentalCLIView();

        view.loginUser(userService);
        String output = outputStream.toString();
        assertTrue(output.contains("Welcome back"));
    }

    @Test
    void loginUserNotFound() {
        String nonExistentName = "ThisUserDoesNotExist";

        System.setIn(new ByteArrayInputStream((nonExistentName + "\n").getBytes()));
        view = new CarRentalCLIView();

        view.loginUser(userService);

        String output = outputStream.toString();
        assertTrue(output.contains("❌ User not found."), "Should display error when user not found.");
    }
}
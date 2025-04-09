package student.view;

import student.model.Booking.CarBooking;
import student.model.Booking.CarBookingService;
import student.model.Car.Car;
import student.model.User.User;
import student.model.User.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * View class for the car rental system.
 * Handles user interaction and display of information.
 */
public class CarRentalView {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the main menu of the car rental system.
     */
    public void displayMenu() {
        System.out.println("""
                \n===== Car Rental System =====
                1. Book Car
                2. View All User Booked Cars
                3. View All Bookings
                4. View Available Cars
                5. View Available Electric Cars
                6. View All Users
                0. Exit
                ==============================
                """);
        System.out.print("Select an option: ");
    }

    /**
     * Gets the user's menu option selection.
     *
     * @return The selected option as an integer, or -1 if input is invalid
     */
    public int getUserOption() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Displays a message for invalid option selection.
     */
    public void displayInvalidOption() {
        System.out.println("❌ Invalid option. Please try again.");
    }

    /**
     * Handles the car booking process.
     *
     * @param userService The user service to use for user operations
     * @param bookingService The booking service to use for booking operations
     */
    public void bookCar(UserService userService, CarBookingService bookingService) {
        displayAvailableCars(bookingService, false);
        System.out.print("➡️ Enter car reg number: ");
        String regNumber = scanner.nextLine();

        displayAllUsers(userService);
        System.out.print("➡️ Enter user ID: ");
        String userId = scanner.nextLine();

        try {
            User user = userService.getUserById(UUID.fromString(userId));
            if (user == null) {
                System.out.println("❌ No user found with id " + userId);
            } else {
                UUID bookingId = bookingService.bookCar(user, regNumber);
                System.out.printf("🎉 Successfully booked car [%s] for user [%s]. Booking ID: %s\n",
                        regNumber, user.getName(), bookingId);
            }
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    /**
     * Displays all users in the system.
     *
     * @param userService The user service to use for retrieving users
     */
    public void displayAllUsers(UserService userService) {
        List<User> users = userService.getUsers();
        if (users.isEmpty()) {
            System.out.println("❌ No users found.");
            return;
        }
        users.forEach(System.out::println);
    }

    /**
     * Displays available cars, either all available cars or only electric ones.
     *
     * @param bookingService The booking service to use for retrieving available cars
     * @param isElectric If true, displays only electric cars; otherwise, displays all available cars
     */
    public void displayAvailableCars(CarBookingService bookingService, boolean isElectric) {
        List<Car> cars = isElectric
                ? bookingService.getAvailableElectricCars()
                : bookingService.getAvailableCars();
        if (cars.isEmpty()) {
            System.out.println("❌ No cars available.");
        } else {
            cars.forEach(System.out::println);
        }
    }

    /**
     * Displays all cars booked by a specific user.
     *
     * @param userService The user service to use for user retrieval
     * @param bookingService The booking service to use for booking operations
     */
    public void displayUserBookings(UserService userService, CarBookingService bookingService) {
        displayAllUsers(userService);
        System.out.print("➡️ Enter user ID: ");
        String userId = scanner.nextLine();

        User user = userService.getUserById(UUID.fromString(userId));
        if (user == null) {
            System.out.println("❌ No user found with id " + userId);
            return;
        }

        List<Car> cars = bookingService.getUserBookedCars(user.getId());
        if (cars.isEmpty()) {
            System.out.printf("❌ User %s has no booked cars.\n", user.getName());
        } else {
            cars.forEach(System.out::println);
        }
    }

    /**
     * Displays all bookings in the system.
     *
     * @param bookingService The booking service to use for retrieving bookings
     */
    public void displayAllBookings(CarBookingService bookingService) {
        List<CarBooking> bookings = bookingService.getBookings();
        if (bookings.isEmpty()) {
            System.out.println("No bookings available 😕");
            return;
        }
        bookings.forEach(System.out::println);
    }
}
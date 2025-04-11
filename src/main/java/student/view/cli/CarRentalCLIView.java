package student.view.cli;

import student.model.Booking.CarBooking;
import student.model.Booking.CarBookingService;
import student.model.Car.Car;
import student.model.Car.CarService;
import student.model.User.User;
import student.model.User.UserService;
import student.view.CarRentalViewInterface;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * View class for the car rental system.
 * Handles user interaction and display of information.
 */
public class CarRentalCLIView implements CarRentalViewInterface {

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
                7. Sort Cars by Price (Ascending)
                8. Filter Cars by Price Range
                9. Search Cars by Keyword
                10. Export Available Cars to CSV
                11. Book Car and Export Booking to CSV
                12. Cancel Booking
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
     * Exports the available cars to a CSV file named "available_cars.csv".
     * The list of cars is sorted by their registration numbers before exporting.
     *
     * @param bookingService The booking service used to retrieve available cars.
     */
    public void exportAvailableCarsToCSV(CarBookingService bookingService) {
        List<Car> cars = bookingService.getAvailableCars();
        if (cars.isEmpty()) {
            System.out.println("❌ No cars available to export.");
            return;
        }

        cars.sort(Comparator.comparing(Car::getRegNumber));
        File file = new File("available_cars.csv");

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("ID,Brand,Price,Type");
            for (Car car : cars) {
                writer.printf("%s,%s,%.2f,%s%n",
                        car.getRegNumber(),
                        car.getBrand(),
                        car.getRentalPricePerDay(),
                        car.isElectric() ? "Electric" : "Gas");
            }
            System.out.println("✅ Available cars exported to available_cars.csv");
        } catch (IOException e) {
            System.out.println("❌ Failed to export cars: " + e.getMessage());
        }
    }

    /**
     * Exports the details of a given car booking to a CSV file.
     * The CSV file is named using the booking ID in the format "booking_{bookingId}.csv".
     *
     * @param booking The car booking whose details are to be exported.
     */
    public void exportBookingToCSV(CarBooking booking) {
        File file = new File("booking_" + booking.getBookingId() + ".csv");

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("BookingID,UserID,UserName,CarID,Brand,Price,BookingTime");
            writer.printf("%s,%s,%s,%s,%s,%.2f,%s%n",
                    booking.getBookingId(),
                    booking.getUser().getId(),
                    booking.getUser().getName(),
                    booking.getCar().getRegNumber(),
                    booking.getCar().getBrand(),
                    booking.getCar().getRentalPricePerDay(),
                    booking.getBookingTime());

            System.out.printf("✅ Booking exported to %s%n", file.getName());
        } catch (IOException e) {
            System.out.println("❌ Failed to export booking: " + e.getMessage());
        }
    }

    /**
     * Books a car for a user and exports the booking details to a CSV file.
     * The method displays available cars, prompts the user for the car registration number and user ID,
     * performs the booking, and then calls {@link #exportBookingToCSV(CarBooking)} to export the booking.
     *
     * @param userService    The user service used to retrieve user details.
     * @param bookingService The booking service used to handle booking operations.
     */
    public void bookCarAndExport(UserService userService, CarBookingService bookingService) {
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
                CarBooking booking = bookingService.getBookings().stream()
                        .filter(b -> b.getBookingId().equals(bookingId))
                        .findFirst().orElseThrow();

                System.out.printf("🎉 Successfully booked car [%s] for user [%s].%n", regNumber, user.getName());
                exportBookingToCSV(booking);
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

    /**
     * Displays a list of cars sorted by rental price in ascending order.
     * This method retrieves the sorted list from the provided car service and then prints each car.
     *
     * @param carService The car service used to retrieve and sort the list of cars.
     */
    public void displayCarsSortedByPrice(CarService carService) {
        List<Car> cars = carService.sortCarsByPrice();
        System.out.println("\n🚗 Cars sorted by rental price (ascending):");
        cars.forEach(System.out::println);
    }

    /**
     * Prompts the user to input a minimum and maximum price, then retrieves and displays the cars
     * that fall within the specified price range.
     *
     * @param carService The car service used to filter the list of cars based on the given price range.
     */
    public void displayCarsByPriceRange(CarService carService) {
        try {
            System.out.print("Enter minimum price: ");
            BigDecimal min = new BigDecimal(scanner.nextLine());
            System.out.print("Enter maximum price: ");
            BigDecimal max = new BigDecimal(scanner.nextLine());

            List<Car> cars = carService.getCarsByPriceRange(min, max);
            if (cars.isEmpty()) {
                System.out.println("❌ No cars found in the given price range.");
            } else {
                cars.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("❌ Invalid input for price range.");
        }
    }

    /**
     * Prompts the user to enter a keyword and displays cars that match the keyword.
     * The search is performed based on various attributes such as brand or registration number.
     *
     * @param carService The car service used to search for cars by keyword.
     */
    public void displayCarsByKeyword(CarService carService) {
        System.out.print("Enter keyword to search (brand, reg, etc.): ");
        String keyword = scanner.nextLine().toLowerCase();
        List<Car> cars = carService.searchCars(keyword);
        if (cars.isEmpty()) {
            System.out.println("❌ No cars matched the keyword.");
        } else {
            cars.forEach(System.out::println);
        }
    }

    @Override
    public void cancelBooking(CarBookingService bookingService) {
        displayAllBookings(bookingService);
        System.out.print("➡️ Enter booking ID to cancel: ");
        String input = scanner.nextLine();

        try {
            UUID bookingId = UUID.fromString(input);
            bookingService.cancelBooking(bookingId);
            System.out.println("✅ Booking canceled successfully.");
        } catch (IllegalStateException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Invalid booking ID format.");
        }
    }

}
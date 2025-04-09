package student.view;

import java.util.Scanner;

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


}
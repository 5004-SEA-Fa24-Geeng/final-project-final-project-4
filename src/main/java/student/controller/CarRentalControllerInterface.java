package student.controller;

/**
 * Interface for controllers in the Car Rental System.
 * <p>
 * This interface defines the entry point method {@code run()} that must be
 * implemented by all controller types (e.g., CLI or GUI).
 * It allows the application to uniformly start the appropriate
 * controller regardless of the interface type.
 * </p>
 */
public interface CarRentalControllerInterface {

    /**
     * Starts the controller and launches the main application loop.
     * Implementations should handle user interaction and coordinate
     * between the view and service layers.
     */
    void run();
}

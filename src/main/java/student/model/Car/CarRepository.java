package student.model.Car;

import java.util.List;

/**
 * Interface for accessing car data in the Car Rental System.
 * <p>
 * This abstraction allows different implementations for retrieving
 * car information (e.g., from a file, database, or memory).
 * </p>
 */
public interface CarRepository {
    /**
     * Retrieves all available cars from the data source.
     *
     * @return a list of {@link Car} objects
     */
    List<Car> getAllCars();
}

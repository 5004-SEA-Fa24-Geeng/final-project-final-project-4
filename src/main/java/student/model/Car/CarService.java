package student.model.Car;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for car-related operations.
 * Provides methods to retrieve and manage car data.
 */
public class CarService {

    private final CarRepository carRepository;

    /**
     * Constructs a new CarService with the specified repository.
     *
     * @param carRepository The repository to use for car data access
     */
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Retrieves all cars in the system.
     *
     * @return A list containing all cars
     */
    public List<Car> getAllCars() {
        return carRepository.getAllCars();
    }

}
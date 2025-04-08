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

    /**
     * Retrieves a specific car by its registration number.
     *
     * @param regNumber The registration number of the car to retrieve
     * @return The car with the specified registration number
     * @throws IllegalStateException if no car with the given registration number is found
     */
    public Car getCar(String regNumber) {
        return getAllCars().stream()
                .filter(car -> regNumber.equals(car.getRegNumber()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Car with reg %s not found", regNumber)));
    }

    /**
     * Retrieves all electric cars in the system.
     *
     * @return A list containing all electric cars
     */
    public List<Car> getAllElectricCars() {
        return getAllCars().stream()
                .filter(Car::isElectric)
                .collect(Collectors.toList());
    }
}
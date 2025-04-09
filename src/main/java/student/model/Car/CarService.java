package student.model.Car;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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

    /**
     * Sorts all cars by their rental price per day in ascending order.
     *
     * @return A list of cars sorted by rental price per day
     */
    public List<Car> sortCarsByPrice() {
        return getAllCars().stream()
                .sorted(Comparator.comparing(Car::getRentalPricePerDay))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves cars whose rental price per day falls within a specified range.
     *
     * @param min The minimum rental price (inclusive)
     * @param max The maximum rental price (inclusive)
     * @return A list of cars within the specified price range
     */
    public List<Car> getCarsByPriceRange(BigDecimal min, BigDecimal max) {
        return getAllCars().stream()
                .filter(car -> {
                    BigDecimal price = car.getRentalPricePerDay();
                    return price.compareTo(min) >= 0 && price.compareTo(max) <= 0;
                })
                .collect(Collectors.toList());
    }

    /**
     * Searches for cars based on a keyword. The search matches against:
     * - registration number
     * - brand name
     * - the word "electric" for electric cars
     *
     * @param keyword The keyword to search for
     * @return A list of cars matching the keyword
     */
    public List<Car> searchCars(String keyword) {
        String lowerKeyword = keyword.toLowerCase(Locale.ROOT);
        return getAllCars().stream()
                .filter(car ->
                        car.getRegNumber().toLowerCase().contains(lowerKeyword) ||
                                car.getBrand().name().toLowerCase().contains(lowerKeyword) ||
                                (car.isElectric() && lowerKeyword.contains("electric"))
                )
                .collect(Collectors.toList());
    }

}
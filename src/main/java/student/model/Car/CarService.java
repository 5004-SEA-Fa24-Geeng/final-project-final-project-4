// Package: student.model.Car
package student.model.Car;

import student.model.utils.CarFilterEngine;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Service class responsible for car-related business logic in the Car Rental System.
 * <p>
 * This class provides methods for retrieving, filtering, searching, and sorting
 * cars. It interacts with a {@link CarRepository} for data access and delegates
 * filtering logic to the {@link CarFilterEngine} utility class.
 * </p>
 */
public class CarService {

    /**
     * Repository used to retrieve car data from a data source (e.g., CSV file).
     */
    private final CarRepository carRepository;

    /**
     * Constructs a {@code CarService} with the given car repository.
     *
     * @param carRepository the car repository implementation to use
     */
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Get all cars in the system.
     */
    public List<Car> getAllCars() {
        return carRepository.getAllCars();
    }

    /**
     * Get a specific car by its registration number.
     * @param regNumber The registration number of the car.
     * @return The car with the given regNumber.
     * @throws IllegalStateException if not found.
     */
    public Car getCar(String regNumber) {
        return getAllCars().stream()
                .filter(car -> regNumber.equals(car.getRegNumber()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        String.format("Car with reg %s not found", regNumber)));
    }

    /**
     * Get all electric cars in the system.
     */
    public List<Car> getAllElectricCars() {
        return getAllCars().stream()
                .filter(Car::isElectric)
                .collect(Collectors.toList());
    }

    /**
     * Returns all cars sorted by rental price in ascending order.
     *
     * @return a sorted list of cars
     */
    public List<Car> sortCarsByPrice() {
        return CarFilterEngine.sortByPrice(getAllCars());
    }

    /**
     * Filters cars whose rental price is within a specified range.
     *
     * @param min the minimum price (inclusive)
     * @param max the maximum price (inclusive)
     * @return a list of cars within the specified price range
     */
    public List<Car> getCarsByPriceRange(BigDecimal min, BigDecimal max) {
        return CarFilterEngine.filterByPriceRange(getAllCars(), min, max);
    }

    /**
     * Searches for cars using a keyword that matches brand, model, or registration number.
     *
     * @param keyword the keyword to search with
     * @return a list of matching cars
     */
    public List<Car> searchCars(String keyword) {
        return CarFilterEngine.searchByKeyword(getAllCars(), keyword);
    }
}
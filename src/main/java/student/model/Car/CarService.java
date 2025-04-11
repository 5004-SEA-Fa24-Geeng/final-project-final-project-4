// Package: student.model.Car
package student.model.Car;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CarService {

    private final CarRepository carRepository;

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
     * Sort all cars by rental price in ascending order.
     */
    public List<Car> sortCarsByPrice() {
        return getAllCars().stream()
                .sorted(Comparator.comparing(Car::getRentalPricePerDay))
                .collect(Collectors.toList());
    }

    /**
     * Filter cars within a specified price range.
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
     * Search cars by keyword matching brand, registration number, or 'electric'.
     */
    public List<Car> searchCars(String keyword) {
        return carRepository.getAllCars().stream()
                .filter(car -> car.getBrand().name().toLowerCase().contains(keyword)
                        || car.getRegNumber().toLowerCase().contains(keyword)
                        || car.getModel().toLowerCase().contains(keyword))
                .toList();
    }

}
package student.model.utils;

import student.model.Car.Car;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Utility class for filtering, sorting, and searching car collections.
 * <p>
 * This class provides static methods commonly used in {@code CarService}
 * to operate on lists of {@link Car} objects without altering business logic.
 * </p>
 */
public class CarFilterEngine {

    /**
     * Sorts a list of cars by rental price in ascending order.
     *
     * @param cars the list of cars to sort
     * @return a new list sorted by rental price
     */
    public static List<Car> sortByPrice(List<Car> cars) {
        return cars.stream()
                .sorted(Comparator.comparing(Car::getRentalPricePerDay))
                .collect(Collectors.toList());
    }

    /**
     * Filters a list of cars by a given rental price range.
     *
     * @param cars the list of cars to filter
     * @param min  the minimum price (inclusive)
     * @param max  the maximum price (inclusive)
     * @return a new list containing cars within the price range
     */
    public static List<Car> filterByPriceRange(List<Car> cars, BigDecimal min, BigDecimal max) {
        return cars.stream()
                .filter(car -> {
                    BigDecimal price = car.getRentalPricePerDay();
                    return price.compareTo(min) >= 0 && price.compareTo(max) <= 0;
                })
                .collect(Collectors.toList());
    }

    /**
     * Searches for cars matching a keyword in brand, registration number, or model.
     * The search is case-insensitive.
     *
     * @param cars    the list of cars to search
     * @param keyword the search keyword
     * @return a list of matching cars
     */
    public static List<Car> searchByKeyword(List<Car> cars, String keyword) {
        String lower = keyword.toLowerCase(Locale.ROOT);
        return cars.stream()
                .filter(car ->
                        car.getBrand().name().toLowerCase().contains(lower)
                                || car.getRegNumber().toLowerCase().contains(lower)
                                || car.getModel().toLowerCase().contains(lower))
                .toList();
    }
}

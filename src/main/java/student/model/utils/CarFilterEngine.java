package student.model.utils;

import student.model.Car.Car;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CarFilterEngine {

    public static List<Car> sortByPrice(List<Car> cars) {
        return cars.stream()
                .sorted(Comparator.comparing(Car::getRentalPricePerDay))
                .collect(Collectors.toList());
    }

    public static List<Car> filterByPriceRange(List<Car> cars, BigDecimal min, BigDecimal max) {
        return cars.stream()
                .filter(car -> {
                    BigDecimal price = car.getRentalPricePerDay();
                    return price.compareTo(min) >= 0 && price.compareTo(max) <= 0;
                })
                .collect(Collectors.toList());
    }

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

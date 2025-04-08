package student.model.Car;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CarRepository {

    private static final List<Car> CARS = Arrays.asList(
            new Car("1234", new BigDecimal("89.00"), Brand.TESLA, true),
            new Car("5678", new BigDecimal("50.00"), Brand.AUDI, false),
            new Car("9012", new BigDecimal("77.00"), Brand.MERCEDES, false),
            new Car("3456", new BigDecimal("66.00"), Brand.VW, true)
    );

    public List<Car> getAllCars() {
        return CARS;
    }
}
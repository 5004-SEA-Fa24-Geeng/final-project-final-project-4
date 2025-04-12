package student.model.Car;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarFileRepositoryTest {

    @Test
    void testGetAllCars_shouldLoadFromCSVCorrectly() {
        CarFileRepository repo = new CarFileRepository();
        List<Car> cars = repo.getAllCars();

        // CSV has 50 rows
        assertEquals(50, cars.size(), "Expected 50 cars from data/cars.csv");

        Car first = cars.get(0);
        assertEquals("1000", first.getRegNumber());
        assertEquals(Brand.TESLA, first.getBrand());
        assertEquals(new BigDecimal("94.64"), first.getRentalPricePerDay());
        assertFalse(first.isElectric());
        assertEquals("Model 3", first.getModel());

        Car last = cars.get(49);
        assertEquals("1049", last.getRegNumber());
        assertEquals(Brand.XIAOMI, last.getBrand());
        assertEquals("SU7", last.getModel());
    }

    @Test
    void testGetAllCars_shouldContainOnlyValidCars() {
        CarFileRepository repo = new CarFileRepository();
        List<Car> cars = repo.getAllCars();

        // All cars should have non-null fields
        for (Car car : cars) {
            assertNotNull(car.getRegNumber());
            assertNotNull(car.getBrand());
            assertNotNull(car.getRentalPricePerDay());
            assertNotNull(car.getModel());
        }
    }
}

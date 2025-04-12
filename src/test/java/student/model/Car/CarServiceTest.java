package student.model.Car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest {

    private CarService carService;

    @BeforeEach
    void setUp() {
        // Create mock repository
        CarRepository mockRepo = new CarRepository() {
            @Override
            public List<Car> getAllCars() {
                return List.of(
                        new Car("ABC123", new BigDecimal("30.00"), Brand.TOYOTA, false, "Corolla"),
                        new Car("XYZ789", new BigDecimal("45.00"), Brand.TESLA, true, "Model 3"),
                        new Car("LMN456", new BigDecimal("25.00"), Brand.BYD, false, "Focus")
                );
            }
        };
        carService = new CarService(mockRepo);
    }

    @Test
    void testGetAllCars_shouldReturnAll() {
        List<Car> cars = carService.getAllCars();
        assertEquals(3, cars.size());
    }

    @Test
    void testGetAllElectricCars_shouldReturnOnlyElectric() {
        List<Car> electricCars = carService.getAllElectricCars();
        assertEquals(1, electricCars.size());
        assertTrue(electricCars.get(0).isElectric());
        assertEquals("Model 3", electricCars.get(0).getModel());
    }

    @Test
    void testGetCar_byValidRegNumber_shouldReturnCar() {
        Car car = carService.getCar("ABC123");
        assertNotNull(car);
        assertEquals("Corolla", car.getModel());
    }

    @Test
    void testGetCar_byInvalidRegNumber_shouldThrow() {
        assertThrows(IllegalStateException.class, () -> carService.getCar("INVALID"));
    }

    @Test
    void testSortCarsByPrice_shouldReturnInAscendingOrder() {
        List<Car> sorted = carService.sortCarsByPrice();
        assertEquals("Focus", sorted.get(0).getModel());  // Cheapest first
        assertEquals("Model 3", sorted.get(2).getModel()); // Most expensive last
    }

    @Test
    void testGetCarsByPriceRange_shouldReturnWithinRange() {
        List<Car> filtered = carService.getCarsByPriceRange(new BigDecimal("26.00"), new BigDecimal("40.00"));
        assertEquals(1, filtered.size());
        assertEquals("Corolla", filtered.get(0).getModel());
    }

    @Test
    void testSearchCars_shouldFindByKeyword() {
        List<Car> results = carService.searchCars("tesla");
        assertEquals(1, results.size());
        assertEquals("Model 3", results.get(0).getModel());
    }
}

package student.model.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.model.Car.Brand;
import student.model.Car.Car;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarFilterEngineTest {

    private List<Car> sampleCars;

    @BeforeEach
    void setUp() {
        sampleCars = List.of(
                new Car("A1", new BigDecimal("50.00"), Brand.TESLA, true, "Model Y"),
                new Car("A2", new BigDecimal("30.00"), Brand.HONDA, false, "Civic"),
                new Car("A3", new BigDecimal("40.00"), Brand.XIAOMI, false, "X1"),
                new Car("A4", new BigDecimal("25.00"), Brand.BYD, true, "Han"),
                new Car("A5", new BigDecimal("60.00"), Brand.AUDI, false, "A4")
        );
    }

    @Test
    void sortByPrice_shouldSortCarsInAscendingOrder() {
        List<Car> sorted = CarFilterEngine.sortByPrice(sampleCars);
        assertEquals("A4", sorted.get(0).getRegNumber()); // 25.00
        assertEquals("A2", sorted.get(1).getRegNumber()); // 30.00
        assertEquals("A3", sorted.get(2).getRegNumber()); // 40.00
        assertEquals("A1", sorted.get(3).getRegNumber()); // 50.00
        assertEquals("A5", sorted.get(4).getRegNumber()); // 60.00
    }

    @Test
    void filterByPriceRange_shouldIncludeOnlyCarsWithinRange() {
        List<Car> filtered = CarFilterEngine.filterByPriceRange(sampleCars, new BigDecimal("30"), new BigDecimal("50"));
        List<String> regNumbers = filtered.stream().map(Car::getRegNumber).toList();

        assertEquals(3, filtered.size());
        assertTrue(regNumbers.contains("A1"));
        assertTrue(regNumbers.contains("A2"));
        assertTrue(regNumbers.contains("A3"));
    }

    @Test
    void filterByPriceRange_shouldReturnEmptyIfNoMatch() {
        List<Car> filtered = CarFilterEngine.filterByPriceRange(sampleCars, new BigDecimal("100"), new BigDecimal("200"));
        assertTrue(filtered.isEmpty());
    }

    @Test
    void searchByKeyword_shouldMatchBrandOrModel_caseInsensitive() {
        List<Car> result1 = CarFilterEngine.searchByKeyword(sampleCars, "byd");
        assertEquals(1, result1.size());
        assertEquals("A4", result1.get(0).getRegNumber());

        List<Car> result2 = CarFilterEngine.searchByKeyword(sampleCars, "model");
        assertEquals(1, result2.size());
        assertEquals("A1", result2.get(0).getRegNumber());

        List<Car> result3 = CarFilterEngine.searchByKeyword(sampleCars, "x1");
        assertEquals(1, result3.size());
        assertEquals("A3", result3.get(0).getRegNumber());
    }

    @Test
    void searchByKeyword_shouldReturnEmptyIfNoMatch() {
        List<Car> result = CarFilterEngine.searchByKeyword(sampleCars, "unknown");
        assertTrue(result.isEmpty());
    }
}

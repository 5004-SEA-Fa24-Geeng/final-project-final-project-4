package student.model.Car;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    void shouldCreateCarWithCorrectValues() {
        Car car = new Car("BYD123", new BigDecimal("99.99"), Brand.BYD, true, "Han");

        assertEquals("BYD123", car.getRegNumber());
        assertEquals(new BigDecimal("99.99"), car.getRentalPricePerDay());
        assertEquals(Brand.BYD, car.getBrand());
        assertTrue(car.isElectric());
        assertEquals("Han", car.getModel());
    }

    @Test
    void shouldCompareEqualCars() {
        Car car1 = new Car("A1", new BigDecimal("50.00"), Brand.TESLA, false, "Model Y");
        Car car2 = new Car("A1", new BigDecimal("50.00"), Brand.TESLA, false, "Model Y");

        assertEquals(car1, car2);
        assertEquals(car1.hashCode(), car2.hashCode());
    }

    @Test
    void shouldNotMatchDifferentCars() {
        Car car1 = new Car("A1", new BigDecimal("50.00"), Brand.TESLA, false, "Model Y");
        Car car2 = new Car("B2", new BigDecimal("70.00"), Brand.AUDI, true, "Q4");

        assertNotEquals(car1, car2);
    }

    @Test
    void toStringShouldContainCarDetails() {
        Car car = new Car("C999", new BigDecimal("88.00"), Brand.XIAOMI, true, "SU7");
        String out = car.toString();

        assertTrue(out.contains("C999"));
        assertTrue(out.contains("88.00"));
        assertTrue(out.contains("SU7"));
        assertTrue(out.contains("XIAOMI"));
    }
}

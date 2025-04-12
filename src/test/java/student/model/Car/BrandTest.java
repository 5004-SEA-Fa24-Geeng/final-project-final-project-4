package student.model.Car;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrandTest {

    @Test
    void testValueOf_shouldReturnCorrectEnum() {
        assertEquals(Brand.TESLA, Brand.valueOf("TESLA"));
        assertEquals(Brand.BYD, Brand.valueOf("BYD"));
        assertEquals(Brand.XIAOMI, Brand.valueOf("XIAOMI"));
    }

    @Test
    void testValueOf_invalidInput_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Brand.valueOf("LAMBORGHINI"));
    }

    @Test
    void testBrandList_shouldContainAllExpectedValues() {
        Brand[] expected = {
                Brand.TESLA, Brand.AUDI, Brand.MERCEDES, Brand.TOYOTA,
                Brand.HONDA, Brand.BYD, Brand.XIAOMI, Brand.SUBARU, Brand.KIA
        };
        assertArrayEquals(expected, Brand.values());
    }
}

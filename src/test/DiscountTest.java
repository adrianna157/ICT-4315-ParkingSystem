import static org.junit.jupiter.api.Assertions.*;

import main.Discount;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import main.CarType;

public class DiscountTest {

    // Happy Path
    @Test
    void testDiscountAppliesOnApplicableDayAndCarType() {
        Discount discount = new Discount("Weekend Special", 20, Arrays.asList("Saturday", "Sunday"), Arrays.asList(CarType.COMPACT, CarType.SUV));

        assertTrue(discount.isApplicable("Saturday", CarType.COMPACT));
        assertTrue(discount.isApplicable("Sunday", CarType.SUV));

        assertTrue(discount.isApplicable("Sunday", CarType.COMPACT));
    }

    @Test
    void testGetters() {
        Discount discount = new Discount("Weekday Discount", 15, Arrays.asList("Monday", "Tuesday"), List.of(CarType.COMPACT));

        assertEquals("Weekday Discount", discount.getName());
        assertEquals(15, discount.getDiscountPercentage());
        assertTrue(discount.getApplicableDays().containsAll(Arrays.asList("Monday", "Tuesday")));
        assertTrue(discount.getApplicableCarTypes().contains(CarType.COMPACT));
    }

    // Sad Path
    @Test
    void testDiscountDoesNotApplyOnNonApplicableDay() {
        Discount discount = new Discount("Weekend Special", 20, Arrays.asList("Saturday", "Sunday"), Arrays.asList(CarType.COMPACT, CarType.SUV));

        assertFalse(discount.isApplicable("Monday", CarType.COMPACT));
    }

    @Test
    void testDiscountDoesNotApplyOnNonApplicableCarType() {
        Discount discount = new Discount("Weekend Special", 20, Arrays.asList("Saturday", "Sunday"), Arrays.asList(CarType.COMPACT, CarType.SUV));

        assertFalse(discount.isApplicable("Saturday", CarType.TRUCK));
    }

    @Test
    void testDiscountDoesNotApplyOnNonApplicableDayAndCarType() {
        Discount discount = new Discount("Weekend Special", 20, Arrays.asList("Saturday", "Sunday"), Arrays.asList(CarType.COMPACT, CarType.SUV));

        assertFalse(discount.isApplicable("Monday", CarType.COMPACT));
    }
}

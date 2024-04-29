import static org.junit.jupiter.api.Assertions.*;


import main.Car;
import main.CarType;
import main.Money;
import main.ParkingPermit;
import main.parking.charges.strategy.BaseChargeStrategy;
import main.Discount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


class BaseChargeStrategyTest {
    private BaseChargeStrategy baseChargeStrategy;
    private BaseChargeStrategy baseChargeStrategy2;
    private Money baseRate;
    private ParkingPermit permit;
    private ParkingPermit permit2;
    private List<Discount> discounts;
    private List<Discount> discount2;
    private final Car car = new Car("Permit01", LocalDate.now(), "LICENSE123", CarType.COMPACT, "John Doe");

    @BeforeEach
    void setUp() {
        baseRate = new Money(10);
        // Set the registration date to a known Sunday and the discount to apply on weekends
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.set(2023, Calendar.JANUARY, 1); // Set to a known Sunday
        Calendar expirationDate = (Calendar) registrationDate.clone();
        expirationDate.add(Calendar.DAY_OF_YEAR, 1); // Set expiration date to one day from registration date
        permit = new ParkingPermit("Permit01", car, expirationDate, registrationDate);
        discounts = List.of(new Discount("Weekend Special", 30, Arrays.asList("Saturday", "Sunday"), List.of(CarType.COMPACT)));

        baseChargeStrategy = new BaseChargeStrategy(baseRate, discounts) {
            @Override
            protected Money calculateFee(Money baseRate, long entryTime, ParkingPermit permit, long exitTime) {
                return baseRate;
            }
        };

        // Set the registration date to a known Monday and the discount to apply on weekdays
        Calendar registrationDate2 = Calendar.getInstance();
        registrationDate2.set(2023, Calendar.JANUARY, 2); // Set to a known Monday
        Calendar expirationDate2 = (Calendar) registrationDate2.clone();
        expirationDate2.add(Calendar.DAY_OF_YEAR, 1); // Set expiration date to one day from registration date
        permit2 = new ParkingPermit("Permit02", car, expirationDate2, registrationDate2);
        discount2 = List.of(new Discount("Weekday Special", 20, Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"), List.of(CarType.COMPACT)));

        baseChargeStrategy2 = new BaseChargeStrategy(baseRate, discount2) {
            @Override
            protected Money calculateFee(Money baseRate, long entryTime, ParkingPermit permit, long exitTime){
                return baseRate;
            }
        };
    }

    // Happy path tests
    @Test
    void calculateParkingFeeValidInputsWithNoDiscount() {
        assertDoesNotThrow(() -> baseChargeStrategy.calculateParkingFee(1, permit, 5));
    }


    @Test
    void calculateParkingFeeDifferentBaseRatesWithNoDiscount() {
        Money differentBaseRate = new Money(20);
        BaseChargeStrategy baseChargeStrategyWithDifferentRate = new BaseChargeStrategy(differentBaseRate, discounts) {
            @Override
            protected Money calculateFee(Money baseRate, long entryTime, ParkingPermit permit, long exitTime) {
                return baseRate;
            }
        };
        assertDoesNotThrow(() -> baseChargeStrategyWithDifferentRate.calculateParkingFee( 1, permit, 5));
    }

    @Test
    void applyDiscountsDiscountAppliedWeekendSpecial() {
        // Calculate the expected discounted rate
        long expectedCents = (long) (baseRate.getCents() * (1 - discounts.getFirst().getDiscountPercentage() / 100));

        // Apply discounts
        Money discountedRate = baseChargeStrategy.applyDiscounts(permit);

        // Check if the 30% discounted rate is as expected
        assertEquals(expectedCents, discountedRate.getCents());
    }

    @Test
    void applyDiscountsDiscountAppliedWeekdaySpecial() {
        // Calculate the expected discounted rate
        long expectedCents = (long) (baseRate.getCents() * (1 - discount2.getFirst().getDiscountPercentage() / 100));

        // Apply discounts
        Money discountedRate = baseChargeStrategy2.applyDiscounts(permit2);

        // Check if the 20% discounted rate is as expected
        assertEquals(expectedCents, discountedRate.getCents());
    }

    // Sad path tests
    @Test
    void calculateParkingFeeNullBaseRate() {

        assertThrows(IllegalArgumentException.class, () -> new BaseChargeStrategy(null, discounts) {
                @Override
                protected Money calculateFee(Money baseRate, long entryTime, ParkingPermit permit, long exitTime) {
                    return baseRate;
                }
            }
        );
    }

    @Test
    void calculateParkingFeeNullPermit() {
        assertThrows(IllegalArgumentException.class, () -> baseChargeStrategy.calculateParkingFee( 1, null, 5));
    }

    @Test
    void calculateParkingFeeNegativeEntryTime() {
        assertThrows(IllegalArgumentException.class, () -> baseChargeStrategy.calculateParkingFee(-1, permit, 5));
    }

    @Test
    void applyDiscountsNullPermit() {
        assertThrows(IllegalArgumentException.class, () -> baseChargeStrategy.applyDiscounts(null));
    }

    @Test
    void applyDiscountsNoDiscountsWithNoDiscountsSet() {
        BaseChargeStrategy baseChargeStrategy = new BaseChargeStrategy(baseRate, new ArrayList<>()) {
            @Override
            protected Money calculateFee(Money baseRate, long entryTime, ParkingPermit permit, long exitTime) {
                return baseRate;
            }
        };
        Money discountedRate = baseChargeStrategy.applyDiscounts(permit);
        assertEquals(baseRate.getCents(), discountedRate.getCents());
    }

    @Test
    void applyDiscountsNoDiscountsWhenNotApplicable() {
        // Try to Apply discounts for a permit that is not eligible for any discounts
        Money discountedRate = baseChargeStrategy.applyDiscounts(permit2);

        // Check if the discounted rate is the same as the base rate since no discounts are applicable
        assertEquals(discountedRate.getCents(), baseRate.getCents());
    }
}
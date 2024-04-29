import main.*;
import main.Discount;
import main.parking.charges.strategy.FlatRateChargeStrategy;
import main.parking.charges.strategy.HourlyChargeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {
    private List<Discount> discounts;
    private List<Discount> discounts2;

    private Calendar registrationDate1;
    private Calendar expirationDate1;

    private Calendar registrationDate2;

    private Calendar expirationDate2;


    private final long entryTime = 0;
    private final long exitTime = 8 * 3600 * 1000; // 8 hours after entry

    @BeforeEach
    void setUp() {
        // Set the registration date to a known Sunday and the discount to apply on weekends
       registrationDate1 = Calendar.getInstance();
        registrationDate1.set(2023, Calendar.JANUARY, 1); // Set to a known Sunday
        expirationDate1 = (Calendar) registrationDate1.clone();
        expirationDate1.add(Calendar.DAY_OF_YEAR, 1); // Set expiration date to one day from registration date
        discounts = List.of(new Discount("Weekend Special", 30, Arrays.asList("Saturday", "Sunday"), List.of(CarType.COMPACT)));

        // Set the registration date to a known Monday and the discount to apply on weekdays
        registrationDate2 = Calendar.getInstance();
        registrationDate2.set(2023, Calendar.JANUARY, 2); // Set to a known Monday
        expirationDate2 = (Calendar) registrationDate2.clone();
        expirationDate2.add(Calendar.DAY_OF_YEAR, 1); // Set expiration date to one day from registration date
        discounts2 = List.of(new Discount("Weekday Special", 20, Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"), List.of(CarType.COMPACT)));
    }

    // Updated test to use the constructor with main.Address components
    @Test
    void creatingParkingLotWithAllFieldsShouldSucceed() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        assertEquals("P123", parkingLot.getLotId());

        assertEquals("123 Main St, Anytown, Anystate 12345", parkingLot.getAddress());
        assertEquals(50, parkingLot.getCapacity());
    }

    @Test
    void settingLotIdShouldChangeValue() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        parkingLot.setLotId("P456");
        assertEquals("P456", parkingLot.getLotId());
    }

    // Updated test to create and set a new main.Address object
    @Test
    void settingAddressShouldChangeValue() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        Address newAddress = new Address("456 Elm St", "", "Othertown", "Otherstate", "67890");
        parkingLot.setAddress(newAddress);
        // Adjust the expected value according to how main.Address.toString() is implemented
        assertEquals("456 Elm St, Othertown, Otherstate 67890", parkingLot.getAddress());
    }

    @Test
    void settingCapacityShouldChangeValue() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        parkingLot.setCapacity(100);
        assertEquals(100, parkingLot.getCapacity());
    }

    @Test
    void assigningHourlyChargeStrategyShouldSucceed() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        parkingLot.assignStrategy("HOURLY", discounts, new Money(200));
        assertInstanceOf(HourlyChargeStrategy.class, parkingLot.getParkingChargeStrategy());
    }

    @Test
    void assigningFlatRateChargeStrategyShouldSucceed() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        parkingLot.assignStrategy("FLAT_RATE", discounts, new Money(200));
        assertInstanceOf(FlatRateChargeStrategy.class, parkingLot.getParkingChargeStrategy());
    }

    @Test
    void shouldApplyDiscountForHourlyParkingOfCompactCar() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        parkingLot.assignStrategy("HOURLY", discounts2, new Money(200));

        // Create a compact car and a permit for it
        Car compactCar = new Car("C123", LocalDate.now(), "ABC123", CarType.COMPACT, "John Doe");
        ParkingPermit permit = new PermitManager().register(compactCar);
        permit.setRegistrationDate(registrationDate2);
        permit.setExpirationDate(expirationDate2);


        Money feeCharged = parkingLot.getParkingChargeStrategy().calculateParkingFee(entryTime, permit, exitTime);

        // Verify that 20% discount is applied to 8 hours * 2 dollars
        assertEquals(1280, feeCharged.getCents());
    }
    @Test
    void shouldCalculateCorrectFeeForHourlyParkingOfCompactCarOverOneHour() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        parkingLot.assignStrategy("HOURLY", discounts2, new Money(200));

        // Create a compact car and a permit for it
        Car compactCar = new Car("C123", LocalDate.now(), "ABC123", CarType.COMPACT, "John Doe");
        ParkingPermit permit = new PermitManager().register(compactCar);
        permit.setRegistrationDate(registrationDate2);
        permit.setExpirationDate(expirationDate2);


        Money feeCharged = parkingLot.getParkingChargeStrategy().calculateParkingFee(entryTime, permit, exitTime);

        // Verify that 20% discount is applied to 8 hours * 2 dollars
        assertEquals(1280, feeCharged.getCents());
    }

    @Test
    void shouldApplyDiscountForFlatRateParkingOfCompactCar() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        parkingLot.assignStrategy("FLAT_RATE", discounts, new Money(200));


        // Create a compact car and a permit for it
        Car compactCar = new Car("C123", LocalDate.now(), "ABC123", CarType.COMPACT, "John Doe");
        ParkingPermit permit = new PermitManager().register(compactCar);
        permit.setRegistrationDate(registrationDate1);
        permit.setExpirationDate(expirationDate1);

        Money feeCharged = parkingLot.getParkingChargeStrategy().calculateParkingFee(entryTime, permit, exitTime);

        // Verify that 30% discount is applied
        assertEquals(140, feeCharged.getCents());
    }

    @Test
    void shouldMaintainFlatRateForParkingOfCompactCarOverOneHour() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        parkingLot.assignStrategy("FLAT_RATE", discounts, new Money(200));

        // Create a compact car and a permit for it
        Car compactCar = new Car("C123", LocalDate.now(), "ABC123", CarType.COMPACT, "John Doe");
        ParkingPermit permit = new PermitManager().register(compactCar);

        // passed 8 hours
        long mockExitTime = 10 * 3600 * 1000; // 10 hours ago
        Money feeCharged = parkingLot.getParkingChargeStrategy().calculateParkingFee(entryTime, permit, mockExitTime);

        // Verify that 30% discount is applied
        assertEquals(140, feeCharged.getCents());
    }

    // Sad path tests remain valid but update the settingAddressToNullShouldThrowException test
    @Test
    void settingLotIdToNullShouldThrowException() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        assertThrows(IllegalArgumentException.class, () -> parkingLot.setLotId(null));
    }

    @Test
    void settingAddressToNullShouldThrowException() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        assertThrows(IllegalArgumentException.class, () -> parkingLot.setAddress(null));
    }

    @Test
    void settingCapacityToNegativeShouldThrowException() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        assertThrows(IllegalArgumentException.class, () -> parkingLot.setCapacity(-1));
    }
    @Test
    void testEqualsWithNull() {
        ParkingLot parkingLot1 = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        assertNotEquals(null, parkingLot1, "Equals method should return false when comparing with null");
    }

    @Test
    void testEqualsWithDifferentClass() {
        ParkingLot parkingLot1 = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        assertNotEquals(parkingLot1, new Object(), "Equals method should return false when comparing with an object of a different class");
    }

    @Test
    void shouldThrowExceptionWhenInvalidParkingChargeStrategyIsAssigned() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);

        assertThrows(IllegalArgumentException.class, () ->  parkingLot.assignStrategy("HOUR", discounts, new Money(200)));
    }

    @Test
    void shouldThrowExceptionWhenBaseRateIsNullForHourlyStrategy() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);

        assertThrows(IllegalArgumentException.class, () -> parkingLot.assignStrategy("HOURLY", discounts, null));
    }

    @Test
    void shouldThrowExceptionWhenPermitIsNullForHourlyStrategy() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        parkingLot.assignStrategy("HOURLY", discounts, new Money(200));

        assertThrows(IllegalArgumentException.class, () -> parkingLot.getParkingChargeStrategy().calculateParkingFee(entryTime, null, exitTime));
    }

    @Test
    void shouldThrowExceptionWhenEntryTimeIsNullForHourlyStrategy() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        parkingLot.assignStrategy("HOURLY", discounts, new Money(200));

        Car compactCar = new Car("C123", LocalDate.now(), "ABC123", CarType.COMPACT, "John Doe");
        ParkingPermit permit = new PermitManager().register(compactCar);

        assertThrows(IllegalArgumentException.class, () -> parkingLot.getParkingChargeStrategy().calculateParkingFee(-1, permit, exitTime));
    }
    @Test
    void shouldThrowExceptionWhenBaseRateIsNullForFlatStrategy() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);

        assertThrows(IllegalArgumentException.class, () -> parkingLot.assignStrategy("FLAT_RATE", discounts, null));
    }

    @Test
    void shouldThrowExceptionWhenPermitIsNullForFlatStrategy() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        parkingLot.assignStrategy("FLAT_RATE", discounts, new Money(200));

        assertThrows(IllegalArgumentException.class, () -> parkingLot.getParkingChargeStrategy().calculateParkingFee(entryTime, null, exitTime));
    }

    @Test
    void shouldThrowExceptionWhenEntryTimeIsNullForFlatStrategy() {
        ParkingLot parkingLot = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        parkingLot.assignStrategy("FLAT_RATE", discounts, new Money(200));

        Car compactCar = new Car("C123", LocalDate.now(), "ABC123", CarType.COMPACT, "John Doe");
        ParkingPermit permit = new PermitManager().register(compactCar);

        assertThrows(IllegalArgumentException.class, () -> parkingLot.getParkingChargeStrategy().calculateParkingFee( -1, permit, exitTime));
    }
}

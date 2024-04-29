import main.Car;
import main.CarType;
import main.Money;
import main.ParkingPermit;
import main.Discount;
import main.parking.charges.strategy.HourlyChargeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HourlyChargeStrategyTest {
    private HourlyChargeStrategy hourlyChargeStrategy;
    private Money baseRate;
    private ParkingPermit permit;

    private final Car car = new Car("Permit01", LocalDate.now(), "LICENSE123", CarType.COMPACT, "John Doe");

    private Discount weeklyDiscount;
    private final long entryTime = 0;

    private final long exitTime = 8 * 3600 * 1000; // 8 hours after entry;


    @BeforeEach
    void setUp() {
        baseRate = new Money(1000);

        // Set the registration date to a weekday
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.set(2023, Calendar.JANUARY, 2); // Set to a known Monday
        Calendar expirationDate = (Calendar) registrationDate.clone();
        expirationDate.add(Calendar.DAY_OF_YEAR, 1); // Set expiration date to one day from registration date
        permit = new ParkingPermit("Permit01", car, expirationDate, registrationDate);

        // Weekday discount
        weeklyDiscount = new Discount("Weekday", 20, Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"), List.of(CarType.COMPACT));

        hourlyChargeStrategy = new HourlyChargeStrategy(baseRate, List.of(weeklyDiscount));
    }

    // Happy path tests
    @Test
    void calculateFeeHappyPath() {
        double discountedRate =  (1 - weeklyDiscount.getDiscountPercentage() / 100);
        long hoursSinceEntry = Duration.between(Instant.ofEpochMilli(entryTime), Instant.ofEpochMilli(exitTime)).toHours();

        double expectedFee = discountedRate * baseRate.getDollars() * hoursSinceEntry;
        Money fee = hourlyChargeStrategy.calculateParkingFee(entryTime, permit, exitTime);

        assertEquals(expectedFee, fee.getDollars());
    }

    @Test
    void calculateFeePrintsCorrectInformation() {
        // Redirect standard output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the method that prints the information
        hourlyChargeStrategy.calculateParkingFee(entryTime, permit, exitTime);

        // Build the expected output
        String expectedOutput = String.format(
                "Hours since Entry: %s%n" +
                "Parking Permit: %s%n" +
                "Hourly Fee Rate amount: $%.2f%n" +
                "Total amount with Discount: $%.2f%n",
                8,
                "Permit01",
                10.00,
                64.00
        );

        // Assert that the actual output matches the expected output
        assertEquals(expectedOutput, outContent.toString());

        // Reset standard output
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    // Sad path tests
    @Test
    void calculateFeeNullBaseRate() {
        assertThrows(IllegalArgumentException.class, () -> new HourlyChargeStrategy(null, List.of(new Discount("Weekend Special", 30, Arrays.asList("Saturday", "Sunday"), List.of(CarType.COMPACT)))));
    }

    @Test
    void calculateFeeNullPermit() {
        assertThrows(IllegalArgumentException.class, () -> hourlyChargeStrategy.calculateParkingFee(1, null, exitTime));
    }

    @Test
    void calculateFeeNegativeEntryTime() {
        assertThrows(IllegalArgumentException.class, () -> hourlyChargeStrategy.calculateParkingFee(-1, permit, exitTime));
    }
}
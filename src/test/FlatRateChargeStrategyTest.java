import main.Car;
import main.CarType;
import main.Money;
import main.ParkingPermit;
import main.Discount;
import main.parking.charges.strategy.FlatRateChargeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlatRateChargeStrategyTest {
    private FlatRateChargeStrategy flatRateChargeStrategy;
    private ParkingPermit permit;
    long entryTime = 0;
    long exitTime = 8 * 3600 * 1000; // 8 hours after entry

    private final Car car = new Car("Permit01", LocalDate.now(), "LICENSE123", CarType.COMPACT, "John Doe");


    @BeforeEach
    void setUp() {
        // Set the base rate to $10
        Money baseRate = new Money(1000);

        // Set the registration date to a known Sunday
        Calendar registrationDate = Calendar.getInstance();
        registrationDate.set(2023, Calendar.JANUARY, 1); // Set to a known Sunday
        Calendar expirationDate = (Calendar) registrationDate.clone();
        expirationDate.add(Calendar.DAY_OF_YEAR, 1); // Set expiration date to one day from registration date
        permit = new ParkingPermit("Permit01", car, expirationDate, registrationDate);

        // Create a FlatRateChargeStrategy with a base rate of $10 and a 30% discount on weekends for compact cars
        flatRateChargeStrategy = new FlatRateChargeStrategy(baseRate, List.of(new Discount("Weekend Special", 30, Arrays.asList("Saturday", "Sunday"), List.of(CarType.COMPACT))));

    }


    // Happy path tests
    @Test
    void calculateFeeHappyPath() {
        Money discountedRate = flatRateChargeStrategy.applyDiscounts(permit);
        Money fee = flatRateChargeStrategy.calculateParkingFee(entryTime, permit, exitTime);

        assertEquals(discountedRate.getDollars(), fee.getDollars());
    }

    @Test
    void calculateFeePrintsCorrectInformation() {

            // Redirect standard output
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            // Call the method that prints the information
            flatRateChargeStrategy.calculateParkingFee(entryTime, permit, exitTime);

            // Build the expected output
            String expectedOutput = String.format(
                    "Hours since Entry: %s%n" +
                    "Parking Permit: %s%n" +
                    "Flat Rate Fee amount: $%.2f%n" +
                    "Total amount with Discount: $%.2f%n",
                    8,
                    "Permit01",
                    10.00,
                    7.00
            );

            // Assert that the actual output matches the expected output
            assertEquals(expectedOutput, outContent.toString());

            // Reset standard output
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    // Sad path tests
    @Test
    void calculateFeeNullBaseRate() {
        assertThrows(IllegalArgumentException.class, () -> new FlatRateChargeStrategy(null, List.of(new Discount("Weekend Special", 30, Arrays.asList("Saturday", "Sunday"), List.of(CarType.COMPACT)))));
    }

    @Test
    void calculateFeeNullPermit() {
        assertThrows(IllegalArgumentException.class, () -> flatRateChargeStrategy.calculateParkingFee(entryTime, null, exitTime));
    }

    @Test
    void calculateFeeNegativeEntryTime() {
        assertThrows(IllegalArgumentException.class, () -> flatRateChargeStrategy.calculateParkingFee(-1, permit, exitTime));
    }
}
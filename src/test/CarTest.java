package test;

import main.Car;
import main.CarType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class CarTest {


    // Happy path tests
    @Test
    void creatingCarWithSUVTypeShouldSucceed() {
        Car car = new Car("123", LocalDate.now(), "ABC123", CarType.SUV, "owner1");
        Assertions.assertEquals("123", car.getPermit());
        Assertions.assertEquals("ABC123", car.getLicense());
        Assertions.assertEquals(CarType.SUV, car.getType());
        Assertions.assertEquals("owner1", car.getOwner());
    }

    @Test
    void creatingCarWithCompactTypeShouldSucceed() {
        Car car = new Car("123", LocalDate.now(), "ABC123", CarType.COMPACT, "owner1");
        Assertions.assertEquals("123", car.getPermit());
        Assertions.assertEquals("ABC123", car.getLicense());
        Assertions.assertEquals(CarType.COMPACT, car.getType());
        Assertions.assertEquals("owner1", car.getOwner());
    }

    @Test
    void settingTypeToSUVShouldChangeValue() {
        Car car = new Car("123", LocalDate.now(), "ABC123", CarType.COMPACT, "owner1");
        car.setType(CarType.SUV);
        Assertions.assertEquals(CarType.SUV, car.getType());
    }

    @Test
    void settingTypeToCompactShouldChangeValue() {
        Car car = new Car("123", LocalDate.now(), "ABC123", CarType.SUV, "owner1");
        car.setType(CarType.COMPACT);
        Assertions.assertEquals(CarType.COMPACT, car.getType());
    }

    @Test
    void testEqualsAndHashCode() {
        Car car1 = new Car("123", LocalDate.now(), "ABC123", CarType.SUV, "owner1");
        Car car2 = new Car("123", LocalDate.now(), "ABC123", CarType.SUV, "owner1");
        Car car3 = new Car("456", LocalDate.now(), "DEF456", CarType.COMPACT, "owner2");

        // Test equals
        assertEquals(car1, car2, "Equals method should return true for cars with the same permit");
        assertNotEquals(car1, car3, "Equals method should return false for cars with different permits");

        // Test hashCode
        assertEquals(car1.hashCode(), car2.hashCode(), "hashCode method should return the same value for cars with the same permit");
        assertNotEquals(car1.hashCode(), car3.hashCode(), "hashCode method should return different values for cars with different permits");
    }

    // Sad path tests
    @Test
    void settingPermitToNullShouldThrowException() {
        Car car = new Car("123", LocalDate.now(), "ABC123", CarType.SUV, "owner1");
        assertThrows(IllegalArgumentException.class, () -> car.setPermit(null));
    }

    @Test
    void settingPermitExpirationToPastDateShouldThrowException() {
        Car car = new Car("123", LocalDate.now(), "ABC123", CarType.SUV, "owner1");
        assertThrows(IllegalArgumentException.class, () -> car.setPermitExpiration(LocalDate.now().minusDays(1)));
    }

    @Test
    void settingLicenseToNullShouldThrowException() {
        Car car = new Car("123", LocalDate.now(), "ABC123", CarType.SUV, "owner1");
        assertThrows(IllegalArgumentException.class, () -> car.setLicense(null));
    }

    @Test
    void settingTypeToNullShouldThrowException() {
        Car car = new Car("123", LocalDate.now(), "ABC123", CarType.SUV, "owner1");
        assertThrows(IllegalArgumentException.class, () -> car.setType(null));
    }

    @Test
    void settingOwnerToNullShouldThrowException() {
        Car car = new Car("123", LocalDate.now(), "ABC123", CarType.SUV, "owner1");
        assertThrows(IllegalArgumentException.class, () -> car.setOwner(null));
    }
}

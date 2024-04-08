import main.Car;
import main.CarType;
import main.ParkingPermit;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class ParkingPermitTest {

    private final Car car = new Car("Permit01", LocalDate.now(), "LICENSE123", CarType.COMPACT, "John Doe");

    // Happy Path
    @Test
    void setId_ShouldUpdateIdSuccessfully() {
        ParkingPermit permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), Calendar.getInstance());
        permit.setId("Permit02");
        assertEquals("Permit02", permit.getId(), "ID should be updated successfully.");
    }

    @Test
    void setCar_ShouldUpdateCarSuccessfully() {
        ParkingPermit permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), Calendar.getInstance());
        Car newCar = new Car("Permit02", LocalDate.now(), "LICENSE456", CarType.SUV, "Jane Doe");
        permit.setCar(newCar);
        assertEquals(newCar, permit.getCar(), "Car should be updated successfully.");
    }

    @Test
    void setExpirationDate_ShouldUpdateExpirationDateSuccessfully() {
        ParkingPermit permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), Calendar.getInstance());
        Calendar newExpirationDate = Calendar.getInstance();
        newExpirationDate.add(Calendar.YEAR, 1);
        permit.setExpirationDate(newExpirationDate);
        assertEquals(newExpirationDate, permit.getExpirationDate(), "Expiration date should be updated successfully.");
    }

    @Test
    void setRegistrationDate_ShouldUpdateRegistrationDateSuccessfully() {
        ParkingPermit permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), Calendar.getInstance());
        Calendar newRegistrationDate = Calendar.getInstance();
        newRegistrationDate.add(Calendar.YEAR, -1);
        permit.setRegistrationDate(newRegistrationDate);
        assertEquals(newRegistrationDate, permit.getRegistrationDate(), "Registration date should be updated successfully.");
    }

    @Test
    void isExpired_ShouldReturnFalseForFutureExpirationDate() {
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.YEAR, 1);
        ParkingPermit permit = new ParkingPermit("Permit01", car, futureDate, Calendar.getInstance());
        assertFalse(permit.isExpired(), "Permit with a future expiration date should not be expired.");
    }

    @Test
    void isRegistered_ShouldReturnTrueForPastRegistrationDate() {
        Calendar pastDate = Calendar.getInstance();
        pastDate.add(Calendar.YEAR, -1);
        ParkingPermit permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), pastDate);
        assertTrue(permit.isRegistered(), "Permit with a past registration date should be considered registered.");
    }

    @Test
    void isRegisteredTo_ShouldReturnTrueForMatchingCar() {
        ParkingPermit permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), Calendar.getInstance());
        assertTrue(permit.isRegisteredTo(car), "Permit should be registered to the provided car.");

    }

    // Sad Path
    @Test
    void setId_WithNullValue_ShouldThrowException() {
        ParkingPermit permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), Calendar.getInstance());
        assertThrows(IllegalArgumentException.class, () -> permit.setId(null), "Setting ID to null should throw IllegalArgumentException.");
    }

    @Test
    void setCar_WithNullValue_ShouldThrowException() {
        ParkingPermit permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), Calendar.getInstance());
        assertThrows(IllegalArgumentException.class, () -> permit.setCar(null), "Setting car to null should throw IllegalArgumentException.");
    }

    @Test
    void setExpirationDate_WithNullValue_ShouldThrowException() {
        ParkingPermit permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), Calendar.getInstance());
        assertThrows(IllegalArgumentException.class, () -> permit.setExpirationDate(null), "Setting expiration date to null should throw IllegalArgumentException.");
    }

    @Test
    void setRegistrationDate_WithNullValue_ShouldThrowException() {
        ParkingPermit permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), Calendar.getInstance());
        assertThrows(IllegalArgumentException.class, () -> permit.setRegistrationDate(null), "Setting registration date to null should throw IllegalArgumentException.");
    }
}

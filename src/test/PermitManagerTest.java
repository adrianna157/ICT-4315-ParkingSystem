import main.Car;
import main.CarType;
import main.ParkingPermit;
import main.PermitManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.*;

class PermitManagerTest {

    private PermitManager permitManager;
    private Car car;

    @BeforeEach
    void setUp() {
        permitManager = new PermitManager();
        car = new Car("Permit01", LocalDate.now(), "LICENSE123", CarType.COMPACT, "John Doe");
    }

    // Happy Path
    @Test
    void register_ShouldSuccessfullyRegisterPermit() {
        ParkingPermit permit = permitManager.register(car);
        assertNotNull(permit, "Permit should be registered successfully.");
        assertTrue(permitManager.isRegistered(permit.getId()), "Permit should be recognized as registered.");
    }

    @Test
    void renew_ShouldRenewPermitSuccessfully() {
        ParkingPermit permit = permitManager.register(car);
        boolean result = permitManager.renew(permit.getId());
        assertTrue(result, "Permit renewal should succeed.");
        assertTrue(permit.getExpirationDate().after(Calendar.getInstance()), "Permit expiration date should be extended.");
    }

    @Test
    void unregister_ShouldRemovePermitSuccessfully() {
        ParkingPermit permit = permitManager.register(car);
        permitManager.unregister(permit.getId());
        assertFalse(permitManager.isRegistered(permit.getId()), "Permit should no longer be registered after being unregistered.");
    }

    // Sad Path
    @Test
    void getPermit_NonExistentPermitShouldReturnNull() {
        ParkingPermit permit = permitManager.getPermit(new Car("NonExistentPermit", LocalDate.now(), "NONEXIST", CarType.SUV, "No One"));
        assertNull(permit, "Query for a non-existent permit should return null.");
    }

    @Test
    void renew_UnregisteredPermitShouldFail() {
        boolean result = permitManager.renew("NonExistentPermit");
        assertFalse(result, "Renewal of an unregistered permit should fail.");
    }
}
import main.Car;
import main.ParkingOffice;
import main.RegisterCarCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterCarCommandTest {
    private ParkingOffice parkingOffice;
    private RegisterCarCommand registerCarCommand;
    private Properties properties; // Make properties an instance variable

    @BeforeEach
    void setUp() {
        parkingOffice = mock(ParkingOffice.class);
        registerCarCommand = new RegisterCarCommand(parkingOffice);
        properties = new Properties(); // Initialize properties in the setUp method
        properties.setProperty("permit", "123");
        properties.setProperty("permitExpiration", "2022-12-31");
        properties.setProperty("license", "ABC123");
        properties.setProperty("type", "SUV");
        properties.setProperty("owner", "customerId123");
    }

    // Happy  path tests
    @Test
    void executeShouldBeNullWhenSuccessful() {
        String result = registerCarCommand.execute(properties);
        assertNull(result);
    }

    @Test
    void executeShouldReturnPermitIdOnSuccess() {
        when(parkingOffice.register(any(Car.class))).thenReturn("permitId123");
        String result = registerCarCommand.execute(properties);
        assertEquals("permitId123", result);
    }

    @Test
    void executeShouldCallRegisterCar() {
        registerCarCommand.execute(properties);
        verify(parkingOffice, times(1)).register(any(Car.class));
    }

    @Test
    void getCommandNameShouldReturnCorrectCommandName() {
        assertEquals("CAR", registerCarCommand.getCommandName());
    }

    @Test
    void getDisplayNameShouldReturnCorrectDisplayName() {
        assertEquals("Register Car", registerCarCommand.getDisplayName());
    }

    // Sad path tests
    @Test
    void executeShouldThrowExceptionWhenPermitIsMissing() {
        properties.remove("permit");
        assertThrows(IllegalArgumentException.class, () -> registerCarCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenPermitExpirationIsMissing() {
        properties.remove("permitExpiration");
        assertThrows(IllegalArgumentException.class, () -> registerCarCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenLicenseIsMissing() {
        properties.remove("license");
        assertThrows(IllegalArgumentException.class, () -> registerCarCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenTypeIsMissing() {
        properties.remove("type");
        assertThrows(IllegalArgumentException.class, () -> registerCarCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenOwnerIsMissing() {
        properties.remove("owner");
        assertThrows(IllegalArgumentException.class, () -> registerCarCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenPermitExpirationIsInvalid() {
        properties.setProperty("permitExpiration", "2022-02-31");
        assertThrows(IllegalArgumentException.class, () -> registerCarCommand.execute(properties));
    }
}
import main.Customer;
import main.ParkingOffice;
import main.RegisterCustomerCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterCustomerCommandTest {
    private ParkingOffice parkingOffice;
    private RegisterCustomerCommand registerCustomerCommand;
    private Properties properties;

    @BeforeEach
    void setUp() {
        parkingOffice = mock(ParkingOffice.class);
        registerCustomerCommand = new RegisterCustomerCommand(parkingOffice);
        properties = new Properties();
        properties.setProperty("customerId", "customerId123");
        properties.setProperty("name", "John Doe");
        properties.setProperty("phoneNumber", "1234567890");
        properties.setProperty("streetAddress1", "123 Main St");
        properties.setProperty("city", "Anytown");
        properties.setProperty("state", "Anystate");
        properties.setProperty("zipCode", "12345");
    }

    // Happy path tests
    @Test
    void executeShouldReturnCustomerIdOnSuccess() {
        when(parkingOffice.register(any(Customer.class))).thenReturn("customerId123");
        String result = registerCustomerCommand.execute(properties);
        assertEquals("customerId123", result);
    }

    @Test
    void executeShouldCallRegisterCustomer() {
        registerCustomerCommand.execute(properties);
        verify(parkingOffice, times(1)).register(any(Customer.class));
    }

    @Test
    void getCommandNameShouldReturnCorrectCommandName() {
        assertEquals("CUSTOMER", registerCustomerCommand.getCommandName());
    }

    @Test
    void getDisplayNameShouldReturnCorrectDisplayName() {
        assertEquals("Register a Customer", registerCustomerCommand.getDisplayName());
    }

    // Sad path tests
    @Test
    void executeShouldThrowExceptionWhenCustomerIdIsMissing() {
        properties.remove("customerId");
        assertThrows(IllegalArgumentException.class, () -> registerCustomerCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenNameIsMissing() {
        properties.remove("name");
        assertThrows(IllegalArgumentException.class, () -> registerCustomerCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenPhoneNumberIsMissing() {
        properties.remove("phoneNumber");
        assertThrows(IllegalArgumentException.class, () -> registerCustomerCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenStreetAddress1IsMissing() {
        properties.remove("streetAddress1");
        assertThrows(IllegalArgumentException.class, () -> registerCustomerCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenCityIsMissing() {
        properties.remove("city");
        assertThrows(IllegalArgumentException.class, () -> registerCustomerCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenStateIsMissing() {
        properties.remove("state");
        assertThrows(IllegalArgumentException.class, () -> registerCustomerCommand.execute(properties));
    }

    @Test
    void executeShouldThrowExceptionWhenZipCodeIsMissing() {
        properties.remove("zipCode");
        assertThrows(IllegalArgumentException.class, () -> registerCustomerCommand.execute(properties));
    }
}
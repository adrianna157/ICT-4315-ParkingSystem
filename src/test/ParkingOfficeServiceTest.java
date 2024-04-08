import main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingOfficeServiceTest {
    private ParkingOffice parkingOffice;
    private ParkingOfficeService parkingOfficeService;
    private Command customerCommand;
    private Command carCommand;

    @BeforeEach
    void setup() {
        parkingOffice = mock(ParkingOffice.class);
        customerCommand = mock(Command.class);
        carCommand = mock(Command.class);
        when(customerCommand.getCommandName()).thenReturn("CUSTOMER");
        when(carCommand.getCommandName()).thenReturn("CAR");
        parkingOfficeService = new ParkingOfficeService(parkingOffice);
        parkingOfficeService.register(customerCommand);
        parkingOfficeService.register(carCommand);
    }

    // Happy Path
    @Test
    void performCommandShouldExecuteWhenGivenValidKeyValueParameters() {
        String[] parameters = {"param1=value1", "param2=value2"};
        parkingOfficeService.performCommand("CUSTOMER", parameters);
        verify(customerCommand, times(1)).execute(any());
        parkingOfficeService.performCommand("CAR", parameters);
        verify(carCommand, times(1)).execute(any());
    }

    @Test
    void performCommandShouldExecuteWithValidCommandAndUnrecognizedKeyInParameters() {
        String[] parameters = {"unrecognizedKey=value1", "param2=value2"};
        parkingOfficeService.performCommand("CUSTOMER", parameters);
        verify(customerCommand, times(1)).execute(any());
        parkingOfficeService.performCommand("CAR", parameters);
        verify(carCommand, times(1)).execute(any());
    }

    // Sad Path
    @Test
    void performCommandShouldNotExecuteWithNullCommand() {
        String[] parameters = {"param1=value1", "param2=value2"};
        parkingOfficeService.performCommand(null, parameters);
        verify(customerCommand, times(0)).execute(any());
        verify(carCommand, times(0)).execute(any());
    }

    @Test
    void performCommandShouldNotExecuteWithNullParameters() {
        parkingOfficeService.performCommand("CUSTOMER", null);
        verify(customerCommand, times(0)).execute(any());
        parkingOfficeService.performCommand("CAR", null);
        verify(carCommand, times(0)).execute(any());
    }

    @Test
    void performCommandShouldReturnErrorMessageWithInvalidParameters() {
        String[] parameters = {"param1"}; // Invalid because it doesn't contain '='
        String result = parkingOfficeService.performCommand("CUSTOMER", parameters);
        assertTrue(result.startsWith("Invalid parameter format:"));
    }

    @Test
    void performCommandShouldReturnErrorMessageWithInvalidCommand() {
        String[] parameters = {"param1=value1"};
        String result = parkingOfficeService.performCommand("INVALID_COMMAND", parameters);
        assertEquals("Command not recognized.", result);
    }

    @Test
    void performCommandForCustomerCommandShouldReturnErrorMessageWithMissingParameters() {
        // Create real instances of RegisterCustomerCommand and RegisterCarCommand
        Command customerCommand = new RegisterCustomerCommand(parkingOffice);

        // Register the commands
        parkingOfficeService.register(customerCommand);

        // Missing required parameters for the CUSTOMER command
        String[] parameters = {"customerId=1234", "name=Jane", "phoneNumber=5806954831", "streetAddress1=testAddress"};
        String result = parkingOfficeService.performCommand("CUSTOMER", parameters);

        assertNotNull(result, "Result should not be null");
        assertEquals(result, "Parameter check failed: Missing required parameters: city, state, zipCode");
    }

    @Test
    void performCommandForCarCommandShouldReturnErrorMessageWithMissingParameters() {
        // Create real instances of RegisterCustomerCommand and RegisterCarCommand
        Command carCommand = new RegisterCarCommand(parkingOffice);

        // Register the commands
        parkingOfficeService.register(carCommand);

        // Missing required parameters for the CUSTOMER command
        String[] parameters = {"permit=123", "permitExpiration=2022-12-31"};
        String result = parkingOfficeService.performCommand("CAR", parameters);

        assertNotNull(result, "Result should not be null");
        assertEquals(result, "Parameter check failed: Missing required parameters: license, type, owner");
    }


}
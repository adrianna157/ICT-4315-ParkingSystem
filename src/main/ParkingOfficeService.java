package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ParkingOfficeService {
    private final ParkingOffice parkingOffice;
    private final Map<String, Command> commands = new HashMap<>();

    public ParkingOfficeService(ParkingOffice parkingOffice) {
        this.parkingOffice = parkingOffice;
        initializeCommands();

    }

    private void initializeCommands() {
        // Instantiate command objects with reference to parkingOffice
        Command registerCarCommand = new RegisterCarCommand(parkingOffice);
        Command registerCustomerCommand = new RegisterCustomerCommand(parkingOffice);

        // Register the commands
        register(registerCarCommand);
        register(registerCustomerCommand);
    }


    public void register(Command command) {
        // Register command with its name as the key in the commands map
        commands.put(command.getCommandName(), command);
    }

    public String performCommand(String commandName, String[] parameters) {
        if (parameters == null) {
            return "Parameters cannot be null.";
        }

        Command command = commands.get(commandName);
        if (command == null) {
            return "Command not recognized.";
        }

        // Convert array of parameters into Properties
        Properties props = new Properties();
        for (String param : parameters) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2) {
                props.setProperty(keyValue[0], keyValue[1]);
            } else {
                return "Invalid parameter format: " + param;
            }
        }

        // Perform parameter check before executing the command
        try {
            command.checkParameters(props);
        } catch (IllegalArgumentException e) {
            // Handle or log the error as needed
            return "Parameter check failed: " + e.getMessage();
        }


        // Execute the command with the properties and return the result
        return command.execute(props);
    }
}

package main;

import java.util.Properties;

public class RegisterCustomerCommand implements Command {
    private final ParkingOffice parkingOffice;

    public RegisterCustomerCommand(ParkingOffice parkingOffice) {
        this.parkingOffice = parkingOffice;
    }

    @Override
    public void checkParameters(Properties properties) {
        Customer.validateProperties(properties);
    }


    @Override
    public String execute(Properties properties) {
        Customer customer = Customer.fromProperties(properties);
        return parkingOffice.register(customer); // This method should return the customer ID
    }

    @Override
    public String getDisplayName() {
        return "Register a Customer"; // Return a user-friendly name for the command
    }

    @Override
    public String getCommandName() {
        return "CUSTOMER"; // Return the command name that's used to invoke this command
    }

}
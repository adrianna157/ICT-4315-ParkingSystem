package main;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Properties;

public class RegisterCarCommand implements Command {
   private ParkingOffice parkingOffice;

   public RegisterCarCommand(ParkingOffice parkingOffice){
       this.parkingOffice = parkingOffice;
   }

   @Override
   public void checkParameters(Properties properties) {
      Car.validateProperties(properties);
   }

    @Override
    public String execute(Properties properties) {
        Car car = Car.fromProperties(properties);
        return parkingOffice.register(car); // This method should return the parking permit ID
    }

    @Override
    public String getDisplayName() {
        return "Register Car";
    }

    @Override
    public String getCommandName() {
        return "CAR";
    }


}

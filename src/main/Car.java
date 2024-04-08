package main;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Car {
    private String permit;
    private LocalDate permitExpiration;
    private String license;
    private CarType type;
    private String owner; // main.Customer ID

    // Constructor, getters, and setters

    public Car(String permit, LocalDate permitExpiration, String license, CarType type, String owner) {
        this.permit = permit;
        this.permitExpiration = permitExpiration;
        this.license = license;
        this.type = type;
        this.owner = owner;
    }

    // Static factory method to create a Car object from Properties
    public static Car fromProperties(Properties properties) {
        // Perform validation and extraction of Car properties
        validateProperties(properties);

        String permit = properties.getProperty("permit");
        LocalDate permitExpiration = LocalDate.parse(properties.getProperty("permitExpiration"));
        String license = properties.getProperty("license");
        CarType type = CarType.valueOf(properties.getProperty("type"));
        String owner = properties.getProperty("owner");

        return new Car(permit, permitExpiration, license, type, owner);
    }

    // Validate the properties to ensure all required fields are present
    public static void validateProperties(Properties properties) {
        String[] requiredParams = {"permit", "permitExpiration","license", "type", "owner"};
        List<String> missingParams = new ArrayList<>();
        for (String param : requiredParams) {
            if (properties.getProperty(param) == null) {
                missingParams.add(param);
            }
        }
        if (!missingParams.isEmpty()) {
            throw new IllegalArgumentException("Missing required parameters: " + String.join(", ", missingParams));
        }
        // Example validation for date format
        try {
            LocalDate.parse(properties.getProperty("permitExpiration"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid format for permit expiration date.", e);
        }
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        if (permit == null) {
            throw new IllegalArgumentException("Permit cannot be null");
        }
        this.permit = permit;
    }

    public LocalDate getPermitExpiration() {
        return permitExpiration;
    }

    public void setPermitExpiration(LocalDate permitExpiration) {
        if (permitExpiration == null || permitExpiration.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Permit expiration cannot be null or a past date");
        }
        this.permitExpiration = permitExpiration;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        if (license == null) {
            throw new IllegalArgumentException("License cannot be null");
        }
        this.license = license;
    }

    public CarType getType() {
        return type;
    }

    public void setType(CarType type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Permit: " + permit + ", License: " + license + ", Type: " + type + ", Owner: " + owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return permit.equals(car.permit);
    }

    @Override
    public int hashCode() {
        return permit.hashCode();
    }
}

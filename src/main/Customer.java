package main;

import main.Address;
import main.Car;
import main.CarType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Customer {
    private String customerId;
    private String name;
    private Address address;
    private String phoneNumber;
    private final List<Car> cars;

    public Customer(String customerId, String name, Address address, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.cars = new ArrayList<>();
    }

    // Static factory method to create a Customer object from Properties
    public static Customer fromProperties(Properties properties) {
        // Perform validation and extraction of Customer properties
        validateProperties(properties);

        String customerId = properties.getProperty("customerId");
        String name = properties.getProperty("name");
        String phoneNumber = properties.getProperty("phoneNumber");
        String streetAddress1 = properties.getProperty("streetAddress1");
        String streetAddress2 = properties.getProperty("streetAddress2", ""); // optional
        String city = properties.getProperty("city");
        String state = properties.getProperty("state");
        String zipCode = properties.getProperty("zipCode");

        Address address = new Address(streetAddress1, streetAddress2, city, state, zipCode);

        return new Customer(customerId, name, address, phoneNumber);
    }

    // Validate the properties to ensure all required fields are present
    public static void validateProperties(Properties properties) {
        String[] requiredParams = {"customerId", "name", "phoneNumber", "streetAddress1", "city", "state", "zipCode"};
        List<String> missingParams = new ArrayList<>();
        for (String param : requiredParams) {
            if (!properties.containsKey(param)) {
                missingParams.add(param);
            }
        }
        if (!missingParams.isEmpty()) {
            throw new IllegalArgumentException("Missing required parameters: " + String.join(", ", missingParams));
        }
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("main.Customer ID cannot be null");
        }
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("main.Address cannot be null");
        }
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        this.phoneNumber = phoneNumber;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void addCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("main.Car cannot be null");
        }
        cars.add(car);
    }

    public void removeCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("main.Car cannot be null");
        }
        cars.remove(car);
    }

    public Car register(String license, LocalDate date, String model, CarType type, String color) {
        return new Car(license, date, model, type, color);
    }

    @Override
    public String toString() {
        return "main.Customer ID: " + customerId + ", Name: " + name + ", main.Address: " + address.toString() + ", Phone: " + phoneNumber + ", Cars: " + cars.size() + "Cars Owned: " + cars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId.equals(customer.customerId);
    }

    @Override
    public int hashCode() {
        return customerId.hashCode();
    }
}
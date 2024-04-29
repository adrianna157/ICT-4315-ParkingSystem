package main;

import main.parking.charges.strategy.IParkingChargeStrategy;
import main.parking.charges.factory.ParkingChargeStrategyFactory;

import java.util.List;

public class ParkingLot {
    private String lotId;
    private Address address;
    private int capacity;

    private IParkingChargeStrategy parkingChargeStrategy;

    // Constructor that takes an main.Address object
    public ParkingLot(String lotId, Address address, int capacity) {
        this.lotId = lotId;
        this.address = address;
        this.capacity = capacity;
    }

    // Constructor that takes individual address components
    public ParkingLot(String lotId, String streetAddress1, String streetAddress2, String city, String state, String zipCode, int capacity) {
        this.lotId = lotId;
        this.address = new Address(streetAddress1, streetAddress2, city, state, zipCode);
        this.capacity = capacity;
    }

    // Method to assign a strategy using the factory
    // Call this method with either "FLAT_RATE" or "HOURLY" to assign the corresponding strategy
    public void assignStrategy(String strategy, List<Discount> discounts, Money baseRate) {
        this.parkingChargeStrategy = ParkingChargeStrategyFactory.create(strategy, discounts, baseRate);
    }

    public IParkingChargeStrategy getParkingChargeStrategy() {
        return parkingChargeStrategy;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        if (lotId == null) {
            throw new IllegalArgumentException("Lot ID cannot be null");
        }
        this.lotId = lotId;
    }

    public String getAddress() {
        return address.getAddressInfo();
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("main.Address cannot be null");
        }
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "main.ParkingLot ID: " + lotId + ", main.Address: " + address + ", Capacity: " + capacity;
    }
}
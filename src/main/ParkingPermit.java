package main;

import java.util.Calendar;

public class ParkingPermit {
    private String id;
    private Car car;
    private Calendar expirationDate;
    private Calendar registrationDate;

    public ParkingPermit(String id, Car car, Calendar expirationDate, Calendar registrationDate) {
        this.id = id;
        this.car = car;
        this.expirationDate = expirationDate;
        this.registrationDate = registrationDate;
    }

    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }
        this.car = car;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date cannot be null");
        }
        this.expirationDate = expirationDate;
    }

    public Calendar getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Calendar registrationDate) {
        if (registrationDate == null) {
            throw new IllegalArgumentException("Registration date cannot be null");
        }
        this.registrationDate = registrationDate;
    }

    public boolean isExpired() {
        return expirationDate.before(Calendar.getInstance());
    }

    public boolean isRegistered() {
        return registrationDate.before(Calendar.getInstance());
    }

    public boolean isRegisteredTo(Car car) {
        return this.car.equals(car);
    }


}

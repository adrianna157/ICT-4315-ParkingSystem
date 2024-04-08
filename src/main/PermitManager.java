package main;

import java.util.Calendar;
import java.util.HashMap;

public class PermitManager {
    private HashMap<String, ParkingPermit> permits = new HashMap<>();

    public ParkingPermit register(Car car) {
        String id = car.getPermit();
        Calendar registrationDate = Calendar.getInstance();
        Calendar expirationDate = (Calendar) registrationDate.clone();
        expirationDate.add(Calendar.YEAR, 1);
        ParkingPermit permit = new ParkingPermit(id, car, expirationDate, registrationDate);
        permits.put(id, permit);
        return permit;
    }

    public ParkingPermit getPermit(Car car) {
        return permits.get(car.getPermit());
    }

    public void unregister(String id) {
        permits.remove(id);
    }

    public boolean isRegistered(String id) {
        return permits.containsKey(id);
    }

    public boolean isExpired(String id) {
        return permits.get(id).getExpirationDate().before(Calendar.getInstance());
    }

    public boolean renew(String id) {
        if (isRegistered(id)) {
            ParkingPermit permit = permits.get(id);
            Calendar expirationDate = permit.getExpirationDate();
            expirationDate.add(Calendar.YEAR, 1);
            permit.setExpirationDate(expirationDate);
            return true;
        }
        return false;
    }

    public boolean updateCar(String id, Car car) {
        if (isRegistered(id)) {
            ParkingPermit permit = permits.get(id);
            permit.setCar(car);
            return true;
        }
        return false;
    }

    public boolean updateExpirationDate(String id, Calendar expirationDate) {
        if (isRegistered(id)) {
            ParkingPermit permit = permits.get(id);
            permit.setExpirationDate(expirationDate);
            return true;
        }
        return false;
    }

    public boolean updateRegistrationDate(String id, Calendar registrationDate) {
        if (isRegistered(id)) {
            ParkingPermit permit = permits.get(id);
            permit.setRegistrationDate(registrationDate);
            return true;
        }
        return false;
    }

    public boolean updatePermit(String id, ParkingPermit permit) {
        if (isRegistered(id)) {
            permits.put(id, permit);
            return true;
        }
        return false;
    }
}
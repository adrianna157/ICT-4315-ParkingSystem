package main.parking.charges.strategy;

import main.Money;
import main.ParkingPermit;

public interface IParkingChargeStrategy {
    Money calculateParkingFee(long entryTime, ParkingPermit permit, long exitTime);
}

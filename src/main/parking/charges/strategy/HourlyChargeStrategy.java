package main.parking.charges.strategy;

import main.Discount;
import main.Money;
import main.ParkingPermit;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class HourlyChargeStrategy extends BaseChargeStrategy {
    public HourlyChargeStrategy(Money baseRate, List<Discount> discounts) {
        super(baseRate, discounts);
    }

    @Override
    protected Money calculateFee(Money baseRate, long entryTime, ParkingPermit permit, long exitTime) {
        Instant entryInstant = Instant.ofEpochMilli(entryTime);
        Instant timeOfExitInstant = Instant.ofEpochMilli(exitTime);

        long hoursSinceEntry = Duration.between(entryInstant, timeOfExitInstant).toHours();

        Money discountedFeeAmount = new Money(baseRate.getCents() * hoursSinceEntry);
        System.out.printf(
                "Hours since Entry: %s%n" +
                "Parking Permit: %s%n" +
                "Hourly Fee Rate amount: $%.2f%n" +
                "Total amount with Discount: $%.2f%n",
                hoursSinceEntry,
                permit.getId(),
                this.baseRate.getDollars(),
                discountedFeeAmount.getDollars()
        );

        return discountedFeeAmount;
    }
}
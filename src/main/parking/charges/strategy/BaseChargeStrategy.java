package main.parking.charges.strategy;

import main.CarType;
import main.Discount;
import main.Money;
import main.ParkingPermit;

import java.util.Calendar;
import java.util.List;

public abstract class BaseChargeStrategy implements IParkingChargeStrategy {
    protected Money baseRate;
    private final List<Discount> discounts;

    private static final String[] DAYS = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public BaseChargeStrategy(Money baseRate, List<Discount> discounts) {
        if (baseRate == null) {
            throw new IllegalArgumentException("Base rate cannot be null");
        }

        if (discounts == null) {
            throw new IllegalArgumentException("Discounts cannot be null");
        }

        this.baseRate = baseRate;
        this.discounts = discounts;
    }

    public Money applyDiscounts(ParkingPermit permit) {
        if (permit == null) {
            throw new IllegalArgumentException("Permit cannot be null");
        }

        long discountedCents = baseRate.getCents();
        String day = DAYS[permit.getRegistrationDate().get(Calendar.DAY_OF_WEEK) - 1];
        CarType carType = permit.getCar().getType();

        for (Discount discount : discounts) {
            if (discount.isApplicable(day, carType)) {
                discountedCents *= (1 - discount.getDiscountPercentage() / 100);
            }
        }

        return new Money(discountedCents);
    }

    // This method is what the client code will call to calculate the parking fee
    @Override
    public Money calculateParkingFee( long entryTime, ParkingPermit permit, long exitTime) {

        if(permit == null) {
            throw new IllegalArgumentException("Permit cannot be null");
        }

        if(entryTime < 0) {
            throw new IllegalArgumentException("Entry time cannot be negative");
        }

        if(exitTime < 0) {
            throw new IllegalArgumentException("Exit time cannot be negative");
        }

        Money discountedRate = applyDiscounts(permit);
        return calculateFee(discountedRate, entryTime, permit, exitTime);
    }

    // This method must be implemented by the concrete classes

    protected abstract Money calculateFee(Money baseRate, long entryTime, ParkingPermit permit, long exitTime);
}
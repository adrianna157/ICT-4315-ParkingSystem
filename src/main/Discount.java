package main;

import java.util.List;

public class Discount {
    // Identifies the discount (e.g., "Weekend Special").
    private final String name;
    // How much to discount (e.g., 30%). This is a percentage.
    private final double discountPercentage;
    // The days of the week when the discount is applicable (e.g., "Saturday", "Sunday").
    private final List<String> applicableDays;
    // The car types to which the discount applies (e.g., COMPACT, SUV).
    private final List<CarType> applicableCarTypes;

    public Discount(String name, double discountPercentage, List<String> applicableDays, List<CarType> applicableCarTypes) {
        this.name = name;
        this.discountPercentage = discountPercentage;
        this.applicableDays = applicableDays;
        this.applicableCarTypes = applicableCarTypes;
    }

    public boolean isApplicable(String day, CarType carType) {
        return applicableDays.contains(day) && applicableCarTypes.contains(carType);
    }


    public String getName() {
        return name;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public List<String> getApplicableDays() {
        return applicableDays;
    }

    public List<CarType> getApplicableCarTypes() {
        return applicableCarTypes;
    }
}

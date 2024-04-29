package main.parking.charges.factory;

import main.Discount;
import main.Money;
import main.parking.charges.strategy.FlatRateChargeStrategy;
import main.parking.charges.strategy.HourlyChargeStrategy;
import main.parking.charges.strategy.IParkingChargeStrategy;

import java.util.List;

public class ParkingChargeStrategyFactory {
    public static IParkingChargeStrategy create(String strategy, List<Discount> discounts, Money baseRate) {
        return switch (strategy) {
            case "FLAT_RATE" -> new FlatRateChargeStrategy(baseRate, discounts);
            case "HOURLY" -> new HourlyChargeStrategy(baseRate, discounts);
            default -> throw new IllegalArgumentException("Unknown strategy: " + strategy);
        };
    }
}

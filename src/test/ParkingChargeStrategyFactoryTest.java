import main.parking.charges.factory.ParkingChargeStrategyFactory;
import main.parking.charges.strategy.*;
import main.CarType;

import main.Discount;
import main.Money;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ParkingChargeStrategyFactoryTest {

    // Happy Path
    @Test
    void testCreateFlatRateStrategyHappyPath() {
        Money baseRate = new Money(1000);
        Discount discount = new Discount("Weekend Special", 30, Arrays.asList("Saturday", "Sunday"), Arrays.asList(CarType.COMPACT, CarType.SUV));
        IParkingChargeStrategy strategy = ParkingChargeStrategyFactory.create("FLAT_RATE", Collections.singletonList(discount), baseRate);
        assertInstanceOf(FlatRateChargeStrategy.class, strategy);
    }

    @Test
    void testCreateHourlyStrategyHappyPath() {
        Money baseRate = new Money(1000);
        Discount discount = new Discount("Weekend Special", 30, Arrays.asList("Saturday", "Sunday"), Arrays.asList(CarType.COMPACT, CarType.SUV));
        IParkingChargeStrategy strategy = ParkingChargeStrategyFactory.create("HOURLY", Collections.singletonList(discount), baseRate);
        assertInstanceOf(HourlyChargeStrategy.class, strategy);
    }


    // Sad Path
    @Test
    void testCreateUnknownStrategySadPath() {
        Money baseRate = new Money(1000);
        Discount discount = new Discount("Weekend Special", 30, Arrays.asList("Saturday", "Sunday"), Arrays.asList(CarType.COMPACT, CarType.SUV));
        assertThrows(IllegalArgumentException.class, () -> ParkingChargeStrategyFactory.create("UNKNOWN", Collections.singletonList(discount), baseRate));
    }

    @Test
    void testCreateWithNullStrategySadPath() {
        Money baseRate = new Money(1000);
        Discount discount = new Discount("Weekend Special", 30, Arrays.asList("Saturday", "Sunday"), Arrays.asList(CarType.COMPACT, CarType.SUV));
        assertThrows(NullPointerException.class, () -> ParkingChargeStrategyFactory.create(null, Collections.singletonList(discount), baseRate));
    }

}
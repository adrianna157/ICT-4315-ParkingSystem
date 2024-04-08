import main.ParkingCharge;
import main.Money;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParkingChargeTest {

    // Happy Path
    @Test
    void testParkingChargeInitialization() {
        Money amount = new Money(500);
        Instant now = Instant.now();
        ParkingCharge charge = new ParkingCharge("123", "A1", now, amount);

        assertAll("Ensure correct initialization",
                () -> assertEquals("123", charge.getPermitId(), "Permit ID should be correctly initialized."),
                () -> assertEquals("A1", charge.getLotId(), "Lot ID should be correctly initialized."),
                () -> assertEquals(now, charge.getIncurred(), "Incurred time should be correctly initialized."),
                () -> assertEquals(amount, charge.getAmount(), "Amount should be correctly initialized.")
        );
    }

    @Test
    void testToString() {
        Money amount = new Money(1000);
        Instant now = Instant.now();
        ParkingCharge charge = new ParkingCharge("456", "B2", now, amount);
        String expected = "Charge[Permit ID: 456, Lot ID: B2, Incurred: " + now + ", Amount: $" + amount.getDollars() + "]";
        assertEquals(expected, charge.toString(), "String representation should match expected format.");
    }

    // Sad Path
    @Test
    void testSetNullPermitId() {
        Instant now = Instant.now();
        Money amount = new Money(200);
        ParkingCharge charge = new ParkingCharge("789", "C3", now, amount);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> charge.setPermitId(null));
        assertEquals("Permit ID cannot be null", exception.getMessage(), "Setting null Permit ID should throw IllegalArgumentException.");
    }

    @Test
    void testSetNullLotId() {
        Instant now = Instant.now();
        Money amount = new Money(300);
        ParkingCharge charge = new ParkingCharge("101112", "D4", now, amount);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> charge.setLotId(null));
        assertEquals("Lot ID cannot be null", exception.getMessage(), "Setting null Lot ID should throw IllegalArgumentException.");
    }

    @Test
    void testSetNullIncurred() {
        Money amount = new Money(400);
        ParkingCharge charge = new ParkingCharge("131415", "E5", Instant.now(), amount);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> charge.setIncurred(null));
        assertEquals("Incurred date cannot be null", exception.getMessage(), "Setting null Incurred date should throw IllegalArgumentException.");
    }

    @Test
    void testSetNullAmount() {
        Instant now = Instant.now();
        ParkingCharge charge = new ParkingCharge("161718", "F6", now, new Money(500));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> charge.setAmount(null));
        assertEquals("Amount cannot be null", exception.getMessage(), "Setting null Amount should throw IllegalArgumentException.");
    }
}

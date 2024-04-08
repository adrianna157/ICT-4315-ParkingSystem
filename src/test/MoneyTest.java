import main.Money;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    // Happy Path
    @Test
    void testMoneyInitialization() {
        Money money = new Money(100);
        assertEquals(100, money.getCents(), "Cents should be correctly initialized.");
    }

    @Test
    void testGetDollars() {
        Money money = new Money(12345);
        assertEquals(123.45, money.getDollars(), "Dollars conversion should be correct.");
    }

    @Test
    void testGetCents() {
        Money money = new Money(567);
        assertEquals(567, money.getCents(), "Cents should be correct.");
    }

    @Test
    void testToString() {
        Money money = new Money(567);
        assertEquals("$5.67", money.toString(), "String representation should match.");
    }

    // Sad Path
    @Test
    void testNegativeCentsInitialization() {
        Money money = new Money(-100);
        assertTrue(money.getCents() < 0, "Cents can be negative, but should it?");
    }

    @Test
    void testSetNegativeCents() {
        Money money = new Money(0);
        money.setCents(-200);
        assertEquals(-200, money.getCents(), "Setting negative cents should be possible, but should it?");
    }

    @Test
    void testLargeCentsValue() {
        Money money = new Money(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, money.getCents(), "Should handle large long values correctly.");
    }
}
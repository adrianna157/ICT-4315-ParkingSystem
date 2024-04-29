import main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionManagerTest {

    private TransactionManager transactionManager;
    private ParkingPermit permit;
    private ParkingLot lot;

    String hourlyStrategy;
    List<Discount> discounts;
    Money baseRate;

    @BeforeEach
    void setUp() {
        transactionManager = new TransactionManager();
        Car car = new Car("Permit01", LocalDate.now(), "LICENSE123", CarType.COMPACT, "John Doe");
        permit = new ParkingPermit("Permit01", car, Calendar.getInstance(), Calendar.getInstance());
        lot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        hourlyStrategy = "HOURLY";
        discounts = List.of(new Discount("Weekeday Discount", 10, List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"), List.of(CarType.COMPACT)));
        baseRate = new Money(1000);
    }

    // Happy Path
    @Test
    void park_ShouldCreateTransaction() {
        ParkingTransaction transaction = transactionManager.park(Calendar.getInstance(), permit, lot, hourlyStrategy, discounts, baseRate);

        assertNotNull(transaction, "Transaction should not be null.");
        assertTrue(transactionManager.getTransactions().contains(transaction), "Transaction should be added to the list of transactions.");
    }

    @Test
    void getParkingCharges_ShouldReturnCorrectTotal() {
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.HOUR, 8);
        permit.setExpirationDate(expirationDate);

        transactionManager.park(Calendar.getInstance(), permit, lot, hourlyStrategy, discounts, baseRate);


        Money totalCharges = transactionManager.getParkingCharges(permit);

        assertEquals(8000, totalCharges.getCents(), "Total charges should be the sum of all charges for the permit.");
    }

    @Test
    void getTransactions_ShouldReturnAllTransactions() {
        transactionManager.park(Calendar.getInstance(), permit, lot, hourlyStrategy, discounts, baseRate);

        List<ParkingTransaction> transactions = transactionManager.getTransactions();

        assertFalse(transactions.isEmpty(), "Transactions list should not be empty after parking.");
    }

    @Test
    void getTransactionsForPermit_ShouldReturnCorrectTransactions() {
        ParkingTransaction transaction1 = transactionManager.park(Calendar.getInstance(), permit, lot, hourlyStrategy, discounts, baseRate);

        // Simulating parking with a different permit
        Car anotherCar = new Car("Permit02", LocalDate.now(), "LICENSE456", CarType.SUV, "Jane Doe");
        ParkingPermit anotherPermit = new ParkingPermit("Permit02", anotherCar, Calendar.getInstance(), Calendar.getInstance());
        transactionManager.park(Calendar.getInstance(), anotherPermit, lot, hourlyStrategy, discounts, baseRate);

        List<ParkingTransaction> transactions = transactionManager.getTransactions(permit);

        assertTrue(transactions.contains(transaction1), "Transactions list for the permit should contain the transaction.");
        assertEquals(1, transactions.size(), "Transactions list for the permit should only contain transactions for that permit.");
    }

    // Sad Path
    @Test
    void getTransactionsForUnregisteredPermit_ShouldReturnEmptyList() {
        // Simulating checking transactions for an unregistered permit
        Car unregisteredCar = new Car("Permit99", LocalDate.now(), "XYZ999", CarType.SUV, "Unknown Owner");
        ParkingPermit unregisteredPermit = new ParkingPermit("Permit99", unregisteredCar, Calendar.getInstance(), Calendar.getInstance());

        List<ParkingTransaction> transactions = transactionManager.getTransactions(unregisteredPermit);

        assertTrue(transactions.isEmpty(), "Transactions list for an unregistered permit should be empty.");
    }
}

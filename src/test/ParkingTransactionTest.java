import main.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class ParkingTransactionTest {

    private final Calendar transactionDate = Calendar.getInstance();
    private final ParkingPermit permit = new ParkingPermit("Permit01", new Car("Permit01", LocalDate.now(), "LICENSE123", CarType.COMPACT, "John Doe"), Calendar.getInstance(), Calendar.getInstance());
    ParkingLot lot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
    private final Money feeCharged = new Money(500);

    // Happy Path
    @Test
    void setTransactionDate_ShouldUpdateDateSuccessfully() {
        ParkingTransaction transaction = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        Calendar newDate = Calendar.getInstance();
        newDate.add(Calendar.DAY_OF_MONTH, 1);
        transaction.setTransactionDate(newDate);
        assertEquals(newDate, transaction.getTransactionDate(), "Transaction date should be updated successfully.");
    }

    @Test
    void setPermit_ShouldUpdatePermitSuccessfully() {
        ParkingTransaction transaction = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        ParkingPermit newPermit = new ParkingPermit("Permit02", new Car("Permit02", LocalDate.now(), "LICENSE456", CarType.SUV, "Jane Doe"), Calendar.getInstance(), Calendar.getInstance());
        transaction.setPermit(newPermit);
        assertEquals(newPermit, transaction.getPermit(), "Permit should be updated successfully.");
    }

    @Test
    void setLot_ShouldUpdateLotSuccessfully() {
        ParkingTransaction transaction = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        ParkingLot newLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        transaction.setLot(newLot);
        assertEquals(newLot, transaction.getLot(), "Lot should be updated successfully.");
    }

    @Test
    void setFeeCharged_ShouldUpdateFeeSuccessfully() {
        ParkingTransaction transaction = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        Money newFee = new Money(1000);
        transaction.setFeeCharged(newFee);
        assertEquals(newFee, transaction.getFeeCharged(), "Fee charged should be updated successfully.");
    }

    // Sad Path
    @Test
    void setTransactionDate_WithNull_ShouldThrowException() {
        ParkingTransaction transaction = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        assertThrows(IllegalArgumentException.class, () -> transaction.setTransactionDate(null), "Setting transaction date to null should throw IllegalArgumentException.");
    }

    @Test
    void setPermit_WithNull_ShouldThrowException() {
        ParkingTransaction transaction = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        assertThrows(IllegalArgumentException.class, () -> transaction.setPermit(null), "Setting permit to null should throw IllegalArgumentException.");
    }

    @Test
    void setLot_WithNull_ShouldThrowException() {
        ParkingTransaction transaction = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        assertThrows(IllegalArgumentException.class, () -> transaction.setLot(null), "Setting lot to null should throw IllegalArgumentException.");
    }

    @Test
    void setFeeCharged_WithNull_ShouldThrowException() {
        ParkingTransaction transaction = new ParkingTransaction(transactionDate, permit, lot, feeCharged);
        assertThrows(IllegalArgumentException.class, () -> transaction.setFeeCharged(null), "Setting fee charged to null should throw IllegalArgumentException.");
    }
}
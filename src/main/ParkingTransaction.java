package main;

import java.util.Calendar;

public class ParkingTransaction {
    private Calendar transactionDate;
    private ParkingPermit permit;
    private ParkingLot lot;
    private Money feeCharged;

    public ParkingTransaction(Calendar transactionDate, ParkingPermit permit, ParkingLot lot, Money feeCharged){
        this.transactionDate = transactionDate;
        this.permit = permit;
        this.lot = lot;
        this.feeCharged = feeCharged;
    }

    // getters and setters
    public Calendar getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Calendar transactionDate) {
        if (transactionDate == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        this.transactionDate = transactionDate;
    }

    public ParkingPermit getPermit() {
        return permit;
    }

    public void setPermit(ParkingPermit permit) {
        if (permit == null) {
            throw new IllegalArgumentException("Permit cannot be null");
        }
        this.permit = permit;
    }

    public ParkingLot getLot() {
        return lot;
    }

    public void setLot(ParkingLot lot) {
        if (lot == null) {
            throw new IllegalArgumentException("Lot cannot be null");
        }
        this.lot = lot;
    }

    public Money getFeeCharged() {
        return feeCharged;
    }

    public void setFeeCharged(Money feeCharged) {
        if (feeCharged == null) {
            throw new IllegalArgumentException("Fee charged cannot be null");
        }
        this.feeCharged = feeCharged;
    }


}

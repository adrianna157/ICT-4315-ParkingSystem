package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TransactionManager {
    private ArrayList<ParkingTransaction> transactions = new ArrayList<>();
    private HashMap<String, ArrayList<ParkingTransaction>> vehicleTransaction = new HashMap<>();

    public ParkingTransaction park(Calendar date, ParkingPermit permit, ParkingLot lot, Money feeCharged) {
        ParkingTransaction transaction = new ParkingTransaction(date, permit, lot, feeCharged);
        transactions.add(transaction);
        String carId = permit.getCar().getPermit();
        if (!vehicleTransaction.containsKey(carId)) {
            vehicleTransaction.put(carId, new ArrayList<>());
        }
        vehicleTransaction.get(carId).add(transaction);
        return transaction;
    }

    public Money getParkingCharges(ParkingPermit permit) {
        ArrayList<ParkingTransaction> carTransactions = vehicleTransaction.get(permit.getCar().getPermit());
        long totalCents = 0;
        for (ParkingTransaction transaction : carTransactions) {
            totalCents += transaction.getFeeCharged().getCents();
        }
        return new Money(totalCents);
    }

    public ArrayList<ParkingTransaction> getTransactions() {
        return transactions;
    }

    public ArrayList<ParkingTransaction> getTransactions(ParkingPermit permit) {
        // Return an empty list if no transactions are found for the given permit
        return vehicleTransaction.getOrDefault(permit.getCar().getPermit(), new ArrayList<>());
    }
}
package main;

import java.time.Instant;

public class ParkingCharge {
    private String permitId;
    private String lotId;
    private Instant incurred;
    private Money amount;

    public ParkingCharge(String permitId, String lotId, Instant incurred, Money amount) {
        this.permitId = permitId;
        this.lotId = lotId;
        this.incurred = incurred;
        this.amount = amount;
    }

    // getters and setters
    public String getPermitId() {
        return permitId;
    }

    public void setPermitId(String permitId) {
        if (permitId == null) {
            throw new IllegalArgumentException("Permit ID cannot be null");
        }
        this.permitId = permitId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        if (lotId == null) {
            throw new IllegalArgumentException("Lot ID cannot be null");
        }
        this.lotId = lotId;
    }

    public Instant getIncurred() {
        return incurred;
    }

    public void setIncurred(Instant incurred) {
        if (incurred == null) {
            throw new IllegalArgumentException("Incurred date cannot be null");
        }
        this.incurred = incurred;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Charge[Permit ID: " + permitId + ", Lot ID: " + lotId + ", Incurred: " + incurred + ", Amount: " + amount + "]";
    }
}
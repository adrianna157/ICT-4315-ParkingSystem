package main;

public class Money {
    private long cents;

    public Money(long cents) {
        this.cents = cents;
    }

    // getters and setters
    public long getCents() {
        return cents;
    }

    public void setCents(long cents) {
        this.cents = cents;
    }

    public double getDollars() {
        return cents / 100.0;
    }

    @Override
    public String toString() {
        return "$" + getDollars();
    }
}
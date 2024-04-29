package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ParkingOffice {
    private String name;
    private Address address;
    private List<Customer> customers = new ArrayList<>();
    private List<Car> cars = new ArrayList<>();
    private List<ParkingLot> lots = new ArrayList<>();
    private List<ParkingCharge> charges = new ArrayList<>();
    private final TransactionManager transactionManager = new TransactionManager();
    private final PermitManager permitManager = new PermitManager();

    public ParkingOffice(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.name = name;
    }


    public void setAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        this.address = address;
    }

    public void setCustomers(List<Customer> customers) {
        if (customers == null) {
            throw new IllegalArgumentException("Customers cannot be null");
        }
        this.customers = customers;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        if (cars == null) {
            throw new IllegalArgumentException("Cars cannot be null");
        }
        this.cars = cars;
    }

    public void setLots(List<ParkingLot> lots) {
        if (lots == null) {
            throw new IllegalArgumentException("Parking lots cannot be null");
        }
        this.lots = lots;
    }

    public List<ParkingCharge> getCharges() {
        return charges;
    }

    public void setCharges(List<ParkingCharge> charges) {
        if (charges == null) {
            throw new IllegalArgumentException("Charges cannot be null");
        }
        this.charges = charges;
    }

    // Register a customer with the parking office
    public Customer register(String customerId, String name, Address address, String phone) {
        Customer newCustomer = new Customer(customerId, name, address, phone);
        customers.add(newCustomer);
        return newCustomer;
    }

    // Register a car for a customer
    public Car register(Customer customer, String permit, LocalDate permitExpiration, String license, CarType type) {
        Car newCar = new Car(permit, permitExpiration, license, type, customer.getName());
        cars.add(newCar);
        customer.addCar(newCar);
        permitManager.register(newCar);
        return newCar;
    }

    // Add a parking charge to the list of charges and calculate the total charges in cents
    public Money addCharge(ParkingCharge charge) {
        charges.add(charge);
        long totalCents = 0;
        for (ParkingCharge c : charges) {
            // Correctly accumulate the cent values, not the dollar values
            totalCents += c.getAmount().getCents(); // Use getCents() instead of getDollars()
        }
        return new Money(totalCents);
    }

    public ParkingTransaction park(Car car, ParkingLot lot, String strategy, List<Discount> discounts, Money baseRate) {
        ParkingPermit permit = permitManager.getPermit(car);
        Calendar date = Calendar.getInstance();
        return transactionManager.park(date, permit, lot, strategy, discounts, baseRate);
    }

    public List<String> getCustomerIds() {
        if (customers == null) {
            throw new IllegalArgumentException("Customers cannot be null");
        }
        return customers.stream()
                .map(Customer::getCustomerId)
                .collect(Collectors.toList());
    }

    public List<String> getPermitIds() {
        if (cars == null) {
            throw new IllegalArgumentException("Cars cannot be null");
        }
        return cars.stream()
                .map(Car::getPermit)
                .collect(Collectors.toList());
    }

    public List<String> getPermitIds(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        return cars.stream()
                .filter(car -> car.getOwner().equals(customer.getCustomerId()))
                .map(Car::getPermit)
                .collect(Collectors.toList());
    }

    public PermitManager getPermitManager() {
        return permitManager;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public String register(Car car) {
        cars.add(car);
        return car.getPermit();
    }

    public String register(Customer customer){
        customers.add(customer);
        return customer.getCustomerId();
    }
}


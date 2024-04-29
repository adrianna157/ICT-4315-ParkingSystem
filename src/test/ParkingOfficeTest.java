import main.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParkingOfficeTest {
    private String hourlyStrategy;
    private List<Discount> discounts;
    private Money baseRate;

    @BeforeEach
    void setUp() {
        hourlyStrategy = "HOURLY_RATE";
        discounts = List.of(new Discount("Weekeday Discount", 10, List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"), List.of(CarType.COMPACT)));
        baseRate = new Money(1000);
    }


    // Happy Path
    @Test
    void testRegisterCustomer() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", null, "City", "State", "Zip"));
        Customer customer = office.register("Cust01", "John Doe", new Address("Customer Street1", "Customer Street2", "Customer City", "Customer State", "Customer Zip"), "1234567890");

        assertNotNull(customer, "Customer registration should succeed.");
        assertEquals("John Doe", customer.getName(), "Customer name should be set correctly.");
        assertEquals("Customer Street1, Customer Street2, Customer City, Customer State Customer Zip", customer.getAddress().toString(), "Customer address should be set correctly.");
    }

    @Test
    void testRegisterCar() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "", "City", "State", "Zip"));
        Customer customer = new Customer("Cust01", "John Doe", new Address("Customer Street1", "Customer Street2", "Customer City", "Customer State", "Customer Zip"), "1234567890");
        Car car = office.register(customer, "Permit01", LocalDate.now().plusYears(1), "LICENSE123", CarType.COMPACT);

        assertNotNull(car, "Car registration should succeed.");
        assertTrue(office.getCars().contains(car), "The registered car should be in the office's list of cars.");
    }

    @Test
    void testAddCharge() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        ParkingCharge charge = new ParkingCharge("Permit01", "Lot01", Instant.now(), new Money(500));
        office.setCharges(new ArrayList<>(List.of(charge)));

        Money totalCharges = office.addCharge(new ParkingCharge("Permit02", "Lot02", Instant.now(), new Money(1000)));
        assertEquals(1500, totalCharges.getCents(), "Total charges should sum up correctly.");
    }
    @Test
    void testGetCustomerIds() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        Customer customer1 = office.register("Cust01", "John Doe", new Address("Customer Street1", "Customer Street2", "Customer City", "Customer State", "Customer Zip"), "1234567890");
        Customer customer2 = office.register("Cust02", "Jane Doe", new Address("Customer Street1", "Customer Street2", "Customer City", "Customer State", "Customer Zip"), "0987654321");

        List<String> customerIds = office.getCustomerIds();
        assertTrue(customerIds.contains(customer1.getCustomerId()), "getCustomerIds should return a list containing the ID of customer1");
        assertTrue(customerIds.contains(customer2.getCustomerId()), "getCustomerIds should return a list containing the ID of customer2");
    }

    @Test
    void testGetPermitIds() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        Customer customer = new Customer("Cust01", "John Doe", new Address("Customer Street1", "Customer Street2", "Customer City", "Customer State", "Customer Zip"), "1234567890");
        Car car1 = office.register(customer, "Permit01", LocalDate.now().plusYears(1), "LICENSE123", CarType.COMPACT);
        Car car2 = office.register(customer, "Permit02", LocalDate.now().plusYears(1), "LICENSE456", CarType.SUV);

        List<String> permitIds = office.getPermitIds();
        assertTrue(permitIds.contains(car1.getPermit()), "getPermitIds should return a list containing the permit of car1");
        assertTrue(permitIds.contains(car2.getPermit()), "getPermitIds should return a list containing the permit of car2");
    }

    @Test
    void testGetPermitIdsForCustomer() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        Customer customer1 = new Customer("Cust01", "John Doe", new Address("Customer Street1", "Customer Street2", "Customer City", "Customer State", "Customer Zip"), "1234567890");
        Customer customer2 = new Customer("Cust02", "Jane Doe", new Address("Customer Street1", "Customer Street2", "Customer City", "Customer State", "Customer Zip"), "0987654321");
        Car car1 = new Car("Permit01", LocalDate.now().plusYears(1), "LICENSE123", CarType.COMPACT, customer1.getCustomerId());
        Car car2 = new Car("Permit02", LocalDate.now().plusYears(1), "LICENSE456", CarType.SUV, customer2.getCustomerId());

        // Add the cars to the customers' list of cars
        customer1.addCar(car1);
        customer2.addCar(car2);

        // Add the cars and customers to the office
        office.setCars(Arrays.asList(car1, car2));
        office.setCustomers(Arrays.asList(customer1, customer2));

        List<String> permitIdsForCustomer1 = office.getPermitIds(customer1);
        assertTrue(permitIdsForCustomer1.contains(car1.getPermit()), "getPermitIds(Customer) should return a list containing the permit of car1 for customer1");
        assertFalse(permitIdsForCustomer1.contains(car2.getPermit()), "getPermitIds(Customer) should not return a list containing the permit of car2 for customer1");
    }

    @Test
    void renewPermit_ShouldExtendExpirationDate() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("123 Main St", null, "Anytown", "Anystate", "12345"));
        Customer customer = office.register("Cust01", "John Doe", new Address("456 Elm St", null, "Anytown", "Anystate", "67890"), "123-456-7890");
        Car car = office.register(customer, "Permit01", LocalDate.now().plusYears(1), "ABC123", CarType.COMPACT);

        PermitManager permitManager = office.getPermitManager();
        ParkingPermit permit = permitManager.getPermit(car);
        Calendar expectedExpiration = (Calendar) permit.getExpirationDate().clone();
        expectedExpiration.add(Calendar.YEAR, 1);

        assertTrue(permitManager.renew(permit.getId()), "Permit renewal should succeed.");
        assertEquals(expectedExpiration, permit.getExpirationDate(), "Permit expiration date should be extended by 1 year.");
    }

    @Test
    void unregisterPermit_ShouldRemovePermit() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("123 Main St", null, "Anytown", "Anystate", "12345"));
        Customer customer = office.register("Cust01", "John Doe", new Address("456 Elm St", null, "Anytown", "Anystate", "67890"), "123-456-7890");
        Car car = office.register(customer, "Permit01", LocalDate.now().plusYears(1), "ABC123", CarType.COMPACT);

        PermitManager permitManager = office.getPermitManager();
        ParkingPermit permit = permitManager.getPermit(car);
        permitManager.unregister(permit.getId());

        assertFalse(permitManager.isRegistered(permit.getId()), "Permit should be unregistered.");
    }

    @Test
    void parkingWithoutPermit_ShouldNotCreateTransaction() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("123 Main St", null, "Anytown", "Anystate", "12345"));
        ParkingLot lot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);


        // Simulate a car parking without a valid permit by not registering the car first
        Car unregisteredCar = new Car("Permit99", LocalDate.now().plusYears(1), "XYZ999", CarType.SUV, "John Doe");

        assertThrows(IllegalArgumentException.class, () -> office.park(unregisteredCar, lot, hourlyStrategy, discounts, baseRate), "Parking without a valid permit should not create a transaction.");
    }

    @Test
    void getTransactionsForUnregisteredCar_ShouldReturnEmptyList() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("123 Main St", null, "Anytown", "Anystate", "12345"));
        Calendar today = Calendar.getInstance();
        TransactionManager transactionManager = office.getTransactionManager();
        // Simulate checking transactions for an unregistered car
        Car unregisteredCar = new Car("Permit99", LocalDate.now().plusYears(1), "XYZ999", CarType.SUV, "John Doe");
        ParkingPermit dummyPermit = new ParkingPermit("Permit99", unregisteredCar, today, today); // A dummy permit not registered in the system

        assertTrue(transactionManager.getTransactions(dummyPermit).isEmpty(), "Transactions list for an unregistered car should be empty.");
    }

    @Test
    void updatePermitDetails_ShouldReflectChanges() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("123 Main St", null, "Anytown", "Anystate", "12345"));
        Customer customer = office.register("Cust01", "John Doe", new Address("456 Elm St", null, "Anytown", "Anystate", "67890"), "123-456-7890");
        Car car = office.register(customer, "Permit01", LocalDate.now().plusYears(1), "ABC123", CarType.COMPACT);

        PermitManager permitManager = office.getPermitManager();
        ParkingPermit permit = permitManager.getPermit(car);
        Car newCarDetails = new Car(permit.getId(), LocalDate.now().plusYears(2), "NEW123", CarType.SUV, customer.getName());

        assertTrue(permitManager.updateCar(permit.getId(), newCarDetails), "Updating car details in permit should succeed.");
        assertEquals(newCarDetails.getLicense(), permitManager.getPermit(newCarDetails).getCar().getLicense(), "Permit should reflect updated car details.");
    }


    // Sad Path
    @Test
    void testSetNameNull() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> office.setName(null));
        assertEquals("Name cannot be null", exception.getMessage(), "Setting name to null should throw IllegalArgumentException.");
    }

    @Test
    void testSetAddressNull() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> office.setAddress(null));
        assertEquals("Address cannot be null", exception.getMessage(), "Setting address to null should throw IllegalArgumentException.");
    }

    @Test
    void testSetCustomersNull() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> office.setCustomers(null));
        assertEquals("Customers cannot be null", exception.getMessage(), "Setting customers to null should throw IllegalArgumentException.");
    }

    @Test
    void testSetCarsNull() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> office.setCars(null));
        assertEquals("Cars cannot be null", exception.getMessage(), "Setting cars to null should throw IllegalArgumentException.");
    }

    @Test
    void testSetLotsNull() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> office.setLots(null));
        assertEquals("Parking lots cannot be null", exception.getMessage(), "Setting lots to null should throw IllegalArgumentException.");
    }

    @Test
    void testSetChargesNull() {
        ParkingOffice office = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> office.setCharges(null));
        assertEquals("Charges cannot be null", exception.getMessage(), "Setting charges to null should throw IllegalArgumentException.");
    }

    @Test
    void testGetPermitIdsForCustomerWithNullCustomer() {
        ParkingOffice office1 = new ParkingOffice("Parking Office", new Address("Street1", "Street2", "City", "State", "Zip"));
        assertThrows(IllegalArgumentException.class, () -> office1.getPermitIds(null), "getPermitIds(Customer) method should throw an exception when the customer is null");
    }
}
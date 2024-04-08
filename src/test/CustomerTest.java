package test;

import main.Address;
import main.Car;
import main.CarType;
import main.Customer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerTest {

    // Happy path tests
    @Test
    void creatingCustomerWithAllFieldsShouldSucceed() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        assertEquals("C123", customer.getCustomerId());
        assertEquals("John Doe", customer.getName());
        assertEquals(address, customer.getAddress());
        assertEquals("1234567890", customer.getPhoneNumber());
    }

    @Test
    void settingCustomerIdShouldChangeValue() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        customer.setCustomerId("C456");
        assertEquals("C456", customer.getCustomerId());
    }

    @Test
    void settingNameShouldChangeValue() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        customer.setName("Jane Doe");
        assertEquals("Jane Doe", customer.getName());
    }

    @Test
    void settingAddressShouldChangeValue() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        Address newAddress = new Address("456 Elm St", "Apt 5C", "Chicago", "IL", "60007");
        customer.setAddress(newAddress);
        assertEquals(newAddress, customer.getAddress());
    }

    @Test
    void settingPhoneNumberShouldChangeValue() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        customer.setPhoneNumber("0987654321");
        assertEquals("0987654321", customer.getPhoneNumber());
    }

    @Test
    void registerCarShouldReturnCar() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        Car car = customer.register("ABC123", LocalDate.now(), "Model1", CarType.COMPACT, "Red");
        assertNotNull(car);
    }

    @Test
    void testEqualsAndHashCode() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer1 = new Customer("C123", "John Doe", address, "1234567890");
        Customer customer2 = new Customer("C123", "John Doe", address, "1234567890");
        Customer customer3 = new Customer("C456", "Jane Doe", address, "0987654321");

        // Test equals
        assertTrue(customer1.equals(customer2), "Equals method should return true for customers with the same ID");
        assertFalse(customer1.equals(customer3), "Equals method should return false for customers with different IDs");

        // Test hashCode
        assertEquals(customer1.hashCode(), customer2.hashCode(), "hashCode method should return the same value for customers with the same ID");
        assertNotEquals(customer1.hashCode(), customer3.hashCode(), "hashCode method should return different values for customers with different IDs");
    }


    // Sad path tests
    @Test
    void settingCustomerIdToNullShouldThrowException() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        assertThrows(IllegalArgumentException.class, () -> customer.setCustomerId(null));
    }

    @Test
    void settingNameToNullShouldThrowException() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        assertThrows(IllegalArgumentException.class, () -> customer.setName(null));
    }

    @Test
    void settingAddressToNullShouldThrowException() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        assertThrows(IllegalArgumentException.class, () -> customer.setAddress(null));
    }

    @Test
    void settingPhoneNumberToNullShouldThrowException() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        assertThrows(IllegalArgumentException.class, () -> customer.setPhoneNumber(null));
    }


    @Test
    void addCarShouldAddCarToCustomer() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        Car car = new Car("ABC123", LocalDate.now(), "Model1", CarType.COMPACT, "C123");
        customer.addCar(car);
        List<Car> cars = customer.getCars();
        assertTrue(cars.contains(car));
    }

    @Test
    void removeCarShouldRemoveCarFromCustomer() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        Car car = new Car("ABC123", LocalDate.now(), "Model1", CarType.COMPACT, "C123");
        customer.addCar(car);
        customer.removeCar(car);
        List<Car> cars = customer.getCars();
        assertFalse(cars.contains(car));
    }

    @Test
    void toStringShouldIncludeCars() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        Customer customer = new Customer("C123", "John Doe", address, "1234567890");
        Car car = new Car("ABC123", LocalDate.now(), "Model1", CarType.COMPACT, "C123");
        customer.addCar(car);
        String customerString = customer.toString();
        assertTrue(customerString.contains("Cars: 1"));
        assertTrue(customerString.contains(car.toString()));
    }

    @Test
    void testEqualsWithNull() {
        Customer customer1 = new Customer("C123", "John Doe", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), "1234567890");
        assertFalse(customer1.equals(null), "Equals method should return false when comparing with null");
    }

    @Test
    void testEqualsWithDifferentClass() {
        Customer customer1 = new Customer("C123", "John Doe", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), "1234567890");
        assertFalse(customer1.equals(new Object()), "Equals method should return false when comparing with an object of a different class");
    }
}

package test;

import main.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AddressTest {

    // Happy path tests
    @Test
    void creatingAddressWithAllFieldsShouldSucceed() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        assertEquals("123 Main St", address.getStreetAddress1());
        assertEquals("Apt 4B", address.getStreetAddress2());
        assertEquals("Springfield", address.getCity());
        assertEquals("IL", address.getState());
        assertEquals("62701", address.getZipCode());
    }

    @Test
    void settingStreetAddress1ShouldChangeValue() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        address.setStreetAddress1("456 Elm St");
        assertEquals("456 Elm St", address.getStreetAddress1());
    }

    @Test
    void settingStreetAddress2ShouldChangeValue() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        address.setStreetAddress2("Apt 5C");
        assertEquals("Apt 5C", address.getStreetAddress2());
    }

    @Test
    void settingCityShouldChangeValue() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        address.setCity("Chicago");
        assertEquals("Chicago", address.getCity());
    }

    @Test
    void settingStateShouldChangeValue() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        address.setState("NY");
        assertEquals("NY", address.getState());
    }

    @Test
    void settingZipCodeShouldChangeValue() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        address.setZipCode("10001");
        assertEquals("10001", address.getZipCode());
    }

    @Test
    void getAddressInfoShouldReturnFormattedAddress() {
        Address address = new Address("123 Main St", "Apt 4B", "Springfield", "IL", "62701");
        String expected = "123 Main St, Apt 4B, Springfield, IL 62701";
        assertEquals(expected, address.getAddressInfo());
    }

    // Sad path tests
    @Test
    void creatingAddressWithEmptySecondStreetShouldSucceed() {
        Address address = new Address("123 Main St", "", "Springfield", "IL", "62701");
        assertEquals("123 Main St", address.getStreetAddress1());
        assertEquals("", address.getStreetAddress2());
        assertEquals("Springfield", address.getCity());
        assertEquals("IL", address.getState());
        assertEquals("62701", address.getZipCode());
    }

    @Test
    void getAddressInfoWithEmptySecondStreetShouldReturnFormattedAddress() {
        Address address = new Address("123 Main St", "", "Springfield", "IL", "62701");
        String expected = "123 Main St, Springfield, IL 62701";
        assertEquals(expected, address.getAddressInfo());
    }
}
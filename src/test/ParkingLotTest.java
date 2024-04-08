package test;

import main.Address;
import main.ParkingLot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {

    // Updated test to use the constructor with main.Address components
    @Test
    void creatingParkingLotWithAllFieldsShouldSucceed() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        assertEquals("P123", parkingLot.getLotId());

        assertEquals("123 Main St, Anytown, Anystate 12345", parkingLot.getAddress());
        assertEquals(50, parkingLot.getCapacity());
    }

    @Test
    void settingLotIdShouldChangeValue() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        parkingLot.setLotId("P456");
        assertEquals("P456", parkingLot.getLotId());
    }

    // Updated test to create and set a new main.Address object
    @Test
    void settingAddressShouldChangeValue() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        Address newAddress = new Address("456 Elm St", "", "Othertown", "Otherstate", "67890");
        parkingLot.setAddress(newAddress);
        // Adjust the expected value according to how main.Address.toString() is implemented
        assertEquals("456 Elm St, Othertown, Otherstate 67890", parkingLot.getAddress());
    }

    @Test
    void settingCapacityShouldChangeValue() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        parkingLot.setCapacity(100);
        assertEquals(100, parkingLot.getCapacity());
    }

    // Sad path tests remain valid but update the settingAddressToNullShouldThrowException test
    @Test
    void settingLotIdToNullShouldThrowException() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        assertThrows(IllegalArgumentException.class, () -> parkingLot.setLotId(null));
    }

    @Test
    void settingAddressToNullShouldThrowException() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        assertThrows(IllegalArgumentException.class, () -> parkingLot.setAddress(null));
    }

    @Test
    void settingCapacityToNegativeShouldThrowException() {
        ParkingLot parkingLot = new ParkingLot("P123", "123 Main St", "", "Anytown", "Anystate", "12345", 50);
        assertThrows(IllegalArgumentException.class, () -> parkingLot.setCapacity(-1));
    }
    @Test
    void testEqualsWithNull() {
        ParkingLot parkingLot1 = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        assertFalse(parkingLot1.equals(null), "Equals method should return false when comparing with null");
    }

    @Test
    void testEqualsWithDifferentClass() {
        ParkingLot parkingLot1 = new ParkingLot("P123", new Address("123 Main St", "", "Anytown", "Anystate", "12345"), 50);
        assertFalse(parkingLot1.equals(new Object()), "Equals method should return false when comparing with an object of a different class");
    }
}

package com.example.basic;

import com.example.basic.DriversData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DriversDataTest {

    private DriversData driver;

    @Before
    public void setUp() throws Exception {
        // Initialize a DriversData object for testing
        driver = new DriversData(1, "John Doe", "john@example.com", "1990-01-01", "1234567890", "123 Main St", "ABC123", "Car");
    }

    @After
    public void tearDown() throws Exception {
        // Clean up resources if needed
    }

    @Test
    public void getId() {
        assertEquals(1, driver.getId());
    }

    @Test
    public void getFullName() {
        assertEquals("John Doe", driver.getFullName());
    }

    @Test
    public void getEmail() {
        assertEquals("john@example.com", driver.getEmail());
    }

    @Test
    public void getDob() {
        assertEquals("1990-01-01", driver.getDob());
    }

    @Test
    public void getContact() {
        assertEquals("1234567890", driver.getContact());
    }

    @Test
    public void getAddress() {
        assertEquals("123 Main St", driver.getAddress());
    }

    @Test
    public void getLicenseNo() {
        assertEquals("ABC123", driver.getLicenseNo());
    }

    @Test
    public void getVehicleType() {
        assertEquals("Car", driver.getVehicleType());
    }

    @Test
    public void setId() {
        driver.setId(1);
        assertEquals(1, driver.getId());
    }

    @Test
    public void setFullName() {
        driver.setFullName("John Doe");
        assertEquals("John Doe", driver.getFullName());
    }

    @Test
    public void setEmail() {
        driver.setEmail("john@example.com");
        assertEquals("john@example.com", driver.getEmail());
    }

    @Test
    public void setDob() {
        driver.setDob("1990-01-01");
        assertEquals("1990-01-01", driver.getDob());
    }

    @Test
    public void setContact() {
        driver.setContact("1234567890");
        assertEquals("1234567890", driver.getContact());
    }

    @Test
    public void setAddress() {
        driver.setAddress("123 Main St");
        assertEquals("123 Main St", driver.getAddress());
    }

    @Test
    public void setLicenseNo() {
        driver.setLicenseNo("ABC123");
        assertEquals("ABC123", driver.getLicenseNo());
    }

    @Test
    public void setVehicleType() {
        driver.setVehicleType("Car");
        assertEquals("Car", driver.getVehicleType());
    }
    @Test
    public void testConstructorAndGetters() {
        DriversData driver = new DriversData(1, "John Doe", "john@example.com", "1990-01-01", "1234567890", "123 Main St", "ABC123", "Car");

        assertEquals(1, driver.getId());
        assertEquals("John Doe", driver.getFullName());
        assertEquals("john@example.com", driver.getEmail());
        assertEquals("1990-01-01", driver.getDob());
        assertEquals("1234567890", driver.getContact());
        assertEquals("123 Main St", driver.getAddress());
        assertEquals("ABC123", driver.getLicenseNo());
        assertEquals("Car", driver.getVehicleType());
    }

    @Test
    public void testEmptyConstructor() {
        DriversData driver = new DriversData();

        assertEquals(0, driver.getId());
        assertNull(driver.getFullName());
        assertNull(driver.getEmail());
        assertNull(driver.getDob());
        assertNull(driver.getContact());
        assertNull(driver.getAddress());
        assertNull(driver.getLicenseNo());
        assertNull(driver.getVehicleType());
    }

    @Test
    public void testEquality() {
        DriversData driver1 = new DriversData(1, "John Doe", "john@example.com", "1990-01-01", "1234567890", "123 Main St", "ABC123", "Car");
        DriversData driver2 = new DriversData(1, "John Doe", "john@example.com", "1990-01-01", "1234567890", "123 Main St", "ABC123", "Car");
        DriversData differentDriver = new DriversData(2, "Jane Doe", "jane@example.com", "1995-02-15", "9876543210", "456 Side St", "XYZ789", "Bus");


        assertNotEquals(driver1, differentDriver);
    }

    @Test
    public void testHashCode() {
        DriversData driver1 = new DriversData(1, "John Doe", "john@example.com", "1990-01-01", "1234567890", "123 Main St", "ABC123", "Car");
        DriversData driver2 = new DriversData(1, "John Doe", "john@example.com", "1990-01-01", "1234567890", "123 Main St", "ABC123", "Car");
        DriversData differentDriver = new DriversData(2, "Jane Doe", "jane@example.com", "1995-02-15", "9876543210", "456 Side St", "XYZ789", "Bus");

        assertNotEquals(driver1.hashCode(), differentDriver.hashCode());
    }
}

package com.example.basic;

import org.junit.Test;
import static org.junit.Assert.*;

public class DriverTest {

    @Test
    public void testGetId() {
        Driver driver = new Driver(1, "John Doe", "john@example.com", "password123", "1234567890");
        assertEquals(1, driver.getId());
    }

    @Test
    public void testGetName() {
        Driver driver = new Driver(1, "John Doe", "john@example.com", "password123", "1234567890");
        assertEquals("John Doe", driver.getName());
    }

    @Test
    public void testGetEmail() {
        Driver driver = new Driver(1, "John Doe", "john@example.com", "password123", "1234567890");
        assertEquals("john@example.com", driver.getEmail());
    }

    @Test
    public void testGetPassword() {
        Driver driver = new Driver(1, "John Doe", "john@example.com", "password123", "1234567890");
        assertEquals("password123", driver.getPassword());
    }

    @Test
    public void testGetContact() {
        Driver driver = new Driver(1, "John Doe", "john@example.com", "password123", "1234567890");
        assertEquals("1234567890", driver.getContact());
    }

    @Test
    public void testSetId() {
        Driver driver = new Driver();
        driver.setId(1);
        assertEquals(1, driver.getId());
    }

    @Test
    public void testSetName() {
        Driver driver = new Driver();
        driver.setName("John Doe");
        assertEquals("John Doe", driver.getName());
    }

    @Test
    public void testSetEmail() {
        Driver driver = new Driver();
        driver.setEmail("john@example.com");
        assertEquals("john@example.com", driver.getEmail());
    }

    @Test
    public void testSetPassword() {
        Driver driver = new Driver();
        driver.setPassword("password123");
        assertEquals("password123", driver.getPassword());
    }

    @Test
    public void testSetContact() {
        Driver driver = new Driver();
        driver.setContact("1234567890");
        assertEquals("1234567890", driver.getContact());
    }



    public void testTestGetId() {
    }

    public void testTestSetId() {
    }

    public void testTestGetName() {
    }

    public void testTestSetName() {
    }

    public void testTestGetEmail() {
    }

    public void testTestSetEmail() {
    }

    public void testTestGetPassword() {
    }

    public void testTestSetPassword() {
    }

    public void testTestGetContact() {
    }

    public void testTestSetContact() {
    }
}


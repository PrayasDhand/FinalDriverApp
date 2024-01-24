package com.example.basic;

public class DriversData {
    private int id;
    private String fullName;
    private String email;
    private String dob;
    private String contact;
    private String address;
    private String licenseNo;
    private String vehicleType;

    // Empty constructor required for Firebase
    public DriversData() {
    }

    public DriversData(int id, String fullName, String email, String dob, String contact, String address, String licenseNo, String vehicleType) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.dob = dob;
        this.contact = contact;
        this.address = address;
        this.licenseNo = licenseNo;
        this.vehicleType = vehicleType;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}

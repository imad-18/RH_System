package com.ensa.rhsystem.finaluiproject.modules;

import java.sql.Date;
public class User {
    private int idUser;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    private Date hireDate;
    private String jobTitle;
    private Department department; // Foreign key reference to Department
    private String role;

    private final String[] roleAdminOrEmployee = {"Admin", "User"};

    public User() {
    }

    public User(int idUser, Department department, String jobTitle,
                Date hireDate, Date dateOfBirth, String address,
                String phoneNumber, String emailAddress, String lastName,
                String firstName) {
        this.idUser = idUser;
        this.department = department;
        this.jobTitle = jobTitle;
        this.hireDate = hireDate;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    // Getters and Setters
    public String getRole (){
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String[] getRoleAdminOrEmployee() {
        return roleAdminOrEmployee;
    }
}
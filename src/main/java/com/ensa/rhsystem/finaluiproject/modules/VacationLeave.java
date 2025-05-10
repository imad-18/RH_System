package com.ensa.rhsystem.finaluiproject.modules;

import java.sql.Date;

public class VacationLeave {
    private int idVacationLeave;
    private String statusOkNo;
    private Date startDate;
    private Date endDate;
    private User user; // Foreign key reference to User
    private LeaveType leaveType; // Foreign key reference to LeaveType
    private int idUser;

    private String leaveReason;
    private String firstName;
    private String lastName;

    public VacationLeave() {
    }



    public VacationLeave(int idVacationLeave, String statusOkNo, Date startDate, Date endDate, User user, LeaveType leaveType, int idUser, String leaveReason, String firstName, String lastName) {
        this.idVacationLeave = idVacationLeave;
        this.statusOkNo = statusOkNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.leaveType = leaveType;
        this.idUser = idUser;
        this.leaveReason = leaveReason;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    // Getters and Setters
    public int getIdVacationLeave() {
        return idVacationLeave;
    }

    public void setIdVacationLeave(int idVacationLeave) {
        this.idVacationLeave = idVacationLeave;
    }

    public String getStatusOkNo() {
        return statusOkNo;
    }

    public void setStatusOkNo(String statusOkNo) {
        this.statusOkNo = statusOkNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }
}

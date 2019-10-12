package com.example.demo_app.models;

public class Leave {
    public String userid, username, uid, leaveType, leaveReason, fromDate, toDate, approved;

    public Leave() {

    }

    public Leave(String userid, String username, String uid, String leaveType, String leaveReason, String fromDate, String toDate, String approved) {
        this.userid = userid;
        this.username = username;
        this.uid = uid;
        this.leaveReason = leaveReason;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.approved = approved;
    }

    public Leave(String uid, String username, String leaveReason, String fromDate, String toDate, String approved) {
        this.uid = uid;
        this.username = username;
        this.leaveReason = leaveReason;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.approved = approved;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
}

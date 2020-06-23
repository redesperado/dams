package com.jqf.dams.bean;

public class AdminBean {

    private int id;

    private String userName;

    private String password;

    private String pwdValue;

    private String role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPwdValue() {
        return pwdValue;
    }

    public void setPwdValue(String pwdValue) {
        this.pwdValue = pwdValue;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

package com.example.android.scmu_epra.auth;

import java.util.List;

public class Account {

    private String name;
    private String phone;
    private String email;
    private boolean isAdmin;
    private List<Integer> privileges;

    public Account(String name, String phone, String email, boolean isAdmin, List<Integer> privileges) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.isAdmin = isAdmin;
        this.privileges = privileges;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<Integer> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Integer> privileges) {
        this.privileges = privileges;
    }
}

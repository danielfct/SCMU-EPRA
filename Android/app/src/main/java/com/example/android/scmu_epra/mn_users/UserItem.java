package com.example.android.scmu_epra.mn_users;

import java.util.ArrayList;

public class UserItem {

    private String name;
    private String mobileNr;
    private String email;
    private String password;
    private boolean isAdmin;
    private ArrayList<Integer> permissions;

    public UserItem(String name, String mobileNr, String email, String password,
                    boolean isAdmin, ArrayList<Integer> permissions) {
        this.name = name;
        this.mobileNr = mobileNr;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNr() {
        return mobileNr;
    }

    public void setMobileNr(String mobileNr) {
        this.mobileNr = mobileNr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public ArrayList<Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<Integer> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Integer permission) {
        this.permissions.add(permission);
    }

    public void removePermission(Integer permission) {
        this.permissions.remove(permission);
    }

}

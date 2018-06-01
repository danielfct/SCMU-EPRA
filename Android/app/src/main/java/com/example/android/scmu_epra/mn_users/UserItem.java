package com.example.android.scmu_epra.mn_users;

import java.util.ArrayList;

public class UserItem {

    private String name;
    private String email;
    private boolean isAdmin;
    private ArrayList<Integer> permissions;

    public UserItem(String name, String email, boolean isAdmin, ArrayList<Integer> permissions) {
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<Integer> permissions) {
        this.permissions = permissions;
    }

}

package com.example.android.scmu_epra.connection;

public class ServerConnection {

    private String ip;
    private long timeout;

    public ServerConnection(String ip, long timeout) {
        this.ip = ip;
        this.timeout = timeout;
    }

    public void authenticateUser(String email, String password) {

    }

    public void registerUser(String name, String phone, String email, String password) {

    }

}

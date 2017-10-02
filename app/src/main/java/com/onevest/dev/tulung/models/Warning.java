package com.onevest.dev.tulung.models;

/**
 * Created by Shoddiq Jati Premono on 17/09/2017.
 */

public class Warning {
    private String name;
    private String phone;

    public Warning(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Warning() {
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
}

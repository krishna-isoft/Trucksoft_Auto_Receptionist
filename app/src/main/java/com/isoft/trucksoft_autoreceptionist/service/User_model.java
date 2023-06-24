package com.isoft.trucksoft_autoreceptionist.service;

import java.io.Serializable;

public class User_model implements Serializable {

    private String name;
    private String id;
    private String phone;

    public String getName() {
        return name;
    }
    public void setName(String named) {
        this.name = named;
    }

    public String getId() {
        return id;
    }
    public void setId(String idz) {
        this.id = idz;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String idz) {
        this.phone = idz;
    }


}

package com.isoft.trucksoft_autoreceptionist.service;

import java.io.Serializable;

public class Sign_Model implements Serializable {

    private String id;
    private String companyname;
    private String name;
    private String emp;
    private String reason;
    private String client_contact;
    private String emp_name;

    public String getId() {
        return id;
    }

    public void setId(String idz) {
        this.id = idz;
    }
    public String getCompanyname() {
        return companyname;
    }
    public void setCompanyname(String companynamez) {
        this.companyname = companynamez;
    }
    public String getName() {
        return name;
    }
    public void setName(String namez) {
        this.name = namez;
    }

    public String getEmp() {
        return emp;
    }
    public void setEmp(String empz) {
        this.emp = empz;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reasonz) {
        this.reason = reasonz;
    }


    public String getClient_contact() {
        return client_contact;
    }

    public void setClient_contact(String client_contactz) {
        this.client_contact = client_contactz;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_namez) {
        this.emp_name = emp_namez;
    }
}

package com.szbk.Orderhandlerv2.model.Entity;

import javax.persistence.*;

@Entity
@Table(name = "laboruser")
public class LaborUser {
    //Auto generated ID for the customer.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String email;
    private String password;

    public LaborUser() {}

    public LaborUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LaborUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

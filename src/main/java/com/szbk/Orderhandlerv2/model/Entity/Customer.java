package com.szbk.Orderhandlerv2.model.Entity;


import javax.persistence.*;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String customerName;
    private String password;
    private String email;

    @Column(name = "groupname")
    private String groupName;

    @Column(name = "companyname")
    private String companyName;

    @Column(name = "innername")
    private String innerName;

    public Customer() {}

    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Customer(String customerName, String password, String email, String groupName, String companyName) {
        this.customerName = customerName;
        this.password = password;
        this.email = email;
        this.groupName = groupName;
        this.companyName = companyName;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInnerName() {
        return this.innerName;
    }

    public void setInnerName(String innerName) {
        this.innerName = innerName;
    }

    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "id: " + this.id + " customer name: " + this.customerName + " password: " + password + " email: " + email
                + " groupname: " + groupName + " companyname: " + companyName + " innerName: " + innerName;
    }
}

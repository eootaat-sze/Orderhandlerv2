package com.szbk.Orderhandlerv2.model.Entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "customerorder")
public class CustomerOrder {
    //Auto generated ID for the customer.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "customerid")
    private long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "sequencename")
    private String sequenceName;
    private String sequence;

    @Column(name = "editedsequence")
    private String editedSequence;
    private int scale;
    private String purification;
    private String type;

    @Column(name = "orderdate")
    private LocalDate orderDate;
    private String status;
    private int price;

    @Column(name = "customerinnerid")
    private String customerInnerId;

    public CustomerOrder() {
        this.status = "Megrendelt";
    }

    public CustomerOrder(String sequenceName, String sequence, int scale, String purification, String type, LocalDate orderDate) {
        this.sequenceName = sequenceName;
        this.sequence = sequence;
        this.scale = scale;
        this.purification = purification;
        this.type = type;
        this.orderDate = orderDate;
        this.status = "Új megrendelés";
    }

    public long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSequenceName() {
        return this.sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String getSequence() {
        return this.sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getEditedSequence() {
        return this.editedSequence;
    }

    public void setEditedSequence(String editedSequence) {
        this.editedSequence = editedSequence;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getPurification() {
        return this.purification;
    }

    public void setPurification(String purification) {
        this.purification = purification;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(LocalDate d) {
        this.orderDate = d;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        if (price >= 0) {
            this.price = price;
        }
    }

    public String getCustomerInnerId() {
        return this.customerInnerId;
    }

    public void setCustomerInnerId(String customerInnerId) {
        this.customerInnerId = customerInnerId;
    }

    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "sequence: " + sequence + ", ed.sequence: " + editedSequence + " , scale: " + scale + ", purification: " + purification + ", type: " + type +
                ", orderDate: " + orderDate + "customerId: " + customerId;
    }
}

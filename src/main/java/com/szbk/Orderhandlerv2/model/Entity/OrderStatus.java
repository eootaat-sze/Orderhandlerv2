package com.szbk.Orderhandlerv2.model.Entity;

import javax.persistence.*;

@Entity
@Table(name = "orderstatus")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    public OrderStatus() {}

    public OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return this.id;
    }
}

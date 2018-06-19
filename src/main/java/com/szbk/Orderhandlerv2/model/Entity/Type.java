package com.szbk.Orderhandlerv2.model.Entity;

import javax.persistence.*;

@Entity
@Table(name = "ordertype")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private int price;

    public Type() {}

    public Type(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

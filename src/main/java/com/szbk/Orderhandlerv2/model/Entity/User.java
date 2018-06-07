/*
The role of this class: the Vaadin data binder needs an entity for binding in the login view. I use the same login view
for the customers and for the laborusers to login, which means, I can't bind a customer for the form, for example,
because what if a laboruser want to login? So I create this placeholder bean with email and password, bind this to the
form and after that I can handle whether a laboruser or a customer wants to login. Not the prettiest solution, I know.
 */

package com.szbk.Orderhandlerv2.model.Entity;

public class User {
    private String email;
    private String password;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
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

    @Override
    public String toString() {
        return "email: " + email + ", password: " + password;
    }
}
package com.example.aprendiz.salesapp.models;

import com.example.aprendiz.salesapp.clients.SalesAPI;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jatorresdev on 24/09/16.
 */
public class User {
    Integer id;
    String name;

    @SerializedName("last_name")
    String lastName;

    String email;
    String cellphone;
    String telephone;
    String photo;
    String password;

    public User(String name, String lastName, String email, String cellphone,
                String telephone, String photo, String password) {

        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.cellphone = cellphone;
        this.telephone = telephone;
        this.photo = photo;
        this.password = password;
    }

    public String getFullName() {
        return this.name + " " + this.lastName;
    }


    @Override
    public String toString() {
        return (id + "\n" +
                name + "\n" +
                lastName + "\n" +
                email + "\n" +
                cellphone + "\n" +
                telephone + "\n" +
                photo);
    }
}

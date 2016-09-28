package com.example.aprendiz.salesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jatorresdev on 24/09/16.
 */
public class User {
    private Integer id;
    private String name;

    @SerializedName("last_name")
    private
    String lastName;

    private String email;
    private String cellphone;
    private String telephone;
    private String photo;
    private String password;

    public User(String name, String lastName, String email, String cellphone,
                String telephone, String photo, String password) {

        this.setName(name);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setCellphone(cellphone);
        this.setTelephone(telephone);
        this.setPhoto(photo);
        this.setPassword(password);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return this.getName() + " " + this.getLastName();
    }
}

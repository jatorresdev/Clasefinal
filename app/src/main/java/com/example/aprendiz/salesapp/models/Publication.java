package com.example.aprendiz.salesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jatorresdev on 25/09/16.
 */
public class Publication {
    private String id;
    private String title;
    private String description;
    private String city;
    private String photo;
    private User user;

    public Publication(String id, String title, String description, String city, String photo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.city = city;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

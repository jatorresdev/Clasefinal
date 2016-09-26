package com.example.aprendiz.salesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jatorresdev on 25/09/16.
 */
public class Publication {
    private Integer id;
    private String title;
    private String description;
    private String city;
    private String photo;

    @SerializedName("user_id")
    private
    Integer userId;


    public Publication(String title, String description, String city, String photo,
                       Integer user_id) {

        this.setTitle(title);
        this.setDescription(description);
        this.setCity(city);
        this.setPhoto(photo);
        this.setUserId(user_id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

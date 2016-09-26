package com.example.aprendiz.salesapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jatorresdev on 25/09/16.
 */
public class Publication {
    Integer id;
    String title;
    String description;
    String city;
    String photo;

    @SerializedName("user_id")
    Integer user_id;

    public Publication(String title, String description, String city, String photo,
                       Integer user_id) {

        this.title = title;
        this.description = description;
        this.city = city;
        this.photo = photo;
        this.user_id = user_id;
    }

}

package com.example.aprendiz.salesapp.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by APRENDIZ on 20/09/2016.
 */
public class Publish {

    private int idPublish;
    private String Title;
    private String Description;
    private String Photo;
    private String City;


    public Publish(JSONObject object) throws JSONException {
        try{


            this.idPublish = object.getInt("idPublish");
            this.Title = object.getString("Title");
            this.Description = object.getString("Photo");
            this.City = object.getString("City");
        }catch (JSONException e){

            e.printStackTrace();

        }



    }

    public int getIdPublish() {
        return idPublish;
    }

    public void setIdPublish(int idPublish) {
        this.idPublish = idPublish;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}

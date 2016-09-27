package com.example.aprendiz.salesapp.models;

/**
 * Created by User on 26/09/2016 modelo comentario
 */
public class Commentary {

    private String id;
    private String message;
    private String publication_id;
    private int user_id;


    /*public Commentary(String id, String message, String publication_id, int user_id) {
        this.id = id;
        this.message = message;
        this.publication_id = publication_id;
        this.user_id = user_id;
    }*/

    public Commentary(String publication_id, String message) {

        this.message = message;
        this.publication_id = publication_id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPublication_id() {
        return publication_id;
    }

    public void setPublication_id(String publication_id) {
        this.publication_id = publication_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullName() {
        return this.publication_id + " " + this.message;
    }
}

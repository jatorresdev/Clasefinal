package com.example.aprendiz.salesapp.models;

/**
 * Created by User on 26/09/2016 modelo comentario
 */
public class Commentary {

    private String id;
    private String message;
    private Publication publication;
    private User user;

    public Commentary(String id, String message) {
        this.setId(id);
        this.setMessage(message);
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

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

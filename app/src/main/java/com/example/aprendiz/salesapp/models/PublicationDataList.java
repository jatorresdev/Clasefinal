package com.example.aprendiz.salesapp.models;

import java.util.ArrayList;

/**
 * Created by jatorresdev on 25/09/16.
 */
public class PublicationDataList {
    private ArrayList<Publication> data;

    public ArrayList<Publication> getData() {
        return data;
    }

    public void setData(ArrayList<Publication> publications) {
        this.data = publications;
    }
}

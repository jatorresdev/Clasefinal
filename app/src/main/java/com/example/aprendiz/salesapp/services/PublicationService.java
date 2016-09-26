package com.example.aprendiz.salesapp.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jatorresdev on 24/09/16.
 */
public interface PublicationService {

    @GET("/api/publication")
    Call<ResponseBody> getPublications();
}

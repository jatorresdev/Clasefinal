package com.example.aprendiz.salesapp.services;

import com.example.aprendiz.salesapp.models.Publication;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by jatorresdev on 24/09/16.
 */
public interface PublicationService {

    @GET("/api/publication")
    Call<ResponseBody> getPublications();

    @GET("/api/publication")
    Call<ResponseBody> getPublicationById(@Query("id") String id,@Query("comes") String comentario);

    @POST("/api/publication")
    Call<ResponseBody> insertPublication(@Body Publication publication);

    @PUT("/api/publication")
    Call<ResponseBody> updatePublication(@Body Publication publication);

    @DELETE("/api/publication")
    Call<ResponseBody> deletePublication(@Field("id") String id);
}

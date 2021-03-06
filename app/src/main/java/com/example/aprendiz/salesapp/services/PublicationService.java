package com.example.aprendiz.salesapp.services;

import com.example.aprendiz.salesapp.models.PublicationData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by jatorresdev on 24/09/16.
 */
public interface PublicationService {

    @GET("/api/publication")
    Call<ResponseBody> getPublications();

    @GET("/api/publication/{id}")
    Call<PublicationData> getPublicationById(@Path("id") String id);

    @Multipart
    @POST("/api/publication")
    Call<PublicationData> insertPublication(@Part("title") RequestBody title,
                                            @Part("description") RequestBody description,
                                            @Part("city") RequestBody city,
                                            @Part MultipartBody.Part file);

    @Multipart
    @POST("/api/publication/{id}")
    Call<PublicationData> updatePublication(@Path("id") String id,
                                            @Part("title") RequestBody title,
                                            @Part("description") RequestBody description,
                                            @Part("city") RequestBody city,
                                            @Part MultipartBody.Part file,
                                            @Part("_method") RequestBody method);

    @DELETE("/api/publication/{id}")
    Call<ResponseBody> deletePublication(@Path("id") String id);
}

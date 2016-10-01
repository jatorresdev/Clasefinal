package com.example.aprendiz.salesapp.services;

import com.example.aprendiz.salesapp.models.Publication;
import com.example.aprendiz.salesapp.models.PublicationData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jatorresdev on 24/09/16.
 */
public interface PublicationService {

    @GET("/api/publication")
    Call<ResponseBody> getPublications();

    @GET("/api/publication")
    Call<ResponseBody> getPublicationById(@Query("id") String id, @Query("comes") String comentario);

    @Multipart
    @POST("/api/publication")
    Call<PublicationData> insertPublication(@Part("title") RequestBody title,
                                            @Part("description") RequestBody description,
                                            @Part("city") RequestBody city,
                                            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @PUT("/api/publication/{id}")
    Call<ResponseBody> updatePublication(@Path("id") String id, @Field("title") String title,
                                         @Field("description") String description, @Field("city") String city, @Field("photo") String photo);

    @DELETE("/api/publication")
    Call<ResponseBody> deletePublication(@Field("id") String id);
}

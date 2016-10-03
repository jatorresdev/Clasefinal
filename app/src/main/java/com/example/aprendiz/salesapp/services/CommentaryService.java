package com.example.aprendiz.salesapp.services;

/*Commentary services*/

import com.example.aprendiz.salesapp.models.Commentary;
import com.example.aprendiz.salesapp.models.CommentaryData;
import com.example.aprendiz.salesapp.models.PublicationData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommentaryService {

    @GET("/api/publication/{idPublication}/comment")
    Call<ResponseBody> getCommentaryForId(@Path("idPublication") String idPublication);

    @FormUrlEncoded
    @POST("/api/publication/{idPublication}/comment")
    Call<ResponseBody> createCommentary(@Path("idPublication") String idPublication, @Field("message") String message);

    @FormUrlEncoded
    @PUT("/api/publication/{idPublication}/comment/{idComentario}")
    Call<ResponseBody> updateCommentary(@Path("idPublication") String idPublication,@Field("message") String message,@Path("idComentario") String idComentario);

}

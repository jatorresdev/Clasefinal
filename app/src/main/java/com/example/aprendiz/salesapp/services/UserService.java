package com.example.aprendiz.salesapp.services;

import com.example.aprendiz.salesapp.models.User;
import com.example.aprendiz.salesapp.models.UserData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

/**
 * Created by jatorresdev on 24/09/16.
 */
public interface UserService {
    @FormUrlEncoded
    @POST("/api/user/login")
    Call<UserData> loginUser(@Field("email") String email, @Field("password") String password);

    @Multipart
    @POST("/api/user")
    Call<ResponseBody> createUser(@Part("name") RequestBody name,
                                  @Part("last_name") RequestBody lastName,
                                  @Part("cellphone") RequestBody cellphone,
                                  @Part("telephone") RequestBody telephone,
                                  @Part("email") RequestBody email,
                                  @Part("password") RequestBody password,
                                  @Part MultipartBody.Part file);

    @PUT("/api/user")
    Call<UserData> updateUser(@Body User user);
}

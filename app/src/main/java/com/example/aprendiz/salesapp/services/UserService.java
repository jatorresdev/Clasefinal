package com.example.aprendiz.salesapp.services;

import com.example.aprendiz.salesapp.models.User;
import com.example.aprendiz.salesapp.models.UserData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by jatorresdev on 24/09/16.
 */
public interface UserService {
    @POST("/api/user")
    Call<UserData> createUser(@Body User user);

    @PUT("/api/user")
    Call<ResponseBody> updateUser(@Body User user);
}

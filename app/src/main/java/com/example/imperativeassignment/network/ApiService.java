package com.example.imperativeassignment.network;


import com.example.imperativeassignment.ui.models.CreateUserRequestParser;
import com.example.imperativeassignment.ui.models.CreateUserResponseParser;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface ApiService {

    @GET("profile/create_profile")
    Call<CreateUserResponseParser> createUser(@Body CreateUserRequestParser request);

}

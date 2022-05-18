package com.example.imperativeassignment.network;

import com.example.imperativeassignment.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    private static ApiService retroApiClient = APIClient.getClient().create(ApiService.class);

    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}

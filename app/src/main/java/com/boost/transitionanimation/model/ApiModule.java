package com.boost.transitionanimation.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiModule {

    public static ApiInterface getApiInterface(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://random.cat")
                .build();

        return retrofit.create(ApiInterface.class);
    }
}

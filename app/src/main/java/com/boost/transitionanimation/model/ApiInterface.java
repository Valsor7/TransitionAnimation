package com.boost.transitionanimation.model;


import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("meow")
    Call<CatModel> getRandomUrl();
}

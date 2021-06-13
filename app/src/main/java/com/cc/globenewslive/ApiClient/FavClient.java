package com.cc.globenewslive.ApiClient;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavClient {
    public static final String BASEURL="https://livenewsglobe.com/wp-json/newspaper/v2/";
    public static Retrofit retrofit;
    public static Retrofit getRetrofitClient(){
        if(retrofit==null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

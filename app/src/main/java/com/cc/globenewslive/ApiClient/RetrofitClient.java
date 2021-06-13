package com.cc.globenewslive.ApiClient;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String BASEURL="https://www.livenewsglobe.com/wp-json/Newspaper/v2/";
    public static Retrofit retrofit;
    public static Retrofit getRetrofitClient(Context mContext){
        if(retrofit==null){
            OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new NetworkConnectionInterceptor(mContext));
            // Adding NetworkConnectionInterceptor with okHttpClientBuilder.

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(oktHttpClient.build())
                    .build();
        }
        return retrofit;
    }
}

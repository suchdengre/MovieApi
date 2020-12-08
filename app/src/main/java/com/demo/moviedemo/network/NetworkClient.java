package com.demo.moviedemo.network;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class NetworkClient {

    private static final String BASE_URL = "http://www.omdbapi.com/";
    private static final int DEFAULT_TIMEOUT = 30;
    private static Retrofit retrofit = null;

    static synchronized Retrofit getClient(int timeout) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(timeout != 0 ? timeout : DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.connectTimeout(timeout != 0 ? timeout : DEFAULT_TIMEOUT, TimeUnit.SECONDS);


        /*if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }*/

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;
    }

}
package com.demo.moviedemo.network;

import com.demo.moviedemo.network.ApiModels.MoviesDetailResponse;
import com.demo.moviedemo.network.ApiModels.MoviesResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface APIFactory {


    @GET("http://www.omdbapi.com/")
    Call<MoviesResponse> getMovies(@QueryMap Map<String, Object> body);

    @GET("http://www.omdbapi.com/")
    Call<MoviesDetailResponse> getMoviesDetail(@QueryMap Map<String, Object> body);
}
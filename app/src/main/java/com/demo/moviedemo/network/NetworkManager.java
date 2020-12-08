package com.demo.moviedemo.network;

import android.util.Log;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkManager {

    private static NetworkManager mInstance = null;
    private static int mCustomTimeout = 0;
    private static boolean isCustomTimeout = false;

    public static synchronized NetworkManager getInstance() {
        if (mInstance == null)
            mInstance = new NetworkManager();

        return mInstance;
    }

    public static synchronized NetworkManager setTimeout(int timeoutInSeconds) {
        mCustomTimeout = timeoutInSeconds;
        isCustomTimeout = true;
        return mInstance;
    }

    private APIFactory getResponseFactory() {
        APIFactory factory = NetworkClient.getClient(isCustomTimeout ? mCustomTimeout : 0).create(APIFactory.class);
        isCustomTimeout = false;
        return factory;
    }

    public void request(final Map<String, Object> queryMap, final REQUEST type, final RequestCallback callback) {
        Call request = getRequest(queryMap, type);
        if (request != null)
            enqueueRequest(request, type, callback);

    }

    private Call getRequest(Map<String, Object> queryMap, REQUEST type) {
        Call call = null;

        switch (type) {

            case FETCH_MOVIES:
                call = getResponseFactory().getMovies(queryMap);
                break;
            case DETAIL_MOVIES:
                call = getResponseFactory().getMoviesDetail(queryMap);
                break;
        }

        return call;
    }

    private void enqueueRequest(Call request, final REQUEST type, final RequestCallback callback) {

        request.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
               Log.e("onResponse", "" + response.toString());
                if (response.isSuccessful())
                    callback.onSuccess(type, response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                callback.onFailure(type);
            }
        });
    }

    public enum REQUEST {
        FETCH_MOVIES,
        DETAIL_MOVIES

    }

    public interface RequestCallback {
        void onSuccess(REQUEST type, Response response);

        void onFailure(REQUEST type);
    }


}
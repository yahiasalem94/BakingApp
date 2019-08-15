package com.example.android.bakingapp.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/";
    /* The format we want our API to return */
    private static final String format = "json";

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
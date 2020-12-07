package com.project.delieveryapp;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {

    private static Retrofit retrofit = null;

    public Retrofit call() {

               /*  Retrofit retrofit = new Retrofit.Builder().baseUrl("http://livesale.asrpos.com/").
                         addConverterFactory(GsonConverterFactory.create()).build();

                return retrofit;*/
        String URL = "http://167.86.86.78/SingleStoreApi/";
//        String URL = "http://shinwariapi.fadelsoft.com/";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //.baseUrl("http://173.212.240.166:94/molsindia/")
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create()).client(client)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit call1() {
        Retrofit retrofit = null;


               /*  Retrofit retrofit = new Retrofit.Builder().baseUrl("http://livesale.asrpos.com/").
                         addConverterFactory(GsonConverterFactory.create()).build();

                return retrofit;*/


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
//                    .baseUrl("http://167.86.86.78/SingleStoreUi/")
                    .baseUrl("http://shinwari.fadelsoft.com/")
                    .addConverterFactory(GsonConverterFactory.create()).client(client)
                    .build();
        }

        return retrofit;
    }


    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}

package com.isoft.trucksoft_autoreceptionist.service;

import android.content.Context;

import com.isoft.trucksoft_autoreceptionist.Preference;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sab99r
 */
public class ServiceGenerator {
   static Preference pref;

    public static <S> S createService(Class<S> serviceClass, Context context) {
        pref=Preference.getInstance(context);

        OkHttpClient httpClient=new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://peopleforhumanity.in/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient).build();
        String burl=pref.getString(Constant.BASE_URL_GUEST);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(burl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient).build();
        return retrofit.create(serviceClass);

    }

}

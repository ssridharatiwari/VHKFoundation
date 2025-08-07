package com.vhkfoundation.retrofit;

import android.util.Log;


import androidx.annotation.NonNull;

import com.vhkfoundation.commonutility.GlobalVariables;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    private static final int CONNECTION_TIME_OUT = 60;
    private static final int WRITE_TIME_OUT = 60;
    private static final int READ_TIME_OUT = 60;
    public static Retrofit retrofit = null;
    public static OkHttpClient okClient = new OkHttpClient.Builder().
            connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS).
            writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS).
            readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).
            addInterceptor(new Interceptor() {

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Token","")
                    .addHeader("Deviceid","").build();

            // try the request
            Response response = chain.proceed(request);
            int tryCount = 0;
            while (!response.isSuccessful() && tryCount < 3) {
                Log.d("intercept", "Request is not successful - " + tryCount);
                tryCount++;
                // retry the request
                response.close();
                response = chain.proceed(request);
            }
            // otherwise just pass the original response on
            return response;
        }
    }).build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().
                    baseUrl(GlobalVariables.PRE_URL).
                    client(okClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}

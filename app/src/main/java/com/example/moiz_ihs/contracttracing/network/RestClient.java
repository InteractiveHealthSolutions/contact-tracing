package com.example.moiz_ihs.contracttracing.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HTTP;

/**
 * Created by Moiz-IHS on 5/26/2017.
 */

public class RestClient {

    private static final int TIMEOUT = 15;  // Request Time out
    private static ApiInterface restClient;
    private static final String BASE_URL = "http://199.172.1.129:8181/tbrmobileweb/";

    static
    {
        setupRestClient();
    }


    private static void setupRestClient()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest = request.newBuilder()
                                .addHeader("Accept","application/json")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT,TimeUnit.SECONDS)
                .readTimeout(TIMEOUT,TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();


        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)

                .build();

        restClient = retrofit.create(ApiInterface.class);

    }


    public static  ApiInterface getRestClient()
    {
        return restClient;
    }
}

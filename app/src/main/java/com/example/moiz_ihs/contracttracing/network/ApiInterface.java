package com.example.moiz_ihs.contracttracing.network;

import com.example.moiz_ihs.contracttracing.models.ApiResponse;
import com.example.moiz_ihs.contracttracing.models.Data;

import org.json.JSONArray;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Moiz-IHS on 5/26/2017.
 */

public interface ApiInterface {
    @GET(EndPoints.END_POINT)
    Call<ApiResponse> getData(@QueryMap JSONArray options);
}

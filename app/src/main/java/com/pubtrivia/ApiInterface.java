package com.pubtrivia;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("api.php")
    Call<QuestionsModel> getQuestions(
            @QueryMap Map<String, String> options
    );
}

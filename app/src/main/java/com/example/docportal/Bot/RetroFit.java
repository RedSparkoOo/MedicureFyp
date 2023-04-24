package com.example.docportal.Bot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetroFit {

    @GET
    Call<MsgModel> getMessage(@Url String url);
}

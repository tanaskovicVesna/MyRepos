package com.tanaskovic.vesna.repository;

import com.tanaskovic.vesna.model.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface Api_Retrofit {
    //define base url
    public static String  BASE_URL = "https://api.github.com/users/tanaskovicVesna/";

    @GET("repos")
    Call<List<Repo>> getRepos(
            @Header("Authorization") String authkey
    );
}

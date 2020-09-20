package com.tanaskovic.vesna.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Repo {

    @SerializedName("name")
    @Expose
    private String name;

    public Repo(String name) {
        this.name = name;
    }
    public Repo(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

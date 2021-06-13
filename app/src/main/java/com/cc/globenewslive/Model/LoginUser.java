package com.cc.globenewslive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginUser {
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("caps")
    @Expose
    private Caps caps;
    @SerializedName("cap_key")
    @Expose
    private String capKey;
    @SerializedName("roles")
    @Expose
    private List<String> roles = null;
    @SerializedName("allcaps")
    @Expose
    private Allcaps allcaps;
    @SerializedName("filter")
    @Expose
    private Object filter;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Caps getCaps() {
        return caps;
    }

    public void setCaps(Caps caps) {
        this.caps = caps;
    }

    public String getCapKey() {
        return capKey;
    }

    public void setCapKey(String capKey) {
        this.capKey = capKey;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Allcaps getAllcaps() {
        return allcaps;
    }

    public void setAllcaps(Allcaps allcaps) {
        this.allcaps = allcaps;
    }

    public Object getFilter() {
        return filter;
    }

    public void setFilter(Object filter) {
        this.filter = filter;
    }
}

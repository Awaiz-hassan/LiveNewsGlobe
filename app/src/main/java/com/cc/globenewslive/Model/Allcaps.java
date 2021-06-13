package com.cc.globenewslive.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Allcaps {

    @SerializedName("read")
    @Expose
    private Boolean read;
    @SerializedName("level_0")
    @Expose
    private Boolean level0;
    @SerializedName("subscriber")
    @Expose
    private Boolean subscriber;

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getLevel0() {
        return level0;
    }

    public void setLevel0(Boolean level0) {
        this.level0 = level0;
    }

    public Boolean getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Boolean subscriber) {
        this.subscriber = subscriber;
    }

}

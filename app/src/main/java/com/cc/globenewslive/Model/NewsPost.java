package com.cc.globenewslive.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsPost implements Parcelable {

        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("guid")
        @Expose
        private String guid;
        @SerializedName("post_type")
        @Expose
        private String postType;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("thumbnail_url")
        @Expose
        private List<String> thumbnailUrl = null;
        @SerializedName("msg")
        @Expose
        private Integer msg;

    protected NewsPost(Parcel in) {
        content = in.readString();
        guid = in.readString();
        postType = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        title = in.readString();
        thumbnailUrl = in.createStringArrayList();
        if (in.readByte() == 0) {
            msg = null;
        } else {
            msg = in.readInt();
        }
    }

    public static final Creator<NewsPost> CREATOR = new Creator<NewsPost>() {
        @Override
        public NewsPost createFromParcel(Parcel in) {
            return new NewsPost(in);
        }

        @Override
        public NewsPost[] newArray(int size) {
            return new NewsPost[size];
        }
    };

    public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(List<String> thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public Integer getMsg() {
            return msg;
        }

        public void setMsg(Integer msg) {
            this.msg = msg;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(guid);
        dest.writeString(postType);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(title);
        dest.writeStringList(thumbnailUrl);
        if (msg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(msg);
        }
    }
}


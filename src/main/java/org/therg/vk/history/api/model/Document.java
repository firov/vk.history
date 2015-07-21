package org.therg.vk.history.api.model;

import com.google.gson.annotations.SerializedName;

public class Document {
    @SerializedName("id")
    public long id;

    @SerializedName("owner_id")
    public long ownerId;

    @SerializedName("title")
    public String title;

    @SerializedName("size")
    public long size;

    @SerializedName("url")
    public long url;
}
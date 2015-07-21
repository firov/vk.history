package org.therg.vk.history.api.model;

import com.google.gson.annotations.SerializedName;

public class Audio {
    @SerializedName("id")
    public long id;

    @SerializedName("owner_id")
    public long ownerId;

    @SerializedName("artist")
    public String artist;

    @SerializedName("title")
    public String title;

    @SerializedName("url")
    public String url;
}
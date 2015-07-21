package org.therg.vk.history.api.model;

import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("id")
    public long id;

    @SerializedName("owner_id")
    public long ownerId;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("link")
    public String url;
}

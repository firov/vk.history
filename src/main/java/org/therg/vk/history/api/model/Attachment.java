package org.therg.vk.history.api.model;

import com.google.gson.annotations.SerializedName;

public class Attachment {
    @SerializedName("type")
    public String type;

    @SerializedName("photo")
    public Photo photo;

    @SerializedName("audio")
    public Audio audio;

    @SerializedName("video")
    public Video video;

    @SerializedName("document")
    public Document document;
}


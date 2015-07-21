package org.therg.vk.history.api.model;

import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("id")
    public long id;

    @SerializedName("album_id")
    public long albumId;

    @SerializedName("owner_id")
    public long ownerId;

    @SerializedName("photo_75")
    public String urlXS;

    @SerializedName("photo_130")
    public String urlS;

    @SerializedName("photo_604")
    public String urlM;

    @SerializedName("photo_807")
    public String urlL;

    @SerializedName("photo_1280")
    public String urlXL;

    @SerializedName("photo_2560")
    public String urlXXL;

    @SerializedName("width")
    public int width;

    @SerializedName("height")
    public int height;

    @SerializedName("text")
    public String text;

    @SerializedName("date")
    public long date;

    @SerializedName("access_key")
    public String access_key;

    public String getBestUrl() {
        if (urlXXL != null)
            return urlXXL;
        if (urlXL != null)
            return urlXL;
        if (urlL != null)
            return urlL;
        if (urlM != null)
            return urlM;
        if (urlS != null)
            return urlS;

        return urlXS;
    }
}
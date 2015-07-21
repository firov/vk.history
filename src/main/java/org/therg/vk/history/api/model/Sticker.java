package org.therg.vk.history.api.model;

import com.google.gson.annotations.SerializedName;

public class Sticker {
    @SerializedName("id")
    public long id;

    @SerializedName("product_id")
    public long productId;

    @SerializedName("photo_64")
    public String urlS;

    @SerializedName("photo_128")
    public String urlM;

    @SerializedName("photo_256")
    public String urlL;

    @SerializedName("photo_352")
    public String urlXL;

    @SerializedName("width")
    public int width;

    @SerializedName("height")
    public int height;

    public String getBiggestSizeUrl() {
        if (urlXL != null)
            return urlXL;

        if (urlL != null)
            return urlL;

        if (urlM != null)
            return urlM;

        return urlS;
    }

    public String getSmallestSizeUrl() {
        if (urlS != null)
            return urlS;

        if (urlM != null)
            return urlM;

        if (urlL != null)
            return urlL;

        return urlXL;
    }
}

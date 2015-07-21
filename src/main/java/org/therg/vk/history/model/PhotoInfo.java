package org.therg.vk.history.model;

public class PhotoInfo {
    public PhotoInfo() {

    }

    public PhotoInfo(long id, String url) {
        this.id = id;
        this.url = url;
    }

    public long id;
    public String url;
    public String path;
}

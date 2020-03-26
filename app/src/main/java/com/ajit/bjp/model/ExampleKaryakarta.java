package com.ajit.bjp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExampleKaryakarta {

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("encoding")
    @Expose
    private String encoding;

    @SerializedName("feed")
    @Expose
    private KaryaKartaFeed feed;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public KaryaKartaFeed getFeed() {
        return feed;
    }

    public void setFeed(KaryaKartaFeed feed) {
        this.feed = feed;
    }
}

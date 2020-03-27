package com.ajit.bjp.model.corona;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExampleCorona {

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("encoding")
    @Expose
    private String encoding;

    @SerializedName("feed")
    @Expose
    private CoronaFeed feed;

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

    public CoronaFeed getFeed() {
        return feed;
    }

    public void setFeed(CoronaFeed feed) {
        this.feed = feed;
    }
}

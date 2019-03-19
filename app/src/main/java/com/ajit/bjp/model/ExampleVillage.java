
package com.ajit.bjp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExampleVillage {

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("encoding")
    @Expose
    private String encoding;
    @SerializedName("feed")
    @Expose
    private VillageFeed villageFeed;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEncoding() {
        return encoding;
    }

    public VillageFeed getVillageFeed() {
        return villageFeed;
    }

    public void setVillageFeed(VillageFeed villageFeed) {
        this.villageFeed = villageFeed;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }


}

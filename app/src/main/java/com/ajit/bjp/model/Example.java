
package com.ajit.bjp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("encoding")
    @Expose
    private String encoding;
    @SerializedName("feed")
    @Expose
    private PersonFeed personFeed;

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

    public PersonFeed getPersonFeed() {
        return personFeed;
    }

    public void setPersonFeed(PersonFeed personFeed) {
        this.personFeed = personFeed;
    }

}

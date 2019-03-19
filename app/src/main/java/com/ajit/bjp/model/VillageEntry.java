
package com.ajit.bjp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VillageEntry {

    @SerializedName("id")
    @Expose
    private Id_ id;
    @SerializedName("updated")
    @Expose
    private Updated_ updated;
    @SerializedName("category")
    @Expose
    private List<Category_> category = null;
    @SerializedName("title")
    @Expose
    private Title_ title;
    @SerializedName("content")
    @Expose
    private Content content;
    @SerializedName("link")
    @Expose
    private List<Link_> link = null;
    @SerializedName("gsx$village")
    @Expose
    private Gsx$village gsx$village;
    @SerializedName("gsx$scheme")
    @Expose
    private Gsx$scheme gsx$scheme;
    @SerializedName("gsx$year")
    @Expose
    private Gsx$year gsx$year;
    @SerializedName("gsx$details")
    @Expose
    private Gsx$details gsx$details;

    @SerializedName("gsx$sanctionedamount")
    @Expose
    private Gsx$sanctionedamount gsx$sanctionedamount;

    @SerializedName("gsx$distance")
    @Expose
    private Gsx$distance gsx$distance;

    @SerializedName("gsx$status")
    @Expose
    private Gsx$status gsx$status;

    @SerializedName("gsx$remarks")
    @Expose
    private Gsx$remarks gsx$remarks;

    public Gsx$remarks getGsx$remarks() {
        return gsx$remarks;
    }

    public void setGsx$remarks(Gsx$remarks gsx$remarks) {
        this.gsx$remarks = gsx$remarks;
    }

    public Id_ getId() {
        return id;
    }

    public void setId(Id_ id) {
        this.id = id;
    }

    public Updated_ getUpdated() {
        return updated;
    }

    public void setUpdated(Updated_ updated) {
        this.updated = updated;
    }

    public List<Category_> getCategory() {
        return category;
    }

    public void setCategory(List<Category_> category) {
        this.category = category;
    }

    public Title_ getTitle() {
        return title;
    }

    public void setTitle(Title_ title) {
        this.title = title;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public List<Link_> getLink() {
        return link;
    }

    public void setLink(List<Link_> link) {
        this.link = link;
    }

    public Gsx$village getGsx$village() {
        return gsx$village;
    }

    public void setGsx$village(Gsx$village gsx$village) {
        this.gsx$village = gsx$village;
    }

    public Gsx$scheme getGsx$scheme() {
        return gsx$scheme;
    }

    public void setGsx$scheme(Gsx$scheme gsx$scheme) {
        this.gsx$scheme = gsx$scheme;
    }

    public Gsx$year getGsx$year() {
        return gsx$year;
    }

    public void setGsx$year(Gsx$year gsx$year) {
        this.gsx$year = gsx$year;
    }

    public Gsx$details getGsx$details() {
        return gsx$details;
    }

    public void setGsx$details(Gsx$details gsx$details) {
        this.gsx$details = gsx$details;
    }

    public Gsx$sanctionedamount getGsx$sanctionedamount() {
        return gsx$sanctionedamount;
    }

    public void setGsx$sanctionedamount(Gsx$sanctionedamount gsx$sanctionedamount) {
        this.gsx$sanctionedamount = gsx$sanctionedamount;
    }

    public Gsx$distance getGsx$distance() {
        return gsx$distance;
    }

    public void setGsx$distance(Gsx$distance gsx$distance) {
        this.gsx$distance = gsx$distance;
    }

    public Gsx$status getGsx$status() {
        return gsx$status;
    }

    public void setGsx$status(Gsx$status gsx$status) {
        this.gsx$status = gsx$status;
    }

}

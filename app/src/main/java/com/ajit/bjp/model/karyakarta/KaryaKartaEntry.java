package com.ajit.bjp.model.karyakarta;

import com.ajit.bjp.model.Category_;
import com.ajit.bjp.model.Content;
import com.ajit.bjp.model.Gs$cell;
import com.ajit.bjp.model.Id_;
import com.ajit.bjp.model.Link_;
import com.ajit.bjp.model.Title_;
import com.ajit.bjp.model.Updated_;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KaryaKartaEntry {

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
    private List<Link_> link;

    @SerializedName("gs$cell")
    @Expose
    private Gs$cell gs$cell;

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

    public Gs$cell getGs$cell() {
        return gs$cell;
    }

    public void setGs$cell(Gs$cell gs$cell) {
        this.gs$cell = gs$cell;
    }

}

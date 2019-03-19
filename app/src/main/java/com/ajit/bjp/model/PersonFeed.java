
package com.ajit.bjp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonFeed {

    @SerializedName("xmlns")
    @Expose
    private String xmlns;
    @SerializedName("xmlns$openSearch")
    @Expose
    private String xmlns$openSearch;
    @SerializedName("xmlns$gsx")
    @Expose
    private String xmlns$gsx;
    @SerializedName("id")
    @Expose
    private Id id;
    @SerializedName("updated")
    @Expose
    private Updated updated;
    @SerializedName("category")
    @Expose
    private List<Category> category = null;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("link")
    @Expose
    private List<Link> link = null;
    @SerializedName("author")
    @Expose
    private List<Author> author = null;
    @SerializedName("openSearch$totalResults")
    @Expose
    private OpenSearch$totalResults openSearch$totalResults;
    @SerializedName("openSearch$startIndex")
    @Expose
    private OpenSearch$startIndex openSearch$startIndex;
    @SerializedName("entry")
    @Expose
    private List<Entry> entry = null;

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getXmlns$openSearch() {
        return xmlns$openSearch;
    }

    public void setXmlns$openSearch(String xmlns$openSearch) {
        this.xmlns$openSearch = xmlns$openSearch;
    }

    public String getXmlns$gsx() {
        return xmlns$gsx;
    }

    public void setXmlns$gsx(String xmlns$gsx) {
        this.xmlns$gsx = xmlns$gsx;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Updated getUpdated() {
        return updated;
    }

    public void setUpdated(Updated updated) {
        this.updated = updated;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    public OpenSearch$totalResults getOpenSearch$totalResults() {
        return openSearch$totalResults;
    }

    public void setOpenSearch$totalResults(OpenSearch$totalResults openSearch$totalResults) {
        this.openSearch$totalResults = openSearch$totalResults;
    }

    public OpenSearch$startIndex getOpenSearch$startIndex() {
        return openSearch$startIndex;
    }

    public void setOpenSearch$startIndex(OpenSearch$startIndex openSearch$startIndex) {
        this.openSearch$startIndex = openSearch$startIndex;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

}

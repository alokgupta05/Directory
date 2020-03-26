package com.ajit.bjp.model.karyakarta;

import com.ajit.bjp.model.Author;
import com.ajit.bjp.model.Category;
import com.ajit.bjp.model.Gs$colCount;
import com.ajit.bjp.model.Gs$rowCount;
import com.ajit.bjp.model.Id;
import com.ajit.bjp.model.Link;
import com.ajit.bjp.model.OpenSearch$startIndex;
import com.ajit.bjp.model.OpenSearch$totalResults;
import com.ajit.bjp.model.Title;
import com.ajit.bjp.model.Updated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KaryaKartaFeed {

    @SerializedName("xmlns")
    @Expose
    private String xmlns;

    @SerializedName("xmlns$openSearch")
    @Expose
    private String xmlns$openSearch;

    @SerializedName("xmlns$batch")
    @Expose
    private String xmlns$batch;

    @SerializedName("xmlns$gs")
    @Expose
    private String xmlns$gs;

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

    @SerializedName("gs$rowCount")
    @Expose
    private Gs$rowCount gs$rowCount;

    @SerializedName("gs$colCount")
    @Expose
    private Gs$colCount gs$colCount;

    @SerializedName("entry")
    @Expose
    private List<KaryaKartaEntry> entry;

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

    public String getXmlns$batch() {
        return xmlns$batch;
    }

    public void setXmlns$batch(String xmlns$batch) {
        this.xmlns$batch = xmlns$batch;
    }

    public String getXmlns$gs() {
        return xmlns$gs;
    }

    public void setXmlns$gs(String xmlns$gs) {
        this.xmlns$gs = xmlns$gs;
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

    public Gs$rowCount getGs$rowCount() {
        return gs$rowCount;
    }

    public void setGs$rowCount(Gs$rowCount gs$rowCount) {
        this.gs$rowCount = gs$rowCount;
    }

    public Gs$colCount getGs$colCount() {
        return gs$colCount;
    }

    public void setGs$colCount(Gs$colCount gs$colCount) {
        this.gs$colCount = gs$colCount;
    }

    public List<KaryaKartaEntry> getEntry() {
        return entry;
    }

    public void setEntry(List<KaryaKartaEntry> entry) {
        this.entry = entry;
    }
}

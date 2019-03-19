
package com.ajit.bjp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entry {

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
    @SerializedName("gsx$department")
    @Expose
    private Gsx$department gsx$department;
    @SerializedName("gsx$designation")
    @Expose
    private Gsx$designation gsx$designation;
    @SerializedName("gsx$charge")
    @Expose
    private Gsx$charge gsx$charge;
    @SerializedName("gsx$name")
    @Expose
    private Gsx$name gsx$name;
    @SerializedName("gsx$mobileno")
    @Expose
    private Gsx$mobileno gsx$mobileno;
    @SerializedName("gsx$officeno")
    @Expose
    private Gsx$officeno gsx$officeno;
    @SerializedName("gsx$homeno")
    @Expose
    private Gsx$homeno gsx$homeno;
    @SerializedName("gsx$floor")
    @Expose
    private Gsx$floor gsx$floor;
    @SerializedName("gsx$deskno")
    @Expose
    private Gsx$deskno gsx$deskno;
    @SerializedName("gsx$paname")
    @Expose
    private Gsx$paname gsx$paname;
    @SerializedName("gsx$pano")
    @Expose
    private Gsx$pano gsx$pano;


    public Gsx$email getGsx$email() {
        return gsx$email;
    }

    public void setGsx$email(Gsx$email gsx$email) {
        this.gsx$email = gsx$email;
    }

    @SerializedName("gsx$email")
    @Expose

    private Gsx$email gsx$email;

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

    public Gsx$department getGsx$department() {
        return gsx$department;
    }

    public void setGsx$department(Gsx$department gsx$department) {
        this.gsx$department = gsx$department;
    }

    public Gsx$designation getGsx$designation() {
        return gsx$designation;
    }

    public void setGsx$designation(Gsx$designation gsx$designation) {
        this.gsx$designation = gsx$designation;
    }

    public Gsx$charge getGsx$charge() {
        return gsx$charge;
    }

    public void setGsx$charge(Gsx$charge gsx$charge) {
        this.gsx$charge = gsx$charge;
    }

    public Gsx$name getGsx$name() {
        return gsx$name;
    }

    public void setGsx$name(Gsx$name gsx$name) {
        this.gsx$name = gsx$name;
    }

    public Gsx$mobileno getGsx$mobileno() {
        return gsx$mobileno;
    }

    public void setGsx$mobileno(Gsx$mobileno gsx$mobileno) {
        this.gsx$mobileno = gsx$mobileno;
    }

    public Gsx$officeno getGsx$officeno() {
        return gsx$officeno;
    }

    public void setGsx$officeno(Gsx$officeno gsx$officeno) {
        this.gsx$officeno = gsx$officeno;
    }

    public Gsx$homeno getGsx$homeno() {
        return gsx$homeno;
    }

    public void setGsx$homeno(Gsx$homeno gsx$homeno) {
        this.gsx$homeno = gsx$homeno;
    }

    public Gsx$floor getGsx$floor() {
        return gsx$floor;
    }

    public void setGsx$floor(Gsx$floor gsx$floor) {
        this.gsx$floor = gsx$floor;
    }

    public Gsx$deskno getGsx$deskno() {
        return gsx$deskno;
    }

    public void setGsx$deskno(Gsx$deskno gsx$deskno) {
        this.gsx$deskno = gsx$deskno;
    }

    public Gsx$paname getGsx$paname() {
        return gsx$paname;
    }

    public void setGsx$paname(Gsx$paname gsx$paname) {
        this.gsx$paname = gsx$paname;
    }

    public Gsx$pano getGsx$pano() {
        return gsx$pano;
    }

    public void setGsx$pano(Gsx$pano gsx$pano) {
        this.gsx$pano = gsx$pano;
    }

}

package com.example.mockitomadeclear.chap05;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WikiPage {
    @JsonIgnore
    private int pageid;
    @JsonIgnore
    private int ns;
    private String title;
    private String extract;

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public int getNs() {
        return ns;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    @Override
    public String toString() {
        return "WikiPage{" +
            "pageid=" + pageid +
            ", ns=" + ns +
            ", title='" + title + '\'' +
            ", extract='" + extract + '\'' +
            '}';
    }
}

package com.example.apach.parser;

/**
 * Created by apach on 01.10.16.
 */

public class JSONData {
    private String type;
    private String title;
    private String url;
    private String desc;

    public JSONData() {
    }

    public JSONData(String type, String title, String url, String desc){
        super();
        this.type = type;
        this.title = title;
        this.url  = url;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

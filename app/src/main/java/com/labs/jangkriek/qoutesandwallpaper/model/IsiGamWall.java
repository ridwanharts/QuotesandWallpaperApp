package com.labs.jangkriek.qoutesandwallpaper.model;

import com.google.firebase.database.Exclude;

public class IsiGamWall {

    @Exclude
    public String id;

    public String title, desc, url, url1;

    @Exclude
    public String category;

    @Exclude
    public boolean isFav = false;


    public IsiGamWall(String id, String title, String desc, String url, String url1, String category) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.url1 = url1;
        this.category = category;
    }
}

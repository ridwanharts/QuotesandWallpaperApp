package com.labs.jangkriek.qoutesandwallpaper.model;

import com.google.firebase.database.Exclude;

public class Wallpaper {

    @Exclude
    public String id;

    public String title, desc, url, ig, fb;

    @Exclude
    public String category;

    @Exclude
    public boolean isFav = false;


    public Wallpaper(String id, String title, String desc, String ig, String fb, String url, String category) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.ig = ig;
        this.fb = fb;
        this.url = url;
        this.category = category;
    }

}

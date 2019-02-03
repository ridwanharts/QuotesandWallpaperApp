package com.labs.jangkriek.qoutesandwallpaper.model;

import com.google.firebase.database.Exclude;

public class IsiHadist {

    @Exclude
    public String id;

    public String noHadist, judulHadist, hadistDari, hadistIsi, terjDari, terIsi, faedah;

    @Exclude
    public String category;

    @Exclude
    public boolean isFav = false;


    public IsiHadist(String id, String noHadist, String judulHadist, String hadistDari, String hadistIsi, String terjDari, String terIsi, String faedah, String category) {
        this.id = id;
        this.noHadist = noHadist;
        this.judulHadist = judulHadist;
        this.hadistDari = hadistDari;
        this.hadistIsi = hadistIsi;
        this.terjDari = terjDari;
        this.terIsi = terIsi;
        this.faedah = faedah;
        this.category = category;
    }

}

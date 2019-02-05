package com.labs.jangkriek.qoutesandwallpaper.model;

import com.google.firebase.database.Exclude;

public class IsiHadist {

    @Exclude
    public String id;

    public String no_hadist, judul, hadist_dr, hadist_isi, terj_dr, terj_isi, faedah;

    @Exclude
    public String category;

    @Exclude
    public boolean isFav = false;


    public IsiHadist(String id, String no_hadist, String judul, String hadist_dr, String hadist_isi, String terj_dr, String terj_isi, String faedah, String category) {
        this.id = id;
        this.no_hadist = no_hadist;
        this.judul = judul;
        this.hadist_dr = hadist_dr;
        this.hadist_isi = hadist_isi;
        this.terj_dr = terj_dr;
        this.terj_isi = terj_isi;
        this.faedah = faedah;
        this.category = category;
    }

}

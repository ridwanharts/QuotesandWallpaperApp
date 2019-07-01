package com.labs.jangkriek.qoutesandwallpaper.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.Utility;
import com.labs.jangkriek.qoutesandwallpaper.adapter.GamWallAdapter;
import com.labs.jangkriek.qoutesandwallpaper.adapter.QuoteAdapter;
import com.labs.jangkriek.qoutesandwallpaper.model.IsiGamWall;
import com.labs.jangkriek.qoutesandwallpaper.model.IsiQuote;

import java.util.ArrayList;
import java.util.List;

public class DetilCatGamWallActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<IsiGamWall> isiQuoteList;
    List<IsiGamWall> favList;
    GamWallAdapter adapter;
    DatabaseReference dbWallpaper, dbFav;
    ProgressBar progressBar;
    ImageView ivLogo;
    int mNoOfColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_cat_gam_wall);

        Toolbar toolbar = findViewById(R.id.gm_toolbar_wall);
        // toolbar
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //IKLAN
        AdView adView = findViewById(R.id.adView1);
        adView.loadAd(new AdRequest.Builder().build());

        Intent intent = getIntent();
        final String category = intent.getStringExtra("category");
        final String thumb = intent.getStringExtra("logo");

        ivLogo = findViewById(R.id.gm_logo_detail);

        //Toast.makeText(DetilCatQuoteActivity.this, "link : " + thumb, Toast.LENGTH_SHORT).show();
        Glide.with(DetilCatGamWallActivity.this).asBitmap().load(thumb).into(ivLogo);


        toolbar.setTitle(category);
        TextView tvCatDetail = findViewById(R.id.gm_tv_cat_detil);
        tvCatDetail.setText(category.toUpperCase());

        isiQuoteList = new ArrayList<>();
        favList = new ArrayList<>();
        recyclerView = findViewById(R.id.gm_recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());

        recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
        adapter = new GamWallAdapter(this, isiQuoteList);
        progressBar = findViewById(R.id.gm_pb2);
        recyclerView.setAdapter(adapter);
        dbWallpaper = FirebaseDatabase.getInstance().getReference("GambarWall").child(category);
        progressBar.setVisibility(View.VISIBLE);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            dbFav = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("gamwall_favourites")
                    .child(category);
            getFavWallpaper(category);
        }else{
            getWallpaper(category);
        }

        //Membuat Event Pada Siklus Hidup Iklan
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                //Kode disini akan di eksekusi saat Iklan Ditutup
                //Toast.makeText(getApplicationContext(), "Iklan Dititup", Toast.LENGTH_SHORT).show();
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                //Kode disini akan di eksekusi saat Iklan Gagal Dimuat
                //Toast.makeText(getApplicationContext(), "Iklan Gagal Dimuat", Toast.LENGTH_SHORT).show();
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                //Kode disini akan di eksekusi saat Pengguna Meniggalkan Aplikasi/Membuka Aplikasi Lain
                //Toast.makeText(getApplicationContext(), "Iklan Ditinggalkan", Toast.LENGTH_SHORT).show();
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                //Kode disini akan di eksekusi saat Pengguna Mengklik Iklan
                //Toast.makeText(getApplicationContext(), "Iklan Diklik", Toast.LENGTH_SHORT).show();
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                //Kode disini akan di eksekusi saat Iklan Selesai Dimuat
                //Toast.makeText(getApplicationContext(), "Iklan Selesai Dimuat", Toast.LENGTH_SHORT).show();
                super.onAdLoaded();
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getFavWallpaper(final String category){

        dbFav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressBar.setVisibility(View.INVISIBLE);
                    for(DataSnapshot wallpaperSnapshot: dataSnapshot.getChildren()){

                        String id = wallpaperSnapshot.getKey();
                        String title = wallpaperSnapshot.child("title").getValue(String.class);
                        String desc = wallpaperSnapshot.child("desc").getValue(String.class);
                        String url = wallpaperSnapshot.child("url").getValue(String.class);
                        String url1 = wallpaperSnapshot.child("url1").getValue(String.class);


                        IsiGamWall wall = new IsiGamWall(id, title, desc, url, url1,  category);
                        favList.add(wall);

                    }
                    //adapter.notifyDataSetChanged();
                }
                getWallpaper(category);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getWallpaper(final String category){

        dbWallpaper.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressBar.setVisibility(View.INVISIBLE);
                    for(DataSnapshot wallpaperSnapshot: dataSnapshot.getChildren()){

                        String id = wallpaperSnapshot.getKey();
                        String title = wallpaperSnapshot.child("title").getValue(String.class);
                        String desc = wallpaperSnapshot.child("desc").getValue(String.class);
                        String url = wallpaperSnapshot.child("url").getValue(String.class);
                        String url1 = wallpaperSnapshot.child("url1").getValue(String.class);



                        IsiGamWall wall = new IsiGamWall(id, title, desc, url, url1, category);
                        if (isFav(wall)){
                            wall.isFav = true;

                        }
                        isiQuoteList.add(wall);

                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean isFav(IsiGamWall wall){
        for(IsiGamWall f : favList){
            if(f.id.equals(wall.id)){

                return true;
            }
        }
        return false;
    }
}

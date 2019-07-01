package com.labs.jangkriek.qoutesandwallpaper.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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
import com.labs.jangkriek.qoutesandwallpaper.adapter.QuoteAdapter;
import com.labs.jangkriek.qoutesandwallpaper.model.IsiQuote;

import java.util.ArrayList;
import java.util.List;

public class DetilCatQuoteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<IsiQuote> isiQuoteList;
    List<IsiQuote> favList;
    QuoteAdapter adapter;
    DatabaseReference dbWallpaper, dbFav;
    ProgressBar progressBar;
    ImageView ivLogo, ivIG, ivFB;
    int mNoOfColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_cat_quote);

        Toolbar toolbar = findViewById(R.id.toolbar_wall);
        // toolbar
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //IKLAN
        AdView adView = findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());

        Intent intent = getIntent();
        final String category = intent.getStringExtra("category");
        final String thumb = intent.getStringExtra("logo");
        final String ig = intent.getStringExtra("ig");
        final String fb = intent.getStringExtra("fb");

        ivLogo = findViewById(R.id.logo_detail);

        /*ivIG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ig.equals("#")){

                }else{
                    Toast.makeText(DetilCatQuoteActivity.this, "IG profile not available", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });*/

        /*ivFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fb.equals("#")){

                }else{
                    Toast.makeText(DetilCatQuoteActivity.this, "FB profile not available", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });*/

        //Toast.makeText(DetilCatQuoteActivity.this, "link : " + thumb, Toast.LENGTH_SHORT).show();
        Glide.with(DetilCatQuoteActivity.this).asBitmap().load(thumb).into(ivLogo);


        toolbar.setTitle(category);
        TextView tvCatDetail = findViewById(R.id.tv_cat_detil);
        tvCatDetail.setText(category.toUpperCase());

        isiQuoteList = new ArrayList<>();
        favList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());

        recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns){

        });
        adapter = new QuoteAdapter(this, isiQuoteList);
        progressBar = findViewById(R.id.pb2);
        recyclerView.setAdapter(adapter);
        dbWallpaper = FirebaseDatabase.getInstance().getReference("images").child(category);
        progressBar.setVisibility(View.VISIBLE);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            dbFav = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favourites")
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
                        String ig = wallpaperSnapshot.child("ig").getValue(String.class);
                        String fb = wallpaperSnapshot.child("fb").getValue(String.class);
                        String url = wallpaperSnapshot.child("url").getValue(String.class);


                        IsiQuote wall = new IsiQuote(id, title, desc, url, ig, fb,  category);
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
                        String ig = wallpaperSnapshot.child("ig").getValue(String.class);
                        String fb = wallpaperSnapshot.child("fb").getValue(String.class);
                        String url = wallpaperSnapshot.child("url").getValue(String.class);



                        IsiQuote wall = new IsiQuote(id, title, desc, ig, fb, url, category);
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

    private boolean isFav(IsiQuote wall){
        for(IsiQuote f : favList){
            if(f.id.equals(wall.id)){

                return true;
            }
        }
        return false;
    }
}

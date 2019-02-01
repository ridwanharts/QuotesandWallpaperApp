package com.labs.jangkriek.qoutesandwallpaper.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.Utility;
import com.labs.jangkriek.qoutesandwallpaper.adapter.WallpaperAdapter;
import com.labs.jangkriek.qoutesandwallpaper.model.Wallpaper;

import java.util.ArrayList;
import java.util.List;

public class WallpaperActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Wallpaper> wallpaperList;
    List<Wallpaper> favList;
    WallpaperAdapter adapter;
    DatabaseReference dbWallpaper, dbFav;
    ProgressBar progressBar;
    ImageView ivLogo, ivIG, ivFB;
    int mNoOfColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        Intent intent = getIntent();
        final String category = intent.getStringExtra("category");
        final String thumb = intent.getStringExtra("logo");
        final String ig = intent.getStringExtra("ig");
        final String fb = intent.getStringExtra("fb");

        ivLogo = findViewById(R.id.logo_detail);
        ivIG = findViewById(R.id.iv_source_ig);
        ivFB = findViewById(R.id.iv_source_fb);

        ivIG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ig.equals("#")){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("" + ig));
                    startActivity(intent);
                }else{
                    Toast.makeText(WallpaperActivity.this, "IG profile not available", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        ivFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fb.equals("#")){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("" + fb));
                    startActivity(intent);
                }else{
                    Toast.makeText(WallpaperActivity.this, "FB profile not available", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        //Toast.makeText(WallpaperActivity.this, "link : " + thumb, Toast.LENGTH_SHORT).show();
        Glide.with(WallpaperActivity.this).asBitmap().load(thumb).into(ivLogo);

        Toolbar toolbar = findViewById(R.id.toolbar_wall);
        toolbar.setTitle(category);
        setSupportActionBar(toolbar);
        TextView tvCatDetail = findViewById(R.id.tv_cat_detil);
        tvCatDetail.setText(category.toUpperCase());

        wallpaperList = new ArrayList<>();
        favList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());

        recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
        adapter = new WallpaperAdapter(this, wallpaperList);
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


                        Wallpaper wall = new Wallpaper(id, title, desc, url, ig, fb,  category);
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



                        Wallpaper wall = new Wallpaper(id, title, desc, ig, fb, url, category);
                        if (isFav(wall)){
                            wall.isFav = true;

                        }
                        wallpaperList.add(wall);

                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean isFav(Wallpaper wall){
        for(Wallpaper f : favList){
            if(f.id.equals(wall.id)){

                return true;
            }
        }
        return false;
    }
}

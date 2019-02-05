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
                    Toast.makeText(DetilCatQuoteActivity.this, "IG profile not available", Toast.LENGTH_SHORT)
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
                    Toast.makeText(DetilCatQuoteActivity.this, "FB profile not available", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        //Toast.makeText(DetilCatQuoteActivity.this, "link : " + thumb, Toast.LENGTH_SHORT).show();
        Glide.with(DetilCatQuoteActivity.this).asBitmap().load(thumb).into(ivLogo);


        toolbar.setTitle(category);
        TextView tvCatDetail = findViewById(R.id.tv_cat_detil);
        tvCatDetail.setText(category.toUpperCase());

        isiQuoteList = new ArrayList<>();
        favList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());

        recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
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

package com.labs.jangkriek.qoutesandwallpaper.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.adapter.HadistAdapter;
import com.labs.jangkriek.qoutesandwallpaper.model.IsiHadist;

import java.util.ArrayList;
import java.util.List;

public class DetilCatHadistActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<IsiHadist> wallpaperList;
    List<IsiHadist> favList;
    HadistAdapter adapter;
    DatabaseReference dbWallpaper, dbFav;
    ProgressBar progressBar;
    int mNoOfColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_cat_hadist);

        Toolbar toolbar = findViewById(R.id.toolbar_wall_hadist);
        // toolbar
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        final String category = intent.getStringExtra("category");
        final String desk = intent.getStringExtra("desk");

        //Toast.makeText(DetilCatQuoteActivity.this, "link : " + thumb, Toast.LENGTH_SHORT).show();

        toolbar.setTitle(category);
        TextView tvCatDetail = findViewById(R.id.tv_cat_detil_hadist);
        TextView tvCatDesk = findViewById(R.id.deskripsi_hadist);
        tvCatDetail.setText(category);
        tvCatDesk.setText(desk);

        wallpaperList = new ArrayList<>();
        favList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_hadist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNoOfColumns = UtilityHadist.calculateNoOfColumns(getApplicationContext());

        recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
        adapter = new HadistAdapter(this, wallpaperList);
        progressBar = findViewById(R.id.pb_cat_hadist);
        recyclerView.setAdapter(adapter);
        dbWallpaper = FirebaseDatabase.getInstance().getReference("hadist").child(category);
        progressBar.setVisibility(View.VISIBLE);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            dbFav = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("hadist_favourites")
                    .child(category);
            getFavWallpaper(category);
        }else{
            getWallpaper(category);
        }
    }

    public static class UtilityHadist {

        public static int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumns = (int) (dpWidth / 200);
            return noOfColumns;
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
                    for(DataSnapshot hadistSnapshot: dataSnapshot.getChildren()){

                        String id = hadistSnapshot.getKey();
                        String noHadist = hadistSnapshot.child("no_hadist").getValue(String.class);
                        String jHadist = hadistSnapshot.child("judul").getValue(String.class);
                        String hadistDari = hadistSnapshot.child("hadist_dr").getValue(String.class);
                        String hadistIsi = hadistSnapshot.child("hadist_isi").getValue(String.class);
                        String terjDari = hadistSnapshot.child("terj_dr").getValue(String.class);
                        String terjIsi = hadistSnapshot.child("terj_isi").getValue(String.class);
                        String faedah = hadistSnapshot.child("faedah").getValue(String.class);


                        IsiHadist had = new IsiHadist(id, noHadist, jHadist, hadistDari, hadistIsi, terjDari, terjIsi, faedah ,  category);
                        favList.add(had);

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
                    for(DataSnapshot hadistSnapshot: dataSnapshot.getChildren()){

                        String id = hadistSnapshot.getKey();
                        String noHadist = hadistSnapshot.child("no_hadist").getValue(String.class);
                        String jHadist = hadistSnapshot.child("judul").getValue(String.class);
                        String hadistDari = hadistSnapshot.child("hadist_dr").getValue(String.class);
                        String hadistIsi = hadistSnapshot.child("hadist_isi").getValue(String.class);
                        String terjDari = hadistSnapshot.child("terj_dr").getValue(String.class);
                        String terjIsi = hadistSnapshot.child("terj_isi").getValue(String.class);
                        String faedah = hadistSnapshot.child("faedah").getValue(String.class);

                        IsiHadist had = new IsiHadist(id, noHadist, jHadist, hadistDari, hadistIsi, terjDari, terjIsi, faedah ,  category);
                        if (isFav(had)){
                            had.isFav = true;

                        }
                        wallpaperList.add(had);

                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean isFav(IsiHadist wall){
        for(IsiHadist f : favList){
            if(f.id.equals(wall.id)){

                return true;
            }
        }
        return false;
    }
}

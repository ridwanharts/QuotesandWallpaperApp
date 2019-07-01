package com.labs.jangkriek.qoutesandwallpaper.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.labs.jangkriek.qoutesandwallpaper.AmalanRutinActivity;
import com.labs.jangkriek.qoutesandwallpaper.fragment.FavFragment;
import com.labs.jangkriek.qoutesandwallpaper.fragment.HomeFragment;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    TextView tvTitle;
    RelativeLayout rel;
    ImageView ivCeklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

        tvTitle = findViewById(R.id.titleAtas);
        ivCeklist = findViewById(R.id.ceklist);
        ivCeklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AmalanRutinActivity.class);
                startActivity(i);
            }
        });
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        showFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fr = new HomeFragment();
        rel = findViewById(R.id.relative_bg);
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                fr = new HomeFragment();
                tvTitle.setText("Quote Wallpaper Islam");
                //rel.setBackgroundResource(R.drawable.bggradient);
                break;
            case R.id.nav_fav:
                fr = new FavFragment();
                tvTitle.setText("Favourite");
                //rel.setBackgroundResource(R.drawable.bggradientfav);
                break;
            case R.id.nav_set:
                fr = new SettingFragment();
                tvTitle.setText("Setting");
                //root.setBackgroundColor((R.drawable.bggradient));
                break;
        }
        showFragment(fr);
        return true;
    }

    private void showFragment(Fragment fr){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fr).commit();
    }
}

package com.labs.jangkriek.qoutesandwallpaper.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.ads.MobileAds;
import com.labs.jangkriek.qoutesandwallpaper.fragment.FavFragment;
import com.labs.jangkriek.qoutesandwallpaper.fragment.HomeFragment;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this,
                "ca-app-pub-2732887939805010~5353004466");

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        showFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fr = new HomeFragment();
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                fr = new HomeFragment();
                break;
            case R.id.nav_fav:
                fr = new FavFragment();
                break;
            case R.id.nav_set:
                fr = new SettingFragment();
                break;
        }
        showFragment(fr);
        return true;
    }

    private void showFragment(Fragment fr){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fr).commit();
    }
}

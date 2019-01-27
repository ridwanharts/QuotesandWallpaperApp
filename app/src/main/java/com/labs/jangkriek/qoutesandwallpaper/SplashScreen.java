package com.labs.jangkriek.qoutesandwallpaper;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.labs.jangkriek.qoutesandwallpaper.activities.MainActivity;

public class SplashScreen extends AppCompatActivity {

    TextView tvQuotes, tvN, tvWall;
    ImageView ivLogo, ivSplash;
    Animation fromLeftToRight, riseShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        fromLeftToRight = AnimationUtils.loadAnimation(this, R.anim.from_left_to_right);
        riseShow = AnimationUtils.loadAnimation(this, R.anim.rises_show);

        Typeface fredoka = Typeface.createFromAsset(getAssets(), "fonts/Fredoka.ttf");
        Typeface monseratLight = Typeface.createFromAsset(getAssets(), "fonts/MontserratLight.ttf");
        Typeface monseratMedium = Typeface.createFromAsset(getAssets(), "fonts/MontserratMedium.ttf");
        Typeface monseratRegular = Typeface.createFromAsset(getAssets(), "fonts/MontserratRegular.ttf");

        tvQuotes = findViewById(R.id.txt_quotes);
        tvN = findViewById(R.id.txt_n);
        tvWall = findViewById(R.id.txt_wallpaper);
        ivLogo = findViewById(R.id.iv_logo);
        ivSplash = findViewById(R.id.iv_splash);

        //set font
        tvQuotes.setTypeface(monseratLight);
        tvN.setTypeface(monseratLight);
        tvWall.setTypeface(monseratLight);

        ivSplash.startAnimation(riseShow);
        ivLogo.startAnimation(riseShow);
        tvQuotes.setTranslationX(400);
        tvN.setTranslationX(400);
        tvWall.setTranslationX(400);

        tvQuotes.setAlpha(0);
        tvN.setAlpha(0);
        tvWall.setAlpha(0);

        tvQuotes.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500);
        tvN.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600);
        tvWall.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700);

        final Intent i = new Intent(this, MainActivity.class);
        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}

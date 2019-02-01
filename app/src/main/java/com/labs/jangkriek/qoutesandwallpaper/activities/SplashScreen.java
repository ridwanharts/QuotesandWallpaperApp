package com.labs.jangkriek.qoutesandwallpaper.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.activities.MainActivity;

public class SplashScreen extends AppCompatActivity {

    TextView tvQuotes, tvN, tvWall;
    ImageView ivLogo, ivSplash;
    Animation fromLeftToRight, riseShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        fromLeftToRight = AnimationUtils.loadAnimation(this, R.anim.from_left_to_right);
        riseShow = AnimationUtils.loadAnimation(this, R.anim.rises_show);

        Typeface fredoka = Typeface.createFromAsset(getAssets(), "fonts/Fredoka.ttf");

        tvQuotes = findViewById(R.id.txt_quotes);
        ivLogo = findViewById(R.id.iv_logo);

        //set font
        tvQuotes.setTypeface(fredoka);

        ivLogo.startAnimation(riseShow);

        final Intent i = new Intent(this, MainActivity.class);
        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();*/

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}

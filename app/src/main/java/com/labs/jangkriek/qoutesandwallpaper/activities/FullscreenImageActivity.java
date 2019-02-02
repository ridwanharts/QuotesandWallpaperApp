package com.labs.jangkriek.qoutesandwallpaper.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.labs.jangkriek.qoutesandwallpaper.R;

public class FullscreenImageActivity extends AppCompatActivity {

    private Uri mImageUri;
    ImageView fullScreenImageView;
    private String fav, quote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fullScreenImageView = findViewById(R.id.fullScreenImageView);

        Intent intent = getIntent();
        final String img = intent.getStringExtra("image");

        if (intent.getStringExtra("idActivity")!= null){
            quote = intent.getStringExtra("idActivity");
        }

        //Toast.makeText(getApplicationContext(),"" + quote, Toast.LENGTH_SHORT).show();
        mImageUri = Uri.parse(img);
        if (mImageUri != null) {

            Glide.with(this)
                    .load(mImageUri)
                    .into(fullScreenImageView);
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (quote.equals("quote")){
            intent = new Intent(FullscreenImageActivity.this,
                    DetilCatQuoteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else {
            intent = new Intent(FullscreenImageActivity.this,
                    MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }



    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.full_image_share, menu);

        MenuItem menuItem = menu.findItem(R.id.image_share_menu);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        shareActionProvider.setShareIntent(createShareIntent());
        return true;
    }*/



/*    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, mImageUri);
        return shareIntent;
    }

    @Override
    public boolean onLongClick(View v) {

        Intent shareIntent = createShareIntent();
        startActivity(Intent.createChooser(shareIntent, "send to"));
        return true;
    }*/
}

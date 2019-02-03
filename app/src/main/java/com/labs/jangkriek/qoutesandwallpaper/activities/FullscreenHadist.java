package com.labs.jangkriek.qoutesandwallpaper.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.labs.jangkriek.qoutesandwallpaper.R;

public class FullscreenHadist extends AppCompatActivity {

    private Uri mImageUri;
    private TextView noH, judulH, hDari, hIsi, tDari, tIsi, faedah;
    private String hadist;
    private ImageView fullScreenImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_hadist);

        Toolbar toolbar = findViewById(R.id.toolbar_d_had);
        // toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        noH = findViewById(R.id.detil_no_hadist);
        judulH = findViewById(R.id.judul);
        hDari = findViewById(R.id.detil_hadist_dari);
        hIsi = findViewById(R.id.detil_hadist_isi);
        tDari = findViewById(R.id.detil_hadist_terj_dari);
        tIsi = findViewById(R.id.detil_hadist_terj_isi);
        faedah = findViewById(R.id.detil_hadist_faedah);
        fullScreenImageView = findViewById(R.id.fullScreenImageView);

        Intent intent = getIntent();

        final String noH1 = intent.getStringExtra("NO_HADIST");
        final String judulH1 = intent.getStringExtra("JUDUL_HADIST");
        final String hDari1 = intent.getStringExtra("HADIST_DARI");
        final String hIsi1 = intent.getStringExtra("HADIST_ISI");
        final String tDari1 = intent.getStringExtra("TERJ_DARI");
        final String tIsi1 = intent.getStringExtra("TER_ISI");
        final String faedah1 = intent.getStringExtra("FAEDAH");

        noH.setText(noH1);
        judulH.setText(judulH1);
        hDari.setText(hDari1);
        hIsi.setText(hIsi1);
        tDari.setText(tDari1);
        tIsi.setText(tIsi1);
        faedah.setText(faedah1);

        if (intent.getStringExtra("idActivity")!= null){
            hadist = intent.getStringExtra("idActivity");
        }

        Toast.makeText(getApplicationContext(),"" + hadist, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (hadist.equals("hadist")){
            intent = new Intent(FullscreenHadist.this,
                    DetilCatHadistActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }else {
            intent = new Intent(FullscreenHadist.this,
                    MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }



    }
}

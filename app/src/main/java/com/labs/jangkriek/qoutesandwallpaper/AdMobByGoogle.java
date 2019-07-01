package com.labs.jangkriek.qoutesandwallpaper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdMobByGoogle {

    public class AdMob_By_Google extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_main);

            //Inisialisasi Banner com.labs.jangkriek.qoutesandwallpaper.AdMobByGoogle
            AdView adView = findViewById(R.id.adView);
            adView.loadAd(new AdRequest.Builder().build());

            //Membuat Event Pada Siklus Hidup Iklan
            adView.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    //Kode disini akan di eksekusi saat Iklan Ditutup
                    Toast.makeText(getApplicationContext(), "Iklan Dititup", Toast.LENGTH_SHORT).show();
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    //Kode disini akan di eksekusi saat Iklan Gagal Dimuat
                    Toast.makeText(getApplicationContext(), "Iklan Gagal Dimuat", Toast.LENGTH_SHORT).show();
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLeftApplication() {
                    //Kode disini akan di eksekusi saat Pengguna Meniggalkan Aplikasi/Membuka Aplikasi Lain
                    Toast.makeText(getApplicationContext(), "Iklan Ditinggalkan", Toast.LENGTH_SHORT).show();
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    //Kode disini akan di eksekusi saat Pengguna Mengklik Iklan
                    Toast.makeText(getApplicationContext(), "Iklan Diklik", Toast.LENGTH_SHORT).show();
                    super.onAdOpened();
                }

                @Override
                public void onAdLoaded() {
                    //Kode disini akan di eksekusi saat Iklan Selesai Dimuat
                    Toast.makeText(getApplicationContext(), "Iklan Selesai Dimuat", Toast.LENGTH_SHORT).show();
                    super.onAdLoaded();
                }

            });
        }
    }
}

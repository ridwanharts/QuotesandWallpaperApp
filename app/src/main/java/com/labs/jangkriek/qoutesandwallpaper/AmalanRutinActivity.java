package com.labs.jangkriek.qoutesandwallpaper;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.Calendar;

public class AmalanRutinActivity extends AppCompatActivity {

    CheckBox cb1, cb2, cb3, cb4, cb5, cb6;
    boolean cek;
    Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amalan_rutin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        // toolbar
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        cb1 = findViewById(R.id.checkBox1);
        cb2 = findViewById(R.id.checkBox2);
        cb3 = findViewById(R.id.checkBox3);
        cb4 = findViewById(R.id.checkBox4);
        cb5 = findViewById(R.id.checkBox5);
        cb6 = findViewById(R.id.checkBox6);
        reset = findViewById(R.id.reset);

        final String cbtext1 = cb1.getText().toString();
        final String cbtext2 = cb2.getText().toString();
        final String cbtext3 = cb3.getText().toString();
        final String cbtext4 = cb4.getText().toString();
        final String cbtext5 = cb5.getText().toString();
        final String cbtext6 = cb6.getText().toString();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        if(preferences.contains("checked") && preferences.getBoolean("checked",false) == true) {
            if ((cb1.getText().toString().equals(cbtext1))){
                cb1.setChecked(true);
            }else if ((cb2.getText().toString().equals(cbtext2))){
                cb2.setChecked(true);
            }else if ((cb3.getText().toString().equals(cbtext3))){
                cb3.setChecked(true);
            }else if ((cb4.getText().toString().equals(cbtext4))){
                cb4.setChecked(true);
            }else if ((cb5.getText().toString().equals(cbtext5))){
                cb5.setChecked(true);
            }else if ((cb6.getText().toString().equals(cbtext6))){
                cb6.setChecked(true);
            }
        }else {
            if ((cb1.getText().toString().equals(cbtext1))){
                cb1.setChecked(false);
            }else if ((cb2.getText().toString().equals(cbtext2))){
                cb2.setChecked(false);
            }else if ((cb3.getText().toString().equals(cbtext3))){
                cb3.setChecked(false);
            }else if ((cb4.getText().toString().equals(cbtext4))){
                cb4.setChecked(false);
            }else if ((cb5.getText().toString().equals(cbtext5))){
                cb5.setChecked(false);
            }else if ((cb6.getText().toString().equals(cbtext6))){
                cb6.setChecked(false);
            }

        }


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetDialog();
            }
        });
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb1.isChecked()){
                    showPopDialog(cbtext1);
                    editor.putBoolean("checked", true);
                    editor.apply();
                }else{
                    editor.putBoolean("checked", false);
                    editor.apply();
                }
            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (cb2.isChecked()){
                    showPopDialog(cbtext2);
                    editor.putBoolean("checked", true);
                    editor.apply();
                }else{
                    editor.putBoolean("checked", false);
                    editor.apply();
                }
            }
        });

        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb3.isChecked()){
                    showPopDialog(cbtext3);
                    editor.putBoolean("checked", true);
                    editor.apply();
                }else{
                    editor.putBoolean("checked", false);
                    editor.apply();
                }
            }
        });

        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb4.isChecked()){
                    showPopDialog(cbtext4);
                    editor.putBoolean("checked", true);
                    editor.apply();
                }else{
                    editor.putBoolean("checked", false);
                    editor.apply();
                }
            }
        });

        cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (cb5.isChecked()){
                    showPopDialog(cbtext5);
                    editor.putBoolean("checked", true);
                    editor.apply();
                }else{
                    editor.putBoolean("checked", false);
                    editor.apply();
                }
            }
        });

        cb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (cb6.isChecked()){
                    showPopDialog(cbtext6);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showPopDialog(final String text) {
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        new AlertDialog.Builder(this).setMessage("Kamu yakin sudah melakukan " + text.toUpperCase() + " hari ini ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        if ((cb1.getText().toString().equals(text))){
                            cb1.setChecked(true);
                        }else if ((cb2.getText().toString().equals(text))){
                            cb2.setChecked(true);
                        }else if ((cb3.getText().toString().equals(text))){
                            cb3.setChecked(true);
                        }else if ((cb4.getText().toString().equals(text))){
                            cb4.setChecked(true);
                        }else if ((cb5.getText().toString().equals(text))){
                            cb5.setChecked(true);
                        }else if ((cb6.getText().toString().equals(text))){
                            cb6.setChecked(true);
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        if ((cb1.getText().toString().equals(text))){
                            cb1.setChecked(false);
                        }else if ((cb2.getText().toString().equals(text))){
                            cb2.setChecked(false);
                        }else if ((cb3.getText().toString().equals(text))){
                            cb3.setChecked(false);
                        }else if ((cb4.getText().toString().equals(text))){
                            cb4.setChecked(false);
                        }else if ((cb5.getText().toString().equals(text))){
                            cb5.setChecked(false);
                        }else if ((cb6.getText().toString().equals(text))){
                            cb6.setChecked(false);
                        }
                    }
                })
                .show();
    }

    private void showResetDialog() {
        new AlertDialog.Builder(this).setMessage("Kamu yakin reset semua ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        cb1.setChecked(false);
                        cb2.setChecked(false);
                        cb3.setChecked(false);
                        cb4.setChecked(false);
                        cb5.setChecked(false);
                        cb6.setChecked(false);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {

                    }
                })
                .show();
    }
}

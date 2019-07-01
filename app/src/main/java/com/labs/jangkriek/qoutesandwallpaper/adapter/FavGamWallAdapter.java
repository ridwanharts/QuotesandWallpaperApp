package com.labs.jangkriek.qoutesandwallpaper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.labs.jangkriek.qoutesandwallpaper.BuildConfig;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.activities.FullscreenGamWallActivity;
import com.labs.jangkriek.qoutesandwallpaper.activities.FullscreenImageActivity;
import com.labs.jangkriek.qoutesandwallpaper.model.IsiGamWall;
import com.labs.jangkriek.qoutesandwallpaper.model.IsiQuote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class FavGamWallAdapter extends RecyclerView.Adapter<FavGamWallAdapter.WallViewHolder> {

    private Context context;
    private List<IsiGamWall> isiGamWallList;
    private ProgressBar progressBar;
    private InterstitialAd mInterstitialAd;

    public FavGamWallAdapter(Context context, List<IsiGamWall> isiGamWallList) {
        this.context = context;
        this.isiGamWallList = isiGamWallList;

    }

    @Override
    public FavGamWallAdapter.WallViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_fav_gamwall, viewGroup, false);
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        return new FavGamWallAdapter.WallViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FavGamWallAdapter.WallViewHolder categoryViewHolder, int i) {
        IsiGamWall wall = isiGamWallList.get(i);
        categoryViewHolder.tvName.setText(wall.title);
        RequestOptions myOptions = new RequestOptions()
                .centerCrop();
        Glide.with(context).asBitmap().apply(myOptions).load(wall.url).into(categoryViewHolder.ivThumb);

        if (wall.isFav){
            categoryViewHolder.checkBox.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return isiGamWallList.size();
    }

    public class WallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView tvName;
        ImageView ivThumb;
        CheckBox checkBox;
        ImageButton buttonShare, buttonDown;

        public WallViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.gamwall_fav_txt_view_title);
            ivThumb = itemView.findViewById(R.id.gamwall_fav_iv_imagevwall);
            checkBox = itemView.findViewById(R.id.gamwall_fav_fav_button);
            buttonShare = itemView.findViewById(R.id.gamwall_fav_share_button);
            buttonDown = itemView.findViewById(R.id.gamwall_fav_download_button);

            checkBox.setOnCheckedChangeListener(this);
            buttonDown.setOnClickListener(this);
            buttonShare.setOnClickListener(this);
            ivThumb.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            int pos = getAdapterPosition();
            IsiGamWall img = isiGamWallList.get(pos);

            switch(v.getId()){
                case R.id.gamwall_fav_share_button:
                    shareWallpaper(isiGamWallList.get(getAdapterPosition()));
                    Toast.makeText(context, "Share quote", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.gamwall_fav_download_button:
                    downloadWallpaper(isiGamWallList.get(getAdapterPosition()));
                    Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                    break;
                case R.id.gamwall_fav_iv_imagevwall:
                    Intent i = new Intent(context, FullscreenGamWallActivity.class);
                    i.putExtra("image", img.url);
                    i.putExtra("imagefull", img.url1);
                    i.putExtra("idActivity", "fav");
                    context.startActivity(i);
                    break;
            }



        }

        private void shareWallpaper(IsiGamWall w) {
            RequestOptions myOptions = new RequestOptions()
                    .centerCrop();
            Glide.with(context).asBitmap().apply(myOptions).load(w.url1)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("image/*");
                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource));
                            context.startActivity(Intent.createChooser(i, "Sobat Hijrah"));

                        }
                    });

        }

        private void downloadWallpaper(final IsiGamWall isiQuote){

            RequestOptions myOptions = new RequestOptions()
                    .centerCrop();
            Glide.with(context).asBitmap().apply(myOptions).load(isiQuote.url1)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse(saveWallpaper(resource, isiQuote.id));
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            if (uri != null) {
                                //saveWallpaper(resource, isiQuote.id);
                                i.setDataAndType(Uri.parse(saveWallpaper(resource, isiQuote.id)), "image/*");
                                //context.startActivity(Intent.createChooser(i, "Quotes App"));
                            }

                        }
                    });
        }

        private String saveWallpaper(Bitmap bitmap, String id){

            if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    i.setData(uri);
                    context.startActivity(i);

                }else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
                return null;
            }

            String savedImagePath = null;

            String imageFileName = "JPEG_" + id + ".jpg";
            File storageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            + "/SobatHijrah/Wallpaper");
            boolean success = true;
            if (!storageDir.exists()) {
                success = storageDir.mkdirs();
            }
            if (success) {
                File imageFile = new File(storageDir, imageFileName);
                savedImagePath = imageFile.getAbsolutePath();
                try {
                    OutputStream fOut = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Add the image to the system gallery
                galleryAddPic(savedImagePath);
            }
            return savedImagePath;
        }

        private void galleryAddPic(String imagePath) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(imagePath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        }

        private Uri getLocalBitmapUri(Bitmap bmp){
            Uri bmpUri = null;
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "images_wallpaper_"+System.currentTimeMillis() + ".png");
            try {
                FileOutputStream out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmpUri;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(FirebaseAuth.getInstance().getCurrentUser()==null){
                Toast.makeText(context, "Please login first...", Toast.LENGTH_SHORT).show();
                buttonView.setChecked(false);
                return;
            }

            int position = getAdapterPosition();
            IsiGamWall wall = isiGamWallList.get(position);

            DatabaseReference dbFav = FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("gamwall_favourites")
                    .child(wall.category);


            if(isChecked){
                dbFav.child(wall.id).setValue(wall);

            }else {
                dbFav.child(wall.id).setValue(null);
                Toast.makeText(context, "remove from favourite", Toast.LENGTH_SHORT).show();

            }


        }
    }
}

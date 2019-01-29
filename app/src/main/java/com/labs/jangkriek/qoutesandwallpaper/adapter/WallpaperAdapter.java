package com.labs.jangkriek.qoutesandwallpaper.adapter;

import android.Manifest;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.labs.jangkriek.qoutesandwallpaper.BuildConfig;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.model.Category;
import com.labs.jangkriek.qoutesandwallpaper.model.Wallpaper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallViewHolder> {

    private Context context;
    private List<Wallpaper> wallpaperList;
    private ProgressBar progressBar;

    public WallpaperAdapter(Context context, List<Wallpaper> wallpaperList) {
        this.context = context;
        this.wallpaperList = wallpaperList;

    }

    @Override
    public WallpaperAdapter.WallViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_wallpaper, viewGroup, false);
        return new WallpaperAdapter.WallViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WallpaperAdapter.WallViewHolder categoryViewHolder, int i) {
        Wallpaper wall = wallpaperList.get(i);
        categoryViewHolder.tvName.setText(wall.title);
        Glide.with(context).load(wall.url).into(categoryViewHolder.ivThumb);

        if (wall.isFav){
            categoryViewHolder.checkBox.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class WallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView tvName;
        ImageView ivThumb;
        CheckBox checkBox;
        ImageButton buttonShare, buttonDown;

        public WallViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_view_title);
            ivThumb = itemView.findViewById(R.id.iv_imagevwall);
            checkBox = itemView.findViewById(R.id.fav_button);
            buttonShare = itemView.findViewById(R.id.share_button);
            buttonDown = itemView.findViewById(R.id.download_button);

            checkBox.setOnCheckedChangeListener(this);
            buttonDown.setOnClickListener(this);
            buttonShare.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.share_button:
                    shareWallpaper(wallpaperList.get(getAdapterPosition()));
                    break;
                case R.id.download_button:
                    downloadWallpaper(wallpaperList.get(getAdapterPosition()));
                    break;
            }

        }

        private void shareWallpaper(Wallpaper wallpaper) {
            progressBar = (ProgressBar)((Activity) context).findViewById(R.id.pb2);
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(context).asBitmap().load(wallpaper.url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            progressBar.setVisibility(View.GONE);
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("image/*");
                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource));
                            context.startActivity(Intent.createChooser(i, "Quotes App"));

                        }
                    });
        }

        private void downloadWallpaper(final Wallpaper wallpaper){
            progressBar = (ProgressBar)((Activity) context).findViewById(R.id.pb2);
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(context).asBitmap().load(wallpaper.url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            progressBar.setVisibility(View.GONE);
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            Uri uri = saveWallpaperGetUri(resource, wallpaper.id);
                            if (uri!=null){
                                i.setDataAndType(uri, "image/*");
                                context.startActivity(Intent.createChooser(i, "Quotes App"));
                            }
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    });
        }


        private Uri saveWallpaperGetUri(Bitmap bitmap, String id){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    i.setData(uri);
                    context.startActivity(i);

                }else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
                return null;
            }

            File folder = new File(Environment.getExternalStorageDirectory().toString()+"/Download");
            folder.mkdirs();
            Date currentTime = Calendar.getInstance().getTime();
            File file = new File(folder, "images_quote" + currentTime + ".jpg");
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private Uri getLocalBitmapUri(Bitmap bmp){
            Uri bmpUri = null;
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "images_quotes_"+System.currentTimeMillis() + ".png");
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
            Wallpaper wall = wallpaperList.get(position);

            DatabaseReference dbFav = FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favourites")
                    .child(wall.category);


            if(isChecked){
                dbFav.child(wall.id).setValue(wall);
            }else {
                dbFav.child(wall.id).setValue(null);
            }

        }
    }
}

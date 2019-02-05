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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.labs.jangkriek.qoutesandwallpaper.BuildConfig;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.activities.FullscreenImageActivity;
import com.labs.jangkriek.qoutesandwallpaper.model.IsiQuote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.WallViewHolder> {

    private Context context;
    private List<IsiQuote> isiQuoteList;
    private ProgressBar progressBar;

    public FavAdapter(Context context, List<IsiQuote> isiQuoteList) {
        this.context = context;
        this.isiQuoteList = isiQuoteList;

    }

    @Override
    public FavAdapter.WallViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_fav_quotes, viewGroup, false);
        return new FavAdapter.WallViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FavAdapter.WallViewHolder categoryViewHolder, int i) {
        IsiQuote wall = isiQuoteList.get(i);
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
        return isiQuoteList.size();
    }

    public class WallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView tvName;
        ImageView ivThumb;
        CheckBox checkBox;
        ImageButton buttonShare, buttonDown;

        public WallViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.fav_txt_view_title);
            ivThumb = itemView.findViewById(R.id.fav_iv_imagevwall);
            checkBox = itemView.findViewById(R.id.fav_fav_button);
            buttonShare = itemView.findViewById(R.id.fav_share_button);
            buttonDown = itemView.findViewById(R.id.fav_download_button);

            checkBox.setOnCheckedChangeListener(this);
            buttonDown.setOnClickListener(this);
            buttonShare.setOnClickListener(this);
            ivThumb.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.fav_share_button:
                    shareWallpaper(isiQuoteList.get(getAdapterPosition()));
                    Toast.makeText(context, "Share quote", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fav_download_button:
                    downloadWallpaper(isiQuoteList.get(getAdapterPosition()));
                    Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fav_fav_button:
                        Toast.makeText(context, "Add to favourite", Toast.LENGTH_SHORT).show();
                    break;
            }

            int pos = getAdapterPosition();
            IsiQuote img = isiQuoteList.get(pos);
            Intent i = new Intent(context, FullscreenImageActivity.class);
            i.putExtra("image", img.url);
            i.putExtra("idActivity", "fav");
            context.startActivity(i);

        }

        private void shareWallpaper(IsiQuote w) {
            RequestOptions myOptions = new RequestOptions()
                    .centerCrop();
            Glide.with(context).asBitmap().apply(myOptions).load(w.url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("image/*");
                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource));
                            context.startActivity(Intent.createChooser(i, "Quotes App"));

                        }
                    });
        }

        private void downloadWallpaper(final IsiQuote isiQuote){

            RequestOptions myOptions = new RequestOptions()
                    .centerCrop();
            Glide.with(context).asBitmap().apply(myOptions).load(isiQuote.url)
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
                            + "/QuotesApp");
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
            IsiQuote wall = isiQuoteList.get(position);

            DatabaseReference dbFav = FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favourites")
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

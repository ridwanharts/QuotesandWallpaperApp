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
import com.labs.jangkriek.qoutesandwallpaper.activities.FullscreenHadist;
import com.labs.jangkriek.qoutesandwallpaper.activities.FullscreenImageActivity;
import com.labs.jangkriek.qoutesandwallpaper.model.IsiHadist;
import com.labs.jangkriek.qoutesandwallpaper.model.Wallpaper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class FavHadistAdapter extends RecyclerView.Adapter<FavHadistAdapter.WallViewHolder> {

    private Context context;
    private List<IsiHadist> hadistList;
    private ProgressBar progressBar;

    public FavHadistAdapter(Context context, List<IsiHadist> hadistList) {
        this.context = context;
        this.hadistList = hadistList;

    }

    @Override
    public FavHadistAdapter.WallViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_fav_hadist, viewGroup, false);
        return new FavHadistAdapter.WallViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FavHadistAdapter.WallViewHolder categoryViewHolder, int i) {
        IsiHadist wall = hadistList.get(i);
        IsiHadist had = hadistList.get(i);
        int j = i+1;
        categoryViewHolder.tvNo.setText(j +".");
        categoryViewHolder.tvNoHadist.setText(had.noHadist);
        categoryViewHolder.tvJudulHadist.setText(had.judulHadist);
        //categoryViewHolder.tvHadistDari.setText(had.hadistDari);
        //categoryViewHolder.tvHadistIsi.setText(had.hadistIsi);
        categoryViewHolder.tvTerjDari.setText(had.terjDari);
        //categoryViewHolder.tvFaedah.setText(had.faedah);


        if (wall.isFav){
            categoryViewHolder.checkBox.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return hadistList.size();
    }

    public class WallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        TextView tvNo, tvNoHadist, tvJudulHadist, tvHadistDari, tvHadistIsi, tvTerjDari, tvTerjIsi, tvFaedah;
        CheckBox checkBox;
        ImageButton buttonShare, buttonCopy;

        public WallViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNo = itemView.findViewById(R.id.fav_tvNomor);
            tvTerjDari = itemView.findViewById(R.id.fav_hadist_ter_dari);
            tvNoHadist = itemView.findViewById(R.id.fav_noHadist);
            tvJudulHadist = itemView.findViewById(R.id.fav_txt_view_judul_hadist);

            checkBox = itemView.findViewById(R.id.fav_fav_button_hadist);
            buttonShare = itemView.findViewById(R.id.fav_share_button_hadist);
            buttonCopy = itemView.findViewById(R.id.fav_copy_button_hadist);

            checkBox.setOnCheckedChangeListener(this);
            buttonCopy.setOnClickListener(this);
            buttonShare.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.fav_share_button:
                    /*shareWallpaper(wallpaperList.get(getAdapterPosition()));*/
                    Toast.makeText(context, "Share quote", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fav_download_button:
                    /*downloadWallpaper(wallpaperList.get(getAdapterPosition()));*/
                    Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.fav_fav_button:
                        Toast.makeText(context, "Add to favourite", Toast.LENGTH_SHORT).show();
                    break;
            }

            int pos = getAdapterPosition();
            IsiHadist had = hadistList.get(pos);

            Intent i = new Intent(context, FullscreenHadist.class);
            i.putExtra("NO_HADIST", had.noHadist);
            i.putExtra("JUDUL_HADIST", had.judulHadist);
            i.putExtra("HADIST_DARI", had.hadistDari);
            i.putExtra("HADIST_ISI", had.hadistIsi);
            i.putExtra("TERJ_DARI", had.terjDari);
            i.putExtra("TER_ISI", had.terIsi);
            i.putExtra("FAEDAH", had.faedah);
            i.putExtra("idActivity", "hadist");
            context.startActivity(i);

        }

        /*private void shareWallpaper(Wallpaper w) {
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
        }*/

        /*private void downloadWallpaper(final Wallpaper wallpaper){

            RequestOptions myOptions = new RequestOptions()
                    .centerCrop();
            Glide.with(context).asBitmap().apply(myOptions).load(wallpaper.url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse(saveWallpaper(resource, wallpaper.id));
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            if (uri != null) {
                                //saveWallpaper(resource, wallpaper.id);
                                i.setDataAndType(Uri.parse(saveWallpaper(resource, wallpaper.id)), "image/*");
                                //context.startActivity(Intent.createChooser(i, "Quotes App"));
                            }

                        }
                    });
        }*/

        /*private String saveWallpaper(Bitmap bitmap, String id){

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
        }*/

        /*private void galleryAddPic(String imagePath) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(imagePath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        }*/

        /*private Uri getLocalBitmapUri(Bitmap bmp){
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
        }*/

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(FirebaseAuth.getInstance().getCurrentUser()==null){
                Toast.makeText(context, "Please login first...", Toast.LENGTH_SHORT).show();
                buttonView.setChecked(false);
                return;
            }

            int position = getAdapterPosition();
            IsiHadist had = hadistList.get(position);

            DatabaseReference dbFav = FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("hadist_favourites")
                    .child(had.category);


            if(isChecked){
                dbFav.child(had.id).setValue(had);
            }else {
                dbFav.child(had.id).setValue(null);
                Toast.makeText(context, "remove from favourite", Toast.LENGTH_SHORT).show();
            }

        }
    }
}

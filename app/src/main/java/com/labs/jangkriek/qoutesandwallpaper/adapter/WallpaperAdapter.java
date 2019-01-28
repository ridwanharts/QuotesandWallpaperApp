package com.labs.jangkriek.qoutesandwallpaper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.model.Category;
import com.labs.jangkriek.qoutesandwallpaper.model.Wallpaper;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallViewHolder> {

    private Context context;
    private List<Wallpaper> wallpaperList;

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

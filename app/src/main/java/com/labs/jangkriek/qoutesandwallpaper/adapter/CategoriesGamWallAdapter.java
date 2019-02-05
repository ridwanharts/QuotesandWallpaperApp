package com.labs.jangkriek.qoutesandwallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.activities.DetilCatQuoteActivity;
import com.labs.jangkriek.qoutesandwallpaper.model.GamWall;
import com.labs.jangkriek.qoutesandwallpaper.model.Quote;

import java.util.List;

public class CategoriesGamWallAdapter extends RecyclerView.Adapter<CategoriesGamWallAdapter.CategoryViewHolder> {

    private Context context;
    private List<GamWall> gamWallList;
    private InterstitialAd mInterstitialAd;

    public CategoriesGamWallAdapter(Context context, List<GamWall> gamWallList) {
        this.context = context;
        this.gamWallList = gamWallList;

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public CategoriesGamWallAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_cat_quote, viewGroup, false);
        return new CategoriesGamWallAdapter.CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoriesGamWallAdapter.CategoryViewHolder categoryViewHolder, int i) {
        GamWall cat = gamWallList.get(i);
        categoryViewHolder.tvName.setText(cat.name);
        RequestOptions myOptions = new RequestOptions()
                .centerCrop();
        Glide.with(context).asBitmap().apply(myOptions).load(cat.thumb).into(categoryViewHolder.ivThumb);

    }

    @Override
    public int getItemCount() {
        return gamWallList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        ImageView ivThumb;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_view_cat_name);
            ivThumb = itemView.findViewById(R.id.iv_imagevcat);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            /*if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }*/

            int pos = getAdapterPosition();
            GamWall cat = gamWallList.get(pos);
            Intent i = new Intent(context, DetilCatQuoteActivity.class);

            i.putExtra("category", cat.name);
            i.putExtra("logo", cat.thumb);
            context.startActivity(i);
        }
    }
}

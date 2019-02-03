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
import com.labs.jangkriek.qoutesandwallpaper.activities.DetilCatHadistActivity;
import com.labs.jangkriek.qoutesandwallpaper.activities.DetilCatQuoteActivity;
import com.labs.jangkriek.qoutesandwallpaper.model.Category;
import com.labs.jangkriek.qoutesandwallpaper.model.Hadist;

import java.util.List;

public class CategoriesHadistAdapter extends RecyclerView.Adapter<CategoriesHadistAdapter.CategoryViewHolder> {

    private Context context;
    private List<Hadist> hadistList;
    private InterstitialAd mInterstitialAd;

    public CategoriesHadistAdapter(Context context, List<Hadist> hadists) {
        this.context = context;
        this.hadistList = hadists;

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public CategoriesHadistAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_cat_hadist, viewGroup, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoriesHadistAdapter.CategoryViewHolder categoryViewHolder, int i) {
        Hadist cat = hadistList.get(i);
        categoryViewHolder.tvName.setText(cat.jHadist);

    }

    @Override
    public int getItemCount() {
        return hadistList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        ImageView ivThumb;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_view_cat_hadist);
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
            Hadist cat = hadistList.get(pos);
            Intent i = new Intent(context, DetilCatHadistActivity.class);

            i.putExtra("category", cat.jHadist);
            context.startActivity(i);
        }
    }
}

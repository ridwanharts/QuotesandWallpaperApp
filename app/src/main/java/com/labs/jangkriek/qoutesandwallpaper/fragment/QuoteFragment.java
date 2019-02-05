package com.labs.jangkriek.qoutesandwallpaper.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.Utility;
import com.labs.jangkriek.qoutesandwallpaper.adapter.CategoriesQuoteAdapter;
import com.labs.jangkriek.qoutesandwallpaper.model.Quote;

import java.util.ArrayList;
import java.util.List;

public class QuoteFragment extends Fragment {

    private ProgressBar progressBar;
    private DatabaseReference dbCategories;
    private List<Quote> quoteList;
    private RecyclerView recyclerView;
    private CategoriesQuoteAdapter categoriesQuoteAdapter;
    private int mNoOfColumns;

    public QuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quote, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view);

        mNoOfColumns = Utility.calculateNoOfColumns(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mNoOfColumns));

        quoteList = new ArrayList<>();
        categoriesQuoteAdapter = new CategoriesQuoteAdapter(getActivity(), quoteList);
        recyclerView.setAdapter(categoriesQuoteAdapter);

        dbCategories = FirebaseDatabase.getInstance().getReference("categories");
        dbCategories.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getKey();
                        String desc = ds.child("desc").getValue(String.class);
                        String thumb = ds.child("thumbnail").getValue(String.class);
                        String ig = ds.child("ig").getValue(String.class);
                        String fb = ds.child("fb").getValue(String.class);

                        Quote cat = new Quote(name, desc, thumb, ig, fb);
                        quoteList.add(cat);
                    }
                    categoriesQuoteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

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
import com.labs.jangkriek.qoutesandwallpaper.adapter.CategoriesHadistAdapter;
import com.labs.jangkriek.qoutesandwallpaper.model.Hadist;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BelajarFragment extends Fragment {


    private ProgressBar progressBar;
    private DatabaseReference dbCategories;
    private List<Hadist> hadistList;
    private RecyclerView recyclerView;
    private CategoriesHadistAdapter categoriesAdapter;
    private int mNoOfColumns;

    public BelajarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_belajar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.pb_cat_hadist);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view_cat_hadist);

        mNoOfColumns = Utility.calculateNoOfColumns(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mNoOfColumns));

        hadistList = new ArrayList<>();
        categoriesAdapter = new CategoriesHadistAdapter(getActivity(), hadistList);
        recyclerView.setAdapter(categoriesAdapter);

        dbCategories = FirebaseDatabase.getInstance().getReference("cat_hadist");
        dbCategories.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getKey();
                        String desc = ds.child("desc").getValue(String.class);

                        Hadist cat = new Hadist(name, desc);
                        hadistList.add(cat);
                    }
                    categoriesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

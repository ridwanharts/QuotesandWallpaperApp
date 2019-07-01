package com.labs.jangkriek.qoutesandwallpaper.fragment.inFavFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.adapter.FavHadistAdapter;
import com.labs.jangkriek.qoutesandwallpaper.fragment.SettingFragment;
import com.labs.jangkriek.qoutesandwallpaper.model.IsiHadist;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavHadistFragment extends Fragment {

    List<IsiHadist> favWallpaper;
    RecyclerView recyclerView;
    ProgressBar pb;
    FavHadistAdapter adapter;
    DatabaseReference dbFav;
    int mNoOfColumns;

    public FavHadistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_belajar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favWallpaper = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view_fav_hadist);
        pb = view.findViewById(R.id.pb_fav_hadist);
        adapter = new FavHadistAdapter(getActivity(), favWallpaper);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNoOfColumns = UtilityHadist.calculateNoOfColumns(getActivity());

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mNoOfColumns));
        recyclerView.setAdapter(adapter);

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, new SettingFragment())
                    .commit();
            return;
        }

        dbFav = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("hadist_favourites");

        pb.setVisibility(View.VISIBLE);

        dbFav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pb.setVisibility(View.INVISIBLE);
                for(DataSnapshot cat : dataSnapshot.getChildren()){
                    for(DataSnapshot hadistSnapshot : cat.getChildren()){
                        String id = hadistSnapshot.getKey();
                        String noHadist = hadistSnapshot.child("no_hadist").getValue(String.class);
                        String jHadist = hadistSnapshot.child("judul").getValue(String.class);
                        String hadistDari = hadistSnapshot.child("hadist_dr").getValue(String.class);
                        String hadistIsi = hadistSnapshot.child("hadist_isi").getValue(String.class);
                        String terjDari = hadistSnapshot.child("terj_dr").getValue(String.class);
                        String terjIsi = hadistSnapshot.child("terj_isi").getValue(String.class);
                        String faedah = hadistSnapshot.child("faedah").getValue(String.class);

                        IsiHadist had = new IsiHadist(id, noHadist, jHadist, hadistDari, hadistIsi, terjDari, terjIsi, faedah,  cat.getKey());
                        had.isFav = true;
                        favWallpaper.add(had);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static class UtilityHadist {

        public static int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumns = (int) (dpWidth / 200);
            return noOfColumns;
        }
    }

}

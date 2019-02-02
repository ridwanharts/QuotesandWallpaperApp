package com.labs.jangkriek.qoutesandwallpaper.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.labs.jangkriek.qoutesandwallpaper.FavHadistFragment;
import com.labs.jangkriek.qoutesandwallpaper.FavQuoteFragment;
import com.labs.jangkriek.qoutesandwallpaper.FavQuranFragment;
import com.labs.jangkriek.qoutesandwallpaper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        showFragment(new QuoteFragment());
        return inflater.inflate(R.layout.parent_fragment_fav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = view.findViewById(R.id.topNavFav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        showFragment(new FavQuoteFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fr = new FavQuoteFragment();
        switch (menuItem.getItemId()){
            case R.id.nav_quote:
                fr = new FavQuoteFragment();
                break;
            case R.id.nav_quran:
                fr = new FavQuranFragment();
                break;
            case R.id.nav_hadist:
                fr = new FavHadistFragment();
                break;
        }
        showFragment(fr);
        return true;
    }

    private void showFragment(Fragment fr){
        //getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fr).commit();
        //getChildFragmentManager().beginTransaction().replace(R.id.content_main, fr).commit();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fr).commit();
    }

    /*List<Wallpaper> favWallpaper;
    RecyclerView recyclerView;
    ProgressBar pb;
    QuoteAdapter adapter;
    DatabaseReference dbFav;

    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.parent_fragment_fav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favWallpaper = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view3);
        pb = view.findViewById(R.id.pb3);
        adapter = new QuoteAdapter(getActivity(), favWallpaper);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                .child("favourites");

        pb.setVisibility(View.VISIBLE);

        dbFav.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pb.setVisibility(View.INVISIBLE);
                for(DataSnapshot cat : dataSnapshot.getChildren()){
                    for(DataSnapshot wall : cat.getChildren()){
                        String id = wall.getKey();
                        String title = wall.child("title").getValue(String.class);
                        String desc = wall.child("desc").getValue(String.class);
                        String ig = wall.child("ig").getValue(String.class);
                        String fb = wall.child("fb").getValue(String.class);
                        String url = wall.child("url").getValue(String.class);

                        Wallpaper w = new Wallpaper(id, title, desc, ig, fb, url, cat.getKey());
                        w.isFav = true;
                        favWallpaper.add(w);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }*/
}

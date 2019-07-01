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

import com.labs.jangkriek.qoutesandwallpaper.R;
import com.labs.jangkriek.qoutesandwallpaper.fragment.inHomeFragment.GamWallFragment;
import com.labs.jangkriek.qoutesandwallpaper.fragment.inHomeFragment.HadistFragment;
import com.labs.jangkriek.qoutesandwallpaper.fragment.inHomeFragment.QuoteFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        showFragment(new QuoteFragment());
        return inflater.inflate(R.layout.parent_fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        BottomNavigationView bottomNavigationView;
        bottomNavigationView = view.findViewById(R.id.topNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        showFragment(new QuoteFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fr = new QuoteFragment();
        switch (menuItem.getItemId()){
            case R.id.nav_quote:
                fr = new QuoteFragment();
                break;
            case R.id.nav_quran:
                fr = new GamWallFragment();
                break;
            case R.id.nav_hadist:
                fr = new HadistFragment();
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

    /*private ProgressBar progressBar;
    private DatabaseReference dbCategories;
    private List<Quote> categoryList;
    private RecyclerView recyclerView;
    private CategoriesQuoteAdapter categoriesAdapter;
    private int mNoOfColumns;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.parent_fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = view.findViewById(R.id.recycler_view);

        mNoOfColumns = Utility.calculateNoOfColumns(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mNoOfColumns));

        categoryList = new ArrayList<>();
        categoriesAdapter = new CategoriesQuoteAdapter(getActivity(), categoryList);
        recyclerView.setAdapter(categoriesAdapter);

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
                        categoryList.add(cat);
                    }
                    categoriesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}

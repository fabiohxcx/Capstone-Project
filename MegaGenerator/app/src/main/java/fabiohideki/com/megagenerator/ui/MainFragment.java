package fabiohideki.com.megagenerator.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fabiohideki.com.megagenerator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    BottomNavigationView navigation;

    FragmentTransaction transaction;

    public MainFragment() {
        // Required empty public constructor
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();

                    HomeFragment homeFragment = new HomeFragment();

                    transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_inner, homeFragment); // replace a Fragment with Frame Layout
                    transaction.commit(); // commit the changes
                    return true;
                case R.id.navigation_notifications:
                    Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();

                    NewsFragment newsFragment = new NewsFragment();

                    transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_inner, newsFragment); // replace a Fragment with Frame Layout
                    transaction.commit(); // commit the changes

                    return true;
            }
            return false;
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        navigation = rootView.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        HomeFragment homeFragment = new HomeFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_inner, homeFragment); // replace a Fragment with Frame Layout
        transaction.commit(); // commit the changes

        super.onViewCreated(view, savedInstanceState);
    }
}

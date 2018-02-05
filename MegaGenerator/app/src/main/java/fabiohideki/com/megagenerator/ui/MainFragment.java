package fabiohideki.com.megagenerator.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private FragmentTransaction transaction;

    private Fragment mFragment;

    private final String TAG_FRAGMENT_HOME = "frag_home";
    private final String TAG_FRAGMENT_NEWS = "frag_news";

    public MainFragment() {
        // Required empty public constructor
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_HOME) != null) {
                        //if the fragment exists, show it.
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(TAG_FRAGMENT_HOME)).commit();
                    } else {
                        //if the fragment does not exist, add it to fragment manager.
                        fragmentManager.beginTransaction().add(R.id.frame_inner, new HomeFragment(), TAG_FRAGMENT_HOME).commit();
                    }
                    if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_NEWS) != null) {
                        //if the other fragment is visible, hide it.
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(TAG_FRAGMENT_NEWS)).commit();
                    }
                    break;

                case R.id.navigation_notifications:

                    if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_NEWS) != null) {
                        //if the fragment exists, show it.
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(TAG_FRAGMENT_NEWS)).commit();
                    } else {
                        //if the fragment does not exist, add it to fragment manager.
                        fragmentManager.beginTransaction().add(R.id.frame_inner, new NewsFragment(), TAG_FRAGMENT_NEWS).commit();
                    }
                    if (fragmentManager.findFragmentByTag(TAG_FRAGMENT_HOME) != null) {
                        //if the other fragment is visible, hide it.
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(TAG_FRAGMENT_HOME)).commit();
                    }

                    break;
            }

            return true;
        }

    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Log.d("Fabio", "onCreateView: MainFrag");

        if (savedInstanceState == null) {

            Log.d("Fabio", "savedInstanceState null: MainFrag");

            mFragment = new HomeFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_inner, mFragment, TAG_FRAGMENT_HOME); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes
        }

        Log.d("Fabio", "onActivityCreated: MainFrag");

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}

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

    private final String TAG_FRAGMENT = "tag";

    public MainFragment() {
        // Required empty public constructor
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment currentFragment = getFragmentManager().findFragmentByTag(TAG_FRAGMENT);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (!(currentFragment instanceof HomeFragment)) mFragment = new HomeFragment();
                    break;

                case R.id.navigation_notifications:
                    if (!(currentFragment instanceof NewsFragment)) mFragment = new NewsFragment();
                    break;
            }

            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_inner, mFragment, TAG_FRAGMENT); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes

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

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            mFragment = new HomeFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_inner, mFragment, TAG_FRAGMENT); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes
        }

        super.onViewCreated(view, savedInstanceState);
    }
}

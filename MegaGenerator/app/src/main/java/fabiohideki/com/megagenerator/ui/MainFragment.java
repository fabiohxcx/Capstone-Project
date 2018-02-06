package fabiohideki.com.megagenerator.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.custom.CustomViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindString(R.string.title_home)
    String mTitleHome;

    @BindString(R.string.title_news)
    String mTitleNews;

    @BindView(R.id.viewpager)
    CustomViewPager mViewPager;


    private FragmentTransaction transaction;

    private Fragment mFragment;

    private final String TAG_FRAGMENT_HOME = "frag_home";
    private final String TAG_FRAGMENT_NEWS = "frag_news";

    public MainFragment() {
        // Required empty public constructor
    }

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

/*
        Log.d("Fabio", "onCreateView: MainFrag");

        if (savedInstanceState == null) {

            Log.d("Fabio", "savedInstanceState null: MainFrag");

            mFragment = new HomeFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_inner, mFragment, TAG_FRAGMENT_HOME); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes
        }

        Log.d("Fabio", "onActivityCreated: MainFrag");*/

        setupViewPager(mViewPager);
        mViewPager.setPagingEnabled(false);

        return rootView;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        HomeFragment homeFragment = new HomeFragment();
        adapter.addFragment(homeFragment, mTitleHome);

        NewsFragment newsFragment = new NewsFragment();
        adapter.addFragment(newsFragment, mTitleNews);

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    break;

                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(1);
                    break;
            }

            return true;
        }

    };

}

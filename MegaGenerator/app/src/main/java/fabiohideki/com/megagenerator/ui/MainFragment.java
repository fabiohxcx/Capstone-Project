package fabiohideki.com.megagenerator.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.adapter.ViewPagerAdapter;
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

    private HomeFragment mHomeFragment;
    private NewsFragment mNewsFragment;

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
        setFragmentTitleInActionBar(getActivity(), getString(R.string.title_home));

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setupViewPager(mViewPager);
        mViewPager.setPagingEnabled(false);

        return rootView;
    }

    private void setFragmentTitleInActionBar(Activity activity, String title) {
        if (activity != null)
            ((AppCompatActivity) activity).getSupportActionBar().setTitle(title);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mViewPager != null && getActivity() != null) {
            if (mViewPager.getCurrentItem() == 0) {
                setFragmentTitleInActionBar(getActivity(), getString(R.string.title_home));
            } else if (mViewPager.getCurrentItem() == 1) {
                setFragmentTitleInActionBar(getActivity(), getString(R.string.title_news));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewPager != null && getActivity() != null) {
            if (mViewPager.getCurrentItem() == 0) {
                setFragmentTitleInActionBar(getActivity(), getString(R.string.title_home));
            } else if (mViewPager.getCurrentItem() == 1) {
                setFragmentTitleInActionBar(getActivity(), getString(R.string.title_news));
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            mHomeFragment.setRetainInstance(true);
        }

        adapter.addFragment(mHomeFragment, mTitleHome);

        if (mNewsFragment == null) {
            mNewsFragment = new NewsFragment();
            mNewsFragment.setRetainInstance(true);
        }
        adapter.addFragment(mNewsFragment, mTitleNews);

        viewPager.setAdapter(adapter);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    setFragmentTitleInActionBar(getActivity(), getString(R.string.title_home));
                    break;

                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(1);
                    setFragmentTitleInActionBar(getActivity(), getString(R.string.title_news));
                    break;
            }

            return true;
        }

    };

}

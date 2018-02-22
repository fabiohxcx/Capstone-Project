package fabiohideki.com.megagenerator.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.BuildConfig;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.adapter.ViewPagerStateAdapter;
import fabiohideki.com.megagenerator.custom.CustomViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.main_viewpager)
    CustomViewPager mViewPager;

    private ViewPagerStateAdapter adapter = new ViewPagerStateAdapter(getSupportFragmentManager());

    private MainFragment mMainFragment;
    private HistoryResultFragment mHistoryResultFragment;
    private NearbyLotteryFragment mNearByLotteryFragment;
    private GeneratorFragment mGeneratorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Fabio", "MainActivity: onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        setupViewPager();
        mViewPager.setPagingEnabled(false);
        mViewPager.setOffscreenPageLimit(1);

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mViewPager.setCurrentItem(0);

        } else if (id == R.id.nav_generator) {
            mViewPager.setCurrentItem(1);
            getSupportActionBar().setTitle(getString(R.string.number_generator));

        } else if (id == R.id.nav_results) {
            mViewPager.setCurrentItem(2);
            getSupportActionBar().setTitle(getString(R.string.results));

        } else if (id == R.id.nav_near_lottery) {
            mViewPager.setCurrentItem(3);
            getSupportActionBar().setTitle(getString(R.string.near_lottery));

        } else if (id == R.id.nav_how_to_play) {
            mViewPager.setCurrentItem(4);
            getSupportActionBar().setTitle(getString(R.string.how_to_play));

        } else if (id == R.id.nav_price_prob) {
            mViewPager.setCurrentItem(5);
            getSupportActionBar().setTitle(getString(R.string.prices_probability));

        } else if (id == R.id.nav_about) {

            int year = Calendar.getInstance().get(Calendar.YEAR);
            String versionName = BuildConfig.VERSION_NAME;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(getString(R.string.about));

            ssBuilder.setSpan(
                    foregroundColorSpan,
                    0,
                    getString(R.string.about).length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            builder.setTitle(ssBuilder);

            AlertDialog alertDialog = builder.create();

            alertDialog.setMessage(Html.fromHtml("<p>" + getString(R.string.version_app) + " " + versionName + " - " + year + "</p>"
                    + getString(R.string.about_content)));

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();

            ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager() {

        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
            mMainFragment.setRetainInstance(true);
        }
        adapter.addFragment(mMainFragment);

        if (mGeneratorFragment == null) {
            mGeneratorFragment = new GeneratorFragment();
            mGeneratorFragment.setRetainInstance(true);
        }
        adapter.addFragment(mGeneratorFragment);

        if (mHistoryResultFragment == null) {
            mHistoryResultFragment = new HistoryResultFragment();
            mHistoryResultFragment.setRetainInstance(true);
        }
        adapter.addFragment(mHistoryResultFragment);

        if (mNearByLotteryFragment == null) {
            mNearByLotteryFragment = new NearbyLotteryFragment();
            mNearByLotteryFragment.setRetainInstance(true);
        }
        adapter.addFragment(mNearByLotteryFragment);

        HowToPlayFragment howToPlayFragment = new HowToPlayFragment();
        adapter.addFragment(howToPlayFragment);

        PricesFragment pricesFragment = new PricesFragment();
        adapter.addFragment(pricesFragment);

        mViewPager.setAdapter(adapter);
    }

}

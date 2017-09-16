package com.onevest.dev.tulung.main.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.main.adapter.MainAdapter;
import com.onevest.dev.tulung.main.fragments.AccountFragment;
import com.onevest.dev.tulung.main.fragments.TimelineFragment;
import com.onevest.dev.tulung.main.fragments.WarningFragment;
import com.onevest.dev.tulung.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //region BindView
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.container)
    ViewPager viewPager;

    @BindView(R.id.fab)
    FloatingActionMenu fab;

    @BindView(R.id.general_fab)
    FloatingActionButton generalFab;

    @OnClick(R.id.general_fab)
    public void generalHandler() {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(Constants.CATEGORY, Constants.GENERAL);
        fab.close(true);
        startActivity(intent);
    }

    @BindView(R.id.failed_fab)
    FloatingActionButton failedFab;

    @OnClick(R.id.failed_fab)
    public void failedHandler() {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(Constants.CATEGORY, Constants.FAILED);
        fab.close(true);
        startActivity(intent);
    }

    @BindView(R.id.fuel_fab)
    FloatingActionButton fuelFab;

    @OnClick(R.id.fuel_fab)
    public void fuelHandler() {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(Constants.CATEGORY, Constants.FUEL);
        fab.close(true);
        startActivity(intent);
    }

    @BindView(R.id.wheel_fab)
    FloatingActionButton wheelFab;

    @OnClick(R.id.wheel_fab)
    public void wheelHandler() {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(Constants.CATEGORY, Constants.WHEEL);
        fab.close(true);
        startActivity(intent);
    }
    //endregion

    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_warning,
            R.drawable.ic_account};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fab.setVisibility(View.GONE);
                        break;
                    case 2:
                        fab.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fab.setVisibility(View.GONE);
                        break;
                    case 2:
                        fab.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        adapter.addFragment(new TimelineFragment(), "Timeline");
        adapter.addFragment(new WarningFragment(), "Warning");
        adapter.addFragment(new AccountFragment(), "Account");
        viewPager.setAdapter(adapter);
    }

//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

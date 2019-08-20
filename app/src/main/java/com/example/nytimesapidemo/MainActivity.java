package com.example.nytimesapidemo;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {
    private ViewPager viewPager;
    private MEmailedFragment mefFragment;
    private MSharedFragment msFragment;
    private MViewedFragment mvFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager_id);
        TabLayout tabLayout = findViewById(R.id.table_layout);
        mefFragment = MEmailedFragment.getInstance();
        msFragment = MSharedFragment.getInstance();
        mvFragment = MViewedFragment.getInstance();
        this.addPages();

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);





    }

    private void addPages() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(mefFragment,"most emailed");
        adapter.addFragment(msFragment,"most shared");
        adapter.addFragment(mvFragment,"most viewed");

        viewPager.setAdapter(adapter);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}

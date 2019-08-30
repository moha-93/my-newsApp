package com.moha.nytimesapp.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.moha.nytimesapp.fragment.MEmailedFragment;
import com.moha.nytimesapp.fragment.MSharedFragment;
import com.moha.nytimesapp.fragment.MViewedFragment;
import com.moha.nytimesapp.R;
import com.moha.nytimesapp.adapter.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity  {
    private ViewPager viewPager;
    private MEmailedFragment mefFragment;
    private MSharedFragment msFragment;
    private MViewedFragment mvFragment;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ImageView imageView;
    private String uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager_id);
         tabLayout = findViewById(R.id.table_layout);
        mefFragment = MEmailedFragment.getInstance();
        msFragment = MSharedFragment.getInstance();
        mvFragment = MViewedFragment.getInstance();
         addPages();



         uri = "https://cdn.pixabay.com/photo/2016/11/29/13/17/breakfast-1869772_960_720.jpg";
         imageView = findViewById(R.id.img_tb);
        Picasso.get().load(uri).fit().into(imageView);



    }

    private void addPages() {
        adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(mefFragment, "most emailed");
        adapter.addFragment(msFragment, "most shared");
        adapter.addFragment(mvFragment, "most viewed");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }



}

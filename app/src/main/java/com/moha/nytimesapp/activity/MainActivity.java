package com.moha.nytimesapp.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.moha.nytimesapp.modelDB.FavoriteViewModel;
import com.moha.nytimesapp.fragment.MEmailedFragment;
import com.moha.nytimesapp.fragment.MSharedFragment;
import com.moha.nytimesapp.fragment.MViewedFragment;
import com.moha.nytimesapp.R;
import com.moha.nytimesapp.adapter.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;


public class MainActivity extends AppCompatActivity implements HasAndroidInjector {
    private ViewPager viewPager;
    private MEmailedFragment meFragment;
    private MSharedFragment msFragment;
    private MViewedFragment mvFragment;
    private TabLayout tabLayout;
    private FavoriteViewModel viewModel;

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager_id);
        tabLayout = findViewById(R.id.table_layout);
        meFragment = MEmailedFragment.getInstance();
        msFragment = MSharedFragment.getInstance();
        mvFragment = MViewedFragment.getInstance();
        addPages();
        ImageView imageView = findViewById(R.id.img_tb);
        Picasso.get().load(getResources().getString(R.string.img_url)).fit().into(imageView);
        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
    }

    private void addPages() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(meFragment, "most emailed");
        adapter.addFragment(msFragment, "most shared");
        adapter.addFragment(mvFragment, "most viewed");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorites_layout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.fav_list) {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}

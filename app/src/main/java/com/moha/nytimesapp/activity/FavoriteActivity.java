package com.moha.nytimesapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moha.nytimesapp.modelDB.Favorite;
import com.moha.nytimesapp.modelDB.FavoriteViewModel;
import com.moha.nytimesapp.adapter.FavoriteAdapter;
import com.moha.nytimesapp.R;
import com.moha.nytimesapp.utility.ChromeUtils;
import com.moha.nytimesapp.utility.RecycleItemTouchHelper;

import java.util.ArrayList;
import java.util.List;


public class FavoriteActivity extends AppCompatActivity implements FavoriteAdapter.OnItemClickListener, RecycleItemTouchHelper.RecycleItemTouchListener {
    FavoriteViewModel viewModel;
    private RecyclerView recyclerView;
    private List<Favorite> favoriteList = new ArrayList<>();
    private TextView emptyView;
    FavoriteAdapter adapter;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar_2nd);
        toolbar.setTitle("Favorite List");
        setSupportActionBar(toolbar);

        rootLayout=findViewById(R.id.root_layout);
        emptyView = findViewById(R.id.txt_empty_view);
        recyclerView = findViewById(R.id.favorite_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper.SimpleCallback simpleCallback = new RecycleItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        loadFavoriteItems();

    }

    private void loadFavoriteItems() {
        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        viewModel.getAllFavorites().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(@Nullable List<Favorite> favorites) {
                displayFavoriteItems(favorites);
            }
        });
    }

    private void displayFavoriteItems(List<Favorite> favorites) {
         adapter = new FavoriteAdapter(this, favorites);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(this);
        favoriteList = favorites;
        if (favorites.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

    }

    @Override
    public void OnItemClick(int position) {
        Favorite favorite = favoriteList.get(position);
        String url = favorite.getWebUrl();
        ChromeUtils.launchChromeTabs(url, FavoriteActivity.this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.getAllFavorites().removeObservers(this);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof FavoriteAdapter.FavoriteViewHolder){
            final Favorite deletedItem = favoriteList.get(viewHolder.getAdapterPosition());
            final int deletedIndex= viewHolder.getAdapterPosition();
            // Delete item from adapter
            adapter.removeItem(deletedIndex);
            // Delete item from Room database
            FavoriteViewModel.delete(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout, "Item removed from favorites list",
            Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deletedItem,deletedIndex);
                    FavoriteViewModel.insert(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW).show();

        }

    }
}


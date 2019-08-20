package com.example.nytimesapidemo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private ArticleAdapter adapter;
    List<Article> articlesList;
    private FavoriteDbHelper dbHelper;
    public boolean isDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        dbHelper = new FavoriteDbHelper(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Favorite list");
        }


        RecyclerView recyclerView = findViewById(R.id.favorite_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articlesList = new ArrayList<>();
        adapter = new ArticleAdapter(articlesList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getOfflineArticles();



    }

    @SuppressLint("StaticFieldLeak")
    private void getOfflineArticles() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                articlesList.clear();
                dbHelper.getArticles();
                articlesList.addAll(ArticleAdapter.offlineArticles);

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isDestroy) {
            getOfflineArticles();
        }
    }


}

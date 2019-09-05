package com.moha.nytimesapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.moha.nytimesapp.modal.Article;
import com.moha.nytimesapp.adapter.ArticleAdapter;
import com.moha.nytimesapp.database.FavoriteDbHelper;
import com.moha.nytimesapp.R;
import com.moha.nytimesapp.utility.ChromeUtils;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements ArticleAdapter.OnItemClickListener {
    private ArticleAdapter adapter;
    List<Article> articlesList;
    private FavoriteDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = findViewById(R.id.toolbar_2nd);
        toolbar.setTitle("Favorite List");
        setSupportActionBar(toolbar);

        dbHelper = new FavoriteDbHelper(this);
        RecyclerView recyclerView = findViewById(R.id.favorite_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articlesList = new ArrayList<>();
        adapter = new ArticleAdapter(articlesList, this);
        getOfflineArticles();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

    }

    @SuppressLint("StaticFieldLeak")
    private void getOfflineArticles() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                ArticleAdapter.offlineArticles.addAll(dbHelper.getArticles());
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
    public void OnItemClick(int position) {
        Article articles = articlesList.get(position);
        String url = articles.getWebUrl();
        ChromeUtils.launchChromeTabs(url,FavoriteActivity.this);
    }
}


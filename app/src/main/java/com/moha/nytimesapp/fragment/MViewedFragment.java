package com.moha.nytimesapp.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.moha.nytimesapp.BuildConfig;
import com.moha.nytimesapp.utility.NetworkUtils;
import com.moha.nytimesapp.R;
import com.moha.nytimesapp.activity.FavoriteActivity;
import com.moha.nytimesapp.activity.WebActivity;
import com.moha.nytimesapp.adapter.ArticleAdapter;
import com.moha.nytimesapp.modal.Article;
import com.moha.nytimesapp.modal.Response;
import com.moha.nytimesapp.rest.ApiClient;
import com.moha.nytimesapp.rest.nyTimesAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;


public class MViewedFragment extends Fragment implements ArticleAdapter.OnItemClickListener {

    public static final String API_KEY = BuildConfig.ApiKey;
    private ArrayList<Article> arrayList;
    private List<Article> articleList;
    protected FragmentActivity mActivity;
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    StaggeredGridLayoutManager mLayoutManager;
    private boolean isDark = false;
    @SuppressLint("StaticFieldLeak")
    static MViewedFragment instance;

    public MViewedFragment() {
        // Required empty public constructor
    }

    public static MViewedFragment getInstance() {
        if (instance == null) {
            instance = new MViewedFragment();

        }
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mviewed, container, false);
        coordinatorLayout = view.findViewById(R.id.mv_content);
        FloatingActionButton actionButton = view.findViewById(R.id.btnDM_mv);
        FloatingActionButton btnAdd_favorite = view.findViewById(R.id.btn_mv_favorite);
        recyclerView = view.findViewById(R.id.mvRecycler_view);
        recyclerView.setHasFixedSize(true);
        setOrientation();

        if (savedInstanceState != null) {
            arrayList = savedInstanceState.getParcelableArrayList("articles");
        } else {
            arrayList = new ArrayList<>();
        }

        if (NetworkUtils.isNetworkAvailable(mActivity)) {
            loadJson();

        } else {
            Toast.makeText(mActivity, "No connection...", Toast.LENGTH_SHORT).show();
        }

        isDark = getThemeStatePref();
        if (isDark) {

            coordinatorLayout.setBackgroundColor(getResources().getColor(R.color.black));

        } else {
            coordinatorLayout.setBackgroundColor(getResources().getColor(R.color.white));

        }


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDark = !isDark;
                if (isDark) {

                    coordinatorLayout.setBackgroundColor(getResources().getColor(R.color.black));


                } else {
                    coordinatorLayout.setBackgroundColor(getResources().getColor(R.color.white));

                }
                setOrientation();
                adapter = new ArticleAdapter(articleList, mActivity, isDark);
                recyclerView.setAdapter(adapter);
                saveThemeStatePref(isDark);
                adapter.setOnItemClickListener(MViewedFragment.this);
            }
        });

        btnAdd_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoriteIntent = new Intent(getContext(), FavoriteActivity.class);
                startActivity(favoriteIntent);
            }
        });


        return view;
    }

    private void loadJson() {
        nyTimesAPI timesAPI = ApiClient.getRetrofit().create(nyTimesAPI.class);
        Call<Response> call = timesAPI.getViewed(1, API_KEY);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        articleList = response.body().getArticles();
                        arrayList.addAll(articleList);
                        adapter = new ArticleAdapter(arrayList, mActivity, isDark);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyItemRangeInserted(adapter.getItemCount(), arrayList.size() - 1);
                        adapter.setOnItemClickListener(MViewedFragment.this);


                    }
                } else {
                    Snackbar.make(coordinatorLayout, "Error: " + response.message(),
                            Snackbar.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
                Snackbar.make(coordinatorLayout, t.getMessage(), Snackbar.LENGTH_LONG).show();

            }
        });

    }

    private void setOrientation() {
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                break;

        }
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void saveThemeStatePref(boolean isDark) {

        SharedPreferences pref = mActivity.getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isDark", isDark);
        editor.apply();
    }

    private boolean getThemeStatePref() {

        SharedPreferences pref = mActivity.getSharedPreferences("myPref", MODE_PRIVATE);
        return pref.getBoolean("isDark", false);

    }

    @Override
    public void OnItemClick(int position) {
        Intent webIntent = new Intent(mActivity, WebActivity.class);
        Article articles = articleList.get(position);
        webIntent.putExtra("url", articles.getWebUrl());
        startActivity(webIntent);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelableArrayList("articles", arrayList);
    }

}

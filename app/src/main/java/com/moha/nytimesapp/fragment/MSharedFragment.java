package com.moha.nytimesapp.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.moha.nytimesapp.utility.ChromeUtils;
import com.moha.nytimesapp.utility.NetworkUtils;
import com.moha.nytimesapp.R;
import com.moha.nytimesapp.activity.FavoriteActivity;
import com.moha.nytimesapp.adapter.ArticleAdapter;
import com.moha.nytimesapp.modal.Article;
import com.moha.nytimesapp.modal.Response;
import com.moha.nytimesapp.rest.ApiClient;
import com.moha.nytimesapp.rest.nyTimesAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


import static android.content.Context.MODE_PRIVATE;


public class MSharedFragment extends Fragment implements ArticleAdapter.OnItemClickListener {

    private List<Article> articleList;
    private ArrayList<Article> arrayList;
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;
    public static final String API_KEY = BuildConfig.ApiKey;
    @SuppressLint("StaticFieldLeak")
    public static MSharedFragment instance;
    public FragmentActivity mActivity;
    private CoordinatorLayout coordinatorLayout;
    StaggeredGridLayoutManager mLayoutManager;
    boolean isDark = false;
    CompositeDisposable disposable = new CompositeDisposable();


    public MSharedFragment() {
        // Required empty public constructor
    }

    public static synchronized MSharedFragment getInstance() {
        if (instance == null) {
            instance = new MSharedFragment();

        }
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mshared, container, false);
        coordinatorLayout = view.findViewById(R.id.msf_content);
        FloatingActionButton btn_darkMode = view.findViewById(R.id.btnDM_msh);
        FloatingActionButton btnAdd_favorite = view.findViewById(R.id.btn_msh_favorite);
        recyclerView = view.findViewById(R.id.mvRecycler_view);
        recyclerView.setHasFixedSize(true);
        setLayoutManager();

        // Check InstanceState
        if (savedInstanceState != null) {
            arrayList = savedInstanceState.getParcelableArrayList("articles");
        } else {
            arrayList = new ArrayList<>();
        }

     //---------------------------------------------------------------------------------------------
        // Check Internet connection and fetching the data
        if (NetworkUtils.isNetworkAvailable(mActivity)) {
            fetchData();
        } else {
            Toast.makeText(mActivity, "No connection...", Toast.LENGTH_SHORT).show();
        }

     //---------------------------------------------------------------------------------------------
        // Define and check for Dark mode state preference
        isDark = getThemeStatePref();
        if (isDark) {

            coordinatorLayout.setBackgroundColor(getResources().getColor(R.color.black));

        } else {
            coordinatorLayout.setBackgroundColor(getResources().getColor(R.color.white));

        }

     //---------------------------------------------------------------------------------------------
        // Button set for Dark mode
        btn_darkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDark = !isDark;
                if (isDark) {

                    coordinatorLayout.setBackgroundColor(getResources().getColor(R.color.black));


                } else {
                    coordinatorLayout.setBackgroundColor(getResources().getColor(R.color.white));

                }
                setLayoutManager();
                adapter = new ArticleAdapter(articleList, mActivity, isDark);
                recyclerView.setAdapter(adapter);
                saveThemeStatePref(isDark);
                adapter.setOnItemClickListener(MSharedFragment.this);
            }
        });

        //---------------------------------------------------------------------------------------------
        //Set button for favorite list
        btnAdd_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoriteIntent = new Intent(getContext(), FavoriteActivity.class);
                startActivity(favoriteIntent);
            }
        });


        return view;

    }

    //Get and display the data from the server
    private void fetchData() {
        nyTimesAPI timesAPI = ApiClient.getRetrofit().create(nyTimesAPI.class);
        disposable.add(timesAPI.getSharedArticles(1, "facebook", API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {

                        try {

                            if (response != null && response.getArticles() != null) {
                                articleList = response.getArticles();
                                arrayList.addAll(articleList);
                                populateList(arrayList);
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            throw new Exception();
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Snackbar.make(coordinatorLayout, "HTTP Error: " +
                                throwable.getMessage(), Snackbar.LENGTH_LONG).show();

                        throw new Exception();
                    }
                }));


    }

    private void populateList(ArrayList<Article> list) {
        adapter = new ArticleAdapter(list, mActivity, isDark);
        recyclerView.setAdapter(adapter);
        adapter.notifyItemRangeInserted(adapter.getItemCount(), arrayList.size() - 1);
        adapter.setOnItemClickListener(MSharedFragment.this);


    }

    /* Use different layouts for Landscape &
     * Portrait mode.2 columns in portrait and 3 columns in landscape
     */
    private void setLayoutManager() {
        switch (getResources().getConfiguration().orientation)
        {
            case Configuration.ORIENTATION_PORTRAIT:
                mLayoutManager =
                        new StaggeredGridLayoutManager(
                                2, StaggeredGridLayoutManager.VERTICAL);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                mLayoutManager =
                        new StaggeredGridLayoutManager(
                                3, StaggeredGridLayoutManager.VERTICAL);
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
        Article articles = articleList.get(position);
        String url = articles.getWebUrl();
        ChromeUtils.launchChromeTabs(url,mActivity);
    }

    @Override
    public void onStop() {
        disposable.clear();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
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

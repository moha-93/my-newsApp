package com.moha.nytimesapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.moha.nytimesapp.utility.ChromeUtils;
import com.moha.nytimesapp.utility.NetworkUtils;
import com.moha.nytimesapp.R;
import com.moha.nytimesapp.adapter.ArticleAdapter;
import com.moha.nytimesapp.modal.Article;
import com.moha.nytimesapp.modal.Response;
import com.moha.nytimesapp.rest.WebServiceAPI;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


import static android.content.Context.MODE_PRIVATE;
import static com.moha.nytimesapp.dependency.base.App.API_KEY;


public class MSharedFragment extends Fragment implements ArticleAdapter.OnItemClickListener {
    private static final String LIST_STATE = "list_state";
    @SuppressLint("StaticFieldLeak")
    private static MSharedFragment instance;
    protected FragmentActivity mActivity;
    private RecyclerView recyclerView;
    private List<Article> articleList;
    private ArrayList<Article> articleInstance = new ArrayList<>();
    private ArticleAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    StaggeredGridLayoutManager mLayoutManager;
    boolean isDark = false;
    FloatingActionButton btn_darkMode;
    CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    WebServiceAPI serviceAPI;

    public MSharedFragment() {
        // Required empty public constructor
    }

    public static MSharedFragment getInstance() {
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
        btn_darkMode = view.findViewById(R.id.btnDM_msh);
        recyclerView = view.findViewById(R.id.ms_recycler_view);
        recyclerView.setHasFixedSize(true);
        setLayoutManager();

        // Check InstanceState
        if (savedInstanceState != null) {
            articleInstance = savedInstanceState.getParcelableArrayList(LIST_STATE);
            displayData();
        } else {
            recyclerView.setHasFixedSize(true);
            setLayoutManager();
            if (NetworkUtils.isNetworkAvailable(mActivity)) {
                fetchData();
            } else {
                Toast.makeText(mActivity, "Oops! No internet connection", Toast.LENGTH_LONG).show();
            }
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
                displayData();
                saveThemeStatePref(isDark);
            }
        });

        return view;
    }

    //Get and display the data from the server
    private void fetchData() {
        disposable.add(serviceAPI.getSharedArticles(30, "twitter", API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response>() {
                    @Override
                    public void accept(Response response) throws Exception {
                        try {
                            if (response != null && response.getArticles() != null) {
                                articleList = response.getArticles();
                                articleInstance.addAll(articleList);
                                populateList(articleList);
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

    private void populateList(List<Article> list) {
        adapter = new ArticleAdapter(list, mActivity, isDark);
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(0);
        adapter.notifyItemRangeInserted(adapter.getItemCount(), list.size() - 1);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(MSharedFragment.this);

    }

    private void displayData() {
        recyclerView.setHasFixedSize(true);
        setLayoutManager();
        adapter = new ArticleAdapter(articleInstance, mActivity, isDark);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(this);

    }

    /* Use different layouts for Landscape &
     * Portrait mode.2 columns in portrait and 3 columns in landscape
     */
    private void setLayoutManager() {
        switch (getResources().getConfiguration().orientation) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
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
    public void OnItemClick(int position) {
        Article articles = articleList.get(position);
        String url = articles.getWebUrl();
        ChromeUtils.launchChromeTabs(url, mActivity);
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
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putParcelableArrayList(LIST_STATE, articleInstance);
    }

}

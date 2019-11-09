package com.moha.nytimesapp.modelDB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

public  class  FavoriteViewModel extends AndroidViewModel {
    public  static FavoriteRepository  repository;
    private  LiveData<List<Favorite>> allFavorites;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        repository = new FavoriteRepository(application);
        allFavorites = repository.getAllFavorites();
        initDB(application);
    }

    private void initDB(Context context) {
        FavoriteDatabase favoriteDatabase = FavoriteDatabase.getInstance(context);
        FavoriteViewModel.repository  = FavoriteRepository.getInstance(favoriteDatabase.favoriteDao());
    }


    public static void insert(Favorite favorite){
        repository.insert(favorite);
    }

    public static void delete(Favorite favorite){
        repository.delete(favorite);
    }

    public static int isFavorite(int itemId){
        return repository.isFavorite(itemId);
    }

    public  LiveData<List<Favorite>> getAllFavorites(){
        return allFavorites;
    }
}

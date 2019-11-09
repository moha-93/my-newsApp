package com.moha.nytimesapp.modelDB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

 public class FavoriteRepository implements FavoriteDao  {

    private  FavoriteDao favoriteDao;
    private  LiveData<List<Favorite>> allFavorites;
    public static FavoriteRepository instance;

    public FavoriteRepository(Application application) {
        FavoriteDatabase database = FavoriteDatabase.getInstance(application);
        favoriteDao = database.favoriteDao();
        allFavorites = favoriteDao.getFavItems();
    }

     public  FavoriteRepository(Context context) {
        favoriteDao =FavoriteDatabase.getInstance(context).favoriteDao();

     }

     public FavoriteRepository(FavoriteDao favoriteDao) {
         this.favoriteDao = favoriteDao;
     }

     public static FavoriteRepository getInstance(FavoriteDao favoriteDao){
        if (instance==null){
            instance = new FavoriteRepository(favoriteDao);
        }
        return instance;
     }

     public void insert(Favorite favorite){
        new insertAsyncTask(favoriteDao).execute(favorite);

    }

    public void delete(Favorite favorite){
        new deleteAsyncTask(favoriteDao).execute(favorite);

    }

     @Override
     public  LiveData<List<Favorite>> getFavItems() {
         return favoriteDao.getFavItems();
     }


     public  int isFavorite(int itemId){
        return favoriteDao.isFavorite(itemId);
    }

    public  LiveData<List<Favorite>> getAllFavorites(){
        return allFavorites;
    }

    public static class insertAsyncTask extends AsyncTask<Favorite,Void,Void>{
        private FavoriteDao favoriteDao;

        public insertAsyncTask(FavoriteDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {

            favoriteDao.insert(favorites[0]);
            return null;
        }
    }

    public static class deleteAsyncTask extends AsyncTask<Favorite,Void,Void>{
        private FavoriteDao favoriteDao;

        public deleteAsyncTask(FavoriteDao favoriteDao) {
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected Void doInBackground(Favorite... favorites) {

            favoriteDao.delete(favorites[0]);
            return null;
        }
    }
}

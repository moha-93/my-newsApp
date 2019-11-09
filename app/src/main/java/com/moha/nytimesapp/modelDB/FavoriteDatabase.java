package com.moha.nytimesapp.modelDB;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Favorite.class}, version = 1,exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    public static FavoriteDatabase instance;

    public abstract FavoriteDao favoriteDao();

    public static synchronized FavoriteDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, FavoriteDatabase.class, "favorite_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    //.addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

   /* private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavoriteDao favDao;

        private PopulateDbAsyncTask(FavoriteDatabase db) {
            favDao = db.favoriteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Article article = new Article();
            favDao.insert(new Favorite(article.getWebUrl(),article.getHeadLine(),article.getSummary(),article.getPublishDate()));
            return null;
        }
    }*/
}

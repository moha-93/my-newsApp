package com.moha.nytimesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.moha.nytimesapp.database.Contract.*;
import com.moha.nytimesapp.modal.Article;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "article.db";
    public static final int VERSION = 1;

    private SQLiteDatabase db;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_ARTICLE_TABLE = "CREATE TABLE " +
                ArticleTable.TABLE_NAME + " ( " +
                ArticleTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ArticleTable.COLUMN_HEADLINE + " TEXT NOT NULL, " +
                ArticleTable.COLUMN_SUMMARY + " TEXT NOT NULL, " +
                ArticleTable.COLUMN_PUBLISHED_DATE + " TEXT NOT NULL, " +
                ArticleTable.COLUMN_IS_FAVORITE + "BOOLEAN NOT NULL" +
                ")";
        db.execSQL(CREATE_ARTICLE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ArticleTable.TABLE_NAME);
        onCreate(db);

    }

    public void addArticles(Article articles) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ArticleTable.COLUMN_HEADLINE, articles.getHeadLine());
        cv.put(ArticleTable.COLUMN_SUMMARY, articles.getSummary());
        cv.put(ArticleTable.COLUMN_PUBLISHED_DATE, articles.getPublishDate());
        cv.put(ArticleTable.COLUMN_IS_FAVORITE, articles.isFavorite());
        db.insert(ArticleTable.TABLE_NAME, null, cv);
    }

    public void updateArticle(Article articles) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ArticleTable.COLUMN_HEADLINE, articles.getHeadLine());
        cv.put(ArticleTable.COLUMN_SUMMARY, articles.getSummary());
        cv.put(ArticleTable.COLUMN_PUBLISHED_DATE, articles.getPublishDate());
        cv.put(ArticleTable.COLUMN_IS_FAVORITE, articles.isFavorite());
        db.update(ArticleTable.TABLE_NAME, cv, ArticleTable._ID + "=", null);
        addArticles(articles);
        db.close();
    }

    public ArrayList<Article> getArticles() {

        ArrayList<Article> articlesList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ArticleTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Article articles = new Article();

                articles.setHeadLine(cursor.getString(cursor.getColumnIndex(ArticleTable.COLUMN_HEADLINE)));
                articles.setSummary(cursor.getString(cursor.getColumnIndex(ArticleTable.COLUMN_SUMMARY)));
                articles.setPublishDate(cursor.getString(cursor.getColumnIndex(ArticleTable.COLUMN_PUBLISHED_DATE)));
                articles.setFavorite(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ArticleTable.COLUMN_IS_FAVORITE))));
                articlesList.add(articles);

            } while (cursor.moveToNext());

        }
        cursor.close();
        return articlesList;

    }

}




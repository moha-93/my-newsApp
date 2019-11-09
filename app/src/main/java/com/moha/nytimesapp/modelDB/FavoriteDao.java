package com.moha.nytimesapp.modelDB;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("SELECT * FROM favorites_table")
    LiveData<List<Favorite>> getFavItems();

    @Query("SELECT EXISTS (SELECT * FROM favorites_table WHERE id = :itemId)")
    int isFavorite(int itemId);
}

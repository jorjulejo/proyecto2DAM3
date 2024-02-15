package com.example.app_android.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritosDao {

    @Query("SELECT * FROM favoritos")
    LiveData<List<Favoritos>> getAllFavoritos();

    @Query("SELECT Nombre FROM favoritos WHERE id = :id")
    LiveData<String> getNombreById(int id);

    @Insert
    void insert(Favoritos favoritos);

    @Delete
    void delete(Favoritos favoritos);


}

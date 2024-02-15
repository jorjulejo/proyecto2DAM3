package com.example.app_android.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.app_android.models.Favoritos;
import com.example.app_android.models.FavoritosDao;

@Database(entities = {Favoritos.class}, version = 4)
public abstract class FavoritosDatabase extends RoomDatabase {
    public abstract FavoritosDao favoritosDao();

    private static FavoritosDatabase instance; // Agregamos una instancia privada para la base de datos

    public static synchronized FavoritosDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FavoritosDatabase.class, "favoritos_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}

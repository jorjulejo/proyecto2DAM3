package com.example.app_android.models;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.app_android.data.FavoritosDatabase;

import java.util.List;

public class FavoritosModel extends AndroidViewModel {
    private final FavoritosDao favoritosDao;
    private final LiveData<List<Favoritos>> allFavoritos;

    public FavoritosModel(Application application) {
        super(application);
        FavoritosDatabase database = FavoritosDatabase.getInstance(application);
        favoritosDao = database.favoritosDao();
        allFavoritos = favoritosDao.getAllFavoritos();
    }

    public LiveData<List<Favoritos>> getAllFavoritos() {
        return allFavoritos;
    }

    public LiveData<String> getNombreById(int id) {
        return favoritosDao.getNombreById(id);
    }

    public void insertFavoritos(Favoritos favoritos) {
        new InsertFavoritosAsyncTask(favoritosDao).execute(favoritos);
    }

    public void deleteFavoritos(Favoritos favoritos) {
        new DeleteFavoritosAsyncTask(favoritosDao).execute(favoritos);
    }

    private static class InsertFavoritosAsyncTask extends AsyncTask<Favoritos, Void, Void> {
        private final FavoritosDao favoritosDao;

        private InsertFavoritosAsyncTask(FavoritosDao favoritosDao) {
            this.favoritosDao = favoritosDao;
        }

        @Override
        protected Void doInBackground(Favoritos... favoritos) {
            favoritosDao.insert(favoritos[0]);
            return null;
        }
    }

    private static class DeleteFavoritosAsyncTask extends AsyncTask<Favoritos, Void, Void> {
        private FavoritosDao favoritosDao;

        private DeleteFavoritosAsyncTask(FavoritosDao favoritosDao) {
            this.favoritosDao = favoritosDao;
        }

        @Override
        protected Void doInBackground(Favoritos... favoritos) {
            favoritosDao.delete(favoritos[0]);
            return null;
        }
    }
}

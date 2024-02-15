package com.example.app_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_android.models.Favoritos;
import com.example.app_android.models.FavoritosModel;

import java.util.ArrayList;

public class FavoritosActivity extends AppCompatActivity {
    private FavoritosAdapter favoritosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        ListView listViewFavoritos = findViewById(R.id.listViewFavoritos);
        favoritosAdapter = new FavoritosAdapter(this, new ArrayList<>());
        listViewFavoritos.setAdapter(favoritosAdapter);

        ImageView imageView = findViewById(R.id.backButton);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritosActivity.this, PrincipalActivity.class);
            intent.putExtra("token", getIntent().getStringExtra("token"));
            startActivity(intent);
        });

        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(FavoritosActivity.this, MapaActivity.class);
            intent.putExtra("token", getIntent().getStringExtra("token"));
            startActivity(intent);
        });

        // Obteniendo la instancia del ViewModel
        FavoritosModel favoritosModel = new ViewModelProvider(this).get(FavoritosModel.class);

        // Observando los datos de los favoritos
        favoritosModel.getAllFavoritos().observe(this, favoritos -> {
            // Actualizar el adaptador con los nuevos datos
            favoritosAdapter.clear();
            favoritosAdapter.addAll(favoritos);
            favoritosAdapter.notifyDataSetChanged();
        });

        // Establecer el listener para los clicks en los ítems del ListView
        listViewFavoritos.setOnItemClickListener((parent, view, position, id) -> {
            // Obtener el favorito presionado
            Favoritos favorito = (Favoritos) parent.getItemAtPosition(position);

            // Crear un intent para iniciar PrincipalActivity
            Intent intent = new Intent(FavoritosActivity.this, MapaActivity.class);
            // Pasar latitud y longitud como extras
            intent.putExtra("token", getIntent().getStringExtra("token"));
            intent.putExtra("latitud", favorito.getLatitud());
            intent.putExtra("longitud", favorito.getLongitud());

            // Iniciar PrincipalActivity
            startActivity(intent);
        });
    }
}

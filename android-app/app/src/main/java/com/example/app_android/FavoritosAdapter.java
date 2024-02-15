package com.example.app_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.app_android.models.Favoritos;

import java.util.List;

public class FavoritosAdapter extends ArrayAdapter<Favoritos> {

    private Context context;
    private List<Favoritos> favoritosList;

    public FavoritosAdapter(Context context, List<Favoritos> favoritosList) {
        super(context, 0, favoritosList);
        this.context = context;
        this.favoritosList = favoritosList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_favorito, parent, false);
        }

        TextView tvNombre = convertView.findViewById(R.id.tvNombre);
        TextView tvLatitud = convertView.findViewById(R.id.tvLatitud);
        TextView tvLongitud = convertView.findViewById(R.id.tvLongitud);

        Favoritos favorito = favoritosList.get(position);
        tvNombre.setText(favorito.getNombre());
        tvLatitud.setText(String.valueOf(favorito.getLatitud()));
        tvLongitud.setText(String.valueOf(favorito.getLongitud()));

        return convertView;
    }
}


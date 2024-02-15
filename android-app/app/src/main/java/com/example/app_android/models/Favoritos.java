package com.example.app_android.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Favoritos")
public class Favoritos {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "latitud")
    public double latitud;

    @ColumnInfo(name = "longitud")
    public double longitud;

    // Constructor vacío necesario para Room
    public Favoritos() {
    }

    public Favoritos(String nombre, double latitud, double longitud) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public void setLatitud(double latitud){
        this.latitud = latitud;
    }

    public void setLongitud(double longitud){
        this.longitud = longitud;
    }


}

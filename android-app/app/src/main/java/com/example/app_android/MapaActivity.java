package com.example.app_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_android.manager.camaras.DataManagerCamaras;
import com.example.app_android.manager.flujos.DataManagerFlujos;
import com.example.app_android.manager.incidencias.DataManagerIncidencias;
import com.example.app_android.models.DataItem;
import com.example.app_android.models.Favoritos;
import com.example.app_android.models.FavoritosDao;
import com.example.app_android.models.FavoritosModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mMap;
    private DataManagerIncidencias dataManagerIncidencias;
    private DataManagerCamaras dataManagerCamaras;
    private DataManagerFlujos dataManagerFlujos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        // Inicializa los DataManager aquí
        dataManagerIncidencias = new DataManagerIncidencias();
        dataManagerCamaras = new DataManagerCamaras();
        dataManagerFlujos = new DataManagerFlujos();

        ImageView backImageView = findViewById(R.id.backButton);

        backImageView.setOnClickListener(view -> {
            Intent intent = new Intent(this, PrincipalActivity.class);
            intent.putExtra("token", getIntent().getStringExtra("token"));
            intent.putExtra("usuario", getIntent().getStringExtra("usuario"));

            startActivity(intent);
        });

        ImageView favouriteImageView = findViewById(R.id.favoritesButton);

        favouriteImageView.setOnClickListener(view -> {
            Intent intent = new Intent(this, FavoritosActivity.class);
            intent.putExtra("token", getIntent().getStringExtra("token"));
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location ;
        double latitud = getIntent().getDoubleExtra("latitud", 0); // 0 es un valor predeterminado.
        double longitud = getIntent().getDoubleExtra("longitud", 0); // 0 es un valor predeterminado.

// Si los valores son 0, consideramos que podrían ser valores predeterminados (dependiendo de tu caso de uso).
// Asegúrate de que 0,0 no es una coordenada válida que esperarías recibir.
        if (latitud != 0 || longitud != 0) {
            location = new LatLng(latitud, longitud);
        } else {
            // Coordenadas predeterminadas si no se proporcionaron latitud y longitud.
            location = new LatLng(43.3202955, -1.8320004);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        // Configurar el listener para los clics en los marcadores
        mMap.setOnMarkerClickListener(marker -> {
            // Obtener la información asociada al marcador
            String title = marker.getTitle();
            LatLng position = marker.getPosition();

            // Crear y mostrar la ventana emergente con la información del marcador
            AlertDialog.Builder builder = new AlertDialog.Builder(MapaActivity.this);
            builder.setTitle(title);
            builder.setMessage("Latitud: " + position.latitude + "\nLongitud: " + position.longitude);
            builder.setPositiveButton("Añadir a favoritos", (dialog, which) -> {
                // Acción al hacer clic en el botón OK (opcional)
                // Creación del objeto Favoritos
                Favoritos favorito = new Favoritos(title, position.latitude, position.longitude);
                // Invocar el método del ViewModel para insertar el favorito en la base de datos
                FavoritosModel viewModel = new FavoritosModel(getApplication());
                viewModel.insertFavoritos(favorito);
            });
            builder.setNegativeButton("Cerrar", (dialog, which) -> {

            });

            builder.show();

            // Devolver 'true' para indicar que el evento ha sido consumido
            return true;
        });

        // Cargar los datos en el mapa
        loadData();
    }


    private void loadData() {
        Bundle token = getIntent().getExtras();
        String jwtToken = token != null ? token.getString("token") : "";

        // Aquí, asumiendo que cada DataManager tiene un método loadMyApiData y loadOpenDataEuskadiData ajustados
        CompletableFuture<List<DataItem>> futureIncidencias = dataManagerIncidencias.loadMyApiData(jwtToken);
        CompletableFuture<List<DataItem>> futureCamaras = dataManagerCamaras.loadMyApiData(jwtToken);
        CompletableFuture<List<DataItem>> futureFlujos = dataManagerFlujos.loadMyApiData(jwtToken);
        CompletableFuture<List<DataItem>> futureOpenDataIncidencias = dataManagerIncidencias.loadOpenDataEuskadiData();
        CompletableFuture<List<DataItem>> futureOpenDataCamaras = dataManagerCamaras.loadOpenDataEuskadiData();
        // Continúa para otros DataManager según sea necesario...

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futureIncidencias, futureCamaras, futureFlujos, futureOpenDataIncidencias, futureOpenDataCamaras
                // Añade otros futuros según sea necesario
        );

        allFutures.thenRun(() -> {
            List<DataItem> allItems = Stream.of(futureIncidencias, futureCamaras, futureFlujos, futureOpenDataIncidencias, futureOpenDataCamaras).map(CompletableFuture::join).flatMap(List::stream).collect(Collectors.toList());
            runOnUiThread(() -> displayDataOnMap(allItems));
        }).exceptionally(e -> {
            Log.e("MapaActivity", "Error loading data", e);
            return null;
        });
    }

    private void displayDataOnMap(List<DataItem> items) {
        runOnUiThread(() -> {
            for (DataItem item : items) {
                try {
                    int imageResId = getImageResource(item.getOrigin(), item.getType());
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(imageResId);
                    String latitud;
                    String longitud;
                    String descripcion;

                    if ("myAPI".equals(item.getOrigin())) {
                        switch (item.getType()) {
                            case "incidencia":
                                latitud = item.getValueFromJson("latitud");
                                longitud = item.getValueFromJson("longitud");
                                descripcion = item.getValueFromJson("causa");
                                break;
                            case "camara":
                                latitud = item.getValueFromJson("latitud");
                                longitud = item.getValueFromJson("longitud");
                                descripcion = item.getValueFromJson("nombre");
                                break;
                            case "flujo":
                                latitud = item.getValueFromJson("latitud");
                                longitud = item.getValueFromJson("longitud");
                                descripcion = item.getValueFromJson("descripcion");
                                break;
                            default:
                                // Manejar cualquier otro tipo de dato de myAPI
                                // Aquí se puede proporcionar un valor predeterminado o manejarlo según sea necesario
                                latitud = "";
                                longitud = "";
                                descripcion = "";
                                break;
                        }
                    } else if ("openDataEuskadi".equals(item.getOrigin())) {
                        if ("incidencia".equals(item.getType())) {
                            latitud = item.getValueFromJson("latitude");
                            longitud = item.getValueFromJson("longitude");
                            descripcion = item.getValueFromJson("cause");

                        } else if ("camara".equals(item.getType())) {
                            latitud = item.getValueFromJson("latitude");
                            longitud = item.getValueFromJson("longitude");
                            descripcion = item.getValueFromJson("cameraName");
                        } else if ("flujo".equals(item.getType())) {
                            latitud = item.getValueFromJson("latitude");
                            longitud = item.getValueFromJson("longitude");
                            descripcion = item.getValueFromJson("totalVehicles");
                        } else {
                            // Manejar cualquier otro tipo de dato de Open Data Euskadi
                            // Aquí se puede proporcionar un valor predeterminado o manejarlo según sea necesario
                            latitud = "";
                            longitud = "";
                            descripcion = "";
                        }
                    } else {
                        // Manejar cualquier otro origen de datos
                        // Aquí se puede proporcionar un valor predeterminado o manejarlo según sea necesario
                        latitud = "";
                        longitud = "";
                        descripcion = "";
                    }

                    // Convertir a double solo si las cadenas no están vacías y no son nulas
                    if (latitud != null && !latitud.isEmpty() && longitud != null && !longitud.isEmpty()) {
                        double lat = Double.parseDouble(latitud);
                        double lng = Double.parseDouble(longitud);

                        // Verificar si la descripción es nula o está vacía, y proporcionar un valor predeterminado en ese caso
                        String description = descripcion != null && !descripcion.isEmpty() ? descripcion : "Descripción no disponible";

                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(description).icon(icon));

                    }
                } catch (NumberFormatException e) {
                    // Manejar la excepción en caso de que la conversión de String a double falle
                    e.printStackTrace();
                }
            }
        });
    }


    private int getImageResource(String origin, String type) {
        if ("myAPI".equals(origin)) {
            switch (type) {
                case "incidencia":
                    return R.drawable.accidente;
                case "camara":
                    return R.drawable.camara;
                case "flujo":
                    return R.drawable.atasco;
                default:
                    return R.drawable.boton;
            }
        } else if ("openDataEuskadi".equals(origin)) {
            switch (type) {
                case "incidencia":
                    return R.drawable.autos;
                case "camara":
                    return R.drawable.reflex;
                case "flujo":
                    return R.drawable.camino;
                default:
                    return R.drawable.circulo;
            }
        }
        return R.drawable.ic_add; // Una imagen por defecto si no se reconoce el origen
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}

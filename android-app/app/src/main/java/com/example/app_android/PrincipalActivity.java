package com.example.app_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PrincipalActivity extends AppCompatActivity {

    LinearLayout layout;
    LinearLayout layout2;

    ImageView add;
    TextView tvIncidencias;
    TextView tvCamaras;
    TextView tvFlujos;

    Button bttonIncidencias;
    Button bttonCamaras;
    Button bttonFlujos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Bundle token = getIntent().getExtras();

        // Inicialización de componentes aquí
        layout = findViewById(R.id.bttonMapa);
        layout2 = findViewById(R.id.bttonFav);

        add = findViewById(R.id.AddIcon);

        tvIncidencias = findViewById(R.id.tvIncidencias);
        tvCamaras = findViewById(R.id.tvCamaras);
        tvFlujos = findViewById(R.id.tvFlujos);

        bttonIncidencias = findViewById(R.id.btnVerMasIncidencias);
        bttonCamaras = findViewById(R.id.btnVerMasCamaras);
        bttonFlujos = findViewById(R.id.btnVerMasFlujos);

        layout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapaActivity.class);
            intent.putExtra("usuario", token.getString("usuario"));
            intent.putExtra("token", token.getString("token"));
            startActivity(intent);
        });

        layout2.setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoritosActivity.class);
            intent.putExtra("usuario", token.getString("usuario"));
            startActivity(intent);
        });
        verificarAdmin();
    }

    private void verificarAdmin() {
        OkHttpClient client = new OkHttpClient();
        Bundle token = getIntent().getExtras();
        if (token != null && token.containsKey("usuario")) {
            String email = token.getString("usuario");

            // Define el MediaType para indicar que el cuerpo de la solicitud es JSON
            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            // Crea el cuerpo de la solicitud con el email
            String json = "{\"email\":\"" + email + "\"}";
            RequestBody body = RequestBody.create(json, MEDIA_TYPE_JSON);

            // Ajusta la URL
            String url = "http://10.0.2.2:8080/usuarios/seleccionarAdmin";

            // Crea la solicitud POST con el cuerpo que contiene el email
            Request request = new Request.Builder().url(url).post(body).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "No se pudo conectar con el servidor");
                }


                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String respuesta = response.body().string();
                        Gson gson = new Gson();
                        JsonArray jsonArray = gson.fromJson(respuesta, JsonArray.class);

                        for (JsonElement element : jsonArray) {
                            JsonObject adminObject = element.getAsJsonObject();
                            JsonObject snAdminObject = adminObject.getAsJsonObject("snAdmin");
                            String snAdminString = snAdminObject.get("string").getAsString();

                            if ("N".equals(snAdminString)) {
                                runOnUiThread(() -> {
                                    add.setVisibility(View.INVISIBLE);
                                    tvIncidencias.setVisibility(View.INVISIBLE);
                                    tvCamaras.setVisibility(View.INVISIBLE);
                                    tvFlujos.setVisibility(View.INVISIBLE);
                                    bttonIncidencias.setVisibility(View.INVISIBLE);
                                    bttonCamaras.setVisibility(View.INVISIBLE);
                                    bttonFlujos.setVisibility(View.INVISIBLE);
                                });
                                // Si encontramos que no es admin, salimos del bucle
                                return;
                            }
                        }

                        // Si llegamos aquí, significa que todos los elementos son "S" (admin)
                        // Manejar caso donde todos los elementos son "S" (admin)
                    } else {
                        // Manejar caso donde la respuesta no es exitosa
                        showAlert("Error", "Respuesta no exitosa del servidor: " + response.code());
                    }
                }


            });
        }
        add.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormularioActivity.class);
            intent.putExtra("token", token.getString("token"));
            intent.putExtra("usuario", token.getString("usuario"));
            startActivity(intent);
        });

    }

    private void showAlert(final String title, final String message) {
        runOnUiThread(() -> new AlertDialog.Builder(PrincipalActivity.this).setTitle(title).setMessage(message).setPositiveButton("OK", null).create().show());
    }
}
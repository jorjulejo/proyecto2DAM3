package com.example.app_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerTextView = findViewById(R.id.registerTextView);

        // Agregar un OnClickListener al TextView
        registerTextView.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        loginButton.setOnClickListener(v -> {
            final String email = emailEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            // Validar que email y password no estén vacíos
            if(email.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Email y password son requeridos");
                return;
            }

            // Construir el objeto de credenciales
            CredencialesLogin credenciales = new CredencialesLogin(email, password);

            // Convertir credenciales a JSON usando Gson
            String jsonCredenciales = gson.toJson(credenciales);

            // Crear el cuerpo de la solicitud
            RequestBody body = RequestBody.create(jsonCredenciales, MediaType.parse("application/json; charset=utf-8"));

            // Crear la solicitud
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/usuarios/login") // Reemplaza con la URL de tu API
                    .post(body)
                    .build();

            // Realizar la llamada a la API
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    showAlert("Error", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseBody = response.body().string();
                    if (response.isSuccessful()) {
                        // Mostrar el pop-up de inicio de sesión exitoso
                        //responseBody
                    } else {
                        // Mostrar el error
                        showAlert("Error", responseBody);
                    }
                }
            });
        });
    }

    private void showAlert(final String title, final String message) {
        runOnUiThread(() -> new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()
                .show());
    }

    // Clase para representar las credenciales
    class CredencialesLogin {
        private String email;
        private String contrasena;

        public CredencialesLogin(String email, String contrasena) {
            this.email = email;
            this.contrasena = contrasena;
        }
    }

    // Encontrar el TextView con el id registerTextView

}

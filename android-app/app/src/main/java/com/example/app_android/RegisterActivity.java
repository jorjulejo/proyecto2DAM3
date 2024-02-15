package com.example.app_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_android.models.Usuarios;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Asegúrate de que el layout se llame activity_register

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        ImageView backImageView = findViewById(R.id.leftIcon);

        backImageView.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(v -> {
            final String email = emailEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            // Validar que email y password no estén vacíos
            if (email.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Email y contraseña son requeridos");
                return;
            }

            Usuarios usuario = new Usuarios();
            usuario.setEmail(email);
            usuario.setContrasena(password);

            // Convertir usuario a JSON usando Gson
            String jsonUsuario = gson.toJson(usuario);

            // Crear el cuerpo de la solicitud
            RequestBody body = RequestBody.create(jsonUsuario, MediaType.parse("application/json; charset=utf-8"));

            // Crear la solicitud
            Request request = new Request.Builder().url("http://10.0.2.2:8080/usuarios/registro") // Reemplaza con la URL de tu API
                    .post(body).build();

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
                        // Registro exitoso, ahora realizar la llamada a la API de envío de correo
                        CorreoRequest correoRequest = new CorreoRequest();
                        correoRequest.setDestinatario(email);
                        correoRequest.setAsunto("Bienvenido!");
                        correoRequest.setCuerpo("¡Gracias por registrarte en nuestra aplicación!");

                        String jsonCorreo = gson.toJson(correoRequest);

                        // Crear el cuerpo de la solicitud para enviar el correo
                        RequestBody correoBody = RequestBody.create(jsonCorreo, MediaType.parse("application/json; charset=utf-8"));

                        // Crear la solicitud para enviar el correo
                        Request requestM = new Request.Builder().url("http://10.0.2.2:8080/usuarios/enviar-correo") // Reemplaza con la URL de tu API de envío de correo
                                .post(correoBody).build();

                        // Realizar la llamada a la API de envío de correo
                        client.newCall(requestM).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                showAlert("Error al enviar el correo", e.getMessage());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String correoResponse = response.body().string();
                                if (response.isSuccessful()) {
                                    // Éxito al enviar el correo
                                    showAlert("Registro Exitoso", "Se ha enviado un correo de confirmación.");
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // Mostrar el error al enviar el correo
                                    showAlert("Error al enviar el correo", correoResponse);
                                }
                            }
                        });

                        // Redirigir a la actividad principal

                    } else {
                        // Mostrar el error al registrar
                        showAlert("Error al registrar", responseBody);
                    }
                }
            });
        });
    }

    private void showAlert(final String title, final String message) {
        runOnUiThread(() -> new AlertDialog.Builder(RegisterActivity.this).setTitle(title).setMessage(message).setPositiveButton("OK", null).create().show());
    }


}

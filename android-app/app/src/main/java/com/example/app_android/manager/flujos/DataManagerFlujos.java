package com.example.app_android.manager.flujos;

import android.util.Log;

import com.example.app_android.models.DataItem;
import com.example.app_android.models.JsonTransformer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataManagerFlujos {
    private OkHttpClient client;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public DataManagerFlujos() {
        // Configurar OkHttpClient con timeouts personalizados
        client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS) // Aumenta el timeout de lectura a 30 segundos
                .connectTimeout(30, TimeUnit.SECONDS) // Aumenta el timeout de conexión a 30 segundos
                .build();
    }

    public CompletableFuture<List<DataItem>> loadMyApiData(String jwtToken) {
        CompletableFuture<List<DataItem>> future = new CompletableFuture<>();
        Request request = new Request.Builder().url("http://10.0.2.2:8080/api/flujos/seleccionar").addHeader("Authorization", "Bearer " + jwtToken).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    List<DataItem> items = parseItems(responseBody);
                    future.complete(items);
                } else {
                    future.completeExceptionally(new IOException("HTTP error! status: " + response.code()));
                }
            }
        });

        return future;
    }


    private List<DataItem> parseItems(String responseBody) {
        List<DataItem> items = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(responseBody);

        if (jsonElement != null && jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                Map<String, String> transformedData = JsonTransformer.transformIncidentData(jsonObject);
                DataItem dataItem = new DataItem("myAPI", "flujo", transformedData.toString());
                items.add(dataItem);
            }
        } else {
            Log.d("DataManagerFlujos", "No hay datos o el JSON no contiene un array");
        }
        return items;
    }

    public CompletableFuture<String> insertarFlujo(String jsonFlujo, String jwtToken) {
        CompletableFuture<String> future = new CompletableFuture<>();
        String url = "http://10.0.2.2:8080/api/flujos/insertar";

        // Crear el cuerpo de la solicitud
        RequestBody body = RequestBody.create(jsonFlujo, JSON);

        // Construir la solicitud con el token JWT en el encabezado de autorización
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + jwtToken)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    future.complete("Flujo insertada con éxito");
                } else {
                    future.completeExceptionally(new IOException("Error al insertar flujo. HTTP status: " + response.code()));
                }
            }
        });

        return future;
    }


}

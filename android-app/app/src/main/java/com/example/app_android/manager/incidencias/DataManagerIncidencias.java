package com.example.app_android.manager.incidencias;

import android.util.Log;

import com.example.app_android.models.DataItem;
import com.example.app_android.models.JsonTransformer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataManagerIncidencias {
    private OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public CompletableFuture<List<DataItem>> loadMyApiData(String jwtToken) {
        CompletableFuture<List<DataItem>> future = new CompletableFuture<>();
        String url = "http://10.0.2.2:8080/api/incidencias/seleccionar";
        Request request = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + jwtToken).build();

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

    public CompletableFuture<List<DataItem>> loadOpenDataEuskadiData() {
        CompletableFuture<List<DataItem>> future = new CompletableFuture<>();
        String fechaHoy = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        String url = "https://api.euskadi.eus/traffic/v1.0/incidences/byDate/" + fechaHoy;
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    List<DataItem> items = parseItemsFromOpenData(responseBody);
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

        // Verifica si el elemento JSON no es null y es un JsonArray antes de proceder
        if (jsonElement != null && jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                Map<String, String> transformedData = JsonTransformer.transformIncidentData(jsonObject);
                DataItem dataItem = new DataItem("myAPI", "incidencia", transformedData.toString());
                items.add(dataItem);
            }
        } else {
            // Manejar el caso en que no hay datos (jsonArray es null o no es un JsonArray)
            // Podrías loguear esta situación o manejarla de otra manera según sea necesario
            Log.d("DataManagerIncidencias", "No hay datos o el JSON no contiene un array");
        }
        return items;
    }


    private List<DataItem> parseItemsFromOpenData(String responseBody) {
        List<DataItem> items = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray incidencesArray = jsonObject.getAsJsonArray("incidences");
            for (JsonElement incidenceElement : incidencesArray) {
                JsonObject incidenceObject = incidenceElement.getAsJsonObject();
                String incidenceJsonString = incidenceObject.toString();
                DataItem dataItem = new DataItem("openDataEuskadi", "incidencia", incidenceJsonString);
                items.add(dataItem);
            }
        }
        return items;
    }

    public CompletableFuture<String> insertarIncidencia(String jsonIncidencia, String jwtToken) {
        CompletableFuture<String> future = new CompletableFuture<>();
        String url = "http://10.0.2.2:8080/api/incidencias/insertar";

        // Crear el cuerpo de la solicitud
        RequestBody body = RequestBody.create(jsonIncidencia, JSON);

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
                    future.complete("Incidencia insertada con éxito");
                } else {
                    future.completeExceptionally(new IOException("Error al insertar incidencia. HTTP status: " + response.code()));
                }
            }
        });

        return future;
    }


}

package com.example.app_android.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class JsonTransformer {
    public static Map<String, String> transformIncidentData(JsonObject incidentData) {
        Map<String, String> transformedData = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : incidentData.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value != null && !value.isJsonNull()) { // Asegúrate de que el valor no sea null
                if (value.isJsonObject()) {
                    JsonObject valueObject = value.getAsJsonObject();
                    if (valueObject.has("string")) {
                        transformedData.put(key, valueObject.get("string").getAsString());
                    } else {
                        transformedData.put(key, "");
                    }
                } else {
                    // Convierte el valor a String, pero ignora los valores explícitamente nulos
                    String valueStr = value.toString();
                    // Evita añadir el valor si es "null" como cadena
                    if (!"null".equals(valueStr)) {
                        transformedData.put(key, valueStr.replace("\"", "")); // Remueve comillas para valores que no son objetos
                    }
                }
            }
            // Si el valor es null o es un JsonNull, no añadirá el par clave-valor
        }
        return transformedData;
    }
}

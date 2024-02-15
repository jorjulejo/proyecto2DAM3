package com.example.app_android.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DataItem {
    private String origin;
    private String type;
    private String json; // Puede ser un String que contenga el JSON crudo o cambiarlo por JSONObject/JsonNode dependiendo de tus necesidades

    // Constructor
    public DataItem(String origin, String type, String json) {
        this.origin = origin;
        this.type = type;
        this.json = json;
    }

    // Getters y Setters
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    /**
     * Método para obtener un valor específico del JSON, asumiendo que el JSON es un objeto y no un array.
     * Si el valor asociado a la clave es un objeto o array anidado, este método devolverá su representación en String.
     * @param key La clave para buscar en el objeto JSON.
     * @return El valor como String si existe; de lo contrario, un String vacío.
     */
    public String getValueFromJson(String key) {
        if (json == null || json.isEmpty()) {
            return ""; // Devuelve un String vacío si el JSON es nulo o vacío
        }

        JsonElement jsonElement = JsonParser.parseString(json);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonElement valueElement = jsonObject.get(key);
            if (valueElement != null) {
                // Comprobamos si el valor es un objeto o array anidado para devolver su representación en String
                if (valueElement.isJsonObject() || valueElement.isJsonArray()) {
                    return valueElement.toString();
                } else {
                    // Para valores primitivos, devolvemos el valor como String
                    return valueElement.getAsString();
                }
            }
        }
        return ""; // Devuelve un String vacío si la clave no existe o si el elemento no es un objeto
    }

    @Override
    public String toString() {
        // Ejemplo de implementación, ajusta según la estructura de DataItem
        return "DataItem{origin='" + origin + "', type='" + type + "', data='" + json + "'}";
    }

}
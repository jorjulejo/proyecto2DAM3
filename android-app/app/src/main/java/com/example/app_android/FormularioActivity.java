package com.example.app_android;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_android.manager.camaras.DataManagerCamaras;
import com.example.app_android.manager.flujos.DataManagerFlujos;
import com.example.app_android.manager.incidencias.DataManagerIncidencias;
import com.google.gson.JsonObject;

public class FormularioActivity extends AppCompatActivity {
    private Uri imageUri = null;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private LinearLayout layoutCampos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        Spinner spinnerTipo = findViewById(R.id.spinnerTipo);
        layoutCampos = findViewById(R.id.layoutCampos);
        Button btnEnviar = findViewById(R.id.btnEnviar); // Asumiendo que tienes un botón para enviar el formulario

        // Configura el spinner con las opciones
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipo_opciones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        // Maneja la selección del spinner
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = parent.getItemAtPosition(position).toString();
                ajustarCampos(seleccion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Manejo opcional para ninguna selección
            }
        });

        // Configura el botón de envío
        btnEnviar.setOnClickListener(v -> {
            if ("Incidencias".equals(spinnerTipo.getSelectedItem().toString())) {
                try {
                    // Suponiendo que tienes EditText para cada campo en tu layout de incidencias
                    View viewIncidencia = layoutCampos.getChildAt(0); // Obtiene el layout inflado de incidencias
                    String tipo = ((EditText) viewIncidencia.findViewById(R.id.edtTipo)).getText().toString();
                    String causa = ((EditText) viewIncidencia.findViewById(R.id.edtCausa)).getText().toString();
                    String comienzo = ((EditText) viewIncidencia.findViewById(R.id.edtComienzo)).getText().toString();
                    String nivelIncidencia = ((EditText) viewIncidencia.findViewById(R.id.edtNivelIncidencia)).getText().toString();
                    String carretera = ((EditText) viewIncidencia.findViewById(R.id.edtCarretera)).getText().toString();
                    String direccion = ((EditText) viewIncidencia.findViewById(R.id.edtDireccion)).getText().toString();
                    String latitud = ((EditText) viewIncidencia.findViewById(R.id.edtLatitud)).getText().toString();
                    String longitud = ((EditText) viewIncidencia.findViewById(R.id.edtLongitud)).getText().toString();
                    String usuario = getIntent().getStringExtra("usuario");

                    // Construir el objeto JSON
                    JsonObject jsonIncidencia = new JsonObject();
                    jsonIncidencia.addProperty("tipo", tipo);
                    jsonIncidencia.addProperty("causa", causa);
                    jsonIncidencia.addProperty("comienzo", comienzo);
                    jsonIncidencia.addProperty("nivel_incidencia", nivelIncidencia);
                    jsonIncidencia.addProperty("carretera", carretera);
                    jsonIncidencia.addProperty("direccion", direccion);
                    jsonIncidencia.addProperty("latitud", latitud);
                    jsonIncidencia.addProperty("longitud", longitud);
                    jsonIncidencia.addProperty("usuario", usuario);

                    // Aquí deberías tener el token JWT

                    // Llamar al DataManager para insertar la incidencia
                    DataManagerIncidencias dataManager = new DataManagerIncidencias();
                    dataManager.insertarIncidencia(jsonIncidencia.toString(), getIntent().getStringExtra("token")).thenAccept(result -> {
                        // Manejar resultado exitoso en el hilo UI
                        runOnUiThread(() -> Toast.makeText(FormularioActivity.this, "Incidencia insertada con éxito", Toast.LENGTH_SHORT).show());
                        Intent intent = new Intent(FormularioActivity.this, PrincipalActivity.class);
                        intent.putExtra("usuario", getIntent().getStringExtra("usuario"));
                        intent.putExtra("token", getIntent().getStringExtra("token"));
                        startActivity(intent);
                    }).exceptionally(e -> {
                        // Manejar error en el hilo UI
                        runOnUiThread(() -> Toast.makeText(FormularioActivity.this, "Error al insertar incidencia", Toast.LENGTH_SHORT).show());
                        return null;
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    // Manejar excepción, por ejemplo, mostrando un mensaje al usuario
                }
            } else if ("Cámaras".equals(spinnerTipo.getSelectedItem().toString())) {
                View viewCamara = layoutCampos.getChildAt(0);
                String nombre = ((EditText) viewCamara.findViewById(R.id.edtNombreCamara)).getText().toString();
                String url = ((EditText) viewCamara.findViewById(R.id.edtUrlCamara)).getText().toString();
                String latitud = ((EditText) viewCamara.findViewById(R.id.edtLatitudCamara)).getText().toString();
                String longitud = ((EditText) viewCamara.findViewById(R.id.edtLongitudCamara)).getText().toString();
                String carretera = ((EditText) viewCamara.findViewById(R.id.edtCarreteraCamara)).getText().toString();
                String kilometro = ((EditText) viewCamara.findViewById(R.id.edtKilometroCamara)).getText().toString();
                String usuario = getIntent().getStringExtra("usuario");

                JsonObject jsonCamara = new JsonObject();
                jsonCamara.addProperty("nombre", nombre);
                jsonCamara.addProperty("url", url);
                jsonCamara.addProperty("latitud", latitud);
                jsonCamara.addProperty("longitud", longitud);
                jsonCamara.addProperty("carretera", carretera);
                jsonCamara.addProperty("kilometro", kilometro);
                jsonCamara.addProperty("usuario", usuario);

                // Convertir la imagen a base64 y añadirla al JSON
                if (imageUri != null) {
                    String imagenBase64 = convertirImagenABase64(imageUri, getContentResolver());
                    if (imagenBase64 != null) {
                        jsonCamara.addProperty("imagen", imagenBase64);
                    } else {
                        Toast.makeText(this, "Error al convertir la imagen", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                DataManagerCamaras dataManager = new DataManagerCamaras();
                dataManager.insertarCamara(jsonCamara.toString(), getIntent().getStringExtra("token")).thenAccept(result -> {
                    runOnUiThread(() -> Toast.makeText(FormularioActivity.this, result, Toast.LENGTH_SHORT).show());
                    Intent intent = new Intent(FormularioActivity.this, PrincipalActivity.class);
                    intent.putExtra("usuario", getIntent().getStringExtra("usuario"));
                    intent.putExtra("token", getIntent().getStringExtra("token"));
                    startActivity(intent);
                }).exceptionally(e -> {
                    runOnUiThread(() -> Toast.makeText(FormularioActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    return null;
                });
            }else if ("Flujos".equals(spinnerTipo.getSelectedItem().toString())) {
                View viewFlujo = layoutCampos.getChildAt(0);
                String fecha = ((EditText) viewFlujo.findViewById(R.id.edtFechaFlujo)).getText().toString();
                String rangoTiempo = ((EditText) viewFlujo.findViewById(R.id.edtRangoTiempoFlujo)).getText().toString();
                String mediaVelocidad = ((EditText) viewFlujo.findViewById(R.id.edtMediaVelocidadFlujo)).getText().toString();
                String totalVehiculos = ((EditText) viewFlujo.findViewById(R.id.edtTotalVehiculosFlujo)).getText().toString();
                String latitud = ((EditText) viewFlujo.findViewById(R.id.edtLatitudFlujo)).getText().toString();
                String longitud = ((EditText) viewFlujo.findViewById(R.id.edtLongitudFlujo)).getText().toString();
                String usuario = getIntent().getStringExtra("usuario");

                JsonObject jsonFlujo = new JsonObject();
                jsonFlujo.addProperty("fecha", fecha);
                jsonFlujo.addProperty("rango_tiempo", rangoTiempo);
                jsonFlujo.addProperty("media_velocidad", mediaVelocidad);
                jsonFlujo.addProperty("total_vehiculos", totalVehiculos);
                jsonFlujo.addProperty("latitud", latitud);
                jsonFlujo.addProperty("longitud", longitud);
                jsonFlujo.addProperty("usuario", usuario);
                // Añadir cualquier otro campo necesario

                // Asegúrate de tener el DataManagerFlujos o el método adecuado en DataManagerCamaras para enviar datos de flujo
                DataManagerFlujos dataManager = new DataManagerFlujos(); // Asumiendo que tienes un DataManager para Flujos

                // Llama al método para enviar los datos
                dataManager.insertarFlujo(jsonFlujo.toString(), getIntent().getStringExtra("token")).thenAccept(result -> {
                    runOnUiThread(() -> Toast.makeText(FormularioActivity.this, result, Toast.LENGTH_SHORT).show());
                    // Redirige o actualiza la UI según sea necesario
                    Intent intent = new Intent(FormularioActivity.this, PrincipalActivity.class);
                    intent.putExtra("usuario", getIntent().getStringExtra("usuario"));
                    intent.putExtra("token", getIntent().getStringExtra("token"));
                    startActivity(intent);

                }).exceptionally(e -> {
                    runOnUiThread(() -> Toast.makeText(FormularioActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    return null;
                });
            }



        });

    }

    private void ajustarCampos(String seleccion) {
        layoutCampos.removeAllViews();
        View child = null;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (seleccion) {
            case "Incidencias":
                child = inflater.inflate(R.layout.incidencias_layout, null);
                break;
            case "Cámaras":
                child = inflater.inflate(R.layout.camaras_layout, null);
                Button btnSeleccionarImagen = child.findViewById(R.id.btnSeleccionarImagen);
                btnSeleccionarImagen.setOnClickListener(v -> mostrarDialogoSeleccionImagen());
                break;

            case "Flujos":
                child = inflater.inflate(R.layout.flujos_layout, null);
                break;
        }

        if (child != null) {
            layoutCampos.addView(child);
        }
        layoutCampos.setVisibility(View.VISIBLE);


    }

    private void mostrarDialogoSeleccionImagen() {
        final CharSequence[] opciones = {"Tomar foto", "Elegir de galería", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, (dialog, which) -> {
            if (opciones[which].equals("Tomar foto")) {
                abrirCamara();
            } else if (opciones[which].equals("Elegir de galería")) {
                abrirGaleria();
            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void abrirCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void abrirGaleria() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imageUri = null; // Si se captura una nueva imagen, restablece la URI anterior
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                imageUri = data.getData();
                imageBitmap = null; // Si se selecciona una imagen, restablece el bitmap anterior
            }
        }
    }


    private String convertirImagenABase64(Uri imagenUri, ContentResolver contentResolver) {
        try {
            InputStream inputStream = contentResolver.openInputStream(imagenUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}


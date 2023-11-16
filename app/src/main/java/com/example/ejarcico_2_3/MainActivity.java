package com.example.ejarcico_2_3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private EditText descriptionEditText;
    private DatabaseHelper databaseHelper;
    private Uri selectedImageUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        databaseHelper = new DatabaseHelper(this);

        Button selectButton = findViewById(R.id.selectButton);
        selectButton.setOnClickListener(view -> openFileChooser());

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> savePhotograph());


    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Obtener la URI de la imagen seleccionada desde la galería
            selectedImageUri = data.getData();

            // Mostrar la imagen seleccionada en el ImageView
            imageView.setImageURI(selectedImageUri);
        }
    }

    private void savePhotograph() {
        // Obtener la imagen del ImageView
        Drawable drawable = imageView.getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        // Convertir la imagen a un array de bytes (BLOB)
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();

        // Obtener la descripción del EditText
        String description = descriptionEditText.getText().toString().trim();

        // Verificar que la descripción no esté vacía y que la imagen no sea nula
        if (!TextUtils.isEmpty(description) && imageBytes != null) {
            // Crear un objeto Photograph
            Photograph photograph = new Photograph(imageBytes, description);

            // Guardar en la base de datos usando el DatabaseHelper
            long id = databaseHelper.insertPhotograph(photograph);

            if (id != -1) {
                Toast.makeText(this, "Foto Salvada Correctamente con el ID No. " + id, Toast.LENGTH_SHORT).show();

                // Limpiar los campos después de guardar
                imageView.setImageResource(R.drawable.foto); //coloca imagen por defecto
                descriptionEditText.setText(""); //Limpia la descripcion de text
            } else {
                Toast.makeText(this, "Error al salvar la foto", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por faver agrege una descripcion", Toast.LENGTH_SHORT).show();
        }
    }

}

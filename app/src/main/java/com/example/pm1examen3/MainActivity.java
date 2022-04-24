package com.example.pm1examen3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm1examen3.configuracion.SQLiteConexion;
import com.example.pm1examen3.configuracion.Transacciones;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ImageView imageViewIngresar;
    TextView textSeleccionarFoto;
    ImageView imageViewimg;
    Spinner spinnerTiempo;
    EditText textDescripcion;
    EditText textCant;
    EditText textPeriodo;
    Button buttonfoto,buttonGuardar,buttonVista;

  //Trabajemos con array
    ArrayList<String> arrayTiempo;

    String currentPhotoPath;
    ActivityResultLauncher<Intent> launcherTomarFoto;


    AlertDialog.Builder builder;
    AlertDialog dialog;



    static final int TAKE_PIC_REQUEST = 101;
    static final int PETICION_ACCESO_CAM = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        currentPhotoPath = "";
        builder = null;
        dialog = null;

//MAKE YOUR LIST DEPENDS OF UR TIME
        arrayTiempo = new ArrayList<>();

        arrayTiempo.add("Lo necesario");

        arrayTiempo.add("El tiempo es diario");

        arrayTiempo.add("El tiempo es a la hora");

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayTiempo);
        spinnerTiempo = (Spinner) findViewById(R.id.spinnerTiempo);

        spinnerTiempo.setAdapter(spinnerArrayAdapter);

        textSeleccionarFoto = (TextView) findViewById(R.id.buttonfoto);
        imageViewIngresar = (ImageView) findViewById(R.id.imageViewimg);
        textDescripcion = (EditText) findViewById(R.id.textDescripcion);
        textCant = (EditText) findViewById(R.id.textCant);
       textPeriodo = (EditText) findViewById(R.id.textPeriodo);

      buttonGuardar = (Button) findViewById(R.id.buttonGuardar);
      buttonVista = (Button) findViewById(R.id.buttonVista);

        textSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        buttonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              save();
            }
        });

        buttonVista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),activitylista.class);
                startActivity(intent);
            }
        });
        launcherTomarFoto = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        Intent data = result.getData();

                        if (result.getResultCode() == Activity.RESULT_OK) {

                            Uri uri = Uri.parse(currentPhotoPath);
                            imageViewIngresar.setImageURI(uri);
                        }
                    }
                });

    }

    private void save() {
        if(contenido()){
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(Transacciones.Descrip, textDescripcion.getText().toString());
            values.put(Transacciones.Cant, textCant.getText().toString());
            values.put(Transacciones.Tiempo, spinnerTiempo.getSelectedItem().toString());
            values.put(Transacciones.Periodo,textPeriodo.getText().toString());

            values.put(Transacciones.IMAGEN, currentPhotoPath);

            Long result = db.insert(Transacciones.TABLA_Medica, Transacciones.ID, values);

            if(result>0){
                Toast.makeText(getApplicationContext(), "Almacenado!!" ,Toast.LENGTH_LONG).show();
               clean();
            }else {
                Toast.makeText(getApplicationContext(), "Error!!"
                        ,Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean contenido() {
        String Descripcion = textDescripcion.getText().toString();
        String Cantidad = textCant.getText().toString();
        String Periodo = textPeriodo.getText().toString();
        String Tiempo = spinnerTiempo.getSelectedItem().toString();
        String photoPath = currentPhotoPath;


        String mensaje="";

        if(isTextEmpty(Descripcion)) mensaje = "Llenar todos los campos";


        if(!isTextEmpty(mensaje)){
            show("Alerta", mensaje);
            return false;
        }

        return true;
    }

    private void show(String alerta, String mensaje) {
        builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(mensaje).setTitle(alerta);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void permisos() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PETICION_ACCESO_CAM);
        }else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PETICION_ACCESO_CAM){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }

        }else{
            Toast.makeText(getApplicationContext(), "Acceso limitados", Toast.LENGTH_LONG).show();
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir      /* directory */
        );


        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

                ex.toString();
            }

            try {
                if (photoFile != null) {

                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.pm1examen3.fileprovider",
                            photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    takePictureIntent.putExtra("request_code", TAKE_PIC_REQUEST);

                    launcherTomarFoto.launch(takePictureIntent);
                }
            }catch (Exception e){
                Log.i("Error", "dispatchTakePictureIntent: " + e.toString());
            }
        }
    }

    private static boolean isText(String text){


        Pattern pat = Pattern.compile("^[a-zA-ZáéíóúÁÉÓÚÍ ]+$");
        Matcher mat = pat.matcher(text);
        return (mat.matches());
    }


    private static boolean isTextEmpty(String text){
        return (text.length()==0)?true:false;
    }

    private void clean() {
        spinnerTiempo.setSelection(0);
        textDescripcion.setText("");
        textCant.setText("");
        textPeriodo.setText("");
        currentPhotoPath = "";






    }
}
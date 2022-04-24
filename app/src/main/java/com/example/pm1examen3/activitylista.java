package com.example.pm1examen3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Picture;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pm1examen3.configuracion.SQLiteConexion;
import com.example.pm1examen3.configuracion.Transacciones;
import com.example.pm1examen3.configuracion.medicamentos;

import java.util.ArrayList;

public class activitylista extends AppCompatActivity {
    SQLiteConexion conexion;

    ListView listViewVista;

    ArrayList<String> arrayListStringPicture;

    ArrayList<medicamentos> arrayListPicture;
    private Object medicamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitylista);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);

        listViewVista = (ListView) findViewById(R.id.ListViewVista);

        obtenerPictures();

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListStringPicture);

        listViewVista.setAdapter(adp);


        listViewVista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mostrarPicture(i);
            }
        });

    }

    private void mostrarPicture(int i){

        medicamentos picture = arrayListPicture.get(i);

        Bundle bundle = new Bundle();
        bundle.putSerializable("medicamentos", arrayListPicture);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void obtenerPictures() {
        SQLiteDatabase db = conexion.getReadableDatabase();

        medicamentos tempCont = null;


        medicamentos = new ArrayList<>();

        Cursor cursor = db.rawQuery(Transacciones.SELECT_TABLE_Medica, null);


        while (cursor.moveToNext()){

            tempCont = new medicamentos();

            tempCont.setId(cursor.getInt(0));
            tempCont.setDescripcion(cursor.getString(1));
            tempCont.setCantidad(cursor.getString(2));
            tempCont.setTiempo(cursor.getString(3));
            tempCont.setPeriocidad(cursor.getString(4));
            tempCont.setImagen(cursor.getString(5));

           arrayListPicture.add(tempCont);

        }

        cursor.close();

        llenarListaStringPicture();



    }

    private void llenarListaStringPicture(){

        arrayListStringPicture = new ArrayList<String>();

        for (medicamentos p: arrayListPicture) {

            arrayListStringPicture.add(
                    p.getId() + " | " +
                            p.getDescripcion()+
                            " - "+ p.getPeriocidad()
            );
        }

    }
}
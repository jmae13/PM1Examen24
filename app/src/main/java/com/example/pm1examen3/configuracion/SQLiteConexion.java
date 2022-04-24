package com.example.pm1examen3.configuracion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.Nullable;

public class SQLiteConexion extends SQLiteOpenHelper {


        public SQLiteConexion(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(Transacciones.CREATE_TABLE_Medica);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(Transacciones.DROP_TABLE_Medica);
            onCreate(sqLiteDatabase);
        }
}

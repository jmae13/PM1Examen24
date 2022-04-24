package com.example.pm1examen3.configuracion;

public class Transacciones {

    public static final String NameDatabase = " SQLiteDatabase";

    //Creacion de la tabla persona en la base de datos
    public static final String TABLA_Medica = "Medica";

    //Creacion de los atributos de la tabla
    public static final String ID = "id";
    public static final String Descrip = "descrip";
    public static final String Cant= "cant";
    public static final String Tiempo = "tiempo";
    public static final String Periodo = "periodo";
    public static final String IMAGEN = "imagen";


    //Creacion y eliminacion de la tabla

    public static final String CREATE_TABLE_Medica = "CREATE TABLE " + TABLA_Medica +
            "("+
            ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            Descrip +" TEXT, "+
            Cant +" TEXT, "+
            Tiempo +" TEXT, "+
            Periodo +" TEXT, "+
            IMAGEN +" TEXT"+
            ")";
    public static final String DROP_TABLE_Medica = "DROP TABLE IF EXIST " + TABLA_Medica;

    //Seleccionar todas las personas
    public static final String SELECT_TABLE_Medica= "SELECT * FROM " + TABLA_Medica;
}

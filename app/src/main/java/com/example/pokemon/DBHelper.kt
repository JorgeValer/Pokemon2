package com.example.pokemon

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "equipos.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE equipo (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT," +
                    "ciudad TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS equipo")
        onCreate(db)
    }

    fun insertarEquipo(equipo: Equipo) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", equipo.nombre)
            put("ciudad", equipo.ciudad)
        }
        db.insert("equipo", null, values)
        db.close()
    }

    fun obtenerEquipos(): ArrayList<Equipo> {
        val lista = ArrayList<Equipo>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM equipo", null)
        if (cursor.moveToFirst()) {
            do {
                val equipo = Equipo(
                    id = cursor.getInt(0),
                    nombre = cursor.getString(1),
                    ciudad = cursor.getString(2)
                )
                lista.add(equipo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun eliminarEquipo(id: Int) {
        val db = writableDatabase
        db.delete("equipo", "id=?", arrayOf(id.toString()))
        db.close()
    }
}

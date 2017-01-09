package com.grupohi.almacenv1.modelo;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CatMaterial {
	DB almacen;
	SQLiteDatabase db;
	final String log = "";


	public CatMaterial(Context context) {
		almacen = new DB(context);
	}
	public boolean altaCatMateriales(int id, String descripcion) throws JSONException {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_IDMATERIAL, id);
		values.put(DB.COLUMN_DESCRIPCION, descripcion);
		// db.insert(DB.TABLE_OBRAS, null, values);
		db.insertWithOnConflict(DB.TABLE_MATERIALES, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		 db.close();
		return true;
	}
	

}

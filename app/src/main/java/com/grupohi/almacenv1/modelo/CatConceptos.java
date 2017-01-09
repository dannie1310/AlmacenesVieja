package com.grupohi.almacenv1.modelo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.grupohi.almacenv1.lib.SpinnerObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CatConceptos {
	DB almacen;
	SQLiteDatabase db;
	final String log = "";
	Context context;
	public CatConceptos(Context context) {
		almacen = new DB(context);
		this.context=context;
	}
	public boolean altaCatConcepto(int id_concepto, int id_obra, String descripcion, String clave_concepto) throws JSONException {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_IDCONCEPTO, id_concepto);
		values.put(DB.COLUMN_DESCRIPCION, descripcion);
		values.put(DB.COLUMN_IDOBRA, id_obra);
		values.put(DB.COLUMN_CLAVECONCEPTO, clave_concepto);
		// db.insert(DB.TABLE_OBRAS, null, values);
		db.insertWithOnConflict(DB.TABLE_CONCEPTOS, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		 db.close();
		return true;
	}
	public ArrayList<String> getAllLabels() {
		Usuario usuario= new Usuario(context);
		int id_obra=(new Obras(context)).get_idobra(usuario.getObraActiva());
		ArrayList<String> array = new ArrayList<String>();
		String selectQuery = "Select * from " + DB.TABLE_CONCEPTOS + " where "+DB.COLUMN_IDOBRA+"="+id_obra;
		//Log.d(log, selectQuery);
		db = almacen.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor
						.getColumnIndex(DB.COLUMN_IDCONCEPTO));
				String descripcion = cursor.getString(cursor
						.getColumnIndex(DB.COLUMN_DESCRIPCION));
				String clave_concepto = cursor.getString(cursor
						.getColumnIndex(DB.COLUMN_CLAVECONCEPTO));
				Log.i("conxepto", clave_concepto+"=="+descripcion);
				array.add(clave_concepto);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return array;
	}

}

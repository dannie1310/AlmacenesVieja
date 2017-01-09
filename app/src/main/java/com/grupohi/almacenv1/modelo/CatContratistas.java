package com.grupohi.almacenv1.modelo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.grupohi.almacenv1.lib.SpinnerObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CatContratistas {
	DB almacen;
	SQLiteDatabase db;
	final String log = "";


	public CatContratistas(Context context) {
		almacen = new DB(context);
	}
	public boolean altaCatContratistas(int id, String descripcion) throws JSONException {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_IDCONTRATISTA, id);
		values.put(DB.COLUMN_DESCRIPCION, descripcion);
		// db.insert(DB.TABLE_OBRAS, null, values);
		db.insertWithOnConflict(DB.TABLE_CONTRATISTAS, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		 db.close();
		return true;
	}
	
	public List<SpinnerObject> getAllLabels(Context context) {
		List<SpinnerObject> labels = new ArrayList<SpinnerObject>();

		String selectQuery = "Select * from " + DB.TABLE_CONTRATISTAS;
		//Log.d(log, selectQuery);
		db = almacen.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		labels.add(new SpinnerObject(0, "- Selecione -"));
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor
						.getColumnIndex(DB.COLUMN_IDCONTRATISTA));
				String descripcion = cursor.getString(cursor
						.getColumnIndex(DB.COLUMN_DESCRIPCION));
				labels.add(new SpinnerObject(id, descripcion));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return labels;
	}
	

}

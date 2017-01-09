package com.grupohi.almacenv1.modelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.grupohi.almacenv1.modelo.*;

public class PermisosProyectos {
	public static final String log = "Permisos Proyecto";
	public static DB maquinaria;
	public static SQLiteDatabase db;
	Context _context = null;

	public PermisosProyectos(Context context) {
		_context = context;
		maquinaria = new DB(context);
	}

	public boolean altaPermisos(int idusuario, int idobra) throws JSONException {
		db = maquinaria.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_IDUSUARIO, idusuario);
		values.put(DB.COLUMN_IDOBRA, idobra);
		// db.insert(DB.TABLE_USUARIOS, null, values);
		db.insertWithOnConflict(DB.TABLE_PERMISO_PROYECTOS, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		return true;
	}
	public int count(int idusuario) {
		db = maquinaria.getReadableDatabase();
		try {
			if (db != null) {
				Cursor cursor =db.rawQuery("select count(*) from " + DB.TABLE_PERMISO_PROYECTOS + " WHERE "+idusuario, null);

				// ensure there is at least one row and one column
				cursor.moveToFirst();
				if (cursor.getCount() > 0 && cursor.getColumnCount() > 0) {
				    return cursor.getInt(0);
				} else {
				    return 0;
				}
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return 0;
		}
		return 0;
	}
	
	


}

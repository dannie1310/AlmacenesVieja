package com.grupohi.almacenv1.modelo;

import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MaterialesAlamacen {
	DB almacen;
	SQLiteDatabase db;
	final String log = "";


	public MaterialesAlamacen(Context context) {
		almacen = new DB(context);
	}
	public boolean altaMaterialesAlamace(int idalmacen, int idmaterial,  int idobra, String unidad,  Double existencia) throws JSONException {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		// validar que el material que se esta dando de alta no exista en el material si existe solo
		// se suma la existencia y actualiza el campo de cantidad.
		
		if(getID(idalmacen, idmaterial)>0){
			String where =" "+DB.COLUMN_IDALMACEN+"="+idalmacen+ " AND "+DB.COLUMN_IDMATERIAL+"="+idmaterial;
			Double cantidad=getCantidad(idalmacen, idmaterial)+existencia;
			values.put(DB.COLUMN_EXISTENCIA, cantidad);
			db.update(DB.TABLE_EXISTENCIASALMACEN, values, where, null);
		}
		else{
			values.put(DB.COLUMN_EXISTENCIA, existencia);
			values.put(DB.COLUMN_IDALMACEN, idalmacen);
			values.put(DB.COLUMN_IDMATERIAL, idmaterial);
			values.put(DB.COLUMN_IDOBRA, idobra);
			values.put(DB.COLUMN_UNIDAD, unidad);
			db.insertWithOnConflict(DB.TABLE_EXISTENCIASALMACEN, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
		}
		 db.close();
		return true;
	}
	private int getID(int id_almacen, int id_material){
		db = almacen.getWritableDatabase();
		String where =" "+DB.COLUMN_IDALMACEN+"="+id_almacen+ " AND "+DB.COLUMN_IDMATERIAL+"="+id_material;
	    Cursor c = db.query(DB.TABLE_EXISTENCIASALMACEN, new String[]{"_id"}, where,null,null,null,null,null);
	    if (c.moveToFirst()) //if the row exist then return the id
	        return c.getInt(c.getColumnIndex("_id"));
	    return -1;
	}
	private Double getCantidad(int id_almacen, int id_material){
		db = almacen.getWritableDatabase();
		String where =" "+DB.COLUMN_IDALMACEN+"="+id_almacen+ " AND "+DB.COLUMN_IDMATERIAL+"="+id_material;
	    Cursor c = db.query(DB.TABLE_EXISTENCIASALMACEN, new String[]{DB.COLUMN_EXISTENCIA}, where,null,null,null,null,null);
	    if (c.moveToFirst()) //if the row exist then return the id
	        return c.getDouble(c.getColumnIndex(DB.COLUMN_EXISTENCIA));
	    return 0.0;
	}
	
}

package com.grupohi.almacenv1.modelo;

import java.sql.NClob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.grupohi.almacenv1.lib.*;
import com.grupohi.almacenv1.modelo.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Obras {
	DB maquinaria;
	SQLiteDatabase db;
	final String log = "";

	String code = null;
	String name = null;
	boolean selected = false;

	public Obras(Context context) {
		maquinaria = new DB(context);
	}

	public Obras(Context context, String code, String name, boolean selected) {
		maquinaria = new DB(context);
		this.code = code;
		this.name = name;
		this.selected = selected;
	}
	public boolean altaObra(int id, String descripcion, int idbase, String nombrebase) throws JSONException {
		db = maquinaria.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_IDOBRA, id);
		values.put(DB.COLUMN_DESCRIPCION, descripcion);
		values.put(DB.COLUMN_IDBASE, idbase);
		values.put(DB.COLUMN_NOMBREBASE, nombrebase);
		// db.insert(DB.TABLE_OBRAS, null, values);
		db.insertWithOnConflict(DB.TABLE_OBRAS, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		return true;
	}
	
	
	/*public ArrayList getObras(){		
		ArrayList<String> ObraList = new ArrayList<String>();
		
		Cursor mCursor;
		db = maquinaria.getReadableDatabase();
		try {
			if (db != null) {
				String[] columns = { DB.COLUMN_DESCRIPCION, DB.COLUMN_IDOBRA };
				mCursor = db.query(DB.TABLE_OBRAS, columns, null, null, null,
						null, null, "1");
				if (mCursor.moveToFirst()) {
					do {
						Log.d(log,
								"Obra: "
										+ mCursor.getString(mCursor
												.getColumnIndex(DB.COLUMN_DESCRIPCION)));
						// return
						// mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDCONCEPTO));
						/*return mCursor.getString(mCursor
								.getColumnIndex(DB.COLUMN_DESCRIPCION));*/
						/*ObraList.add(0, "");
						
					} while (mCursor.moveToNext());
				}
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return null;
		}
		return ObraList;
	}*/

	public String getObra() { // solo regresa el primer proyecto
		Cursor mCursor=null;
		db = maquinaria.getReadableDatabase();
		try {
			if (db != null) {
				String[] columns = { DB.COLUMN_DESCRIPCION };
				mCursor = db.query(DB.TABLE_OBRAS, columns, null, null, null,
						null, null, "1");
				if (mCursor.moveToFirst()) {
					do {
						Log.d(log,
								"Obra: "
										+ mCursor.getString(mCursor
												.getColumnIndex(DB.COLUMN_DESCRIPCION)));
						// return
						// mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDCONCEPTO));
						return mCursor.getString(mCursor
								.getColumnIndex(DB.COLUMN_DESCRIPCION));
					} while (mCursor.moveToNext());
				}
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return null;
		}finally {
			mCursor.close();
		}
		
		return null;
	}
 public int get_idobra(int id){	    
       String selectQuery="Select "+ DB.COLUMN_IDOBRA+" from "+DB.TABLE_OBRAS +" WHERE "+DB.COLUMN_ID+"="+id+" limit 1"; //+" where "+DB.COLUMN_IDOBRA+" in(SELECT "+DB.COLUMN_IDOBRA+" FROM "+ DB.TABLE_PERMISO_PROYECTOS +" where "+ DB.COLUMN_IDUSUARIO +"=" + usuario.getIdusuarioActivo() + ") ";
	    Log.d(log,selectQuery);
	    int id_viaje=0;
       db = maquinaria.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    if ( cursor.moveToFirst () ) {
	        do {
	        	id_viaje=cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDOBRA));
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    db.close();
	    return id_viaje;
   }
	public int count() {
		Cursor mCursor=null;
		int cantidad = 0;
		db = maquinaria.getReadableDatabase();
		try {
			if (db != null) {
				String[] columns = { DB.COLUMN_DESCRIPCION };
				mCursor = db.query(DB.TABLE_OBRAS, columns, null, null, null,
						null, null, null);
				if (mCursor.moveToFirst()) {
					do {
						cantidad++;
					} while (mCursor.moveToNext());
				}
				return cantidad;
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return 0;
		}
		finally {
			mCursor.close();
		}
		return 0;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public List < SpinnerObject> getAllLabels(Context context){
	    List < SpinnerObject > labels = new ArrayList < SpinnerObject > ();
	    // Select All Query
	    //String selectQuery = "SELECT  * FROM " + DB.TABLE_MAQUINAS;
	    Usuario usuario= new Usuario(context);
	    
        String selectQuery="Select * from "+DB.TABLE_OBRAS +" ORDER BY "+DB.COLUMN_DESCRIPCION; //+" where "+DB.COLUMN_IDOBRA+" in(SELECT "+DB.COLUMN_IDOBRA+" FROM "+ DB.TABLE_PERMISO_PROYECTOS +" where "+ DB.COLUMN_IDUSUARIO +"=" + usuario.getIdusuarioActivo() + ") ";
	    Log.d(log,selectQuery);
        db = maquinaria.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
	    labels.add ( new SpinnerObject (0 ,"- Selecione -" ) );

	    // looping through all rows and adding to list
	    if ( cursor.moveToFirst () ) {
	        do {
	        	int id=cursor.getInt(cursor.getColumnIndex(DB.COLUMN_ID));
	        	String descripcion=cursor.getString(cursor.getColumnIndex(DB.COLUMN_DESCRIPCION));
	        	String nombrebase=cursor.getString(cursor.getColumnIndex(DB.COLUMN_NOMBREBASE));
	        	labels.add ( new SpinnerObject (id ,descripcion+" ("+nombrebase+")" ) );
	        } while (cursor.moveToNext());
	    }
	    // closing connection
	    cursor.close();
	    db.close();

	    // returning labels
	    return labels;
	}
	public int getBase(int id){
		 String selectQuery="Select * from "+DB.TABLE_OBRAS+" WHERE "+DB.COLUMN_ID+"="+id; //+" where "+DB.COLUMN_IDOBRA+" in(SELECT "+DB.COLUMN_IDOBRA+" FROM "+ DB.TABLE_PERMISO_PROYECTOS +" where "+ DB.COLUMN_IDUSUARIO +"=" + usuario.getIdusuarioActivo() + ") ";
		    //Log.d(log,selectQuery);
	        db = maquinaria.getReadableDatabase();
		    Cursor cursor = db.rawQuery(selectQuery, null);
		    int idbase=0;
		    // looping through all rows and adding to list
		    if ( cursor.moveToFirst () ) {
		        do {
		        	idbase =cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDBASE));
		            } while (cursor.moveToNext());
		    }
		    cursor.close();
		    db.close();
		    return idbase;
	}
}

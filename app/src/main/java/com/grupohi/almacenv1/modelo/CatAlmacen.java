package com.grupohi.almacenv1.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;

import com.grupohi.almacenv1.lib.SpinnerObject;
import com.grupohi.almacenv1.lib.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.util.Log;

public class CatAlmacen {
	DB almacen;
	SQLiteDatabase db;
	final String log = "";


	public CatAlmacen(Context context) {
		almacen = new DB(context);
	}
	public boolean altaCatAlmacenes(int id, String descripcion) throws JSONException {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_IDALMACEN, id);
		values.put(DB.COLUMN_DESCRIPCION, descripcion);
		// db.insert(DB.TABLE_OBRAS, null, values);
		db.insertWithOnConflict(DB.TABLE_ALMACEN, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		 db.close();
		return true;
	}
	public List<SpinnerObject> getAllLabels(Context context, int almacen_seleted) {
		List<SpinnerObject> labels = new ArrayList<SpinnerObject>();

		String selectQuery = "Select * from " + DB.TABLE_ALMACEN + " WHERE "+DB.COLUMN_IDALMACEN+" != "+almacen_seleted;
		//Log.d(log, selectQuery);
		db = almacen.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		labels.add(new SpinnerObject(0, "- Selecione -"));
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor
						.getColumnIndex(DB.COLUMN_IDALMACEN));
				String descripcion = cursor.getString(cursor
						.getColumnIndex(DB.COLUMN_DESCRIPCION));
				labels.add(new SpinnerObject(id, descripcion));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return labels;
	}
	public List<ListviewGenerico> getListViewAlmacenMateriales(Context context, int idalmacen) {
		List<ListviewGenerico> labels = new ArrayList<ListviewGenerico>();
		
		String selectQuery = "SELECT EA."+DB.COLUMN_IDMATERIAL+", EA."+DB.COLUMN_EXISTENCIA+", "+ " M."+DB.COLUMN_DESCRIPCION +",EA."+DB.COLUMN_UNIDAD +",EA."+DB.COLUMN_IDOBRA+", A."+DB.COLUMN_DESCRIPCION+" AS nombrealmacen  FROM " + DB.TABLE_EXISTENCIASALMACEN+ " EA "+ 
				" INNER JOIN "+DB.TABLE_MATERIALES+" M "+" ON  M."+DB.COLUMN_IDMATERIAL+" = EA."+DB.COLUMN_IDMATERIAL+
				" INNER JOIN "+DB.TABLE_ALMACEN+" A "+" ON  A."+DB.COLUMN_IDALMACEN+" = EA."+DB.COLUMN_IDALMACEN+
				" WHERE EA."+ DB.COLUMN_IDALMACEN+" = "+idalmacen;
		Log.d(log, selectQuery);
		db = almacen.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		//labels.add(new ListViewOrdenesCompra(0, , selectQuery, existencia));
		// looping through all rows and adding to list
		
		if (cursor.moveToFirst()) {
			do {
				int idmaterial= cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDMATERIAL));
				//int idalmacen= cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDALMACEN));
				Double exist = cursor.getDouble(cursor.getColumnIndex(DB.COLUMN_EXISTENCIA));
				String descripcion = cursor.getString(cursor.getColumnIndex(DB.COLUMN_DESCRIPCION));
				String unidad = cursor.getString(cursor.getColumnIndex(DB.COLUMN_UNIDAD));
				String nombrealmacen = cursor.getString(cursor.getColumnIndex("nombrealmacen"));
				int idobra = cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDOBRA));
			   Log.d(log, "existencia "+exist+"-- "+descripcion);
				
				labels.add(new ListviewGenerico(idmaterial, descripcion, unidad, exist,idobra, idalmacen, nombrealmacen, 0,0,""));
				//labels.add(new ListViewOrdenesCompra(descripcion, unidad, exist));
				//labels.add(new ListViewOrdenesCompra(descripcion, unidad, existencia));
				//labels.add(new SpinnerObject(trans, "#"+trans+" - "+descripcion));
				//Log.d(log, "termino registro 1");
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		//Log.d(log, "returnando..");
		return labels;
	}
	
	/*empieza lo bueno depurar*/
	
	public String guardarsalidatransferencia( List<ListviewGenerico> existencias, List<ListviewGenerico> salidatransferencia, String clavemobil, String observacion){
		int size = existencias.size();
		for (int i = 0; i < size; i++){
		    Double existencia =existencias.get(i).getexistencia();
		    int idmaterial =existencias.get(i).getidmaterial();
		    int idalmacen =existencias.get(i).getidalmacen();
		    updateexistencia(existencia,idmaterial,idalmacen);		    
		}
		
		String cadenaimprimir="";
		size =salidatransferencia.size();
		Vector vector=new Vector();
		for (int i = 0; i < size; i++){		
			int cont=0;
			for (int j=0; j<vector.size(); j++){
				if(vector.elementAt(j).equals(salidatransferencia.get(i).getnombrealmacne()))
					cont++;	
			}
			if(cont==0)
				vector.add(salidatransferencia.get(i).getnombrealmacne());			
		}
		cadenaimprimir+="SALIDA TRANSFERECIA\n";
		for (int j=0; j<vector.size(); j++) {
			Log.e("obra-concepto",vector.elementAt(j)+"");			
			cadenaimprimir+="--------------------------------------------\n";
			cadenaimprimir+=vector.elementAt(j).toString()+"\n";
			cadenaimprimir+="--------------------------------------------\n";
			for (int i = 0; i < size; i++){
					if(vector.elementAt(j).equals(salidatransferencia.get(i).getnombrealmacne())){
						Log.e("concepto","  "+salidatransferencia.get(i).getexistencia()+" - "+salidatransferencia.get(i).getunidad()+"\n   "+salidatransferencia.get(i).getdescripcion());
						cadenaimprimir+="  "+salidatransferencia.get(i).getexistencia()+" - "+salidatransferencia.get(i).getunidad()+"\n   "+salidatransferencia.get(i).getdescripcion()+"\n";
						if(salidatransferencia.get(i).getidcontratista()>0){
							cadenaimprimir+="   Entregado a: "+salidatransferencia.get(i).getnombrecontratista()+"";	
							Log.e("material","  "+salidatransferencia.get(i).getnombrecontratista());
							if(salidatransferencia.get(i).getconcargo()>0){
								cadenaimprimir+="(Con cargo)\n";
								Log.e("material"," -> Con cargo");
							}
							
						}	
					}
							
				
			}
		}
		cadenaimprimir+="\n\nObservaciones: "+observacion;
		
		for (int i = 0; i < size; i++){
			 Double existencia =salidatransferencia.get(i).getexistencia();
			 int idmaterial =salidatransferencia.get(i).getidmaterial();
			 int idalmacen =salidatransferencia.get(i).getidalmacen();
			 String unidad=salidatransferencia.get(i).getunidad();	
			 int idcontratista=salidatransferencia.get(i).getidcontratista();	
			 int concargo=salidatransferencia.get(i).getconcargo();
			 
			 salidatrasferenciapartidas(existencia, idmaterial, unidad, idalmacen, clavemobil, idcontratista, concargo);
		}
		return  cadenaimprimir;
		
	}
	public boolean updateexistencia(Double existencia, int idmaterial, int idalmacen) {
		db = almacen.getWritableDatabase();
		db.execSQL("UPDATE " + DB.TABLE_EXISTENCIASALMACEN + " SET "+DB.COLUMN_EXISTENCIA+"="+existencia+" where "+DB.COLUMN_IDMATERIAL+"="+idmaterial+" AND "+ DB.COLUMN_IDALMACEN+" ="+idalmacen);
		db.close();
		return true;
		
	}
	public boolean salidatrasferenciapartidas(Double cantidad, int idmaterial, String unidad, int idalmacen,  String clavemobil, int idcontratista, int concargo) {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_CLAVESALIDA, clavemobil);
		values.put(DB.COLUMN_IDMATERIAL, idmaterial);
		values.put(DB.COLUMN_EXISTENCIA, cantidad);
		values.put(DB.COLUMN_UNIDAD,unidad);
		values.put(DB.COLUMN_IDALMACEN, idalmacen);
		
		values.put(DB.COLUMN_IDCONTRATISTA, idcontratista);
		values.put(DB.COLUMN_CONCARGO, concargo);
	
		// db.insert(DB.TABLE_USUARIOS, null, values);
		db.insertWithOnConflict(DB.TABLE_SALIDAS_TRANSFERENCIA_PARTIDADAS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		return true;		
	}
	public boolean salidatransferencia(String clavemobil, String observaciones, int idalmacen, int idobra, String Referencia) {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_CLAVESALIDA, clavemobil);
		values.put(DB.COLUMN_OBSERVACIONES, observaciones);
		values.put(DB.COLUMN_IDALMACEN, idalmacen);
		values.put(DB.COLUMN_IDOBRA, idobra);
		values.put(DB.COLUMN_REFERENCIA, Referencia);
	
		// db.insert(DB.TABLE_USUARIOS, null, values);
		db.insertWithOnConflict(DB.TABLE_SALIDAS_TRANSFERENCIA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
		return true;		
	}
	public String guardarsalidainsumos( List<ListviewGenerico> existencias, List<ListviewGenerico> salidainsumos, String clavemobil, String observacion, String referencia){

		int size = existencias.size();
		for (int i = 0; i < size; i++){
		    Double existencia =existencias.get(i).getexistencia();
		    int idmaterial =existencias.get(i).getidmaterial();
		    int idalmacen =existencias.get(i).getidalmacen();
		    updateexistencia(existencia,idmaterial,idalmacen);		    
		}
		
		String cadenaimprimir="";
		size =salidainsumos.size();
		Vector vector=new Vector();
		for (int i = 0; i < size; i++){		
			int cont=0;
			for (int j=0; j<vector.size(); j++){
				if(vector.elementAt(j).equals(salidainsumos.get(i).getnombrealmacne()))
					cont++;	
			}
			if(cont==0)
				vector.add(salidainsumos.get(i).getnombrealmacne());			
		}
		cadenaimprimir+="SALIDA INSUMOS\n";
		for (int j=0; j<vector.size(); j++) {
			Log.e("obra-concepto",vector.elementAt(j)+"");			
			cadenaimprimir+="--------------------------------------------\n";
			cadenaimprimir+=vector.elementAt(j).toString()+"\n";
			cadenaimprimir+="--------------------------------------------\n";
			for (int i = 0; i < size; i++){
					if(vector.elementAt(j).equals(salidainsumos.get(i).getnombrealmacne())){
						Log.e("concepto","  "+salidainsumos.get(i).getexistencia()+" - "+salidainsumos.get(i).getunidad()+"\n   "+salidainsumos.get(i).getdescripcion());
						cadenaimprimir+="  "+salidainsumos.get(i).getexistencia()+" - "+salidainsumos.get(i).getunidad()+"\n   "+salidainsumos.get(i).getdescripcion()+"\n";
						if(salidainsumos.get(i).getidcontratista()>0){
							cadenaimprimir+="   Entregado a: "+salidainsumos.get(i).getnombrecontratista()+"";	
							Log.e("material","  "+salidainsumos.get(i).getnombrecontratista());
							if(salidainsumos.get(i).getconcargo()>0){
								cadenaimprimir+="(Con cargo)";
								Log.e("material"," -> Con cargo");
							}
							
						}	
					}
								
				
			}
		}
		cadenaimprimir+="\n\nObservaciones: "+observacion;
		
		for (int i = 0; i < size; i++){
			 Double existencia =salidainsumos.get(i).getexistencia();
			 int idmaterial =salidainsumos.get(i).getidmaterial();
			 //int idalmacen =salidainsumos.get(i).getidalmacen();
			 String claveconcepto=salidainsumos.get(i).getnombrealmacne();
			 String unidad=salidainsumos.get(i).getunidad();	
			 
			 int idcontratista=salidainsumos.get(i).getidcontratista();
			 int concargo=salidainsumos.get(i).getconcargo();
			 salidainsumospartidas(existencia, idmaterial, unidad, claveconcepto, clavemobil, idcontratista, concargo);
		}
		return  cadenaimprimir;
		
	}
	public boolean salidainsumospartidas(Double cantidad, int idmaterial, String unidad, String claveconcepto,  String clavemobil, int idcontratista, int concargo) {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_CLAVESALIDA, clavemobil);
		values.put(DB.COLUMN_IDMATERIAL, idmaterial);
		values.put(DB.COLUMN_EXISTENCIA, cantidad);
		values.put(DB.COLUMN_UNIDAD,unidad);
		values.put(DB.COLUMN_CLAVECONCEPTO, claveconcepto);
		
		values.put(DB.COLUMN_IDCONTRATISTA, idcontratista);
		values.put(DB.COLUMN_CONCARGO, concargo);
	
		// db.insert(DB.TABLE_USUARIOS, null, values);
		db.insertWithOnConflict(DB.TABLE_SALIDAS_INSUMOS_PARTIDADAS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
		return true;		
	}
	public boolean salidainsumos(String clavemobil, String observaciones, int idalmacen, int idobra, String Referencia, String clave_concepto) {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_CLAVESALIDA, clavemobil);
		values.put(DB.COLUMN_OBSERVACIONES, observaciones);
		values.put(DB.COLUMN_IDALMACEN, idalmacen);
		values.put(DB.COLUMN_IDOBRA, idobra);
		values.put(DB.COLUMN_REFERENCIA, Referencia);
		values.put(DB.COLUMN_CLAVECONCEPTO, clave_concepto);		
	
		// db.insert(DB.TABLE_USUARIOS, null, values);
		db.insertWithOnConflict(DB.TABLE_SALIDAS_INSUMOS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
		return true;		
	}
	
}

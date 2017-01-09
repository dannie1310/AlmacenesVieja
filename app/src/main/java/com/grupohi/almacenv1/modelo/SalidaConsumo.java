package com.grupohi.almacenv1.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SalidaConsumo {
	DB almacen;
	SQLiteDatabase db;
	final String log = "";
	Context conthis;

	public SalidaConsumo(Context context) {
		almacen = new DB(context);
		conthis=context;
	}
	
	public List<ListviewGenerico> getListViewSalidaConsumo(Context context) {
		List<ListviewGenerico> labels = new ArrayList<ListviewGenerico>();
		
		String selectQuery = "SELECT SC.*,  A."+DB.COLUMN_DESCRIPCION+" FROM "
				+ DB.TABLE_SALIDAS_INSUMOS+ " SC "+ 
				" INNER JOIN "+DB.TABLE_ALMACEN+" A "+" ON  A."+DB.COLUMN_IDALMACEN+" = SC."+DB.COLUMN_IDALMACEN
				;//+" WHERE EA."+ DB.COLUMN_IDALMACEN+" = "+idalmacen;
		Log.d(log, selectQuery);
		db = almacen.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		//labels.add(new ListViewOrdenesCompra(0, , selectQuery, existencia));
		// looping through all rows and adding to list
		
		if (cursor.moveToFirst()) {
			do {
				int idalmacen= cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDALMACEN));
				String clavemobil = cursor.getString(cursor.getColumnIndex(DB.COLUMN_CLAVESALIDA));
				String nombrealmacen = cursor.getString(cursor.getColumnIndex(DB.COLUMN_DESCRIPCION));
				String observacion = cursor.getString(cursor.getColumnIndex(DB.COLUMN_OBSERVACIONES));
				String fechahora= cursor.getString(cursor.getColumnIndex(DB.COLUMN_FECHA_HORA));
				
				labels.add(new ListviewGenerico(0, clavemobil, observacion, 0.0,0, idalmacen, nombrealmacen,fechahora));
				//int idmaterial, String descripcion, String unidad, Double existencia, int idobra, int idalmacen, String nombrealmacen) {
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
	public List<ListviewGenerico> SalidaConsumoMateriales(String clavesalida) {
		List<ListviewGenerico> labels = new ArrayList<ListviewGenerico>();
		
		String selectQuery = "SELECT SCP.*, M."+DB.COLUMN_DESCRIPCION+", C."+DB.COLUMN_DESCRIPCION+" as nombrecontratista FROM "
				+ DB.TABLE_SALIDAS_INSUMOS_PARTIDADAS+ " SCP "
				+" LEFT JOIN "+DB.TABLE_MATERIALES+" M "+" ON  M."+DB.COLUMN_IDMATERIAL+" = SCP."+DB.COLUMN_IDMATERIAL
				+" LEFT JOIN "+DB.TABLE_CONTRATISTAS+" C "+" ON  C."+DB.COLUMN_IDCONTRATISTA+" = SCP."+DB.COLUMN_IDCONTRATISTA
				+" WHERE SCP."+ DB.COLUMN_CLAVESALIDA+" = \""+clavesalida+"\"";
		Log.d(log, selectQuery);
		db = almacen.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Double exist = cursor.getDouble(cursor.getColumnIndex(DB.COLUMN_EXISTENCIA));
				String nombrematerial = cursor.getString(cursor.getColumnIndex(DB.COLUMN_DESCRIPCION));
				String unidad = cursor.getString(cursor.getColumnIndex(DB.COLUMN_UNIDAD));
				String claveconcepto = cursor.getString(cursor.getColumnIndex(DB.COLUMN_CLAVECONCEPTO));
				
				String nombrecontratista = cursor.getString(cursor.getColumnIndex("nombrecontratista"));
				
				int idcontratista = cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDCONTRATISTA));
				int concargo = cursor.getInt(cursor.getColumnIndex(DB.COLUMN_CONCARGO));
				
				labels.add(new ListviewGenerico(0, nombrematerial, unidad, exist,0, 0, claveconcepto,idcontratista,concargo,nombrecontratista));
			} while (cursor.moveToNext());
			
		}
		cursor.close();
		db.close();
		return labels;
	}
	
	public String getMaterialesSalidaConsumo(String clave){
		//SalidaConsumoMateriales(clave);
		List<ListviewGenerico> salidaConsumo=SalidaConsumoMateriales(clave);
		int size = salidaConsumo.size();
		
		String cadenaimprimir="";
		Vector vector=new Vector();
		for (int i = 0; i < size; i++){		
			int cont=0;
			for (int j=0; j<vector.size(); j++){
				if(vector.elementAt(j).equals(salidaConsumo.get(i).getnombrealmacne()))
					cont++;	
			}
			if(cont==0)
				vector.add(salidaConsumo.get(i).getnombrealmacne());			
		}
		cadenaimprimir+="SALIDA CONSUMO\n";
		for (int j=0; j<vector.size(); j++) {
			Log.e("obra-concepto",vector.elementAt(j)+"");			
			cadenaimprimir+="--------------------------------------------\n";
			cadenaimprimir+=vector.elementAt(j).toString()+"\n";
			cadenaimprimir+="--------------------------------------------\n";
			for (int i = 0; i < size; i++){
					if(vector.elementAt(j).equals(salidaConsumo.get(i).getnombrealmacne())){
						Log.e("concepto","  "+salidaConsumo.get(i).getexistencia()+" - "+salidaConsumo.get(i).getunidad()+"\n   "+salidaConsumo.get(i).getdescripcion());
						cadenaimprimir+="  "+salidaConsumo.get(i).getexistencia()+" - "+salidaConsumo.get(i).getunidad()+"\n   "+salidaConsumo.get(i).getdescripcion()+"\n";
						if(salidaConsumo.get(i).getidcontratista()>0){
							cadenaimprimir+="   Entregado a: "+salidaConsumo.get(i).getnombrecontratista()+"";	
							Log.e("material","  "+salidaConsumo.get(i).getnombrecontratista());
							if(salidaConsumo.get(i).getconcargo()>0){
								cadenaimprimir+="(Con cargo)\n";
								Log.e("material"," -> Con cargo");
							}
							
						}	
					}
						
					
			}
		}
		//cadenaimprimir+="\n\nObservaciones: "+observacion;*/
		
		return  cadenaimprimir;
		
	}
	
	
	public JSONObject getdata_consumos(){
		Cursor mCursor;
		Usuario usr=new Usuario(conthis);
		Obras obra=new Obras(conthis);
		JSONObject json=new JSONObject();
		int cont=0;
		db = almacen.getReadableDatabase();		
		try {
			if (db != null) {
				String[] columns = {  
									 DB.COLUMN_OBSERVACIONES, 
									 DB.COLUMN_FECHA_HORA, 
									 DB.COLUMN_CLAVESALIDA,
									 DB.COLUMN_IDALMACEN	
								   };
				mCursor = db.query(DB.TABLE_SALIDAS_INSUMOS, columns, null, null, null,
						null, null, null);
				if (mCursor.moveToFirst()) {
					do {
						JSONObject data=new JSONObject();
						//Log.d("class actividad ",  mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDACTIVIDAD))+"");
					data.put(DB.COLUMN_IDALMACEN, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDALMACEN)));
					data.put(DB.COLUMN_OBSERVACIONES, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_OBSERVACIONES)));
					data.put(DB.COLUMN_FECHA_HORA, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_FECHA_HORA)));
					data.put(DB.COLUMN_CLAVESALIDA, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_CLAVESALIDA)));	
					data.put(DB.COLUMN_IDOBRA,usr.getObraActiva());
					data.put(DB.COLUMN_IDBASE,obra.getBase(usr.getObraActiva()));
					json.put(cont++ +"", data);					
					} while (mCursor.moveToNext());
				}
				
				return json;			
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return null;
		}
		
		return null;
	}
	public void deletedata(){
		try {
			Log.i("eliminar entradas","entro");
			db = almacen.getReadableDatabase();
			//db.rawQuery("TRUNCATE TABLE "+DB.TABLE_ACTIVIDES, null);
			db.execSQL("delete from "+ DB.TABLE_SALIDAS_INSUMOS);
			db.execSQL("delete from "+ DB.TABLE_SALIDAS_INSUMOS_PARTIDADAS);
			//db.delete(DB.TABLE_ACTIVIDES, null, null);
			db.close();
			//Log.i("error al eliminar actividades","sumula que elimina datos");
		} catch (Exception e) {
			Log.e("error al eliminar actividades", e.toString());
		}
		//maquinaria.getWritableDatabase().execSQL("DELETE FROM " + DB.TABLE_ACTIVIDES + ";");
	}
	public JSONObject getdata_consumos_partidas(){
		Cursor mCursor;
		
		JSONObject json=new JSONObject();
		int cont=0;
		db = almacen.getReadableDatabase();		
		try {
			if (db != null) {
				String[] columns = {  
						DB.COLUMN_CLAVESALIDA
						, DB.COLUMN_IDMATERIAL
						, DB.COLUMN_UNIDAD
						, DB.COLUMN_EXISTENCIA
						, DB.COLUMN_CLAVECONCEPTO
						, DB.COLUMN_IDCONTRATISTA
						, DB.COLUMN_CONCARGO
						};
				mCursor = db.query(DB.TABLE_SALIDAS_INSUMOS_PARTIDADAS, columns, null, null, null,
						null, null, null);
				if (mCursor.moveToFirst()) {
					do {
						JSONObject data=new JSONObject();				
					
						//Log.d("class actividad ",  mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDACTIVIDAD))+"");
					data.put(DB.COLUMN_CLAVESALIDA, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_CLAVESALIDA)));
					data.put(DB.COLUMN_IDMATERIAL, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_IDMATERIAL)));
					data.put(DB.COLUMN_UNIDAD, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_UNIDAD)));
					data.put(DB.COLUMN_EXISTENCIA, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_EXISTENCIA)));
					data.put(DB.COLUMN_CLAVECONCEPTO, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_CLAVECONCEPTO)));
					data.put(DB.COLUMN_IDCONTRATISTA, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_IDCONTRATISTA)));
					data.put(DB.COLUMN_CONCARGO, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_CONCARGO)));
					
						
					json.put(cont++ +"", data);					
					} while (mCursor.moveToNext());
				}
				
				return json;			
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return null;
		}
		
		return null;
	}

}

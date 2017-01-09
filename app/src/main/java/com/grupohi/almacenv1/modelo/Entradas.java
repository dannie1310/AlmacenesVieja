package com.grupohi.almacenv1.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Entradas {
	DB almacen;
	SQLiteDatabase db;
	final String log = "";	
	Context conthis;
	public Entradas(Context context) {
		almacen = new DB(context);
		conthis=context;
	}
	
	public List<ListViewOrdeneTemp> getListViewOrdenesCompra(Context context) {
		List<ListViewOrdeneTemp> labels = new ArrayList<ListViewOrdeneTemp>();		
		/*String selectQuery = "Select E.*, OC."+DB.COLUMN_FOLIO+", A.* from " + DB.TABLE_ENTRADAS
				+" E INNER JOIN "+DB.TABLE_ALMACEN+" A  ON "+" A."+DB.COLUMN_IDALMACEN+" = E."+DB.COLUMN_IDALMACEN 
				+" INNER JOIN "+DB.TABLE_ORDENCOMPRA+" OC  ON "+" OC."+DB.COLUMN_IDTRANSACCION+" = E."+DB.COLUMN_IDTRANSACCION 
				+" GROUP BY E."+DB.COLUMN_CLAVEENTRADA+", "+ DB.COLUMN_IDTRANSACCION  
				+" ORDER BY "+DB.COLUMN_OBSERVACIONES+ " DESC ";*/
		String selectQuery = "Select E.*, OC."+DB.COLUMN_FOLIO+" from " + DB.TABLE_ENTRADA
		+" E "/*+"INNER JOIN "+DB.TABLE_ALMACEN+" A  ON "+" A."+DB.COLUMN_IDALMACEN+" = E."+DB.COLUMN_IDALMACEN */
		+" INNER JOIN "+DB.TABLE_ORDENCOMPRA+" OC  ON "+" OC."+DB.COLUMN_IDTRANSACCION+" = E."+DB.COLUMN_IDTRANSACCION 
		+ " GROUP BY E."+DB.COLUMN_CLAVEENTRADA;
		/*+" GROUP BY E."+DB.COLUMN_CLAVEENTRADA+", "+ DB.COLUMN_IDTRANSACCION  */
		//+" ORDER BY "+DB.COLUMN_OBSERVACIONES+ " DESC ";
		Log.d(log, selectQuery);
		db = almacen.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				int idtrans= cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDTRANSACCION));	
				int folio= cursor.getInt(cursor.getColumnIndex(DB.COLUMN_FOLIO));	
				//String clav = cursor.getString(cursor.getColumnIndex(DB.COLUMN_CLAVEENTRADA));
				//String unidad = cursor.getString(cursor.getColumnIndex(DB.COLUMN_OBSERVACIONES));
				String observaciones = cursor.getString(cursor.getColumnIndex(DB.COLUMN_OBSERVACIONES));
				//String des_almacen = cursor.getString(cursor.getColumnIndex(DB.COLUMN_DESCRIPCION));
				String foliomobil = cursor.getString(cursor.getColumnIndex(DB.COLUMN_CLAVEENTRADA));
				String remision = cursor.getString(cursor.getColumnIndex(DB.COLUMN_REMISION));
				//int id_item = cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDITEM));
				//String clave_concepto = cursor.getString(cursor.getColumnIndex(DB.COLUMN_CLAVECONCEPTO));
				
				String fechahora=cursor.getString(cursor.getColumnIndex(DB.COLUMN_FECHA_HORA));
				//Log.e("FechaHora",""+fechahora);
				labels.add(new ListViewOrdeneTemp(0, foliomobil, 0.0,0,fechahora,"",/*des_almacen*/"",idtrans,folio,remision,observaciones,0,"",0,/*id_item*/0,""));
				//ListViewOrdeneTemp( int idmaterial, String unidad, Double existencia, 
				//int idalmacen, String clave_concepto, String descripcion_m, String descripcion_al, 
				//int indtrans, int folio, String remision, String observaciones) {
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return labels;
	}
	
	public List<ListViewOrdeneTemp> Reprint(int folio, String clavemobile) {
		List<ListViewOrdeneTemp> labels = new ArrayList<ListViewOrdeneTemp>();
		String selectQuery = "Select E.*,"+" E."+DB.COLUMN_CLAVECONCEPTO+" as CC, OC."+DB.COLUMN_FOLIO+", A."+DB.COLUMN_DESCRIPCION+" as des, M."+DB.COLUMN_DESCRIPCION+" as DESMATERIAL, C."+DB.COLUMN_DESCRIPCION+" AS nombrecontratista from " + DB.TABLE_ENTRADA_PARTIDA
				+" E LEFT JOIN "+DB.TABLE_ALMACEN+" A  ON "+" A."+DB.COLUMN_IDALMACEN+" = E."+DB.COLUMN_IDALMACEN 
				+" LEFT JOIN "+DB.TABLE_ORDENCOMPRA+" OC  ON "+" OC."+DB.COLUMN_IDTRANSACCION+" = E."+DB.COLUMN_IDTRANSACCION 
				+" LEFT JOIN "+DB.TABLE_MATERIALES+" M  ON "+" M."+DB.COLUMN_IDMATERIAL+" = E."+DB.COLUMN_IDMATERIAL
				+" LEFT JOIN "+DB.TABLE_CONTRATISTAS+" C  ON "+" c."+DB.COLUMN_IDCONTRATISTA+" = E."+DB.COLUMN_IDCONTRATISTA
				+" WHERE"//  E."+ DB.COLUMN_FOLIO +" = "+folio
				+" "+DB.COLUMN_CLAVEENTRADA+" = \""+clavemobile+"\" group by _id";
		Log.i("selectQuery", selectQuery);
		db = almacen.getReadableDatabase();		
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				double existencia=cursor.getDouble(cursor.getColumnIndex(DB.COLUMN_EXISTENCIA));
				String clave_conceptox=cursor.getString(cursor.getColumnIndex("CC"))+"";
				if(clave_conceptox.equals("null")) clave_conceptox="";
				String descripcion_m= cursor.getString(cursor.getColumnIndex("DESMATERIAL"));	
				String descripcion_al = cursor.getString(cursor.getColumnIndex("des"))+"";
				if(descripcion_al.equals("null")) descripcion_al="";
				String unidad = cursor.getString(cursor.getColumnIndex(DB.COLUMN_UNIDAD));
				
				//if(descripcion_al.equals("null")) descripcion_al="";
				String nombrecontratista = cursor.getString(cursor.getColumnIndex("nombrecontratista"));
				int idcontratista = cursor.getInt(cursor.getColumnIndex(DB.COLUMN_CONCARGO));
				int id_item = cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDITEM));
				
				int concargo  = cursor.getInt(cursor.getColumnIndex(DB.COLUMN_CONCARGO));
			
				
				//labels.add(new ListViewOrdenesCompra(0, descripcion, unidad, 0.0, idtrans, folio));
				labels.add(new ListViewOrdeneTemp(0, unidad,existencia, 0, clave_conceptox, descripcion_m, descripcion_al+"",0,0,"","",idcontratista,nombrecontratista,concargo, id_item, unidad));
				// int idmaterial, String unidad, Double existencia, int idalmacen, String clave_concepto, String descripcion_m, String descripcion_al, int indtrans, int folio, String remision, String observaciones) {
			} while (cursor.moveToNext());
			return labels;
		}
		
		cursor.close();
		db.close();
		return null;		
	}
	
	public String cadenaimprimir(int folio, String clavemobile){
		/*obtener las obras*/
		
		List<ListViewOrdeneTemp> entrada = new ArrayList<ListViewOrdeneTemp>();
		entrada=Reprint(folio, clavemobile);
		
		int size = entrada.size();
		
		Vector vector=new Vector<String>();
		for (int i = 0; i < size; i++){
			Log.e("array", ""+entrada.get(i).getdescripcionalmacen());
			if(entrada.get(i).getdescripcionalmacen().equals(null) || entrada.get(i).getdescripcionalmacen().equals(""))
				continue;			
			int cont=0;
			Log.e("array", "entra--"+entrada.get(i).getdescripcionalmacen());
			for (int j=0; j<vector.size(); j++){
				if(vector.elementAt(j).equals(entrada.get(i).getdescripcionalmacen()))
					cont++;	
			}
			if(cont==0)
				vector.add(entrada.get(i).getdescripcionalmacen());			
		}
		
		
		for (int i = 0; i < size; i++){
			if(entrada.get(i).getclaveconcepto().equals(null) || entrada.get(i).getclaveconcepto().equals(""))
				continue;			
			int cont=0;
			for (int j=0; j<vector.size(); j++){					
				if(vector.elementAt(j).equals(entrada.get(i).getclaveconcepto()))
					cont++;	
			}
			if(cont==0)
				vector.add(entrada.get(i).getclaveconcepto());			
		}
		
		/*String cadenaimprimir="";
		cadenaimprimir="ENTRADA\n";
		for (int j=0; j<vector.size(); j++) {
			Log.e("obra-concepto",vector.elementAt(j)+"");			
			cadenaimprimir+="--------------------------------------------\n";
			cadenaimprimir+=vector.elementAt(j).toString()+"\n";
			cadenaimprimir+="--------------------------------------------\n";
			for (int i = 0; i < size; i++){
				if(entrada.get(i).getdescripcionalmacen().equals(null) || entrada.get(i).getdescripcionalmacen().equals("") ){
					if(vector.elementAt(j).equals(entrada.get(i).getclaveconcepto())){
						Log.e("concepto","  "+entrada.get(i).getexistencia()+" - "+entrada.get(i).getunidad()+"\n   "+entrada.get(i).getdescripcion_material());
						cadenaimprimir+="  "+entrada.get(i).getexistencia()+" - "+entrada.get(i).getunidad()+"\n   "+entrada.get(i).getdescripcion_material()+"\n";
						if(entrada.get(i).getIdContratista()>0){
							cadenaimprimir+="   Entregado a: "+entrada.get(i).getNombreContratista()+"";	
							Log.e("material","  "+entrada.get(i).getNombreContratista());
							if(entrada.get(i).getconcargo()>0){
								cadenaimprimir+="(Con cargo)\n";
								Log.e("material"," -> Con cargo");
							}
							
						}
					}
				}
				if(entrada.get(i).getclaveconcepto().equals(null) || entrada.get(i).getclaveconcepto().equals("")){
					if(vector.elementAt(j).equals(entrada.get(i).getdescripcionalmacen())){
						Log.e("material","  "+entrada.get(i).getexistencia()+" - "+entrada.get(i).getunidad()+"\n   "+entrada.get(i).getdescripcion_material());
						cadenaimprimir+="  "+entrada.get(i).getexistencia()+" - "+entrada.get(i).getunidad()+"\n   "+entrada.get(i).getdescripcion_material()+"\n";
						if(entrada.get(i).getIdContratista()>0){
							cadenaimprimir+="   Entregado a: "+entrada.get(i).getNombreContratista()+"";	
							Log.e("material","  "+entrada.get(i).getNombreContratista());
							if(entrada.get(i).getconcargo()>0){
								cadenaimprimir+="(Con cargo)\n";
								Log.e("material"," -> Con cargo");
							}
							
						}
					
					}
					
				}
				
			}
		}*/
		String cadenaimprimir="";
		for (int j=0; j<vector.size(); j++) {
			//Log.e("obra-concepto",vector.elementAt(j)+"");					
			cadenaimprimir+=" ALM - "+vector.elementAt(j).toString()+"\n";
			cadenaimprimir+="-----------------------------------------------------------\n";
			//cadenaimprimir+="-----------------------------------------------------------\n";
			for (int i = 0; i < size; i++){
				if(entrada.get(i).getdescripcionalmacen().equals(null) || entrada.get(i).getdescripcionalmacen().equals("") ){
					if(vector.elementAt(j).equals(entrada.get(i).getclaveconcepto())){
						//Log.e("concepto","  "+entrada.get(i).getexistencia()+" - "+entrada.get(i).getunidad()+"\n   "+entrada.get(i).getdescripcion_material());
						String c_cortada=" [ ]  ["+entrada.get(i).getexistencia()+"] ["+entrada.get(i).getunidad()+" ["+entrada.get(i).getdescripcion_material();
						if((" [ ]  ["+entrada.get(i).getexistencia()+"] ["+entrada.get(i).getunidad()+" ["+entrada.get(i).getdescripcion_material()).length()>54)
							c_cortada=(" [ ]  ["+entrada.get(i).getexistencia()+"] ["+entrada.get(i).getunidad()+" ["+entrada.get(i).getdescripcion_material()).substring(0,55)+"...";
						
						cadenaimprimir+=c_cortada+"]\n";
						if(entrada.get(i).getIdContratista()>0){
							cadenaimprimir+="   Entregado a contratista: "+entrada.get(i).getNombreContratista()+"";	
							//Log.e("material","  "+entrada.get(i).getNombreContratista());
							if(entrada.get(i).getconcargo()>0){
								cadenaimprimir+="(Con cargo)\n";
								//Log.e("material"," -> Con cargo");
							}
							
						}
					}
						
				}
				if(entrada.get(i).getclaveconcepto().equals(null) || entrada.get(i).getclaveconcepto().equals("")){
					if(vector.elementAt(j).equals(entrada.get(i).getdescripcionalmacen())){
						//Log.e("material","  "+entrada.get(i).getexistencia()+" - "+entrada.get(i).getunidad()+"\n   "+entrada.get(i).getdescripcion_material());
						
						String c_cortada=" [ ]  ["+entrada.get(i).getexistencia()+"] ["+entrada.get(i).getunidad()+" ["+entrada.get(i).getdescripcion_material();
						if((" [ ]  ["+entrada.get(i).getexistencia()+"] ["+entrada.get(i).getunidad()+" ["+entrada.get(i).getdescripcion_material()).length()>54)
							c_cortada=(" [ ]  ["+entrada.get(i).getexistencia()+"] ["+entrada.get(i).getunidad()+" ["+entrada.get(i).getdescripcion_material()).substring(0,55)+"...";
						
						
						cadenaimprimir+=c_cortada+"]\n";
						if(entrada.get(i).getIdContratista()>0){
							cadenaimprimir+="   Entregado a contratista: "+entrada.get(i).getNombreContratista()+"";	
							//Log.e("material","  "+entrada.get(i).getNombreContratista());
							if(entrada.get(i).getconcargo()>0){
								cadenaimprimir+="(Con cargo)\n";
								//Log.e("material"," -> Con cargo");
							}
							
						}
					}
				}
				
				
			}
		}			
		return cadenaimprimir;
		//*/return "ok--";
	}
	
	public JSONObject getdata_entradas(){
		Cursor mCursor=null;
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
									 DB.COLUMN_CLAVEENTRADA,
									 DB.COLUMN_REMISION,
									 DB.COLUMN_IDTRANSACCION	
								   };
				mCursor = db.query(DB.TABLE_ENTRADA, columns, null, null, null,
						null, null, null);
				if (mCursor.moveToFirst()) {
					do {
						JSONObject data=new JSONObject();
						//Log.d("class actividad ",  mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDACTIVIDAD))+"");
					data.put(DB.COLUMN_IDTRANSACCION, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDTRANSACCION)));
					data.put(DB.COLUMN_OBSERVACIONES, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_OBSERVACIONES)));
					data.put(DB.COLUMN_FECHA_HORA, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_FECHA_HORA)));
					data.put(DB.COLUMN_REMISION, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_REMISION)));	
					data.put(DB.COLUMN_CLAVEENTRADA, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_CLAVEENTRADA)));	
					int id=usr.getObraActiva();
					data.put(DB.COLUMN_IDOBRA,obra.get_idobra(id));
					data.put(DB.COLUMN_IDBASE,obra.getBase(id));
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
		finally{
			mCursor.close();
		}
		
		return null;
	}
	public void deletedata(){
		try {
			Log.i("eliminar entradas","entro");
			db = almacen.getReadableDatabase();
			//db.rawQuery("TRUNCATE TABLE "+DB.TABLE_ACTIVIDES, null);
			db.execSQL("delete from "+ DB.TABLE_ENTRADA);
			db.execSQL("delete from "+ DB.TABLE_ENTRADA_PARTIDA);
			//db.delete(DB.TABLE_ACTIVIDES, null, null);
			db.close();
			//Log.i("error al eliminar actividades","sumula que elimina datos");
		} catch (Exception e) {
			Log.e("error al eliminar actividades", e.toString());
		}
		//maquinaria.getWritableDatabase().execSQL("DELETE FROM " + DB.TABLE_ACTIVIDES + ";");
	}
	public JSONObject getdata_entradas_partidas(){
		Cursor mCursor=null;
				
		JSONObject json=new JSONObject();
		int cont=0;
		db = almacen.getReadableDatabase();		
		try {
			if (db != null) {
				String[] columns = {  
						DB.COLUMN_CLAVEENTRADA
						, DB.COLUMN_IDMATERIAL
						, DB.COLUMN_UNIDAD
						, DB.COLUMN_EXISTENCIA
						, DB.COLUMN_IDALMACEN
						, DB.COLUMN_CLAVECONCEPTO
						, DB.COLUMN_IDCONTRATISTA
						, DB.COLUMN_CONCARGO
						, DB.COLUMN_IDITEM
						};
				mCursor = db.query(DB.TABLE_ENTRADA_PARTIDA , columns, null, null, null,
						null, null, null);
				if (mCursor.moveToFirst()) {
					do {
						JSONObject data=new JSONObject();				
					
						//Log.d("class actividad ",  mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDACTIVIDAD))+"");
					data.put(DB.COLUMN_CLAVEENTRADA, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_CLAVEENTRADA)));
					data.put(DB.COLUMN_IDMATERIAL, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_IDMATERIAL)));
					data.put(DB.COLUMN_UNIDAD, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_UNIDAD)));
					data.put(DB.COLUMN_EXISTENCIA, mCursor.getFloat(mCursor.getColumnIndex(DB.COLUMN_EXISTENCIA)));	
					data.put(DB.COLUMN_IDALMACEN, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDALMACEN)));	
					data.put(DB.COLUMN_CLAVECONCEPTO, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_CLAVECONCEPTO)));	
					data.put(DB.COLUMN_IDCONTRATISTA, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDCONTRATISTA)));	
					data.put(DB.COLUMN_CONCARGO, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_CONCARGO)));	
					data.put(DB.COLUMN_IDITEM, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDITEM)));
					
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
		finally{
			mCursor.close();
		}
		
		return null;
	}
	
	

}

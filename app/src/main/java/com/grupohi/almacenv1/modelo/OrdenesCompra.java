package com.grupohi.almacenv1.modelo;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.grupohi.almacenv1.FormEntradaActivity;
import com.grupohi.almacenv1.lib.*;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CalendarContract.Reminders;
import android.util.Log;

public class OrdenesCompra {
	DB almacen;
	SQLiteDatabase db;
	final String log = "";
	
	private String descripcion_material;	
	private String existencia;
	private String unidad;
	private int cantidad;
	private int transaccion;
	Context conthis;

	public OrdenesCompra(Context context) {
		almacen = new DB(context);
		conthis=context;
	}
	public boolean altaOrdenesCompra(
				int idtransaccion
				, Double existencias
				, int idmaterial
				, int folio
				//, String descripcion
				, String unidad
				, String empresa
				, int id_empresa
				, int id_sucursal
				, int id_moneda
				, int id_item
				, Double precio_unitario) throws JSONException {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_IDTRANSACCION, idtransaccion);
		values.put(DB.COLUMN_EXISTENCIA, existencias);
		
		values.put(DB.COLUMN_IDMATERIAL, idmaterial);
		values.put(DB.COLUMN_FOLIO, folio);

		//values.put(DB.COLUMN_DESCRIPCION, descripcion);		
		values.put(DB.COLUMN_UNIDAD, unidad);		
		values.put(DB.COLUMN_EMPRESA, empresa);		
		values.put(DB.COLUMN_ID_EMPRESA, id_empresa);	
		values.put(DB.COLUMN_ID_SUCURSAL, id_sucursal);	
		values.put(DB.COLUMN_ID_MONEDA, id_moneda);
		values.put(DB.COLUMN_IDITEM, id_item);
		values.put(DB.COLUMN_PRECIO_UNITARIO, precio_unitario);

		// db.insert(DB.TABLE_OBRAS, null, values);
		db.insertWithOnConflict(DB.TABLE_ORDENCOMPRA, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		 db.close();
		return true;
	}
	
	public String getNombreEmpresa(int id_item) {
		Cursor mCursor;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
			
				String[] columns = { DB.COLUMN_EMPRESA };
				mCursor = db.query(DB.TABLE_ORDENCOMPRA, columns,DB.COLUMN_IDTRANSACCION+"="+id_item, null,
						null, null, null);

				if (mCursor.moveToFirst()) {
					do {
						return  mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_EMPRESA));
					} while (mCursor.moveToNext());
				} else {
					return null;
				}
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return null;
		}
		
		return null;
	}	

	public List<SpinnerObject> getAllLabels(Context context) {
		List<SpinnerObject> labels = new ArrayList<SpinnerObject>();

		//String selectQuery = "Select * from " + DB.TABLE_ORDENCOMPRA +" group by "+DB.COLUMN_IDTRANSACCION;
		String selectQuery = "Select * from " + DB.TABLE_ORDENCOMPRA +" group by "+DB.COLUMN_FOLIO;
		Log.d(log, selectQuery);
		db = almacen.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		labels.add(new SpinnerObject(0, "- Selecione -"));
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				/*int trans = cursor.getInt(cursor
						.getColumnIndex(DB.COLUMN_IDTRANSACCION));*/
				int trans = cursor.getInt(cursor
						.getColumnIndex(DB.COLUMN_IDTRANSACCION));
				int folio = cursor.getInt(cursor
						.getColumnIndex(DB.COLUMN_FOLIO));
				String desc=cursor.getString(cursor
								.getColumnIndex(DB.COLUMN_EMPRESA));
				/*String descripcion = cursor.getString(cursor
						.getColumnIndex(DB.COLUMN_DESCRIPCION));*/
				labels.add(new SpinnerObject(trans, "#"+folio+" - "+desc));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return labels;
	}
	public List<ListViewOrdenesCompra> getListViewOrdenesCompra(Context context, int idtrasaccion) {
		List<ListViewOrdenesCompra> labels = new ArrayList<ListViewOrdenesCompra>();
		
		//String selectQuery = "Select * from " + DB.TABLE_ORDENCOMPRA+ " where "+ DB.COLUMN_IDTRANSACCION+" = "+idtrasaccion;
		String selectQuery = "Select * from " + DB.TABLE_ORDENCOMPRA+ " O inner join "
				+DB.TABLE_MATERIALES+" M On O."+DB.COLUMN_IDMATERIAL+"= M."+ DB.COLUMN_IDMATERIAL
				+" where "+ DB.COLUMN_IDTRANSACCION+" = "+idtrasaccion;
		Log.d(log, selectQuery);
		db = almacen.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		//labels.add(new ListViewOrdenesCompra(0, , selectQuery, existencia));
		// looping through all rows and adding to list
		
		if (cursor.moveToFirst()) {
			do {
				int idmaterial= cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDMATERIAL));
				int idtrans= cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDTRANSACCION));
				Double exist = cursor.getDouble(cursor.getColumnIndex(DB.COLUMN_EXISTENCIA));
				String descripcion = cursor.getString(cursor.getColumnIndex(DB.COLUMN_DESCRIPCION));
				String unidad = cursor.getString(cursor.getColumnIndex(DB.COLUMN_UNIDAD));
				int folio= cursor.getInt(cursor.getColumnIndex(DB.COLUMN_FOLIO));
				int id_item= cursor.getInt(cursor.getColumnIndex(DB.COLUMN_IDITEM));
			   // Log.d(log+"++++folio=", ""+folio);
				
				labels.add(new ListViewOrdenesCompra(idmaterial, descripcion, unidad, exist, idtrans, folio,id_item));
				//labels.add(new ListViewOrdenesCompra(descripcion, unidad, exist));
				//labels.add(new ListViewOrdenesCompra(descripcion, unidad, existencia));
				//labels.add(new SpinnerObject(trans, "#"+trans+" - "+descripcion));
				//Log.d(log, "termino registro 1");
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		//if(labels.size()==0)
			//new Alert(context, log, "No hay m�s existencia de material.");
		//Log.d(log, "returnando..");
		return labels;
	}
	public String guardaroperacionentrada( List<ListViewOrdenesCompra> ordencompraactualizado, List<ListViewOrdeneTemp> entrada, String foliomovil){
		int size = ordencompraactualizado.size();
		for (int i = 0; i < size; i++){
		    Double existencia =ordencompraactualizado.get(i).getexistencia();
		    int idmaterial =ordencompraactualizado.get(i).getidmaterial();
		    int idtrans =ordencompraactualizado.get(i).getidtransacion();
		    updateexistencia(existencia,idmaterial,idtrans);		    
		}
		altaentrada(entrada.get(0).gettrasaccion(), foliomovil, entrada.get(0).getremision(),entrada.get(0).getObservaciones());
		String cadenaimprimir="";
		size = entrada.size();
		
		/*obtener las almacenes*/
		Vector vector=new Vector();
		for (int i = 0; i < size; i++){
			if(entrada.get(i).getdescripcionalmacen().equals(null) || entrada.get(i).getdescripcionalmacen().equals(""))
				continue;			
			int cont=0;
			for (int j=0; j<vector.size(); j++){
				if(vector.elementAt(j).equals(entrada.get(i).getdescripcionalmacen()))
					cont++;	
			}
			if(cont==0)
				vector.add(entrada.get(i).getdescripcionalmacen());			
		}
		
		/*obtener las conceptos*/
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
		
		
		
		/*for (int j=0; j<vector.size(); j++) {
			Log.e("obra y conceptos",vector.elementAt(j)+"");
		}
		*/
		//String cadena
		//cadenaimprimir+=" Recepci�n de insumos\n";
		cadenaimprimir+="";
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
		cadenaimprimir+="\n Observaciones: "+entrada.get(0).getObservaciones();
		
		
		
		for (int i = 0; i < size; i++){
			 Double existencia =entrada.get(i).getexistencia();
			 int idmaterial =entrada.get(i).getidmaterial();
			 int idalmacen =entrada.get(i).getidalmacen();
			 int idtrans =entrada.get(i).gettrasaccion();
			 String unidad=entrada.get(i).getunidad();
			 String desc_material=entrada.get(i).getdescripcion_material();
			 String desc_almacen=entrada.get(i).getdescripcionalmacen();
			 String clave_concepto=entrada.get(i).getclaveconcepto();
			 String clave_mobil=foliomovil;//entrada.get(i).getfoliomobil();
			 String remision=entrada.get(i).getremision();		
			 String observaciones=entrada.get(i).getObservaciones();	
			 int idcontratista=entrada.get(i).getIdContratista();
			 int concargo=entrada.get(i).getconcargo();
			 int id_item=entrada.get(i).getId_Item();
			 /*cadenaimprimir+=existencia+" - "+unidad+"\n   "
							+desc_material+"\n   ["+desc_almacen+""+clave_concepto+"]\n\n";*/
		    altaentradapartidas(existencia,idmaterial,unidad, idtrans, idalmacen, clave_concepto, clave_mobil, remision, observaciones, idcontratista, concargo,id_item);		    
		}
		return  cadenaimprimir;
		
	}
	public boolean updateexistencia(Double existencia, int idmaterial, int idtransaccion) {
		db = almacen.getWritableDatabase();
		db.execSQL("UPDATE " + DB.TABLE_ORDENCOMPRA + " SET "+DB.COLUMN_EXISTENCIA+"="+existencia+" where "+DB.COLUMN_IDMATERIAL+"="+idmaterial+" AND "+ DB.COLUMN_IDTRANSACCION+" ="+idtransaccion);
		return true;
		
	}
	public boolean altaentradapartidas(
			Double cantidad, int idmaterial, String unidad, int transaccion, int idalmacen, String claveconcepto, String clavemobil, 
			String remision, String observaciones, int idcontratista, int concargo, int id_item) {
		db = almacen.getWritableDatabase();
		Utils ut= new Utils();
		ContentValues values = new ContentValues();
		
		MaterialesAlamacen almacen=new MaterialesAlamacen(conthis);
		Usuario user= new Usuario(conthis);		
		
		try {
			if(getID(idalmacen, idmaterial, clavemobil)>0){
				String where =" "+DB.COLUMN_IDALMACEN+"="+idalmacen+ " AND "+DB.COLUMN_IDMATERIAL+"="+idmaterial+" AND "+DB.COLUMN_CLAVEENTRADA+"='"+clavemobil+"'";
				cantidad=getCantidad(idalmacen, idmaterial,clavemobil)+cantidad;
				values.put(DB.COLUMN_EXISTENCIA, cantidad);
				db.update(DB.TABLE_ENTRADA_PARTIDA, values, where, null);
				Log.i("entrada_partida", "actualiza");
			}else{
				values.put(DB.COLUMN_CLAVEENTRADA, clavemobil);
				values.put(DB.COLUMN_IDMATERIAL, idmaterial);
				values.put(DB.COLUMN_EXISTENCIA, cantidad);
				values.put(DB.COLUMN_UNIDAD,unidad);
				values.put(DB.COLUMN_IDTRANSACCION, transaccion);
				values.put(DB.COLUMN_IDALMACEN, idalmacen);
				values.put(DB.COLUMN_CLAVECONCEPTO, claveconcepto);
				values.put(DB.COLUMN_REMISION, remision);
				values.put(DB.COLUMN_OBSERVACIONES, observaciones);
				values.put(DB.COLUMN_IDCONTRATISTA, idcontratista);
				values.put(DB.COLUMN_CONCARGO, concargo);
				values.put(DB.COLUMN_IDITEM, id_item);			   
			    db.insertWithOnConflict(DB.TABLE_ENTRADA_PARTIDA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
			    Log.i("entrada_partida", "inserta");
			}
			 almacen.altaMaterialesAlamace(idalmacen, idmaterial, user.getObraActiva(), unidad, cantidad);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// db.insert(DB.TABLE_USUARIOS, null, values);
		
		return true;		
	}
	public boolean altaentrada(int transaccion,  String clavemobil, String remision, String observaciones) {
		db = almacen.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_CLAVEENTRADA, clavemobil);
		values.put(DB.COLUMN_IDTRANSACCION, transaccion);
		values.put(DB.COLUMN_REMISION, remision);
		values.put(DB.COLUMN_OBSERVACIONES, observaciones);		
		
		// db.insert(DB.TABLE_USUARIOS, null, values);
		db.insertWithOnConflict(DB.TABLE_ENTRADA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		return true;		
	}
	
	public void deletecatalogos(){
		try {
			Log.i("eliminar entradas","entro");
			db = almacen.getReadableDatabase();
			//db.rawQuery("TRUNCATE TABLE "+DB.TABLE_ACTIVIDES, null);
			db.execSQL("delete from "+ DB.TABLE_ORDENCOMPRA);
			db.execSQL("delete from "+ DB.TABLE_ALMACEN);
			db.execSQL("delete from "+ DB.TABLE_CONTRATISTAS);
			db.execSQL("delete from "+ DB.TABLE_EXISTENCIASALMACEN);
			db.execSQL("delete from "+ DB.TABLE_MATERIALES);

			//db.delete(DB.TABLE_ACTIVIDES, null, null);
			db.close();
			//Log.i("error al eliminar actividades","sumula que elimina datos");
		} catch (Exception e) {
			Log.e("error al eliminar actividades", e.toString());
		}
		//maquinaria.getWritableDatabase().execSQL("DELETE FROM " + DB.TABLE_ACTIVIDES + ";");
	}
	public JSONObject getdata_existencia(){
		Cursor mCursor;
		Usuario usr=new Usuario(conthis);
		Obras obra=new Obras(conthis);		
				JSONObject json=new JSONObject();
				int cont=0;
				db = almacen.getReadableDatabase();		
				try {
					if (db != null) {
						String[] columns = {  
								  DB.COLUMN_IDTRANSACCION
								, DB.COLUMN_IDMATERIAL
								, DB.COLUMN_UNIDAD
								, DB.COLUMN_EXISTENCIA
								, DB.COLUMN_FOLIO
								, DB.COLUMN_ID_EMPRESA
								, DB.COLUMN_ID_SUCURSAL
								, DB.COLUMN_ID_MONEDA
								, DB.COLUMN_PRECIO_UNITARIO
								};
						mCursor = db.query(DB.TABLE_ORDENCOMPRA, columns, null, null, null,
								null, null, null);
						if (mCursor.moveToFirst()) {
							do {
								JSONObject data=new JSONObject();	
								//Log.d("class actividad ",  mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDACTIVIDAD))+"");
								data.put(DB.COLUMN_IDTRANSACCION, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDTRANSACCION)));
								data.put(DB.COLUMN_IDMATERIAL, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_IDMATERIAL)));
								data.put(DB.COLUMN_UNIDAD, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_UNIDAD)));
								data.put(DB.COLUMN_EXISTENCIA, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_EXISTENCIA)));
								data.put(DB.COLUMN_FOLIO, mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_FOLIO)));
								data.put(DB.COLUMN_ID_EMPRESA, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_ID_EMPRESA)));
								data.put(DB.COLUMN_ID_SUCURSAL, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_ID_SUCURSAL)));
								data.put(DB.COLUMN_ID_MONEDA, mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_ID_MONEDA)));
								data.put(DB.COLUMN_PRECIO_UNITARIO, mCursor.getDouble(mCursor.getColumnIndex(DB.COLUMN_PRECIO_UNITARIO)));
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
	private int getID(int id_almacen, int id_material, String clave_entrada){
		db = almacen.getWritableDatabase();
		String where =" "+DB.COLUMN_IDALMACEN+"="+id_almacen+ " AND "+DB.COLUMN_IDMATERIAL+"="+id_material+" AND "+DB.COLUMN_CLAVEENTRADA+"='"+clave_entrada+"'";
	    Cursor c = db.query(DB.TABLE_ENTRADA_PARTIDA, new String[]{"_id"}, where,null,null,null,null,null);
	    if (c.moveToFirst()) //if the row exist then return the id
	        return c.getInt(c.getColumnIndex("_id"));
	    return -1;
	}
	private Double getCantidad(int id_almacen, int id_material, String clave_entrada){
		db = almacen.getWritableDatabase();
		String where =" "+DB.COLUMN_IDALMACEN+"="+id_almacen+ " AND "+DB.COLUMN_IDMATERIAL+"="+id_material+" AND "+DB.COLUMN_CLAVEENTRADA+"='"+clave_entrada+"'";
	    Cursor c = db.query(DB.TABLE_ENTRADA_PARTIDA, new String[]{DB.COLUMN_EXISTENCIA}, where,null,null,null,null,null);
	    if (c.moveToFirst()) //if the row exist then return the id
	        return c.getDouble(c.getColumnIndex(DB.COLUMN_EXISTENCIA));
	    return 0.0;
	}
	

}

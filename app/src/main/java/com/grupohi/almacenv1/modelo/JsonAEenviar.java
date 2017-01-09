package com.grupohi.almacenv1.modelo;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class JsonAEenviar {
	DB almacen;
	SQLiteDatabase db;
	final String log = "";	
	Context conthis;
	public JsonAEenviar(Context context) {
		almacen = new DB(context);
		conthis=context;
	}
	public JSONObject getM_TRANSACCIONES() {
		Usuario usr=new Usuario(conthis);
		Obras obra=new Obras(conthis);
		JSONObject json=new JSONObject();
		int cont=0;
		db = almacen.getReadableDatabase();		
		try {
			if (db != null) {
				String selectQuery = 	"	SELECT 	"	
						+	"	    E.claveentrada AS clave_transaccion	"	
						+	"	  , 0 AS id_obra	"	
						+	"	  , 0 AS id_almacen	"	
						+	"	  , '' AS clave_concepto	"	
						+	"	  , E.idtransaccion AS id_antecedente	"	
						+	"	  , 1 AS id_tipo_transaccion	"	
						+	"	  , E.fechahora AS fecha_hora	"	
						+	"	  , compras.id_empresa AS id_empresa	"	
						+	"	  , compras.id_sucursal	AS id_sucursal "	
						+	"	  , compras.id_moneda  AS id_moneda	"	
						+	"	  , E.observaciones	AS observaciones"	
						+	"	  , E.remision AS referencia	"	
						+	"	  , ''  AS registro	"	
						+	"	 FROM entrada E	"	
						+	"		"	
						+	"	INNER JOIN	"	
						+	"	(	"	
						+	"	             SELECT DISTINCT	"	
						+	"	                 idtransaccion, id_empresa, id_sucursal,id_moneda	"	
						+	"	             FROM	"	
						+	"	                 ordencompra OC	"	
						+	"	) AS compras ON compras.idtransaccion=E.idtransaccion 	"	
						+	"		"	
						+	"	UNION 	"	
						+	"		"	
						+	"	SELECT 	"	
						+	"	 SI.clavesalida AS clave_transaccion	"	
						+	"	 ,SI.idobra AS id_obra	"	
						+	"	 ,SI.idalmacen AS id_almacen	"	
						+	"	 ,SI.claveconcepto  AS clave_concepto	"	
						+	"	 ,0 AS id_antecedente	"	
						+	"	 ,2 AS id_tipo_transaccion	"	
						+	"	 ,SI.fechaHora AS fecha_hora	"	
						+	"	 ,0 AS id_empresa	"	
						+	"	 ,0 AS id_sucursal	"	
						+	"	 ,0 AS id_moneda	"	
						+	"	 , SI.observaciones	"	
						+	"	 , SI.referencia	"	
						+	"	 , '' AS registro	"	
						+	"	 FROM salidainsumos SI	"	
						+	"		"	
						+	"	UNION	"	
						+	"		"	
						+	"	SELECT 	"	
						+	"	   sf.clavesalida AS clave_transaccion	"	
						+	"	   , sf.idobra AS id_obra	"	
						+	"	   , sf.idalmacen AS id_almacen	"	
						+	"	   ,'' AS clave_concepto	"	
						+	"	   ,0 AS id_antecedente	"	
						+	"	   ,3 AS id_tipo_transaccion	"	
						+	"	   ,sf.fechaHora AS fecha_hora	"	
						+	"	   ,0 AS id_empresa	"	
						+	"	   ,0 AS id_sucursal	"	
						+	"	   ,0 AS id_moneda	"	
						+	"	   , sf.observaciones	"	
						+	"	  , sf.referencia	"	
						+	"	 , '' AS registro	"	
						+	"	 FROM  salidatrasferencia sf	"	;

				//Log.d(log, selectQuery);
				db = almacen.getReadableDatabase();
				Cursor mCursor = db.rawQuery(selectQuery, null);
				if (mCursor.moveToFirst()) {
					
					do {
						JSONObject data=new JSONObject();
						//Log.d("class actividad ",  mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDACTIVIDAD))+"");
					data.put("clave_transaccion", mCursor.getString(mCursor.getColumnIndex("clave_transaccion")));
					data.put("id_almacen", mCursor.getInt(mCursor.getColumnIndex("id_almacen")));
					data.put("clave_concepto", mCursor.getString(mCursor.getColumnIndex("clave_concepto")));	
					data.put("id_antecedente", mCursor.getInt(mCursor.getColumnIndex("id_antecedente")));	
					data.put("id_tipo_transaccion", mCursor.getInt(mCursor.getColumnIndex("id_tipo_transaccion")));	
					data.put("fecha_hora", mCursor.getString(mCursor.getColumnIndex("fecha_hora")));	
					data.put("id_empresa", mCursor.getInt(mCursor.getColumnIndex("id_empresa")));	
					data.put("id_sucursal", mCursor.getInt(mCursor.getColumnIndex("id_sucursal")));	
					data.put("id_moneda", mCursor.getInt(mCursor.getColumnIndex("id_moneda")));	
					data.put("observaciones", mCursor.getString(mCursor.getColumnIndex("observaciones")));	
					data.put("referencia", mCursor.getString(mCursor.getColumnIndex("referencia")));
					
					
					int id=usr.getObraActiva();
					data.put("id_obra",obra.get_idobra(id));
					//data.put(DB.COLUMN_IDBASE,obra.getBase(id));
					data.put("registro",usr.getIdusuarioActivo());
					json.put(cont++ +"", data);					
					} while (mCursor.moveToNext());
				}
				
				return json;			
			}
			db.close();
		} catch (Exception e) {
			Log.i("M_TRANSACCION", " ERROR --> " + e.getMessage()+""+e.toString());
			return null;
		}
		
		return null;
	}
	public JSONObject getM_ITEMS() {
		JSONObject json=new JSONObject();
		int cont=0;
		db = almacen.getReadableDatabase();		
		try {
			if (db != null) {
				String selectQuery = 		"	 SELECT 	"
						+	"	    E.claveentrada AS clave_transaccion	"
						+	"	   , E.idtransaccion AS id_antecedente	"
						+	"	   , EP.iditem AS item_antecedente	"
						+	"	   , EP.idmaterial AS id_material	"
						+	"	   , EP.unidad	as unidad "
						+	"	   , EP.existencia AS cantidad	"//cantidad ingresada //existencia
						+	"	   , EP.idalmacen AS id_almacen	"
						+	"	   , EP.claveconcepto AS clave_concepto	"
						+	"	   , EP.idcontratista AS id_contratista	"
						+	"	   , EP.concargo AS con_cargo	"
						+	"	 FROM entrada E	"
						+	"	INNER JOIN entrada_partida EP ON EP.claveentrada=E.claveentrada	"
						+	"		"
						+	"	union 	"
						+	"	 SELECT 	"
						+	"	 SI.clavesalida AS clave_transaccion	"
						+	"	 ,0 AS id_antecedente	"
						+	"	 ,0 AS item_atencedente	"
						+	"	 , SIP.idmaterial AS id_material	"
						+	"	 , SIP.unidad	"
						+	"	 , SIP.existencia AS cantidad	"
						+	"	 , 0 AS id_almacen	"
						+	"	 ,SIP.claveconcepto AS clave_concepto	"
						+	"	 ,SIP.idcontratista AS id_contratista	"
						+	"	 , SIP.concargo AS con_cargo	"
						+	"		"
						+	"	 FROM salidainsumos SI	"
						+	"	 INNER JOIN salidainsumospartidas SIP ON SI.clavesalida=SIP.clavesalida	"
						+	"		"
						+	"	union	"
						+	"		"
						+	"	SELECT 	"
						+	"	    sf.clavesalida AS clave_transaccion	"
						+	"	  ,0 AS id_antecedente	"
						+	"	   ,0 AS item_atencedente	"
						+	"	   , stp.idmaterial AS id_material	"
						+	"	   , stp.unidad	"
						+	"	   , stp.existencia AS cantidad	"
						+	"	   , stp.idalmacen AS id_almacen	"
						+	"	   , '' AS clave_concepto	"
						+	"	   , stp.idcontratista AS id_contratista	"
						+	"	   , stp.concargo AS con_cargo	"
						+	"	 FROM  salidatrasferencia sf	"
						+	"	inner join salidatransferenciapartidas  stp on sf.clavesalida=stp.clavesalida	";


				//Log.d(log, selectQuery);
				db = almacen.getReadableDatabase();
				Cursor mCursor = db.rawQuery(selectQuery, null);
				if (mCursor.moveToFirst()) {
					
					do {
						JSONObject data=new JSONObject();
						data.put("clave_transaccion", mCursor.getString(mCursor.getColumnIndex("clave_transaccion")));
						data.put("id_antecedente", mCursor.getInt(mCursor.getColumnIndex("id_antecedente")));
						data.put("item_antecedente", mCursor.getInt(mCursor.getColumnIndex("item_antecedente")));	
						data.put("id_material", mCursor.getInt(mCursor.getColumnIndex("id_material")));						
						data.put("unidad", mCursor.getString(mCursor.getColumnIndex("unidad")));
						data.put("cantidad", mCursor.getString(mCursor.getColumnIndex("cantidad")));
						data.put("id_almacen", mCursor.getInt(mCursor.getColumnIndex("id_almacen")));					
						data.put("clave_concepto", mCursor.getString(mCursor.getColumnIndex("clave_concepto")));					
						data.put("id_contratista", mCursor.getInt(mCursor.getColumnIndex("id_contratista")));
						data.put("con_cargo", mCursor.getInt(mCursor.getColumnIndex("con_cargo")));					
						json.put(cont++ +"", data);					
					} while (mCursor.moveToNext());
				}
				
				return json;			
			}
			db.close();
		} catch (Exception e) {
			Log.i("M_TRANSACCION", " ERROR --> " + e.getMessage()+""+e.toString());
			return null;
		}
		
		return null;
	}
	
}

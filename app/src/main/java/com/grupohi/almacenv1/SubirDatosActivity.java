package com.grupohi.almacenv1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.grupohi.almacenv1.lib.Alert;
import com.grupohi.almacenv1.lib.Utils;
import com.grupohi.almacenv1.modelo.DB;
import com.grupohi.almacenv1.modelo.Entradas;
import com.grupohi.almacenv1.modelo.JsonAEenviar;
import com.grupohi.almacenv1.modelo.Obras;
import com.grupohi.almacenv1.modelo.OrdenesCompra;
import com.grupohi.almacenv1.modelo.SalidaConsumo;
import com.grupohi.almacenv1.modelo.SalidaTrasferencia;
import com.grupohi.almacenv1.modelo.Usuario;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.NameValueTable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubirDatosActivity extends Activity {
	// private static final String captura_url =
	// "http://adminapp.grupohi.mx/ordencompraservicesmobile/index.php/data/datajson/";
	//private static String captura_url = "http://saoweb.grupohi.mx?controlador=SAO&accion=procesaAlmacenesMovil";
	private static String captura_url;
	final String log = "Subir Datos";
	TextView textView;
	Button b;
	private ProgressDialog progress;

	/*
	 * Entradas entrada; String entradajson, entradapartidasjson;
	 * 
	 * SalidaConsumo consumo; String consumojson, consumopartidasjson;
	 * 
	 * SalidaTrasferencia transferencia; String transferenciajson,
	 * transferenciapartidasjson;
	 * 
	 * OrdenesCompra ordencompra; String ordencomprajson;
	 */

	JsonAEenviar jsonenviar;
	String DB;
	int _id_obra;
	int _id_usuario;

	String json_m_transacciones;
	String json_m_items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subir_capturado);
		captura_url=getApplicationContext().getString(R.string.url_api)+"?controlador=SAO&accion=procesaAlmacenesMoviles";

		try {
			Utils.copyDataBase(getApplicationContext());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		jsonenviar = new JsonAEenviar(this);
		json_m_transacciones = jsonenviar.getM_TRANSACCIONES().toString();
		Log.e("json_m_transacciones", json_m_transacciones);
		json_m_items = jsonenviar.getM_ITEMS().toString();
		Log.e("json_m_items", json_m_items);

		DB = Usuario.getNombreDB();

		int _id = Usuario.getObraActiva();
		Obras obra = new Obras(this);
		_id_obra = obra.get_idobra(_id);
		Log.i("id_obra", _id_obra + "");
		_id_usuario = Usuario.getIdusuarioActivo();

		/*
		 * entrada=new Entradas(this);
		 * entradajson=entrada.getdata_entradas().toString();
		 * entradapartidasjson=entrada.getdata_entradas_partidas().toString();
		 * 
		 * 
		 * consumo=new SalidaConsumo(this);
		 * consumojson=consumo.getdata_consumos().toString();
		 * consumopartidasjson=consumo.getdata_consumos_partidas().toString();
		 * 
		 * transferencia=new SalidaTrasferencia(this);
		 * transferenciajson=transferencia.getdata_transferencia().toString();
		 * Log.e("Error---aqui pasas","("+transferenciajson+")");
		 * transferenciapartidasjson
		 * =transferencia.getdata_transferencia_partidas().toString();
		 * 
		 * ordencompra=new OrdenesCompra(this);
		 * ordencomprajson=ordencompra.getdata_existencia().toString();
		 */

		final Button button = (Button) findViewById(R.id.btn_subir_captura);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				progress = ProgressDialog.show(SubirDatosActivity.this, "",
						"Cargando?", true);
				new sincronizar().execute();
			}
		});

	}

	class sincronizar extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			JSONObject JSONObjectx ;
			Utils util= new Utils();
			try {
				List<NameValuePair> nameValue = new ArrayList<NameValuePair>(1);

				captura_url += "&id_usuario="+ _id_usuario;
				nameValue.add(new BasicNameValuePair("base",DB));
				nameValue.add(new BasicNameValuePair("id_obra",Base64.encodeToString(Integer.toString(_id_obra).getBytes(), Base64.DEFAULT)));

				Log.e("URL", " " + captura_url);

				if (json_m_transacciones.length() > 2)
					nameValue.add(new BasicNameValuePair("transacciones",
							json_m_transacciones));
				

				if (json_m_items.length() > 2)
					nameValue
							.add(new BasicNameValuePair("items", json_m_items));
				
				if(json_m_transacciones.length() == 2 && json_m_items.length() == 2 )	return "No hay datos que enviar";

				Log.e("json data", " " + nameValue.toString());
				JSONObjectx = util.jsonHttp(captura_url, nameValue);
				Log.e("URL", " " + JSONObjectx.toString());
				return JSONObjectx.toString();
				//if (JSONObjectx.has("error"))
					//return (String) JSONObjectx.get("error");
				//else {
					//return (String) JSONObjectx.get("ok");
				//}

			//} catch (JSONException e) {
				//Log.e("Error... json", " " + e.getMessage());
				
				//throw new RuntimeException(e);
				//return e.getMessage();
				
			}

			catch (Exception e) {
				e.printStackTrace();
				Log.e("Error...", " " + e.getMessage());
				//return e.getMessage();
				return "No hay datos que sincronizar o no tiene internet";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// super.onPostExecute(result);
			progress.dismiss();
			try{
				JSONObject _data=new JSONObject(result);
				if(_data.has("ok")){
					new Alert(SubirDatosActivity.this, log, _data.getString("ok"));
					Entradas entradas = new Entradas(SubirDatosActivity.this);
					SalidaConsumo salcons = new SalidaConsumo(
							SubirDatosActivity.this);
					SalidaTrasferencia saltrans = new SalidaTrasferencia(
							SubirDatosActivity.this);
					entradas.deletedata();
					salcons.deletedata();
					saltrans.deletedata();
				}else if(_data.has("error")){
					new Alert(SubirDatosActivity.this, log, _data.getString("error"));
				}else{
					new Alert(SubirDatosActivity.this, log, result);
				}
			}catch (Exception e){
				new Alert(SubirDatosActivity.this, log, result);
			}
			
			/*if (result.equals("true")) {
				new Alert(SubirDatosActivity.this, log,"Se ha enviado los datos correctamente");
				Entradas entradas = new Entradas(SubirDatosActivity.this);
				SalidaConsumo salcons = new SalidaConsumo(
						SubirDatosActivity.this);
				SalidaTrasferencia saltrans = new SalidaTrasferencia(
						SubirDatosActivity.this);
				entradas.deletedata();
				salcons.deletedata();
				saltrans.deletedata();

				/*
				 * reporte.deletedata(); new Alert(SubirDatosActivity.this, log,
				 * "Se ha enviado los datos correctamente"); actividades=
				 * actividad.getdata().toString(); reportes=
				 * reporte.getdata().toString();
				 */

			/*} else
				new Alert(SubirDatosActivity.this, log, result);*/
		}
	}

}

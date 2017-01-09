package com.grupohi.almacenv1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.grupohi.almacenv1.lib.*;
import com.grupohi.almacenv1.modelo.*;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class ConfigEmpresaActivity  extends Activity implements	AsyncTaskCompleteListener {
	public static final String log = "Configurando la obra";
	//private static final String catalogos = "http://gln.com.mx:82/TestPCG/ordencompraservicesmobile/index.php/data/catalogos";
	private static String catalogos;
	List <SpinnerObject> lables ;
	Obras obra;
	int id;
	Spinner spinner_obras;
	
	private DownloadManager downloadManager;
	 private long downloadReference;
	 
	 ProgressDialog pd ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_empresa);
		obra=new Obras(this);
		lables = obra.getAllLabels(this);
		catalogos =getApplicationContext().getString(R.string.url_api)+"ordenes_compra/?controlador=SAO&accion=getCatalogosMovil&";
		
		spinner_obras = (Spinner) findViewById(R.id.spinner_obras);
		ArrayAdapter<SpinnerObject> spinner_adapter_obras = new ArrayAdapter<SpinnerObject>(
				this, android.R.layout.simple_spinner_item, lables);
		spinner_adapter_obras
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_obras.setAdapter(spinner_adapter_obras);
		
		Usuario usuario=new Usuario(this);
		
		int idobraactiva=usuario.getObraActiva();
		if(idobraactiva>0){
			for (int i = 0; i < spinner_obras.getCount(); i++) {
				int Idobra = Integer.parseInt(((SpinnerObject) spinner_obras.getItemAtPosition(i)).getId() + "");
				String name_=((SpinnerObject) spinner_obras.getItemAtPosition(i)).getValue() + "";
				Log.i("Selecionado", Idobra+"=="+name_);
				Idobra=obra.get_idobra(Idobra);
				if (Idobra == idobraactiva)
					spinner_obras.setSelection(i);
				
			}
		}
		pd = new ProgressDialog(this);
		
		
	}
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public void descargarfile (View view){
		id = Integer.parseInt(((SpinnerObject) spinner_obras.getSelectedItem()).getId() + "");		
		if(id==0){
			new Alert(this, log, "Selecione un Obra ");
		}else{	
			pd.setMessage("loading");
			pd.show();
			

			
			int id_obra=obra.get_idobra(id);
			
			Usuario usuario= new Usuario(this);
			//usuario.setObra(id_obra);
			usuario.setObra(id);//es el id de la tabala de proyectos o el id_obra
			
			
			Obras obra=new Obras(this);
			//new Alert(this, log, "Guardado correctamente");
			Log.d("url",catalogos + "idbase="+obra.getBase(id)+"&idobra="+ id_obra);
			
			//new MyTask(this).execute(catalogos + "?idobra="+ idobra);	
			
			//new MyTask(this).execute(catalogos + "idbase="+obra.getBase(idobra)+"&idobra="+ idobra);
			Log.e("urlcat","="+catalogos + "idbase="+obra.getBase(id)+"&idobra="+ id_obra);
			

		   downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
		   //Uri Download_Uri = Uri.parse("http://gln.com.mx:82/TestPCG/ordencompraservicesmobile/index.php/data/catalogos?idobra="+ idobra);
		   Uri Download_Uri = Uri.parse(catalogos + "idbase="+obra.getBase(id)+"&idobra="+ id_obra);
		   DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
		   Log.e("des","descargando");
		   //Restrict the types of networks over which this download may proceed.
		   request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
		   //Set whether this download may proceed over a roaming connection.
		   request.setAllowedOverRoaming(false);
		   //Set the title of this download, to be displayed in notifications (if enabled).
		   request.setTitle("My Data Download");
		   //Set a description of this download, to be displayed in notifications (if enabled)
		   request.setDescription("f");
		   //Set the local destination for the downloaded file to a path within the application's external files directory
		   request.setDestinationInExternalFilesDir(this,Environment.DIRECTORY_DOWNLOADS,"CountryList.json");
		 
		   //Enqueue a new download and same the referenceId
		   downloadReference = downloadManager.enqueue(request);
		    
		   TextView showCountries = (TextView) findViewById(R.id.countryData);
		   showCountries.setText("Descargado desde el servidor, espera...");
		   
		   Query myDownloadQuery = new Query();
		   myDownloadQuery.setFilterById(downloadReference);
		   Log.e("des","vas .."); 
		   //Query the download manager about downloads that have been requested.
		  /* Cursor cursor = downloadManager.query(myDownloadQuery);
		   if(cursor.moveToFirst()){
			   String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)); 
			   String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
			   procesaJSON(fileName);
		   }*/
		 
			IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
			registerReceiver(downloadReceiver, filter);  
		    
		   /*Button checkStatus = (Button) findViewById(R.id.checkStatus);
		   checkStatus.setEnabled(true);
		   Button cancelDownload = (Button) findViewById(R.id.cancelDownload);
		   cancelDownload.setEnabled(true);*/
		}
		
	}
	public void procesaJSON(String fileName){
		
		   //set the query filter to our previously Enqueued download 
		   
			   /* int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
			   String filename =  cursor.getString(filenameIndex);*/
		   
		try {
	        File dir = Environment.getExternalStorageDirectory();
	        //File yourFile = new File(dir, "path/to/the/file/inside/the/sdcard.ext");
	        File yourFile = new File("", fileName);
	        FileInputStream stream = new FileInputStream(yourFile);
	        String jString = null;
	        try {
	            FileChannel fc = stream.getChannel();
	            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
	            /* Instead of using default, pass in a decoder. */
	            jString = Charset.defaultCharset().decode(bb).toString();
	          }
	          finally {
	            stream.close();
	          }
	        JSONObject jObject = new JSONObject(jString); 
	        Log.e("json-procesor","="+jString);
	    } catch (Exception e) {e.printStackTrace();}
		
		   
		
	}
	 private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
		 
		  @Override
		  public void onReceive(Context context, Intent intent) {
			 // ConfigEmpresaActivity.this.pasarGarbageCollector();
		    
		   //check if the broadcast message is for our Enqueued download
		   long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
		   if(downloadReference == referenceId){
		     
		    /*Button cancelDownload = (Button) findViewById(R.id.cancelDownload);
		    cancelDownload.setEnabled(false);*/
		     
		   
		   
		    final StringBuffer strContent = new StringBuffer("");
		    StringBuffer countryData = new StringBuffer("");
		    TextView showCountries = (TextView) findViewById(R.id.countryData);
			//showCountries.setText("procesando archivo, espera...");
		    
		     
		    //parse the JSON data and display on the screen
		    
		      
		     /*JSONObject responseObj = new JSONObject(strContent.toString()); 
		     JSONArray countriesObj = responseObj.getJSONArray("ordenescompra");*/
		    
		    // ConfigEmpresaActivity.this.pasarGarbageCollector();
					try {
						  ParcelFileDescriptor file;
						  int ch;
					     file = downloadManager.openDownloadedFile(downloadReference);
					     FileInputStream fileInputStream = new ParcelFileDescriptor.AutoCloseInputStream(file);
					 
					     while( (ch = fileInputStream.read()) != -1)
					      strContent.append((char)ch);
					     
					 JSONObject JSONObject = new JSONObject(strContent.toString());
					 OrdenesCompra ordencompra= new OrdenesCompra(ConfigEmpresaActivity.this);
						Log.d(log, "ordenescompra");			
						if (JSONObject.has("ordenescompra")) {
							ordencompra.deletecatalogos();
							JSONArray jsonArray = new JSONArray(JSONObject.getString("ordenescompra"));
							for (int i = 0; i < jsonArray.length(); i++) {
								ordencompra.altaOrdenesCompra(
										  jsonArray.getJSONObject(i).getInt("id_transaccion")
										, jsonArray.getJSONObject(i).getDouble("existencia")
										, jsonArray.getJSONObject(i).getInt("id_material")
										, jsonArray.getJSONObject(i).getInt("numero_folio")
										//, jsonArray.getJSONObject(i).getString("descripcion")
										, jsonArray.getJSONObject(i).getString("unidad")
										, jsonArray.getJSONObject(i).getString("razon_social")
										, jsonArray.getJSONObject(i).getInt("id_empresa")
										, jsonArray.getJSONObject(i).getInt("id_sucursal")
										, jsonArray.getJSONObject(i).getInt("id_moneda")
										, jsonArray.getJSONObject(i).getInt("id_item")
										, jsonArray.getJSONObject(i).getDouble("precio_unitario")
										);
							}				
						}
						//showCountries.setText("Capturando, ordenes de compra...");
						// ConfigEmpresaActivity.this.pasarGarbageCollector();
						
						if (JSONObject.has("almacenes")) {
							Log.d(log, "almacenes");
							CatAlmacen catalmacen=new  CatAlmacen(ConfigEmpresaActivity.this);
							JSONArray jsonArray = new JSONArray(JSONObject.getString("almacenes"));
							for (int i = 0; i < jsonArray.length(); i++) {
								/*JSONObject jsonObject = jsonArray.getJSONObject(i);
								Log.d(log, jsonObject.toString());	*/
								 catalmacen.altaCatAlmacenes(
										   jsonArray.getJSONObject(i).getInt("id")
										 , jsonArray.getJSONObject(i).getString("descripcion")
										 );
								
							}				
						}
						//showCountries.setText("Capturando, almacenes...");
						 //ConfigEmpresaActivity.this.pasarGarbageCollector();
						if (JSONObject.has("materiales")) {
							Log.d(log, "materiales");
							CatMaterial catmateriales=new CatMaterial(ConfigEmpresaActivity.this);
							JSONArray jsonArray = new JSONArray(JSONObject.getString("materiales"));
							for (int i = 0; i < jsonArray.length(); i++) {
								/*JSONObject jsonObject = jsonArray.getJSONObject(i);
								Log.d(log, jsonObject.toString());	*/
								 catmateriales.altaCatMateriales(
										   jsonArray.getJSONObject(i).getInt("id_material")
										 , jsonArray.getJSONObject(i).getString("descripcion")
										 );
							}				
						}
						 //ConfigEmpresaActivity.this.pasarGarbageCollector();
						if (JSONObject.has("materiales_x_almacen")) {
							Log.d(log, "materiales_x_almacen");
							MaterialesAlamacen existencia= new MaterialesAlamacen(ConfigEmpresaActivity.this);				
							JSONArray jsonArray = new JSONArray(JSONObject.getString("materiales_x_almacen"));
							for (int i = 0; i < jsonArray.length(); i++) {
								/*JSONObject jsonObject = jsonArray.getJSONObject(i);
								Log.d(log, jsonObject.toString());		*/
								existencia.altaMaterialesAlamace(
										 jsonArray.getJSONObject(i).getInt("id_almacen")
										//, jsonArray.getJSONObject(i).getString("descripcion")
										, jsonArray.getJSONObject(i).getInt("id_material")
										, jsonArray.getJSONObject(i).getInt("id_obra")
										, jsonArray.getJSONObject(i).getString("unidad")
										, jsonArray.getJSONObject(i).getDouble("cantidad")
										);
								//existencia.altaMaterialesAlamace(idalmacen, descripcion, idmaterial, i, unidad, existencia)
								
							}				
						}
						// ConfigEmpresaActivity.this.pasarGarbageCollector();
						if (JSONObject.has("conceptos")) {
							Log.d(log, "conceptos");
							CatConceptos conceptos=new CatConceptos(ConfigEmpresaActivity.this);
							JSONArray jsonArray = new JSONArray(JSONObject.getString("conceptos"));
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								conceptos.altaCatConcepto(
															jsonArray.getJSONObject(i).getInt("id_concepto"), 
															jsonArray.getJSONObject(i).getInt("id_obra"), 
															jsonArray.getJSONObject(i).getString("descripcion"), 
															jsonArray.getJSONObject(i).getString("clave_concepto")
														);
							}	
							
						}
						if (JSONObject.has("contratistas")) {
							Log.d(log, "contratistas");
							CatContratistas contratistas=new CatContratistas(ConfigEmpresaActivity.this);
							JSONArray jsonArray = new JSONArray(JSONObject.getString("contratistas"));
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = jsonArray.getJSONObject(i);
								contratistas.altaCatContratistas(jsonArray.getJSONObject(i).getInt("id_empresa"), jsonArray.getJSONObject(i).getString("razon_social"));
							}				
						}
						
						new Alert(ConfigEmpresaActivity.this, log, "Se ha descargado correctamente los catálogos ");
						pd.dismiss();
						(new CatConceptos(ConfigEmpresaActivity.this)).getAllLabels();
						
						startActivity(new Intent(ConfigEmpresaActivity.this, MainActivity.class));
					 } catch (FileNotFoundException e) {
					     e.printStackTrace();
					    } catch (IOException e) {
					     e.printStackTrace();
					    } catch (JSONException e) {
					     e.printStackTrace();
					    }

		    
		     
		     
		     
		 
		    /* for (int i=0; i<countriesObj.length(); i++){
		      Gson gson = new Gson();
		      String countryInfo = countriesObj.getJSONObject(i).toString();
		      Country country = gson.fromJson(countryInfo, Country.class);
		      countryData.append(country.getCode() + ": " + country.getName() +"\n");
		     }*/
		      
		     /*TextView showCountries = (TextView) findViewById(R.id.countryData);
		     showCountries.setText(countriesObj.toString());
		      
		     /*Toast toast = Toast.makeText(ConfigEmpresaActivity.this, 
		       "Downloading of data just finished", Toast.LENGTH_LONG);
		     toast.setGravity(Gravity.TOP, 25, 400);
		     toast.show();*/
		     
		      
		   
		 
		   }
		  }
		 }; 
	
	
	public void set_config_obra(View view){
		id = Integer.parseInt(((SpinnerObject) spinner_obras.getSelectedItem()).getId() + "");		
		if(id==0){
			new Alert(this, log, "Selecione un Obra ");
		}else{	
			int id_obra=obra.get_idobra(id);
			Usuario usuario= new Usuario(this);
			usuario.setObra(id_obra);
			
			Obras obra=new Obras(this);
			//new Alert(this, log, "Guardado correctamente");
			Log.d("url",catalogos + "idbase="+obra.getBase(id)+"&idobra="+ id_obra);
			
			//new MyTask(this).execute(catalogos + "?idobra="+ idobra);	
			
			new MyTask(this).execute(catalogos + "idbase="+obra.getBase(id)+"&idobra="+ id_obra);
			Log.e("urlcat","="+catalogos + "idbase="+obra.getBase(id)+"&idobra="+ id_obra);
		}
		
	}
	@Override
	public void onTaskComplete(String result) {
		try {	
			
			JSONObject JSONObject = new JSONObject(result);
			OrdenesCompra ordencompra= new OrdenesCompra(this);
			Log.d(log, "ordenescompra");			
			if (JSONObject.has("ordenescompra")) {
				ordencompra.deletecatalogos();
				JSONArray jsonArray = new JSONArray(JSONObject.getString("ordenescompra"));
				for (int i = 0; i < jsonArray.length(); i++) {
					ordencompra.altaOrdenesCompra(
							  jsonArray.getJSONObject(i).getInt("id_item")
							, jsonArray.getJSONObject(i).getDouble("existencia")
							, jsonArray.getJSONObject(i).getInt("id_material")
							, jsonArray.getJSONObject(i).getInt("numero_folio")
							//, jsonArray.getJSONObject(i).getString("descripcion")
							, jsonArray.getJSONObject(i).getString("unidad")
							, jsonArray.getJSONObject(i).getString("razon_social")
							, jsonArray.getJSONObject(i).getInt("id_empresa")
							, jsonArray.getJSONObject(i).getInt("id_sucursal")
							, jsonArray.getJSONObject(i).getInt("id_moneda")
							, jsonArray.getJSONObject(i).getInt("id_item")
							, jsonArray.getJSONObject(i).getDouble("precio_unitario")
							);
				}				
			}
			
			if (JSONObject.has("almacenes")) {
				Log.d(log, "almacenes");
				CatAlmacen catalmacen=new  CatAlmacen(this);
				JSONArray jsonArray = new JSONArray(JSONObject.getString("almacenes"));
				for (int i = 0; i < jsonArray.length(); i++) {
					/*JSONObject jsonObject = jsonArray.getJSONObject(i);
					Log.d(log, jsonObject.toString());	*/
					 catalmacen.altaCatAlmacenes(
							   jsonArray.getJSONObject(i).getInt("id")
							 , jsonArray.getJSONObject(i).getString("descripcion")
							 );
					
				}				
			}
			if (JSONObject.has("materiales")) {
				Log.d(log, "materiales");
				CatMaterial catmateriales=new CatMaterial(this);
				JSONArray jsonArray = new JSONArray(JSONObject.getString("materiales"));
				for (int i = 0; i < jsonArray.length(); i++) {
					/*JSONObject jsonObject = jsonArray.getJSONObject(i);
					Log.d(log, jsonObject.toString());	*/
					 catmateriales.altaCatMateriales(
							   jsonArray.getJSONObject(i).getInt("id_material")
							 , jsonArray.getJSONObject(i).getString("descripcion")
							 );
				}				
			}
			if (JSONObject.has("materiales_x_almacen")) {
				Log.d(log, "materiales_x_almacen");
				MaterialesAlamacen existencia= new MaterialesAlamacen(this);				
				JSONArray jsonArray = new JSONArray(JSONObject.getString("materiales_x_almacen"));
				for (int i = 0; i < jsonArray.length(); i++) {
					/*JSONObject jsonObject = jsonArray.getJSONObject(i);
					Log.d(log, jsonObject.toString());		*/
					existencia.altaMaterialesAlamace(
							 jsonArray.getJSONObject(i).getInt("id_almacen")
							//, jsonArray.getJSONObject(i).getString("descripcion")
							, jsonArray.getJSONObject(i).getInt("id_material")
							, jsonArray.getJSONObject(i).getInt("id_obra")
							, jsonArray.getJSONObject(i).getString("unidad")
							, jsonArray.getJSONObject(i).getDouble("cantidad")
							);
					//existencia.altaMaterialesAlamace(idalmacen, descripcion, idmaterial, i, unidad, existencia)
					
				}				
			}
			if (JSONObject.has("contratistas")) {
				Log.d(log, "contratistas");
				CatContratistas contratistas=new CatContratistas(this);
				JSONArray jsonArray = new JSONArray(JSONObject.getString("contratistas"));
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					Log.d(log, jsonObject.toString());	
					contratistas.altaCatContratistas(jsonArray.getJSONObject(i).getInt("id_empresa"), jsonArray.getJSONObject(i).getString("razon_social"));
				}				
			}
			new Alert(this, log, "Se ha descargado correctamente los catálogos ");
			startActivity(new Intent(ConfigEmpresaActivity.this, MainActivity.class));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/*public void pasarGarbageCollector(){
		 
        Runtime garbage = Runtime.getRuntime();
        //System.out.println(&quot;Memoria libre antes de limpieza: &quot;+ garbage.freememory());
        Log.i("","Memoria libre antes de limpieza: "+ garbage.freeMemory());
 
        garbage.gc();
 
        //System.out.println(&quot;Memoria libre tras la limpieza: &quot;+ garbage.freememory());
        Log.i("","Memoria libre tras la limpieza: "+ garbage.freeMemory());
    }*/
	
	
	
}



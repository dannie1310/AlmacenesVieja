package com.grupohi.almacenv1;

import java.util.ArrayList;
import java.util.List;


import com.grupohi.almacenv1.lib.*;

import com.grupohi.almacenv1.modelo.CatAlmacen;
import com.grupohi.almacenv1.modelo.CatConceptos;
import com.grupohi.almacenv1.modelo.CatContratistas;
import com.grupohi.almacenv1.modelo.ListViewOrdeneTemp;
import com.grupohi.almacenv1.modelo.ListViewOrdenesCompra;
import com.grupohi.almacenv1.modelo.OrdenesCompra;
import com.grupohi.almacenv1.modelo.Usuario;


import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint({ "CutPasteId", "NewApi" })
public class FormEntradaActivity extends Activity{
	public static final String log = "Entrada a almacen";
	private ListView lstOpciones;
	List<SpinnerObject> lables;
	List<ListViewOrdenesCompra> lables_ordenes_compra;
	Spinner spinner_ordenes_compra;
	OrdenesCompra[] datos;
	OrdenesCompra ordencompra;
	AdaptadorTitulares adaptador;
    int transaccion_actual=0;
    List<ListViewOrdeneTemp> temp_ordenes_compra ;
   
    
    OrdenesCompratemp adaptador_temp;
    
    private ListView lista_orden_compra_temp;
    
    ListViewOrdenesCompra objordenescomprasele;
	
    EditText input_remision, observaciones;
    
    TextView labelAlmacenes, labelcheckcargo;
	Spinner almacenes, contratistas;
	EditText cantidad;
	//EditText clave_concepto;
	public String main_unidad;
	String main_descripcion_material;
	CheckBox concepto_o_almacen, checkBoxContratista, checkBoxCargo;
	String remision;
	String obser;
	
    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_entrada);
		ordencompra = new OrdenesCompra(this);
		
		Log.i("entradas...","vamos bien"+")))-->");

		lables = ordencompra.getAllLabels(this);
		temp_ordenes_compra = new ArrayList<ListViewOrdeneTemp>();

		spinner_ordenes_compra = (Spinner) findViewById(R.id.spinner_ordencompra);
		ArrayAdapter<SpinnerObject> spinner_adapter_maquinas = new ArrayAdapter<SpinnerObject>(
				this, android.R.layout.simple_spinner_item, lables);
		spinner_adapter_maquinas
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner_ordenes_compra.setAdapter(spinner_adapter_maquinas);
		input_remision = (EditText) findViewById(R.id.editText_remision);
		observaciones = (EditText) findViewById(R.id.editTextObservaciones);		

		spinner_ordenes_compra
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentView,
							View selectedItemView, int position, long id) {
						//Log.i("posicion",position+")))-->");
						int trans = Integer
								.parseInt(((SpinnerObject) spinner_ordenes_compra
										.getSelectedItem()).getId() + "");
						lables_ordenes_compra = ordencompra.getListViewOrdenesCompra(FormEntradaActivity.this, trans);
						
						transaccion_actual= trans;
						adaptador = new AdaptadorTitulares(FormEntradaActivity.this);
						lstOpciones.setAdapter(adaptador);
						
						
						temp_ordenes_compra = new ArrayList<ListViewOrdeneTemp>();
						adaptador_temp = new OrdenesCompratemp(FormEntradaActivity.this);
						lista_orden_compra_temp.setAdapter(adaptador_temp);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parentView) {
						// your code here
					}
				});

		lables_ordenes_compra = ordencompra.getListViewOrdenesCompra(this,0);
		adaptador = new AdaptadorTitulares(this);

		lstOpciones = (ListView) findViewById(R.id.listView_materiales_ordencompra);

		lstOpciones.setAdapter(adaptador);

		lstOpciones.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				objordenescomprasele=(ListViewOrdenesCompra) a.getAdapter().getItem(position);
				Double cantidad = Double.parseDouble(((ListViewOrdenesCompra) a.getAdapter().getItem(position)).getexistencia()+"");
				if(cantidad>0){	
					main_unidad=((ListViewOrdenesCompra) a.getAdapter().getItem(position)).getunidad();
					capt_cantidad(
						((ListViewOrdenesCompra) a.getAdapter().getItem(position)).getdescripcion()
						,((ListViewOrdenesCompra) a.getAdapter().getItem(position)).getexistencia()
						
					);
				
				Log.i("unidad a insertar","-->"+main_unidad);
				
				main_descripcion_material=((ListViewOrdenesCompra) a.getAdapter().getItem(position)).getdescripcion();
				}else{
					Log.i("else",position+")))-->");
					new Alert(FormEntradaActivity.this, log, "No hay más existencia de material.");
				}
				
				/*Log.e("",
						""
								+ ((ListViewOrdenesCompra) a.getAdapter()
										.getItem(position)).getdescripcion());*/
			}
		});
		
		//**LO QUE SE VA A ENTREGAR/**/
		adaptador_temp = new OrdenesCompratemp(this);

		lista_orden_compra_temp = (ListView) findViewById(R.id.listView_materiales_ordencompra_temp);		

		lista_orden_compra_temp.setAdapter(adaptador_temp);

		lista_orden_compra_temp.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
			
				final ListViewOrdeneTemp act_x=(ListViewOrdeneTemp)a.getAdapter().getItem(position);
				AlertDialog.Builder alert = new AlertDialog.Builder(FormEntradaActivity.this);
				alert.setTitle(log);
				alert.setMessage("¿Estas seguro de eliminar la entrada del el material?");
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Double cantx=Double.parseDouble((act_x).getexistencia()+"");
					adaptador_temp.remove(act_x);
					lista_orden_compra_temp.setAdapter(adaptador_temp);
					
					objordenescomprasele.setexistencia(cantx+objordenescomprasele.getexistencia());
					lstOpciones.setAdapter(adaptador);
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				});
				alert.show();
			}
		});

	}

	class AdaptadorTitulares extends ArrayAdapter<ListViewOrdenesCompra> {

		Activity context;

		AdaptadorTitulares(Activity context) {
			super(context, R.layout.listview_materiales_ordencompra,
					lables_ordenes_compra);
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(
					R.layout.listview_materiales_ordencompra, null);
			/*Log.e("test", position + " -- ");
			Log.e("test", lables_ordenes_compra.get(position).toString());
			Log.e("test", lables_ordenes_compra.get(position).getdescripcion());*/

			

			TextView lblSubtitulo = (TextView) item
					.findViewById(R.id.LblSubTitulo);
			lblSubtitulo.setText(lables_ordenes_compra.get(position)
					.getdescripcion());
			
			TextView lblTitulo = (TextView) item.findViewById(R.id.LblTitulo);
			
			lblTitulo.setText(lables_ordenes_compra.get(position)
					.getexistencia()
					+ " - "
					+ lables_ordenes_compra.get(position).getunidad());

			return (item);
		}
	}
	private AutoCompleteTextView autoComplete_concepto;
	 private ArrayAdapter<String> adapter_autoComplete_concepto;


	
	public void capt_cantidad(String titulo, Double existencia) {
		
		remision=input_remision.getText().toString();
		Log.e("remision",remision);
		obser= observaciones.getText().toString();
		Log.e("observaciones",obser);
		
		LinearLayout layout = new LinearLayout(FormEntradaActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		/************************/
		AlertDialog.Builder alert = new AlertDialog.Builder(
				FormEntradaActivity.this);

		alert.setTitle(titulo);
		alert.setMessage("Cantidad total " + existencia+" "+main_unidad);

		final TextView labelcantidad = new TextView(FormEntradaActivity.this);
		labelcantidad.setText("Cantidad a ingresar:");
		layout.addView(labelcantidad);

		// Set an EditText view to get user input
		 cantidad = new EditText(FormEntradaActivity.this);
		 cantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
		 cantidad.setText(existencia + "");
		layout.addView(cantidad);
		
		LinearLayout layoutOption = new LinearLayout(FormEntradaActivity.this);
		layoutOption.setOrientation(LinearLayout.HORIZONTAL);
		
		final TextView labelcheck = new TextView(FormEntradaActivity.this);
		labelcheck.setText("¿Para almacen?");
		layoutOption.addView(labelcheck);
		
		/*checkBox*/
		concepto_o_almacen=new CheckBox(FormEntradaActivity.this);
		concepto_o_almacen.setChecked(true);
		layoutOption.addView(concepto_o_almacen);
		layout.addView(layoutOption);
		
		concepto_o_almacen.setOnCheckedChangeListener(new OnCheckedChangeListener(){
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
		        if ( isChecked ){
		        	labelAlmacenes.setText("Selecione un almacen:");
		        	almacenes.setVisibility(View.VISIBLE);
		        	//clave_concepto.setVisibility(View.INVISIBLE);
		        	autoComplete_concepto.setVisibility(View.INVISIBLE);
		        	
		        }else{
		        	labelAlmacenes.setText("¿Clave concepto?");
		        	almacenes.setVisibility(View.INVISIBLE);
		        	//clave_concepto.setVisibility(View.VISIBLE);
		        	autoComplete_concepto.setVisibility(View.VISIBLE);
			   }
		    }
		});

		labelAlmacenes = new TextView(FormEntradaActivity.this);
		labelAlmacenes.setText("Selecione un almacen:");
		layout.addView(labelAlmacenes);
		/* sprinner */
		CatAlmacen  catalmacenes= new CatAlmacen(FormEntradaActivity.this);
		
		lables = catalmacenes.getAllLabels(this,0);
		almacenes = new Spinner(FormEntradaActivity.this);
		ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this,
				android.R.layout.simple_spinner_item, lables);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		almacenes.setAdapter(dataAdapter);
		layout.addView(almacenes);
		
		/* fin sprinner */
		
		/* clave concepto*/
		CatConceptos concept=new CatConceptos(FormEntradaActivity.this);
		ArrayList<String> claves_concepto =concept.getAllLabels();  
		autoComplete_concepto = new AutoCompleteTextView(FormEntradaActivity.this);
		adapter_autoComplete_concepto = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,claves_concepto);
		autoComplete_concepto.setAdapter(adapter_autoComplete_concepto);
		autoComplete_concepto.setThreshold(1);
		autoComplete_concepto.setVisibility(View.INVISIBLE);
		layout.addView(autoComplete_concepto);
		
		
		
		
		/*clave_concepto = new EditText(FormEntradaActivity.this);
		clave_concepto.setVisibility(View.INVISIBLE);
		clave_concepto.setText("");*
		//layout.addView(clave_concepto);
		
		/*CONTRATISTAS*/
		LinearLayout layoutCoption = new LinearLayout(FormEntradaActivity.this);
		layoutCoption.setOrientation(LinearLayout.HORIZONTAL);	
		
		final TextView labelcheckCoption = new TextView(FormEntradaActivity.this);
		labelcheckCoption.setText("¿Entrega a contratista?");
		layoutCoption.addView(labelcheckCoption);
		
		checkBoxContratista=new CheckBox(FormEntradaActivity.this);
		checkBoxContratista.setChecked(false);
		layoutCoption.addView(checkBoxContratista);
		checkBoxContratista.setOnCheckedChangeListener(new OnCheckedChangeListener(){
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
		        if ( isChecked ){	
		        	contratistas.setVisibility(View.VISIBLE);
		        	checkBoxCargo.setVisibility(View.VISIBLE);
		        	labelcheckcargo.setVisibility(View.VISIBLE);
		        	
		        }else{
		        	contratistas.setVisibility(View.INVISIBLE);
		        	checkBoxCargo.setVisibility(View.INVISIBLE);
		        	labelcheckcargo.setVisibility(View.INVISIBLE);
			   }
		    }
		});
		
		labelcheckcargo = new TextView(FormEntradaActivity.this);
		labelcheckcargo.setText(" ¿Con cargo? ");
		labelcheckcargo.setVisibility(View.INVISIBLE);
		layoutCoption.addView(labelcheckcargo);
		
		checkBoxCargo=new CheckBox(FormEntradaActivity.this);
		checkBoxCargo.setChecked(true);
		checkBoxCargo.setVisibility(View.INVISIBLE);
		
		layoutCoption.addView(checkBoxCargo);
		
		layout.addView(layoutCoption);
		
		CatContratistas contratista=new CatContratistas(this);
		lables = contratista.getAllLabels(this);
		contratistas = new Spinner(FormEntradaActivity.this);
		ArrayAdapter<SpinnerObject> dataAdapterC = new ArrayAdapter<SpinnerObject>(this,
				android.R.layout.simple_spinner_item, lables);
		dataAdapterC
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		contratistas.setAdapter(dataAdapterC);
		contratistas.setVisibility(View.INVISIBLE);
		layout.addView(contratistas);
		
		/*FIN DE CONTRATISTAS*/
		
		alert.setView(layout);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int almacen =0;
				String desc_almacen="";
				String concepto="";
				String error="";
				int idmaterial=objordenescomprasele.getidmaterial();
				int idtrans=objordenescomprasele.getidtransacion();
				int foliox=objordenescomprasele.getfolio();
				int id_item=objordenescomprasele.getId_item();
								
				double cant=Double.parseDouble(cantidad.getText().toString());
				if(concepto_o_almacen.isChecked()){
					almacen = Integer.parseInt(((SpinnerObject) almacenes.getSelectedItem()).getId() + "");
					desc_almacen=((SpinnerObject) almacenes.getSelectedItem()).getValue() + "";
					if(almacen==0)
						error="Selecione un almacen";
					
				}					
				else{
					//concepto=clave_concepto.getText().toString();
					concepto=autoComplete_concepto.getText().toString();//autocomplet cambio
					Utils uti=new Utils();
					uti.quitaEspacios(concepto);
					//new Alert(FormEntradaActivity.this, log, concepto.length()+"");
					if(uti.quitaEspacios(concepto).length()==0)
						error="Digite un clave de Concepto";
					
					}
				int idcontratista=0;
				int concargo=0;
				String desc_contratista="";
				if(checkBoxContratista.isChecked()){
					idcontratista = Integer.parseInt(((SpinnerObject) contratistas.getSelectedItem()).getId() + "");
					//Log.e("Contratistaid","="+idcontratista);
					desc_contratista=((SpinnerObject) contratistas.getSelectedItem()).getValue() + "";
					//Log.e("Contratistanombre","="+desc_contratista);
					if(idcontratista==0)
						error="Selecione un contratista";
					else
						if(checkBoxCargo.isChecked())
							concargo=1;
				}
				//validando
				if(cant>objordenescomprasele.getexistencia()){
					new Alert(FormEntradaActivity.this, log, "ha superado la cantidad total de material");
				}
				else if(error!="")
					new Alert(FormEntradaActivity.this, log, error);
				else{				
					Log.i("main_unidad-->","-_>"+main_unidad);
					temp_ordenes_compra.add(new ListViewOrdeneTemp(idmaterial,
							"", cant, almacen, concepto,
							main_descripcion_material, desc_almacen, idtrans,
							foliox, remision, obser, idcontratista,
							desc_contratista, concargo, id_item, main_unidad));
					lista_orden_compra_temp.setAdapter(adaptador_temp);
					objordenescomprasele.setexistencia(objordenescomprasele
							.getexistencia() - cant);
					lstOpciones.setAdapter(adaptador);
				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}
	
	
	class OrdenesCompratemp extends ArrayAdapter<ListViewOrdeneTemp> {
		Activity context;

		OrdenesCompratemp(Activity context) {
			super(context, R.layout.listview_temp,	temp_ordenes_compra);
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(
					R.layout.listview_temp, null);
			/*Log.e("test", position + " -- ");
			Log.e("test", lables_ordenes_compra.get(position).toString());
			Log.e("test", lables_ordenes_compra.get(position).getdescripcion());*/

			TextView lblTitulo = (TextView) item.findViewById(R.id.LblTitulo1);
			lblTitulo.setText(temp_ordenes_compra.get(position)
					.getexistencia()
					+ " - "
					+ temp_ordenes_compra.get(position).getunidad());

			TextView lblSubtitulo = (TextView) item.findViewById(R.id.LblSubTitulo1);
			String extra="";
			if(temp_ordenes_compra.get(position).getIdContratista()>0)
				extra="\nContratista: "+temp_ordenes_compra.get(position).getNombreContratista()+" ";
			if(temp_ordenes_compra.get(position).getconcargo()>0)
				extra+=" (Con cargo) ";
			
			lblSubtitulo.setText(temp_ordenes_compra.get(position).getdescripcion_material()
					+"\n"+temp_ordenes_compra.get(position).getdescripcionalmacen()
					+"\n"+temp_ordenes_compra.get(position).getclaveconcepto()
					+extra);
			return (item);
		}
	}

public void guardarentrada(View view) {	
	remision=input_remision.getText().toString();
	remision=Utils.quitaEspacios(remision);
	Log.e("remision",remision);
	obser= observaciones.getText().toString();
	Log.e("observaciones",obser);
	temp_ordenes_compra.get(0).setObservaciones(obser);
	temp_ordenes_compra.get(0).setremision(remision);
	
	 if(temp_ordenes_compra.size()>0){
		 if(!remision.equals("") && !remision.equals(null)){
			 
		 
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		myAlertDialog.setTitle("Entrada?");
		myAlertDialog.setMessage("¿Estás seguro de registrar la entrada?");
		myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {
			
			Utils uti=new Utils();
			String foliomobil=uti.foliomobile(FormEntradaActivity.this);
			
			String cadenaimprimir=ordencompra.guardaroperacionentrada(lables_ordenes_compra, temp_ordenes_compra, foliomobil);				
			int folio=lables_ordenes_compra.get(0).getfolio();			
			String remision=temp_ordenes_compra.get(0).getremision();
			String Fecha=uti.getFechaHora();
			
			String cadenaheadprint=" Remisión: "+remision+"\n";
			//cadenaheadprint+=" Proveedor: "+ordencompra.getNombreEmpresa(temp_ordenes_compra.get(0).gettrasaccion())+"\n";
			cadenaheadprint+=" Folio control interno: #"+foliomobil+"\n";
			cadenaheadprint+="  Folio orden de compra: #"+folio+"\n";
			//cadenaheadprint+=" Fecha/hora: "+Fecha;
			//Log.i("cadenaheadprint",cadenaheadprint);
			String Proveedor=ordencompra.getNombreEmpresa(temp_ordenes_compra.get(0).gettrasaccion());
			
			Usuario user=new Usuario(FormEntradaActivity.this);
			//user.getNombreUsuario();
			
			MainActivity.printheadproyecto("COMPROBANTE DE RECEPCIÓN DE MATERIALES\n"+user.getNombreObra());
			//nombre del proyecto
			MainActivity.printText(Fecha, "RIGHT");
			MainActivity.printText(Proveedor, "CENTER");
			
			MainActivity.printhead(cadenaheadprint);
			
			MainActivity.printText("RELACIÓN DE MATERIALES QUE SON RECIBIDOS", "CENTER");
			
			MainActivity.printText(cadenaimprimir);
			String cade="";
			cade +=user.getNombreUsuario()+"\n";
			cade +="___________________________________________________________\n";
			cade += " Nombre y firma del quien recibe los materiales: \n\n";
			//cade +="--------------------------------------------\n\n";
			//cade +="____________________________________________\n";
			cade +="___________________________________________________________\n";
			cade += " Nombre y firma de quien entrega los materiales: \n\n";
			
			
			//cade += "-----------------------------------\n\n\n";
			//cade += "___________________________________\n";
			//cade += "--------------------------------------------\n\n";	
			//cade += "____________________________________________\n";
			cade +=" Este documento es un comprobante de recepción \n de materiales del Sistema de Administración de \n Obra, no representa un compromiso de pago hasta \n su validación contra las remisiones del \n proveedor y la revisión de factura.";
			MainActivity.printfoot(cade);
			
			new Alert(FormEntradaActivity.this, log, "Se guardo corrrectamente");		
			
			temp_ordenes_compra = new ArrayList<ListViewOrdeneTemp>();
			adaptador_temp = new OrdenesCompratemp(FormEntradaActivity.this);
			lista_orden_compra_temp.setAdapter(adaptador_temp);
			
		}});
		myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {
				
		}});	
		 myAlertDialog.show();
		 }else{
			 new Alert(FormEntradaActivity.this, log, "El campo remisión es requerido.");
		 }
	}else{
		Log.e("NO hay entradas", "no hay entradas");
		new Alert(FormEntradaActivity.this, log, "No hay entradas que almacenar.");		
	}
	

	
}
public void cancelarentrada(View view) {
	startActivity(new Intent(this,MainActivity.class));	

}

	
	


	

}

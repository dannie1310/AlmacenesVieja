package com.grupohi.almacenv1;

import java.util.ArrayList;
import java.util.List;

import com.grupohi.almacenv1.FormEntradaActivity.AdaptadorTitulares;
import com.grupohi.almacenv1.FormEntradaActivity.OrdenesCompratemp;
import com.grupohi.almacenv1.lib.*;
import com.grupohi.almacenv1.modelo.*;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class FormSalidaConsumoActivity extends Activity{
	public static final String log = "Salida de almacen";
	Spinner spinner_almacenesp;
	List<SpinnerObject> lables;
	private ListView lstOpciones;
	
	AdaptadorMateriales adaptador;
	AdaptadorMaterialesTemp adaptador_temp;
	
	List<ListviewGenerico> lables_almacenes;
	ListviewGenerico objAlmacenSele ;
	
	List<ListviewGenerico> temp_materiales ;
	
	private ListView lista_almacenes_temp;
	
	 EditText observaciones;
	 EditText Referencia;
	 
	 AutoCompleteTextView concepto_inial;
	 TextView labelconcepto_inial;
	 
	//private AutoCompleteTextView autoComplete_concepto;
	private ArrayAdapter<String> adapter_autoComplete_concepto;
	ArrayList<String> claves_concepto;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_salida_consumo);
		final CatAlmacen almacenesx= new CatAlmacen(this);
		
		concepto_inial=(AutoCompleteTextView) findViewById(R.id.editTextConcepto);
		
		CatConceptos concept=new CatConceptos(this);
		claves_concepto =concept.getAllLabels();  
		adapter_autoComplete_concepto = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,claves_concepto);
		concepto_inial.setAdapter(adapter_autoComplete_concepto);
		concepto_inial.setThreshold(1);
		
		
		observaciones = (EditText) findViewById(R.id.editTextSObservaciones);
		Referencia = (EditText) findViewById(R.id.editTextSReferencia);
		
		
		
		lables = almacenesx.getAllLabels(this,0);
		temp_materiales= new ArrayList<ListviewGenerico>();
		spinner_almacenesp = (Spinner) findViewById(R.id.spinner_salida_insummo_almacenes);
		ArrayAdapter<SpinnerObject> spinner_adapter_maquinas = new ArrayAdapter<SpinnerObject>(
				this, android.R.layout.simple_spinner_item, lables);
		spinner_adapter_maquinas
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_almacenesp.setAdapter(spinner_adapter_maquinas);
		
		spinner_almacenesp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				//temp_materiales=new ArrayList<ListviewGenerico>();
				int idalmacen = Integer
						.parseInt(((SpinnerObject) spinner_almacenesp
								.getSelectedItem()).getId() + "");
				Log.e("idalmacen", idalmacen + "");
				lables_almacenes=almacenesx.getListViewAlmacenMateriales(FormSalidaConsumoActivity.this, idalmacen);
				/*transaccion_actual= trans;*/
				adaptador = new AdaptadorMateriales(FormSalidaConsumoActivity.this);
				lstOpciones.setAdapter(adaptador);	
				temp_materiales= new ArrayList<ListviewGenerico>();
				adaptador_temp = new AdaptadorMaterialesTemp(FormSalidaConsumoActivity.this);
				lista_almacenes_temp.setAdapter(adaptador_temp);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		});
		
		lables_almacenes = almacenesx.getListViewAlmacenMateriales(this, 0);
		adaptador = new AdaptadorMateriales(this);

		lstOpciones = (ListView) findViewById(R.id.listViewMaterialesAlmacen);
		lstOpciones.setAdapter(adaptador);
		lstOpciones.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				objAlmacenSele = (ListviewGenerico) a.getAdapter().getItem(position);
				Double cantidad = Double.parseDouble(((ListviewGenerico) a.getAdapter().getItem(position)).getexistencia()+"");
				if(cantidad>0){				
				capt_cantidad(
						((ListviewGenerico) a.getAdapter().getItem(position)).getdescripcion()
						,((ListviewGenerico) a.getAdapter().getItem(position)).getexistencia()
						
					);
				main_unidad=((ListviewGenerico) a.getAdapter().getItem(position)).getunidad();
				
				main_descripcion_material=((ListviewGenerico) a.getAdapter().getItem(position)).getdescripcion();
				}else
					new Alert(FormSalidaConsumoActivity.this, log, "No hay más existencia de material.");
				
				/*Log.e("",
						""
								+ ((ListViewOrdenesCompra) a.getAdapter()
										.getItem(position)).getdescripcion());*/
			}
		});
		adaptador_temp = new AdaptadorMaterialesTemp(this);

		lista_almacenes_temp = (ListView) findViewById(R.id.listViewSalidaInsumos);		

		lista_almacenes_temp.setAdapter(adaptador_temp);

		lista_almacenes_temp.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				final ListviewGenerico act_x=(ListviewGenerico)a.getAdapter().getItem(position);
				AlertDialog.Builder alert = new AlertDialog.Builder(FormSalidaConsumoActivity.this);
				alert.setTitle(log);
				alert.setMessage("¿Estas seguro de eliminar la salida del el material?");
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Double cantx=Double.parseDouble((act_x).getexistencia()+"");
					adaptador_temp.remove(act_x);
					
					lista_almacenes_temp.setAdapter(adaptador_temp);
					
					objAlmacenSele.setexistencia(cantx+objAlmacenSele.getexistencia());
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
	
	class AdaptadorMateriales extends ArrayAdapter<ListviewGenerico> {
		Activity context;
		AdaptadorMateriales(Activity context) {
			super(context, R.layout.listview_generico_temp,
					lables_almacenes);
			this.context = context;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listview_generico_temp, null);
			

			TextView lblTitulo = (TextView) item.findViewById(R.id.GTitulo1);
			//lblTitulo.setText(lables_almacenes.get(position).getdescripcion());
			TextView lblSubtitulo = (TextView) item
					.findViewById(R.id.GSubTitulo1);
			lblTitulo.setText(lables_almacenes.get(position).getexistencia()
					+ " - "
					+ lables_almacenes.get(position).getunidad());
			lblSubtitulo.setText(lables_almacenes.get(position).getdescripcion());
			

			return (item);
		}
	}
	class AdaptadorMaterialesTemp extends ArrayAdapter<ListviewGenerico> {
		Activity context;

		AdaptadorMaterialesTemp(Activity context) {
			super(context, R.layout.listview_generico,	temp_materiales);
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(
					R.layout.listview_generico, null);
			/*Log.e("test", position + " -- ");
			Log.e("test", lables_ordenes_compra.get(position).toString());
			Log.e("test", lables_ordenes_compra.get(position).getdescripcion());*/

			TextView lblTitulo = (TextView) item.findViewById(R.id.GTitulo);
			lblTitulo.setText(temp_materiales.get(position)
					.getexistencia()
					+ " - "
					+ temp_materiales.get(position).getunidad());

			TextView lblSubtitulo = (TextView) item.findViewById(R.id.GSubTitulo);
			
			String extra="";
			if(temp_materiales.get(position).getidcontratista()>0)
				extra="\nContratista: "+temp_materiales.get(position).getnombrecontratista()+" ";
			if(temp_materiales.get(position).getconcargo()>0)
				extra+=" -> Con cargo ";
			
			
			lblSubtitulo.setText(temp_materiales.get(position).getdescripcion()
					+"\n"+temp_materiales.get(position).getnombrealmacne()
					+extra);
			return (item);
		}
	}

	TextView labelAlmacenes, labelcheckcargo;
	Spinner contratistas;
	EditText cantidad;
	AutoCompleteTextView claveconcepto;
	String main_unidad;
	String main_descripcion_material;
	CheckBox idalmacen, checkBoxContratista,checkBoxCargo;
	String obser;
	
	public void capt_cantidad(String titulo, Double existencia) {
		
		LinearLayout layout = new LinearLayout(FormSalidaConsumoActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		/************************/
		AlertDialog.Builder alert = new AlertDialog.Builder(
				FormSalidaConsumoActivity.this);

		alert.setTitle(titulo);
		alert.setMessage("Cantidad total " + existencia+" "+main_unidad);

		final TextView labelcantidad = new TextView(FormSalidaConsumoActivity.this);
		labelcantidad.setText("Cantidad:");
		layout.addView(labelcantidad);

		// Set an EditText view to get user input
		 cantidad = new EditText(FormSalidaConsumoActivity.this);
		 cantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
		 cantidad.setText(existencia + "");
		layout.addView(cantidad);
		
	
		labelAlmacenes = new TextView(FormSalidaConsumoActivity.this);
		labelAlmacenes.setText("¿Clave de concepto?:");
		layout.addView(labelAlmacenes);
		
		//claveconcepto = new EditText(FormSalidaConsumoActivity.this);
		//layout.addView(claveconcepto);
		
		claveconcepto = new AutoCompleteTextView(FormSalidaConsumoActivity.this);
		//adapter_autoComplete_concepto = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,claves_concepto);
		claveconcepto.setAdapter(adapter_autoComplete_concepto);
		claveconcepto.setThreshold(1);
		//autoComplete_concepto.setVisibility(View.INVISIBLE);
		layout.addView(claveconcepto);
		
		
		//concepto_inial.setAdapter(adapter_autoComplete_concepto);
		//concepto_inial.setThreshold(1);
		
		/*CONTRATISTA*/
		LinearLayout layoutoptioncontratista = new LinearLayout(FormSalidaConsumoActivity.this);
		layoutoptioncontratista.setOrientation(LinearLayout.HORIZONTAL);
		
		final TextView labelcheckCoption = new TextView(FormSalidaConsumoActivity.this);
		labelcheckCoption.setText("¿Entrega a contratista?");
		layoutoptioncontratista.addView(labelcheckCoption);
		
		checkBoxContratista=new CheckBox(FormSalidaConsumoActivity.this);
		checkBoxContratista.setChecked(false);
		
		layoutoptioncontratista.addView(checkBoxContratista);
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
		
		labelcheckcargo = new TextView(this);
		labelcheckcargo.setText(" ¿Con cargo? ");
		labelcheckcargo.setVisibility(View.INVISIBLE);
		layoutoptioncontratista.addView(labelcheckcargo);
		
		checkBoxCargo=new CheckBox(this);
		checkBoxCargo.setChecked(false);
		checkBoxCargo.setVisibility(View.INVISIBLE);
		
		layoutoptioncontratista.addView(checkBoxCargo);
		
		layout.addView(layoutoptioncontratista);
		
		CatContratistas contratista=new CatContratistas(this);
		lables = contratista.getAllLabels(this);
		contratistas = new Spinner(this);
		ArrayAdapter<SpinnerObject> dataAdapterC = new ArrayAdapter<SpinnerObject>(this,
				android.R.layout.simple_spinner_item, lables);
		dataAdapterC
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		contratistas.setAdapter(dataAdapterC);
		contratistas.setVisibility(View.INVISIBLE);
		layout.addView(contratistas);
		
		/*FIN DE CONTRATISTA*/
		
		
		
		alert.setView(layout);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int whichButton) {
				/*int idalmacen =Integer.parseInt(((SpinnerObject) almacenes.getSelectedItem()).getId() + "");
				String nombrealmacen =((SpinnerObject) almacenes.getSelectedItem()).getValue() + "";*/
				int idalmacen =0;
				String nombrealmacen=claveconcepto.getText().toString();
				double cant=Double.parseDouble(cantidad.getText().toString());
				String error="";
				
				Utils x=new Utils();
				nombrealmacen=x.quitaEspacios(nombrealmacen);
				if(nombrealmacen.equals("")){
					error="Clave de concepto es requerido";
				}
				if(cant==0)
					error="Teclea una cantidad de salida de material";
				else if(cant>objAlmacenSele.getexistencia())
					error="ha superado la cantidad total de material";
				
				/*CONTRATISTA*/
				int idcontratista=0;
				int concargo=0;
				String desc_contratista="";
				if(checkBoxContratista.isChecked()){
					idcontratista = Integer.parseInt(((SpinnerObject) contratistas.getSelectedItem()).getId() + "");
					Log.e("Contratistaid","="+idcontratista);
					desc_contratista=((SpinnerObject) contratistas.getSelectedItem()).getValue() + "";
					Log.e("Contratistanombre","="+desc_contratista);
					if(idcontratista==0)
						error="Selecione un contratista";
				}

				if(checkBoxCargo.isChecked())
				 concargo=1;
				/*FIN CONTRATISTA*/
				
				
				if(error==""){	
					String desc_almacen="";	
					int idmaterial=objAlmacenSele.getidmaterial();					
					int idobra=objAlmacenSele.getidobra();
					
					temp_materiales.add(new ListviewGenerico(idmaterial, main_descripcion_material, main_unidad, cant, idobra, idalmacen,nombrealmacen, idcontratista, concargo,desc_contratista));
					lista_almacenes_temp.setAdapter(adaptador_temp);
					objAlmacenSele.setexistencia(objAlmacenSele.getexistencia()-cant);
					lstOpciones.setAdapter(adaptador);
				
				}else
					new Alert(FormSalidaConsumoActivity.this, log,error);					
				
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

	public void guardarsalidainsumo(View view) {
		String refe=Referencia.getText().toString();
		String conceptox=concepto_inial.getText().toString();
		if(conceptox.equals("") || conceptox.equals(null))
			 new Alert(FormSalidaConsumoActivity.this, log, "El campo de Concepto es requerido.");
		 
		else if(refe.equals("") || refe.equals(null))
			 new Alert(FormSalidaConsumoActivity.this, log, "El campo de Referencia es requerido.");
		 
		else if (temp_materiales.size() > 0) {
			AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
			myAlertDialog.setTitle("Salida");
			myAlertDialog.setMessage("¿Estás seguro de registrar la salida?");
			myAlertDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {

							CatAlmacen almacenesx = new CatAlmacen(FormSalidaConsumoActivity.this);
							Utils x = new Utils();
							String clavemobile = x.foliomobile( FormSalidaConsumoActivity.this);
							
							String cadenaimprimir = almacenesx
									.guardarsalidainsumos(lables_almacenes,
											temp_materiales, clavemobile,
											observaciones.getText().toString(), Referencia.getText().toString());
							Usuario users = new Usuario(FormSalidaConsumoActivity.this);
							int idalmacen = Integer
									.parseInt(((SpinnerObject) spinner_almacenesp
											.getSelectedItem()).getId() + "");
							Log.e("idobra", "" + users.getObraActiva());
							almacenesx.salidainsumos(clavemobile
									, observaciones.getText().toString()
									, idalmacen
									, users.getObraActiva()
									, Referencia.getText().toString()
									,concepto_inial.getText().toString());

							String Fecha = x.getFechaHora();
							String cadenaheadprint = "";
							cadenaheadprint += "Folio móvil: #" + clavemobile
									+ "\n";
							cadenaheadprint += "Fecha/hora: " + Fecha;

							MainActivity.printheadproyecto(users
									.getNombreObra());
							MainActivity.printhead(cadenaheadprint);
							MainActivity.printText(cadenaimprimir);
							String cade = "";
							cade += "Entrega: " + users.getNombreUsuario()
									+ "\n\n\n";
							// cade
							// +="--------------------------------------------\n\n";
							cade += "____________________________________________\n\n";
							cade += "Recibe: ";
							// cade +=
							// "-----------------------------------\n\n\n";
							cade += "___________________________________\n\n\n";
							// cade +=
							// "--------------------------------------------\n\n";
							cade += "____________________________________________\n\n";
							cade += "Este documento es un comprobante de recepción \nde materiales del Sistema de Administración de \nObra, no representa un compromiso de pago hasta \nsu validación contra las remisiones del \nproveedor y la revisión de factura.";
							MainActivity.printfoot(cade);
							
							temp_materiales= new ArrayList<ListviewGenerico>();
							adaptador_temp = new AdaptadorMaterialesTemp(FormSalidaConsumoActivity.this);
							lista_almacenes_temp.setAdapter(adaptador_temp);
							
							new Alert(FormSalidaConsumoActivity.this, log, "Se guardo corrrectamente");

						}
					});
			myAlertDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {

						}
					});
			myAlertDialog.show();
		} else {
			Log.e("NO hay entradas", "no hay entradas");
			new Alert(this, log, "No hay salidas que registrar");
		}
		
		
	}

}

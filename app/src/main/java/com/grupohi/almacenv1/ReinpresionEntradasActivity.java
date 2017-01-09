package com.grupohi.almacenv1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.grupohi.almacenv1.FormEntradaActivity.AdaptadorTitulares;
import com.grupohi.almacenv1.lib.Alert;
import com.grupohi.almacenv1.lib.Utils;
import com.grupohi.almacenv1.modelo.Entradas;
import com.grupohi.almacenv1.modelo.ListViewOrdeneTemp;
import com.grupohi.almacenv1.modelo.ListViewOrdenesCompra;
import com.grupohi.almacenv1.modelo.OrdenesCompra;
import com.grupohi.almacenv1.modelo.Usuario;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ReinpresionEntradasActivity extends Activity {
	AdaptadorTitulares adaptador;
	public static final String log = "Entrada a almacen";
	private ListView lstOpciones;
	List<ListViewOrdeneTemp> lables_ordenes_compra;
	Entradas ordencompra;
	ListViewOrdeneTemp aux;
	OrdenesCompra ordencomprax;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reimpresion);
		try {
			Utils.copyDataBase(getApplicationContext());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ordencompra = new Entradas(this);
		ordencomprax=new OrdenesCompra(this);
		
		lables_ordenes_compra = ordencompra.getListViewOrdenesCompra(this);
		adaptador = new AdaptadorTitulares(this);

		lstOpciones = (ListView) findViewById(R.id.listViewReimpresion);

		lstOpciones.setAdapter(adaptador);


		lstOpciones.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,long id) {
				
				 aux= (ListViewOrdeneTemp) a.getAdapter().getItem(position);
				
				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(ReinpresionEntradasActivity.this);
				myAlertDialog.setTitle("ReImpresión de Entrada");
				myAlertDialog.setMessage("¿Estás seguro de imprimir?");
				myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
						Log.i(log, "aux.gettrasaccion()= ("+aux.gettrasaccion()+")");
						//int idtransacion= Integer.parseInt(aux.gettrasaccion()+"");
						//int folio= Integer.parseInt(aux.getfolio()+"");
						Utils uti=new Utils();
						
						String clavemobile= aux.getfoliomobil()+"";
						
						int folio= Integer.parseInt(aux.getfolio()+"");
						String observaciones= aux.getObservaciones()+"";
						String fechahora=aux.getclaveconcepto()+"";
						String Proveedor=ordencomprax.getNombreEmpresa(aux.gettrasaccion());
						//Log.e("FechaHora2","1"+fechahora);
						String remision=aux.getremision()+"";
						String Fecha=uti.getFechaHora();
						
						
						
						String cadenatexto=ordencompra.cadenaimprimir(folio, clavemobile);
						cadenatexto+="\n Observaciones: "+observaciones;
						
						String cadenaheadprint=" Remisión: "+remision+"\n";
						//cadenaheadprint+=" Proveedor: "+ordencompra.getNombreEmpresa(temp_ordenes_compra.get(0).gettrasaccion())+"\n";
						cadenaheadprint+=" Folio control interno: #"+clavemobile+"\n";
						cadenaheadprint+="  Folio orden de compra: #"+folio+"\n";
						/*String cadenaheadprint="Folio orden de compra: #"+folio+"\n";
						cadenaheadprint+="Folio m�vil: #"+clavemobile+"\n";
						cadenaheadprint+="Remisi�n: "+remision+"\n";
						cadenaheadprint+="Fecha/hora: "+fechahora;*/
						
						Usuario user=new Usuario(ReinpresionEntradasActivity.this);
						//user.getNombreUsuario();
						
						//MainActivity.printheadproyecto(user.getNombreObra());
						MainActivity.printheadproyecto("COMPROBANTE DE RECEPCIÓN DE MATERIALES\n"+user.getNombreObra());
						MainActivity.printText(Fecha, "RIGHT");
						MainActivity.printText(Proveedor, "CENTER");
						MainActivity.printhead(cadenaheadprint);
						
						MainActivity.printText("RELACIÓN DE MATERIALES QUE SON RECIBIDOS", "CENTER");
						
						MainActivity.printText(cadenatexto);
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
						//MainActivity.printhead(cadenaheadprint);
						/*MainActivity.printText(cadenatexto);
						String cade="";
						cade += "Recibe: "+user.getNombreUsuario()+"\n\n\n";
						//cade +="--------------------------------------------\n\n";
						cade +="____________________________________________\n\n";
						cade += "Entrega: ";
						//cade += "-----------------------------------\n\n\n";
						cade += "___________________________________\n\n\n";
						//cade += "--------------------------------------------\n\n";	
						cade += "____________________________________________\n\n";
						cade +="Este documento es un comprobante de recepci�n \nde materiales del Sistema de Administraci�n de \nObra, no representa un compromiso de pago hasta \nsu validaci�n contra las remisiones del \nproveedor y la revisi�n de factura.";
						MainActivity.printfoot(cade);*/
				}});
				myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
						
				}});	
				 myAlertDialog.show();
			}
		});
		
	}
	class AdaptadorTitulares extends ArrayAdapter<ListViewOrdeneTemp> {
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
			//Log.e("test", position + " -- ");
			//Log.e("test", lables_ordenes_compra.get(position).toString());
			//Log.e("test", lables_ordenes_compra.get(position).getdescripcion_material());

			TextView lblTitulo = (TextView) item.findViewById(R.id.LblTitulo);
			lblTitulo.setText("OC #"
					+ lables_ordenes_compra.get(position).getfolio());

			TextView lblSubtitulo = (TextView) item.findViewById(R.id.LblSubTitulo);
			lblSubtitulo.setText("FM #"+lables_ordenes_compra.get(position).getfoliomobil()+"\nObservación:"+lables_ordenes_compra.get(position).getObservaciones()
					+"\nTiempo: "+lables_ordenes_compra.get(position).getclaveconcepto());
			return (item);
		}
	}
	
	
	

}

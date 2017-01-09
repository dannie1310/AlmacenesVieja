package com.grupohi.almacenv1;

import java.util.ArrayList;
import java.util.List;

import com.grupohi.almacenv1.FormSalidaTrasferenciaActivity.AdaptadorMaterialesTemp;
import com.grupohi.almacenv1.ReimpresionSalidaConsumo.AdaptadorTitulares;
import com.grupohi.almacenv1.modelo.ListviewGenerico;
import com.grupohi.almacenv1.modelo.SalidaTrasferencia;

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

public class ReimprimirSalidaTrasnferncia extends Activity{
	AdaptadorTitulares adaptador;
	public static final String log = "Salida transferencia";
	private ListView lstOpciones;
	List<ListviewGenerico> lables_salida_transferencia;
	SalidaTrasferencia transferencia;
	ListviewGenerico aux;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reimpresion);
		
		transferencia = new SalidaTrasferencia(this);
		
		lables_salida_transferencia = transferencia.getListViewSalidaTrasferencia(this);
		adaptador = new AdaptadorTitulares(this);

		lstOpciones = (ListView) findViewById(R.id.listViewReimpresion);

		lstOpciones.setAdapter(adaptador);

		lstOpciones.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,long id) {
				
				aux= (ListviewGenerico) a.getAdapter().getItem(position);
				
				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(ReimprimirSalidaTrasnferncia.this);
				myAlertDialog.setTitle("ReImpresión de Salida Trasnferencia");
				myAlertDialog.setMessage("¿Estás seguro de imprimir el ticket?");
				myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					String clavemobile= aux.getdescripcion()+"";
					Log.e("clavemobile","="+clavemobile);
					String observacion= aux.getunidad()+"";
					String fechahora= aux.getfechahora()+"";
					String cadenaimprimir=transferencia.getMaterialesSalidaTransferencia(clavemobile);
					cadenaimprimir+="\n\nObservaciones: "+observacion;
					
					Usuario users = new Usuario(ReimprimirSalidaTrasnferncia.this);
					
					String cadenaheadprint = "";
					cadenaheadprint += "Folio móvil: #" + clavemobile
							+ "\n";
					cadenaheadprint += "Fecha/hora: " + fechahora;

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
					cade += "Este documento es un comprobante de recepción \nde materiales del Sistema de Administración de \nObra, no representa un compromiso de pago hasta \nsu validación contra las remisiones del \nproveedor y la revisi�n de factura.";
					MainActivity.printfoot(cade);
					
					

				}});
				myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
						
				}});	
				 myAlertDialog.show();
			}
		});
		
	}
	class AdaptadorTitulares extends ArrayAdapter<ListviewGenerico> {
		Activity context;

		AdaptadorTitulares(Activity context) {
			super(context, R.layout.listview_materiales_ordencompra,
					lables_salida_transferencia);
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(
					R.layout.listview_materiales_ordencompra, null);
			Log.e("test", position + " -- ");
			Log.e("test", lables_salida_transferencia.get(position).toString());
			//Log.e("test", lables_salida_consumos.get(position).getdescripcion_material());

			TextView lblTitulo = (TextView) item.findViewById(R.id.LblTitulo);
			lblTitulo.setText(""
					+ lables_salida_transferencia.get(position).getnombrealmacne());

			TextView lblSubtitulo = (TextView) item.findViewById(R.id.LblSubTitulo);
			lblSubtitulo.setText("FM #"+lables_salida_transferencia.get(position).getdescripcion()+"\nObservación:"+lables_salida_transferencia.get(position).getunidad()
					+"\nTiempo: "+lables_salida_transferencia.get(position).getfechahora());
			return (item);
		}
	}
	

}

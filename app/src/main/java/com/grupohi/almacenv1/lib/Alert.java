package com.grupohi.almacenv1.lib;

import com.grupohi.almacenv1.FormEntradaActivity;
import com.grupohi.almacenv1.MainActivity;

import android.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

public class Alert {
	
	static boolean pregunta=false;
	
	public Alert( Context context, String title, String msj){
	//Builder msj = new AlertDialog.Builder(context);
	AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	alertDialog.setTitle(title);
	alertDialog.setMessage(msj);
	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			// here you can add functions
		}
	});
	//alertDialog.setIcon(R.drawable.icon);
	alertDialog.show();
	}
	
	
	static public boolean pregunta( Context context, String title, String mensaje) {		
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
		myAlertDialog.setTitle("Nueva salida?");
		myAlertDialog.setMessage("¿Deseas hacer otra salida?");
		myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {
			//startActivity(new Intent(FormEntradaActivity.this,FormEntradaActivity.class));
			pregunta=true;
		}});
		myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {
			pregunta=false;
			// startActivity(new Intent(FormEntradaActivity.this,MainActivity.class));	
		}});	
		 myAlertDialog.show();
		return pregunta;
		
	}
	
	

}

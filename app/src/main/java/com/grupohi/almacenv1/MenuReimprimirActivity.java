package com.grupohi.almacenv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuReimprimirActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_reimpresion);
	}
	public void reimprimirentradas(View view) {
		startActivity(new Intent(this, ReinpresionEntradasActivity.class));

	}
	public void imprimirsalidaconsumo(View view) {
		startActivity(new Intent(this, ReimpresionSalidaConsumo.class));

	}
	public void imprimirtransferencia(View view) {
		startActivity(new Intent(this, ReimprimirSalidaTrasnferncia.class));

	}

}

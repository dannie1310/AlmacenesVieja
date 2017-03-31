package com.grupohi.almacenv1;


import com.grupohi.almacenv1.lib.*;
import com.grupohi.almacenv1.modelo.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.PeriodicSync;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.IOException;


public class LoginActivity extends Activity implements	AsyncTaskCompleteListener {
	public static final String log = "Login";
	//private static final String login = "http://gln.com.mx:82/TestPCG/ordencompraservicesmobile/index.php/data/login?"; //PEDRO
	private static String login;  //ELIZABETH
	ProgressBar progressBar;
	EditText user, password;
	Button buttonLogin;
	AlertDialog.Builder msj;
	Obras obras;
	PermisosProyectos permisos;
	String user_text, pass_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		login=getApplicationContext().getString(R.string.url_api)+"ordenes_compra/?controlador=Session&accion=iniciaSesionMovil&";
		user = (EditText) findViewById(R.id.usuario);
		password = (EditText) findViewById(R.id.password);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);

	}
	
	public void login(View view) {
		if (user.getText().length() > 0 || password.getText().length() > 0) {
			user_text = user.getText().toString();
			pass_text = password.getText().toString();
			Usuario usuario =new Usuario(this);
			int idusuario=usuario.getIdUsuario(user_text,pass_text);
			if(idusuario>0){
				usuario.setlogin(idusuario);

				startActivity(new Intent(LoginActivity.this, MainActivity.class));
			}
			
			else
			new MyTask(this).execute(login + "usr=" + user_text + "&pass="	+ pass_text);
			Log.e("login_cadena", login + "usr=" + user_text + "&pass="	+ pass_text);
		} else {
			new Alert(this, log, "Campos vacios .. error");
		}


	}

	@Override
	public void onTaskComplete(String result) {
		try {
			Log.e("login_Rest", result);
			JSONArray jsonArray = new JSONArray(result);	
			if(jsonArray.length()==0){
				new Alert(this, log, "Datos incorrectos");	
			}else{
				String nombre = jsonArray.getJSONObject(0).getString("Descripcion");
				String userCADECO = jsonArray.getJSONObject(0).getString("UsuarioCADECO");
				int idusuario = jsonArray.getJSONObject(0).getInt("IdUsuario");
				Usuario usuario=new Usuario(this);
				if(usuario.altaUsuario(idusuario, user_text, nombre, pass_text, userCADECO)){
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onBackPressed() {
	    // Do Here what ever you want do on back press;
	}

}

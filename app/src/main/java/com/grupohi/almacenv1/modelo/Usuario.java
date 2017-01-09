package com.grupohi.almacenv1.modelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;
import com.grupohi.almacenv1.modelo.*;

public class Usuario {
	public static final String log = "Usuarios";
	public static DB almacen;
	public static SQLiteDatabase db;

	Context _context = null;

	public Usuario(Context context) {
		_context = context;
		almacen = new DB(context);
		//reporte almacen
		//usu de almacen
	}

	public boolean altaUsuario(int idusuario, String user, String nombre, String clave, String cadeco) throws JSONException {
		db = almacen.getWritableDatabase();
		db.execSQL("UPDATE " + DB.TABLE_USUARIOS + " SET "+DB.COLUMN_LOGIN_STATUS+"='0'");
		ContentValues values = new ContentValues();
		values.put(DB.COLUMN_IDUSUARIO, idusuario);
		values.put(DB.COLUMN_NOMBRE, nombre);
		values.put(DB.COLUMN_USUARIO,user);
		values.put(DB.COLUMN_CLAVE, clave);
		values.put(DB.COLUMN_LOGIN_STATUS, true);
		values.put(DB.COLUMN_OBRA_ACTIVA, 0);
		values.put(DB.COLUMN_USERCADECO,cadeco);
		// db.insert(DB.TABLE_USUARIOS, null, values);
		db.insertWithOnConflict(DB.TABLE_USUARIOS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		return true;
	}
	public boolean getLogin() {
		Cursor mCursor=null;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
				// Log.d(log, "todo bien hasta antes de consultar");
				
				String[] columns = { DB.COLUMN_IDUSUARIO };
				mCursor = db.query(DB.TABLE_USUARIOS, columns, DB.COLUMN_LOGIN_STATUS+"=1", null, null, null, "1");

				if (mCursor.moveToFirst()) {
					do {
						int idusuario = mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDUSUARIO));
						Log.d(log, idusuario + " idusuario solo si existe");
						return true;
					} while (mCursor.moveToNext());
				} else {
					// Log.d("consola---","no existe ningun usuario aun");
					return false;
				}
			}
			db.close();
			mCursor.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return false;
		}
		
		return false;
	}	
	public static int getObraActiva() {
		Cursor mCursor=null;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
				// Log.d(log, "todo bien hasta antes de consultar");
				
				String[] columns = { DB.COLUMN_OBRA_ACTIVA };
				mCursor = db.query(DB.TABLE_USUARIOS, columns,DB.COLUMN_LOGIN_STATUS+"=1", null,
						null, null, null);

				if (mCursor.moveToFirst()) {
					do {
						int idobra = mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_OBRA_ACTIVA));
						Log.d(log+"->consulta", idobra + " idobra");
						return idobra;
					} while (mCursor.moveToNext());
				} else {
					//Log.d(log,"no existe ningun usuario aun");
					return 0;
				}
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR este-" + e.getMessage());
			return 0;
		}
		return 0;
	}
	public static int getIdUsuario(String users, String Clave) {
		Cursor mCursor=null;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
				// Log.d(log, "todo bien hasta antes de consultar");
				
				String[] columns = { DB.COLUMN_IDUSUARIO };
				mCursor = db.query(DB.TABLE_USUARIOS, columns, DB.COLUMN_USUARIO+"='"+users+ "' AND "+DB.COLUMN_CLAVE+"='"+Clave+"'", null,
						null, null, null);

				if (mCursor.moveToFirst()) {
					do {
						int idusuario = mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDUSUARIO));
						Log.d(log, idusuario + " idusuario");
						return idusuario;
					} while (mCursor.moveToNext());
				} else {
					// Log.d("consola---","no existe ningun usuario aun");
					return 0;
				}
			}
			db.close();
			mCursor.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return 0;
		}
		
		return 0;
	}
	public void setObra(int idobra) {
		db = almacen.getReadableDatabase();
		db.execSQL("UPDATE " + DB.TABLE_USUARIOS + " SET "+DB.COLUMN_OBRA_ACTIVA+"="+idobra+" WHERE "+DB.COLUMN_IDUSUARIO+"="+getIdusuarioActivo());
		db.close();
	}
	/*public void setObraTurno(int idobra, int turno) {
		Cursor mCursor;
		db = almacen.getReadableDatabase();
		db.execSQL("UPDATE " + DB.TABLE_USUARIOS + " SET "+DB.COLUMN_OBRA_ACTIVA+"="+idobra+", "+DB.COLUMN_TURNO_ACTIVO+"="+turno+" WHERE "+DB.COLUMN_IDUSUARIO+"="+getIdusuarioActivo());
	}*/
	public String getUsuario() {
		Cursor mCursor=null;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
				String[] columns = { DB.COLUMN_USUARIO };
				mCursor = db.query(DB.TABLE_USUARIOS, columns, DB.COLUMN_LOGIN_STATUS+"=1", null,
						null, null, "1");
				if (mCursor.moveToFirst()) {
					do {
						return mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_USUARIO));
					} while (mCursor.moveToNext());
				}
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return null;
		}finally{
			mCursor.close();
		}
		return null;
	}

	public String gethtmlPerfil() {
		Cursor mCursor=null;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
				String[] columns = { DB.COLUMN_NOMBRE, DB.COLUMN_USUARIO };
				mCursor = db.query(DB.TABLE_USUARIOS, columns, null, null,
						null, null, null);
				if (mCursor.moveToFirst()) {
					do {
						// Log.d(log,
						// "[Concepto: "+mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_DESCRIPCION)));
						String html = "";
						String nombre = mCursor.getString(mCursor
								.getColumnIndex(DB.COLUMN_NOMBRE));
						String usuario = mCursor.getString(mCursor
								.getColumnIndex(DB.COLUMN_USUARIO));
						Log.d(log, nombre+ " nombre");
						Log.d(log, usuario+ " usuario");
						Obras obra = new Obras(_context);
						html = "<b>Nombre:</b>" + nombre + "<br>"
								+ "<b>Usuario:</b>" + usuario + "<br>"
								+ "<b>Obra:</b>" + obra.getObra() + "<br>";
						return html;

						// return
						// mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDCONCEPTO));
						// return
						// mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_DESCRIPCION));
					} while (mCursor.moveToNext());
				}
			}
			db.close();
			mCursor.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return null;
		}
		return null;
	}
	public static int getIdusuarioActivo() {
		Cursor mCursor=null;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
				// Log.d(log, "todo bien hasta antes de consultar");
				
				String[] columns = { DB.COLUMN_IDUSUARIO };
				mCursor = db.query(DB.TABLE_USUARIOS, columns,DB.COLUMN_LOGIN_STATUS+"=1", null,
						null, null, null);

				if (mCursor.moveToFirst()) {
					do {
						return  mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_IDUSUARIO));
					} while (mCursor.moveToNext());
				} else {
					Log.d(log,"no existe ningun usuario aun");
					return 0;
				}
			}
			db.close();
			mCursor.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return 0;
		}
		
		return 0;
	}	
	
	public String getNombreUsuario() {
		Cursor mCursor=null;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
			
				String[] columns = { DB.COLUMN_NOMBRE };
				mCursor = db.query(DB.TABLE_USUARIOS, columns,DB.COLUMN_LOGIN_STATUS+"=1", null,
						null, null, null);

				if (mCursor.moveToFirst()) {
					do {
						return  mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_NOMBRE));
					} while (mCursor.moveToNext());
				} else {
					Log.d(log,"no existe ningun usuario aun");
					return null;
				}
			}
			db.close();
			mCursor.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return null;
		}
		
		return null;
	}	
	public String getNombreObra() {
		Cursor mCursor=null;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
			
				String[] columns = { DB.COLUMN_DESCRIPCION };
				mCursor = db.query(DB.TABLE_OBRAS, columns,DB.COLUMN_ID+"="+getObraActiva(), null,
						null, null, null);

				if (mCursor.moveToFirst()) {
					do {
						return  mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_DESCRIPCION));
					} while (mCursor.moveToNext());
				} else {
					Log.d(log,"no existe ningun usuario aun");
					return null;
				}
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return null;
		}
		finally{
			mCursor.close();
		}
		
		return null;
	}	
	public static String getNombreDB() {
		Cursor mCursor=null;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
			
				String[] columns = { DB.COLUMN_NOMBREBASE };
				mCursor = db.query(DB.TABLE_OBRAS, columns,DB.COLUMN_ID+"="+getObraActiva(), null,
						null, null, null);

				if (mCursor.moveToFirst()) {
					do {
						return Base64.encodeToString(mCursor.getString(mCursor.getColumnIndex(DB.COLUMN_NOMBREBASE)).getBytes(), Base64.DEFAULT);
					} while (mCursor.moveToNext());
				} else {
					Log.d(log,"no existe ningun usuario aun");
					return null;
				}
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return null;
		}
		finally{
			mCursor.close();
		}
		
		return null;
	}	
	/*public int getTurnoActivo() {
		Cursor mCursor;
		db = almacen.getReadableDatabase();
		try {
			if (db != null) {
				// Log.d(log, "todo bien hasta antes de consultar");
				
				String[] columns = { DB.COLUMN_TURNO_ACTIVO };
				mCursor = db.query(DB.TABLE_USUARIOS, columns,DB.COLUMN_LOGIN_STATUS+"=1", null,
						null, null, null);

				if (mCursor.moveToFirst()) {
					do {
						return  mCursor.getInt(mCursor.getColumnIndex(DB.COLUMN_TURNO_ACTIVO));
					} while (mCursor.moveToNext());
				} else {
					Log.d(log,"no existe ningun usuario aun");
					return 0;
				}
			}
			db.close();
		} catch (Exception e) {
			Log.d(log, " ERROR " + e.getMessage());
			return 0;
		}
		
		return 0;
	}	*/
	
	public void cerrar_sesion() {
		db = almacen.getWritableDatabase();
		db.execSQL("UPDATE " + DB.TABLE_USUARIOS + " SET "+DB.COLUMN_LOGIN_STATUS+"='0'");
		db.close();
	}
	public void setlogin(int idusuario){
		db = almacen.getWritableDatabase();
		db.execSQL("UPDATE " + DB.TABLE_USUARIOS + " SET "+DB.COLUMN_LOGIN_STATUS+"='0'");
		db.execSQL("UPDATE " + DB.TABLE_USUARIOS + " SET "+DB.COLUMN_LOGIN_STATUS+"='1' WHERE "+ DB.COLUMN_IDUSUARIO+"="+idusuario);
		db.close();
	}
	
	

}
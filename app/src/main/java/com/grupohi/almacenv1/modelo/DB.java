package com.grupohi.almacenv1.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DB extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "almacen";
	private static final int DATABASE_VERSION = 1;
	// --------------------CONCEPTOS----------------------
	public static final String TABLE_CONCEPTOS = "conceptos";
	public static final String COLUMN_IDCONCEPTO = "idconcepto";
	public static final String COLUMN_IDOBRA = "idobra";
	public static final String COLUMN_DESCRIPCION = "descripcion";
	public static final String COLUMN_REFERENCIA = "referencia";
	public static final String COLUMN_CLAVECONCEPTO = "claveconcepto";	
	

	private static final String CREATE_TABLE_CONCEPTO = "CREATE TABLE "
			+ TABLE_CONCEPTOS + "(" 
			+ COLUMN_IDCONCEPTO 
			+ " INTEGER, "
			+ COLUMN_DESCRIPCION 
			+ " TEXT NOT NULL, " 
			+ COLUMN_IDOBRA
			+ " INTEGER, "
			+ COLUMN_CLAVECONCEPTO 
			+ " TEXT, "
			+" PRIMARY KEY(" + COLUMN_IDCONCEPTO + "));";

	// -----------------------USUARIOS--------------------
	public static final String TABLE_USUARIOS = "users";
	public static final String COLUMN_IDUSUARIO = "idusuario";
	public static final String COLUMN_NOMBRE = "nombre";
	public static final String COLUMN_USUARIO = "usuario";
	public static final String COLUMN_LOGIN_STATUS = "status";
	public static final String COLUMN_CLAVE = "clave";
	public static final String COLUMN_OBRA_ACTIVA = "obra_activa";
	public static final String COLUMN_USERCADECO = "usuariocadeco";
	

	private static final String CREATE_TABLE_USUARIOS = "CREATE TABLE "
			+ TABLE_USUARIOS + "(" + COLUMN_IDUSUARIO + " INTEGER, "
			+ COLUMN_NOMBRE + " TEXT NOT NULL,"
			+ COLUMN_USUARIO + " INTEGER NOT NULL,"
			+ COLUMN_LOGIN_STATUS + " INTEGER NOT NULL,"
			+ COLUMN_OBRA_ACTIVA + " INTEGER NOT NULL,"
			+ COLUMN_USERCADECO + " TEXT NOT NULL,"			
			+ COLUMN_CLAVE+ " TEXT NOT NULL,  PRIMARY KEY(" + COLUMN_IDUSUARIO + "));";

	// --------------------OBRAS----------------------

	public static final String TABLE_OBRAS = "obras";

	public static final String COLUMN_IDBASE = "idbase";
	public static final String COLUMN_ID="ID";
	public static final String COLUMN_NOMBREBASE = "nombrebase";
	// public static final String COLUMN_IDOBRA="idobra"; ya esta declarada
	// arriba voy a reutilizar la variable
	// public static final String COLUMN_DESCRIPCION reutizacion de la variable
	// destrita anteriormente
	private static final String CREATE_TABLE_OBRAS = "CREATE TABLE "
			+ TABLE_OBRAS + "( " 
			+ COLUMN_ID + " integer primary key autoincrement not null, "
			+ COLUMN_IDOBRA + " INTEGER, "
			+ COLUMN_DESCRIPCION + " TEXT NOT NULL,"
			+ COLUMN_IDBASE + " INTEGER NOT NULL, "+COLUMN_NOMBREBASE+" TEXT NOT NULL)";
	/*
			+" ,PRIMARY KEY("
			+ COLUMN_IDOBRA + "));";*/
	
	/*--------------ORDENES DE COMPRA-----------------*/
	public static final String TABLE_ORDENCOMPRA = "ordencompra";	
	public static final String COLUMN_IDTRANSACCION = "idtransaccion";
	public static final String COLUMN_EXISTENCIA = "existencia";
	public static final String COLUMN_IDMATERIAL = "idmaterial";
	public static final String COLUMN_FOLIO = "folio";
	
	public static final String COLUMN_ID_EMPRESA = "id_empresa";
	public static final String COLUMN_ID_SUCURSAL = "id_sucursal";
	public static final String COLUMN_PRECIO_UNITARIO = "precio_unitario";
	public static final String COLUMN_ID_MONEDA = "id_moneda";
	public static final String COLUMN_IDITEM = "iditem";	
	
	/*el campo descripcion ya esta creado anteriormente */
	public static final String COLUMN_UNIDAD = "unidad";
	public static final String COLUMN_EMPRESA = "empresa";
	
	private static final String CREATE_TABLE_ORDENCOMPRA = "CREATE TABLE "
			+ TABLE_ORDENCOMPRA + "(" + COLUMN_IDTRANSACCION + " INTEGER, "
			+ COLUMN_EXISTENCIA + "  REAL NOT NULL,"
			+ COLUMN_IDMATERIAL + " INTEGER NOT NULL,"
			+ COLUMN_FOLIO + " INTEGER NOT NULL,"
			//+ COLUMN_DESCRIPCION + " TEXT NOT NULL,"
			+ COLUMN_EMPRESA + " TEXT NOT NULL,"
			+ COLUMN_UNIDAD + " TEXT NOT NULL,"
			+ COLUMN_ID_EMPRESA + " INTEGER NOT NULL,"
			+ COLUMN_ID_SUCURSAL + " INTEGER NOT NULL,"
			+ COLUMN_IDITEM + " INTEGER NOT NULL,"
			+ COLUMN_PRECIO_UNITARIO + " REAL NOT NULL,"
			+ COLUMN_ID_MONEDA + " INTEGER NOT NULL"
			+" );";//, PRIMARY KEY(" + COLUMN_IDTRANSACCION + ")
	
	/*----------------CATALOGOS DE ALMACENES---------------------*/
	public static final String TABLE_ALMACEN= "almacen";
	
	public static final String COLUMN_IDALMACEN = "idalmacen";	
	
	private static final String CREATE_TABLE_ALMACEN= "CREATE TABLE "
			+ TABLE_ALMACEN + "(" + COLUMN_IDALMACEN + " INTEGER, "
			+ COLUMN_DESCRIPCION + " TEXT NOT NULL,"
			+" PRIMARY KEY("+ COLUMN_IDALMACEN + "));";
	
	/*----------------CATALOGOS DE CONTRATISTAS---------------------*/
	public static final String TABLE_CONTRATISTAS= "contratista";
	
	public static final String COLUMN_IDCONTRATISTA = "idcontratista";	
	
	private static final String CREATE_TABLE_CONTRATISTA= "CREATE TABLE "
			+ TABLE_CONTRATISTAS + "(" + COLUMN_IDCONTRATISTA + " INTEGER, "
			+ COLUMN_DESCRIPCION + " TEXT NOT NULL,"
			+" PRIMARY KEY("+ COLUMN_IDCONTRATISTA + "));";
	

	/*----------------CATALOGOS DE MATERIALES---------------------*/
	public static final String TABLE_MATERIALES= "materiales";
	
	private static final String CREATE_TABLE_MATERIALES= "CREATE TABLE "
			+ TABLE_MATERIALES + "(" + COLUMN_IDMATERIAL + " INTEGER, "
			+ COLUMN_DESCRIPCION + " TEXT NOT NULL,"
			+" PRIMARY KEY("+ COLUMN_IDMATERIAL + "));";
	
	/*----------------CATALOGOS DE CONTRATISTAS---------------------*/
	public static final String TABLE_EXISTENCIASALMACEN= "existenciaalmacen";
	

	
	private static final String CREATE_TABLE_EXISTENCIASALMACEN= "CREATE TABLE "
			+ TABLE_EXISTENCIASALMACEN + "(_id INTEGER PRIMARY KEY," + COLUMN_IDALMACEN + " INTEGER, "
			+ COLUMN_IDOBRA + " INTEGER NOT NULL,"
			+ COLUMN_IDMATERIAL + " INTEGER NOT NULL,"
			+ COLUMN_UNIDAD + " TEXT NOT NULL,"
			+ COLUMN_EXISTENCIA  + " REAL NOT NULL)";
			//+ COLUMN_DESCRIPCION + " TEXT NOT NULL )";/*
			//+" PRIMARY KEY("+ COLUMN_IDALMACEN+ "));";*/
	
	//tabla permisos_proyectos de usuario
		public static final String TABLE_PERMISO_PROYECTOS = "permisos_proyectos";
		
		private static final String CREATE_TABLE_PERMISO_PROYECTOS = "CREATE TABLE "
				+ TABLE_PERMISO_PROYECTOS + "("
				+ COLUMN_IDUSUARIO + " INTEGER,"
				+ COLUMN_IDOBRA + " INTEGER, PRIMARY KEY ( "+COLUMN_IDUSUARIO+","+COLUMN_IDOBRA+"));";
		
		/*----------------CAPTURA DE ENTRADAS---------------------*/
		public static final String TABLE_ENTRADA_PARTIDA= "entrada_partida";
		public static final String COLUMN_CLAVEENTRADA = "claveentrada";
		public static final String COLUMN_REMISION = "remision";	
		public static final String COLUMN_OBSERVACIONES = "observaciones";	
		public static final String COLUMN_FECHA_HORA = "fechahora";	
		public static final String COLUMN_CONCARGO = "concargo";	
		
		
		
		public static final String TABLE_ENTRADA= "entrada";
		
		private static final String CREATE_TABLE_ENTRADA= "CREATE TABLE "
				+ TABLE_ENTRADA + "(" + COLUMN_CLAVEENTRADA + " TEXT, "
				+ COLUMN_IDTRANSACCION + " INTEGER NOT NULL,"
				+ COLUMN_REMISION + " TEXT NOT NULL,"	
				+ COLUMN_OBSERVACIONES + " TEXT NOT NULL,"				
				+ COLUMN_FECHA_HORA+" TEXT DEFAULT (datetime('now', 'localtime')) "
				+ ",PRIMARY KEY("+ COLUMN_CLAVEENTRADA + ")"
				+" );";
		
		private static final String CREATE_TABLE_ENTRADAS= "CREATE TABLE "
				+ TABLE_ENTRADA_PARTIDA + "(_id INTEGER PRIMARY KEY," + COLUMN_CLAVEENTRADA + " TEXT, "
				+ COLUMN_IDMATERIAL + " INTEGER NOT NULL,"
				+ COLUMN_IDTRANSACCION + " INTEGER NOT NULL,"
				+ COLUMN_UNIDAD + "  REAL NOT NULL,"
				+ COLUMN_CLAVECONCEPTO + " TEXT ,"	
				+ COLUMN_REMISION + " TEXT NOT NULL,"	
				+ COLUMN_OBSERVACIONES + " TEXT NOT NULL,"
				+ COLUMN_EXISTENCIA + " REAL NOT NULL,"
				+ COLUMN_IDALMACEN + " INTEGER,"
				+ COLUMN_IDCONTRATISTA + " INTEGER,"
				+ COLUMN_IDITEM + " INTEGER,"
				+ COLUMN_CONCARGO + " INTEGER,"
				+ COLUMN_FECHA_HORA+" REAL DEFAULT (datetime('now', 'localtime')) "
				+" );";//,PRIMARY KEY("+ COLUMN_CLAVEENTRADA + ")
		
		/*----------------CAPTURA DE SALIDA-TRANSFERENCIA---------------------*/
		public static final String TABLE_SALIDAS_TRANSFERENCIA= "salidatrasferencia";
		public static final String COLUMN_CLAVESALIDA = "clavesalida";
		
		private static final String CREATE_TABLE_SALIDA_TRANSFERENCIA= "CREATE TABLE "
				+ TABLE_SALIDAS_TRANSFERENCIA + "(" + COLUMN_CLAVESALIDA + " TEXT, "
				+ COLUMN_IDOBRA + " TEXT NOT NULL,"
				+ COLUMN_OBSERVACIONES + " TEXT NOT NULL,"
				+ COLUMN_IDALMACEN + " INTEGER NOT NULL,"		
				+ COLUMN_REFERENCIA + " TEXT NOT NULL,"
				+ COLUMN_FECHA_HORA+" REAL DEFAULT (datetime('now', 'localtime')), "
				+" PRIMARY KEY("+ COLUMN_CLAVESALIDA + "));";
		
		/*----------------CAPTURA DE SALIDA-TRANSFERENCIA---------------------*/
		public static final String TABLE_SALIDAS_TRANSFERENCIA_PARTIDADAS= "salidatransferenciapartidas";
		
		private static final String CREATE_TABLE_SALIDA_TRANSFERENCIA_PARTIDAS= "CREATE TABLE "
				+ TABLE_SALIDAS_TRANSFERENCIA_PARTIDADAS + "(" + COLUMN_CLAVESALIDA + " TEXT, "
				+ COLUMN_IDMATERIAL + " INTEGER NOT NULL,"
				+ COLUMN_UNIDAD + "  TEXT NOT NULL,"
				+ COLUMN_EXISTENCIA + " REAL NOT NULL,"
				+ COLUMN_IDCONTRATISTA + " INTEGER,"
				+ COLUMN_CONCARGO + " INTEGER,"
				+ COLUMN_IDALMACEN + " INTEGER NOT NULL"
				+" );";//PRIMARY KEY("+ COLUMN_CLAVESALIDA + ")
		
		
		
		/*----------------CAPTURA DE SALIDA-INSUMOS---------------------*/
		public static final String TABLE_SALIDAS_INSUMOS= "salidainsumos";
		
		private static final String CREATE_TABLE_SALIDA_INSUMOS= "CREATE TABLE "
				+ TABLE_SALIDAS_INSUMOS + "(" + COLUMN_CLAVESALIDA + " TEXT, "
				+ COLUMN_IDOBRA + " TEXT NOT NULL,"
				+ COLUMN_OBSERVACIONES + " TEXT NOT NULL,"
				+ COLUMN_REFERENCIA + " TEXT NOT NULL,"
				+ COLUMN_CLAVECONCEPTO + " TEXT NOT NULL,"
				+ COLUMN_IDALMACEN + " INTEGER NOT NULL,"			
				+ COLUMN_FECHA_HORA+" REAL DEFAULT (datetime('now', 'localtime')), "
				+" PRIMARY KEY("+ COLUMN_CLAVESALIDA + "));";
		
		
		/*----------------CAPTURA DE SALIDA-INSUMOS-PARTIDAS---------------------*/
		public static final String TABLE_SALIDAS_INSUMOS_PARTIDADAS= "salidainsumospartidas";
		
		private static final String CREATE_TABLE_SALIDA_INSUMOS_PARTIDAS= "CREATE TABLE "
				+ TABLE_SALIDAS_INSUMOS_PARTIDADAS + "(" + COLUMN_CLAVESALIDA + " TEXT, "
				+ COLUMN_IDMATERIAL + " INTEGER NOT NULL,"
				+ COLUMN_UNIDAD + "  TEXT NOT NULL,"
				+ COLUMN_EXISTENCIA + " REAL NOT NULL,"				
				+ COLUMN_IDCONTRATISTA + " INTEGER,"
				+ COLUMN_CONCARGO + " INTEGER,"
				+ COLUMN_CLAVECONCEPTO + " TEXT NOT NULL"
				+" );";//,PRIMARY KEY("+ COLUMN_CLAVESALIDA + ")

	public String[] database = new String[] { CREATE_TABLE_ORDENCOMPRA, CREATE_TABLE_MATERIALES, CREATE_TABLE_EXISTENCIASALMACEN, CREATE_TABLE_PERMISO_PROYECTOS,
			CREATE_TABLE_USUARIOS, CREATE_TABLE_OBRAS, CREATE_TABLE_ALMACEN, CREATE_TABLE_CONTRATISTA,   CREATE_TABLE_ENTRADAS,
			CREATE_TABLE_SALIDA_INSUMOS, CREATE_TABLE_SALIDA_INSUMOS_PARTIDAS, CREATE_TABLE_SALIDA_TRANSFERENCIA_PARTIDAS,CREATE_TABLE_SALIDA_TRANSFERENCIA,
			CREATE_TABLE_ENTRADA, CREATE_TABLE_CONCEPTO};

	public DB(Context context) {		 
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Se ejecuta la sentencia SQL de creaci�n de la tabla
		for (String elemento : database) {
			db.execSQL(elemento);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// En la practica deberemos migrar datos de la tabla antigua
		// a la nueva, por lo que este m�todo deber�a ser m�s elaborado.
		// Se elimina la versi�n anterior de la tabla
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXISTENCIASALMACEN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIALES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALMACEN);	
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERMISO_PROYECTOS);			
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTRATISTAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDENCOMPRA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBRAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRADA_PARTIDA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDAS_INSUMOS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDAS_INSUMOS_PARTIDADAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDAS_TRANSFERENCIA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDAS_TRANSFERENCIA_PARTIDADAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRADA);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CONCEPTO);
		
		 
		// Se crea la nueva versi�n de la tabla
		for (String elemento : database) {
			db.execSQL(elemento);
		}
	}

	public void resetCatalogos(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXISTENCIASALMACEN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIALES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALMACEN);		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERMISO_PROYECTOS);	
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTRATISTAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBRAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRADA_PARTIDA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDAS_INSUMOS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDAS_INSUMOS_PARTIDADAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDAS_TRANSFERENCIA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDAS_TRANSFERENCIA_PARTIDADAS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRADA);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CONCEPTO);
		
		// Se crea la nueva versi�n de la tabla
		for (String elemento : database) {
			db.execSQL(elemento);
		}

	}
}

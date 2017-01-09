package com.grupohi.almacenv1;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bixolon.printer.BixolonPrinter;
import com.grupohi.almacenv1.lib.AsyncTaskCompleteListener;
import com.grupohi.almacenv1.lib.MyTask;
import com.grupohi.almacenv1.modelo.*;
import com.grupohi.almacenv1.printer.DialogManager;
import com.grupohi.almacenv1.printer.WiFiDirectBroadcastReceiver;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements	AsyncTaskCompleteListener {
	public static final String log = "MainPrinterActivity";
	//private static final String obras = "http://gln.com.mx:82/TestPCG/ordencompraservicesmobile/index.php/data/empresasobras?usercadeco=";
	private static String obras;
	
	
	//dac-54 //relacion a una solicitud
	Usuario usuario;
	int idobra;
	Obras obra;
	static Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		usuario = new Usuario(MainActivity.this);
		obra=new Obras(this);
		obras=getApplicationContext().getString(R.string.url_api)+"ordenes_compra/?controlador=SAO&accion=getObrasBaseMovil&usercadeco=";
		
		if (!usuario.getLogin()) {
			startActivity(new Intent(MainActivity.this, LoginActivity.class));
		} else {
			if(obra.count()==0){
				new MyTask(this).execute(obras + ""+ usuario.getUsuario());					
			}else{//descargar catalogos
				if(usuario.getObraActiva()==0)
					startActivity(new Intent(MainActivity.this, ConfigEmpresaActivity.class));				
			}

		}
		mBixolonPrinter = new BixolonPrinter(this, mHandler, null);
		BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.logo1x);
		bitmap = drawable.getBitmap();		
		setTitle(usuario.getNombreObra());

	}
	public void activity_menu_delete(View view) {
		startActivity(new Intent(MainActivity.this, MenuDeleteActivity.class));

	}

	public void activity_menu_reimprimir(View view) {
		startActivity(new Intent(MainActivity.this, MenuReimprimirActivity.class));
	}
	
	public void irentrada(View view) { 
		startActivity(new Intent(MainActivity.this, FormEntradaActivity.class));
	}
	public void salidainsumos(View view) { 
		startActivity(new Intent(MainActivity.this, FormSalidaConsumoActivity.class));
		
	}
	public void salidatransferencia(View view) { 		
		startActivity(new Intent(MainActivity.this, FormSalidaTrasferenciaActivity.class));
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.config_obra:
			startActivity(new Intent(MainActivity.this,	ConfigEmpresaActivity.class));
			return true;			
		case R.id.cerrarsesion:
			usuario.cerrar_sesion();
			startActivity(new Intent(MainActivity.this,	MainActivity.class));
			return true;
		case R.id.config_printer:
			startActivity(new Intent(MainActivity.this,	MainPrinterActivity.class));
			return true;
		case R.id.test_printer:
			startActivity(new Intent(MainActivity.this,	PrintTextActivity.class));
			return true;
		case R.id.conect_printer:
			mBixolonPrinter.findBluetoothPrinters();
			return true;
		case R.id.sincronizadata:
			startActivity(new Intent(MainActivity.this,	SubirDatosActivity.class));
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
	    // Do Here what ever you want do on back press;
	}
	@Override
	public void onTaskComplete(String result) {
		try {
			//JSONObject JSONObject = new JSONObject(result);
			JSONArray jsonArray = new JSONArray(result);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				obra.altaObra( jsonArray.getJSONObject(i).getInt("id"),jsonArray.getJSONObject(i).getString("nombre"), jsonArray.getJSONObject(i).getInt("id_base"),  jsonArray.getJSONObject(i).getString("base"));
				Log.d(log, jsonObject.toString());
				startActivity(new Intent(MainActivity.this, ConfigEmpresaActivity.class));
			}
			Log.e("text",result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
	}
	
	public static final String TAG = "BixolonPrinterSample";
	
	static final String ACTION_GET_DEFINEED_NV_IMAGE_KEY_CODES = "com.bixolon.anction.GET_DEFINED_NV_IMAGE_KEY_CODES";
	static final String ACTION_COMPLETE_PROCESS_BITMAP = "com.bixolon.anction.COMPLETE_PROCESS_BITMAP";
	static final String ACTION_GET_MSR_TRACK_DATA = "com.bixolon.anction.GET_MSR_TRACK_DATA";
	static final String EXTRA_NAME_NV_KEY_CODES = "NvKeyCodes";
	static final String EXTRA_NAME_MSR_MODE = "MsrMode";
	static final String EXTRA_NAME_MSR_TRACK_DATA = "MsrTrackData";
	static final String EXTRA_NAME_BITMAP_WIDTH = "BitmapWidth";
	static final String EXTRA_NAME_BITMAP_HEIGHT = "BitmapHeight";
	static final String EXTRA_NAME_BITMAP_PIXELS = "BitmapPixels";
	
	static final int REQUEST_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE;
	static final int RESULT_CODE_SELECT_FIRMWARE = Integer.MAX_VALUE - 1;
	public static final int MESSAGE_START_WORK = Integer.MAX_VALUE - 2;
	static final int MESSAGE_END_WORK = Integer.MAX_VALUE - 3;
	
	static final String FIRMWARE_FILE_NAME = "FirmwareFileName";

	// Name of the connected device
	private String mConnectedDeviceName = null;
	
	public static BixolonPrinter mBixolonPrinter;
	
	private boolean mIsConnected;
	
	private WiFiDirectBroadcastReceiver mWifiDirectReceiver;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_SELECT_FIRMWARE && resultCode == RESULT_CODE_SELECT_FIRMWARE) {
			final String binaryFilePath = data.getStringExtra(FIRMWARE_FILE_NAME);
			mHandler.obtainMessage(MESSAGE_START_WORK).sendToTarget();
			new Thread(new Runnable() {
				
				public void run() {
					mBixolonPrinter.updateFirmware(binaryFilePath);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mHandler.obtainMessage(MESSAGE_END_WORK).sendToTarget();
				}
			}).start();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private final void setStatus(int resId) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(resId);
	}

	private final void setStatus(CharSequence subtitle) {
		final ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(subtitle);
	}

	private void dispatchMessage(Message msg) {
		switch (msg.arg1) {
		case BixolonPrinter.PROCESS_GET_STATUS:
			if (msg.arg2 == BixolonPrinter.STATUS_NORMAL) {
				Toast.makeText(getApplicationContext(), "No error", Toast.LENGTH_SHORT).show();
			} else {
				StringBuffer buffer = new StringBuffer();
				if ((msg.arg2 & BixolonPrinter.STATUS_COVER_OPEN) == BixolonPrinter.STATUS_COVER_OPEN) {
					buffer.append("Cover is open.\n");
				}
				if ((msg.arg2 & BixolonPrinter.STATUS_PAPER_NOT_PRESENT) == BixolonPrinter.STATUS_PAPER_NOT_PRESENT) {
					buffer.append("Paper end sensor: paper not present.\n");
				}

				Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
			}
			break;
			
		case BixolonPrinter.PROCESS_GET_TPH_THEMISTOR_STATUS:
			if (msg.arg2 == BixolonPrinter.STATUS_TPH_OVER_HEATING) {
				Toast.makeText(getApplicationContext(), "The status of TPH thermistor is overheating.", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case BixolonPrinter.PROCESS_GET_POWER_MODE:
			if (msg.arg2 == BixolonPrinter.STATUS_SMPS_MODE) {
				Toast.makeText(getApplicationContext(), "SMPS mode", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "Battery mode", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case BixolonPrinter.PROCESS_GET_BATTERY_VOLTAGE_STATUS:
			if (msg.arg2 == BixolonPrinter.STATUS_BATTERY_LOW_VOLTAGE) {
				Toast.makeText(getApplicationContext(), "Low voltage", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "Normal voltage", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case BixolonPrinter.PROCESS_GET_RECEIVE_BUFFER_DATA_SIZE:
			Toast.makeText(getApplicationContext(), "Size of data on receive buffer: " + msg.arg2 + " bytes", Toast.LENGTH_SHORT).show();
			break;
			
		case BixolonPrinter.PROCESS_GET_PRINTER_ID:
			Bundle data = msg.getData();
			Toast.makeText(getApplicationContext(), data.getString(BixolonPrinter.KEY_STRING_PRINTER_ID), Toast.LENGTH_SHORT).show();
			break;
			
		case BixolonPrinter.PROCESS_GET_BS_CODE_PAGE:
			data = msg.getData();
			Toast.makeText(getApplicationContext(), data.getString(BixolonPrinter.KEY_STRING_CODE_PAGE), Toast.LENGTH_SHORT).show();
			break;
			
		case BixolonPrinter.PROCESS_GET_PRINT_SPEED:
			switch (msg.arg2) {
			case BixolonPrinter.PRINT_SPEED_LOW:
				Toast.makeText(getApplicationContext(), "Print speed: low", Toast.LENGTH_SHORT).show();
				break;
			case BixolonPrinter.PRINT_SPEED_MEDIUM:
				Toast.makeText(getApplicationContext(), "Print speed: medium", Toast.LENGTH_SHORT).show();
				break;
			case BixolonPrinter.PRINT_SPEED_HIGH:
				Toast.makeText(getApplicationContext(), "Print speed: high", Toast.LENGTH_SHORT).show();
				break;
			}
			break;
			
		case BixolonPrinter.PROCESS_GET_PRINT_DENSITY:
			switch (msg.arg2) {
			case BixolonPrinter.PRINT_DENSITY_LIGHT:
				Toast.makeText(getApplicationContext(), "Print density: light", Toast.LENGTH_SHORT).show();
				break;
			case BixolonPrinter.PRINT_DENSITY_DEFAULT:
				Toast.makeText(getApplicationContext(), "Print density: default", Toast.LENGTH_SHORT).show();
				break;
			case BixolonPrinter.PRINT_DENSITY_DARK:
				Toast.makeText(getApplicationContext(), "Print density: dark", Toast.LENGTH_SHORT).show();
				break;
			}
			break;
			
		case BixolonPrinter.PROCESS_GET_POWER_SAVING_MODE:
			String text = "Power saving mode: ";
			if (msg.arg2 == 0) {
				text += false;
			} else {
				text += true + "\n(Power saving time: " + msg.arg2 + ")";
			}
			Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
			break;

		case BixolonPrinter.PROCESS_AUTO_STATUS_BACK:
			StringBuffer buffer = new StringBuffer(0);
			if ((msg.arg2 & BixolonPrinter.AUTO_STATUS_COVER_OPEN) == BixolonPrinter.AUTO_STATUS_COVER_OPEN) {
				buffer.append("Cover is open.\n");
			}
			if ((msg.arg2 & BixolonPrinter.AUTO_STATUS_NO_PAPER) == BixolonPrinter.AUTO_STATUS_NO_PAPER) {
				buffer.append("Paper end sensor: no paper present.\n");
			}
			
			if (buffer.capacity() > 0) {
				Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "No error.", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case BixolonPrinter.PROCESS_GET_NV_IMAGE_KEY_CODES:
			data = msg.getData();
			int[] value = data.getIntArray(BixolonPrinter.NV_IMAGE_KEY_CODES);
			
			Intent intent = new Intent();
			intent.setAction(ACTION_GET_DEFINEED_NV_IMAGE_KEY_CODES);
			intent.putExtra(EXTRA_NAME_NV_KEY_CODES, value);
			sendBroadcast(intent);
			break;
			
		case BixolonPrinter.PROCESS_EXECUTE_DIRECT_IO:
			buffer = new StringBuffer();
			data = msg.getData();
			byte[] response = data.getByteArray(BixolonPrinter.KEY_STRING_DIRECT_IO);
			for (int i = 0; i < response.length && response[i] != 0; i++) {
				buffer.append(Integer.toHexString(response[i]) + " ");
			}
			
			Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
			break;
			
		case BixolonPrinter.PROCESS_MSR_TRACK:
			intent = new Intent();
			intent.setAction(ACTION_GET_MSR_TRACK_DATA);
			intent.putExtra(EXTRA_NAME_MSR_TRACK_DATA, msg.getData());
			sendBroadcast(intent);
			break;
			
		/*case BixolonPrinter.PROCESS_GET_MSR_MODE:
			intent = new Intent(MainPrinterActivity.this, MsrActivity.class);
			intent.putExtra(EXTRA_NAME_MSR_MODE, msg.arg2);
			startActivity(intent);
			break;*/
		}
	}

	private final Handler mHandler = new Handler(new Handler.Callback() {		
		@SuppressWarnings("unchecked")
		@Override
		public boolean handleMessage(Message msg) {
			Log.d(TAG, "mHandler.handleMessage(" + msg + ")");
			
			switch (msg.what) {
			case BixolonPrinter.MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case BixolonPrinter.STATE_CONNECTED:
					setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
					//mListView.setEnabled(true);
					mIsConnected = true;
					invalidateOptionsMenu();
					break;

				case BixolonPrinter.STATE_CONNECTING:
					setStatus(R.string.title_connecting);
					break;

				case BixolonPrinter.STATE_NONE:
					setStatus(R.string.title_not_connected);
					//mListView.setEnabled(false);
					mIsConnected = false;
					invalidateOptionsMenu();
					//mProgressBar.setVisibility(View.INVISIBLE);
					break;
				}
				return true;
				
			case BixolonPrinter.MESSAGE_WRITE:
				switch (msg.arg1) {
				case BixolonPrinter.PROCESS_SET_SINGLE_BYTE_FONT:
					Bundle data = msg.getData();
					Toast.makeText(getApplicationContext(), data.getString(BixolonPrinter.KEY_STRING_CODE_PAGE), Toast.LENGTH_SHORT).show();
					break;
					
				case BixolonPrinter.PROCESS_SET_DOUBLE_BYTE_FONT:
					mHandler.obtainMessage(MESSAGE_END_WORK).sendToTarget();
					
					Toast.makeText(getApplicationContext(), "Complete to set double byte font.", Toast.LENGTH_SHORT).show();
					break;
					
				case BixolonPrinter.PROCESS_DEFINE_NV_IMAGE:
					mBixolonPrinter.getDefinedNvImageKeyCodes();
					Toast.makeText(getApplicationContext(), "Complete to define NV image", Toast.LENGTH_LONG).show();
					break;
					
				case BixolonPrinter.PROCESS_REMOVE_NV_IMAGE:
					mBixolonPrinter.getDefinedNvImageKeyCodes();
					Toast.makeText(getApplicationContext(), "Complete to remove NV image", Toast.LENGTH_LONG).show();
					break;
					
				case BixolonPrinter.PROCESS_UPDATE_FIRMWARE:
					mBixolonPrinter.disconnect();
					Toast.makeText(getApplicationContext(), "Complete to download firmware.\nPlease reboot the printer.", Toast.LENGTH_SHORT).show();
					break;
				}
				return true;

			case BixolonPrinter.MESSAGE_READ:
				MainActivity.this.dispatchMessage(msg);
				return true;

			case BixolonPrinter.MESSAGE_DEVICE_NAME:
				mConnectedDeviceName = msg.getData().getString(BixolonPrinter.KEY_STRING_DEVICE_NAME);
				Toast.makeText(getApplicationContext(), mConnectedDeviceName, Toast.LENGTH_LONG).show();
				return true;

			case BixolonPrinter.MESSAGE_TOAST:
				//mListView.setEnabled(false);
				Toast.makeText(getApplicationContext(), msg.getData().getString(BixolonPrinter.KEY_STRING_TOAST), Toast.LENGTH_SHORT).show();
				return true;
				
			case BixolonPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
				if (msg.obj == null) {
					Toast.makeText(getApplicationContext(), "No paired device", Toast.LENGTH_SHORT).show();
				} else {
					DialogManager.showBluetoothDialog(MainActivity.this, (Set<BluetoothDevice>) msg.obj);
				}
				return true;
				
			case BixolonPrinter.MESSAGE_PRINT_COMPLETE:
				Toast.makeText(getApplicationContext(), "Complete to print", Toast.LENGTH_SHORT).show();
				return true;
				
			case BixolonPrinter.MESSAGE_ERROR_INVALID_ARGUMENT:
				Toast.makeText(getApplicationContext(), "Invalid argument", Toast.LENGTH_SHORT).show();
				return true;
				
			case BixolonPrinter.MESSAGE_ERROR_NV_MEMORY_CAPACITY:
				Toast.makeText(getApplicationContext(), "NV memory capacity error", Toast.LENGTH_SHORT).show();
				return true;
				
			case BixolonPrinter.MESSAGE_ERROR_OUT_OF_MEMORY:
				Toast.makeText(getApplicationContext(), "Out of memory", Toast.LENGTH_SHORT).show();
				return true;
				
			case BixolonPrinter.MESSAGE_ERROR_CONNECT:
				StringBuffer buffer = new StringBuffer();
				if ((msg.arg1 & BixolonPrinter.STATUS_COVER_OPEN) == BixolonPrinter.STATUS_COVER_OPEN) {
					buffer.append("Cover is open.\n");
				}
				if ((msg.arg1 & BixolonPrinter.STATUS_PAPER_NOT_PRESENT) == BixolonPrinter.STATUS_PAPER_NOT_PRESENT) {
					buffer.append("Paper end sensor: paper not present.\n");
				}

				Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
				break;
				
			case BixolonPrinter.MESSAGE_COMPLETE_PROCESS_BITMAP:
				String text = "Complete to process bitmap.";
				Bundle data = msg.getData();
				byte[] value = data.getByteArray(BixolonPrinter.KEY_STRING_MONO_PIXELS);
				if (value != null) {
					Intent intent = new Intent();
					intent.setAction(ACTION_COMPLETE_PROCESS_BITMAP);
					intent.putExtra(EXTRA_NAME_BITMAP_WIDTH, msg.arg1);
					intent.putExtra(EXTRA_NAME_BITMAP_HEIGHT, msg.arg2);
					intent.putExtra(EXTRA_NAME_BITMAP_PIXELS, value);
					sendBroadcast(intent);
				}
				
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
				return true;
				
			case MESSAGE_START_WORK:
				//mListView.setEnabled(false);
				//mProgressBar.setVisibility(View.VISIBLE);
				return true;
				
			case MESSAGE_END_WORK:
				//mListView.setEnabled(true);
				//mProgressBar.setVisibility(View.INVISIBLE);
				return true;
				
			case BixolonPrinter.MESSAGE_USB_DEVICE_SET:
				if (msg.obj == null) {
					Toast.makeText(getApplicationContext(), "No connected device", Toast.LENGTH_SHORT).show();
				} else {
					DialogManager.showUsbDialog(MainActivity.this, (Set<UsbDevice>) msg.obj, mUsbReceiver);
				}
				return true;
				
			case BixolonPrinter.MESSAGE_USB_SERIAL_SET:
				if (msg.obj == null) {
					Toast.makeText(getApplicationContext(), "No connected device", Toast.LENGTH_SHORT).show();
				} else {
					final HashMap<String, UsbDevice> usbDeviceMap = (HashMap<String, UsbDevice>) msg.obj;
					final String[] items = usbDeviceMap.keySet().toArray(new String[usbDeviceMap.size()]);
					new AlertDialog.Builder(MainActivity.this).setItems(items, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mBixolonPrinter.connect(usbDeviceMap.get(items[which]));
						}
					}).show();
				}
				return true;
				
			case BixolonPrinter.MESSAGE_NETWORK_DEVICE_SET:
				if (msg.obj == null) {
					Toast.makeText(getApplicationContext(), "No connectable device", Toast.LENGTH_SHORT).show();
				}
				DialogManager.showNetworkDialog(MainActivity.this, (Set<String>) msg.obj);
				return true;
			}
			return false;
		}
	});

	private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "mUsbReceiver.onReceive(" + context + ", " + intent + ")");
			String action = intent.getAction();

			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
				mBixolonPrinter.connect();
				Toast.makeText(getApplicationContext(), "Found USB device", Toast.LENGTH_SHORT).show();
			} else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				mBixolonPrinter.disconnect();
				Toast.makeText(getApplicationContext(), "USB device removed", Toast.LENGTH_SHORT).show();
			}

		}
	};
	
	public static void printheadproyecto(String text) {		
		int alignment = BixolonPrinter.ALIGNMENT_CENTER;
		//MainActivity.mBixolonPrinter.printBitmap(bitmap, BixolonPrinter.ALIGNMENT_CENTER, BixolonPrinter.BITMAP_WIDTH_NONE, 60, true);
		MainActivity.mBixolonPrinter.printBitmap(bitmap, alignment, BixolonPrinter.BITMAP_WIDTH_NONE, 60, true);
		
		int attribute = 2;
			attribute |= BixolonPrinter.TEXT_ATTRIBUTE_FONT_A;
	
		int size=0;//int size = BixolonPrinter.TEXT_SIZE_HORIZONTAL1;
		mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_852_LATIN2);
		MainActivity.mBixolonPrinter.printText(text, alignment, attribute, size, false);
		MainActivity.mBixolonPrinter.lineFeed(1, false);
		
		MainActivity.mBixolonPrinter.cutPaper(true);
		MainActivity.mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);
	}
	public static void printhead(String text) {
		int alignment = BixolonPrinter.ALIGNMENT_RIGHT;		
		int attribute = 2;
			attribute |= BixolonPrinter.TEXT_ATTRIBUTE_FONT_A;	
		int size=0;//int size = BixolonPrinter.TEXT_SIZE_HORIZONTAL1;
		mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_852_LATIN2);
		MainActivity.mBixolonPrinter.printText(text, alignment, attribute, size, false);
		MainActivity.mBixolonPrinter.lineFeed(2, false);		
		MainActivity.mBixolonPrinter.cutPaper(true);
		MainActivity.mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);
	}
	public static void printText(String text) {
		int alignment = BixolonPrinter.ALIGNMENT_LEFT;
		
		int attribute = 2;
			attribute |= BixolonPrinter.TEXT_ATTRIBUTE_FONT_A;
	
		int size=0;//int size = BixolonPrinter.TEXT_SIZE_HORIZONTAL1;
		mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_852_LATIN2);
		MainActivity.mBixolonPrinter.printText(text, alignment, attribute, size, false);
		MainActivity.mBixolonPrinter.lineFeed(2, false);		
		MainActivity.mBixolonPrinter.cutPaper(true);
		MainActivity.mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);
	}
	
	public static void printText(String text, String aling) {
		int alignment=BixolonPrinter.ALIGNMENT_LEFT;
		if(aling=="LEFT")
			alignment=BixolonPrinter.ALIGNMENT_LEFT;		
		if(aling=="CENTER")
			alignment=BixolonPrinter.ALIGNMENT_CENTER;		
		if(aling=="RIGHT")
			alignment=BixolonPrinter.ALIGNMENT_RIGHT;		
		
		int attribute = 2;
			attribute |= BixolonPrinter.TEXT_ATTRIBUTE_FONT_A;
	
		int size=0;//int size = BixolonPrinter.TEXT_SIZE_HORIZONTAL1;
		mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_852_LATIN2);
		MainActivity.mBixolonPrinter.printText(text, alignment, attribute, size, false);
		MainActivity.mBixolonPrinter.lineFeed(2, false);		
		MainActivity.mBixolonPrinter.cutPaper(true);
		MainActivity.mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);
	}
	
	public static void printfoot(String text) {
		int alignment = BixolonPrinter.ALIGNMENT_CENTER;
		int attribute = 2;
			attribute |= BixolonPrinter.TEXT_ATTRIBUTE_FONT_A;
		int size=0;//int size = BixolonPrinter.TEXT_SIZE_HORIZONTAL1;
		mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_852_LATIN2);
		MainActivity.mBixolonPrinter.printText(text, alignment, attribute, size, false);
		MainActivity.mBixolonPrinter.lineFeed(3, false);		
		MainActivity.mBixolonPrinter.cutPaper(true);
		MainActivity.mBixolonPrinter.kickOutDrawer(BixolonPrinter.DRAWER_CONNECTOR_PIN5);
	}


}
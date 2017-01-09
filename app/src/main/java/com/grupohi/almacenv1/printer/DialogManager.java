package com.grupohi.almacenv1.printer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.grupohi.almacenv1.*;

import com.bixolon.printer.BixolonPrinter;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@SuppressLint("NewApi")
public class DialogManager {

	private static final String[] CODE_PAGE_ITEMS = {
		"Page 0 437 (USA, Standard Europe)",
		"Page 1 Katakana",
		"Page 2 850 (Multilingual)",
		"Page 3 860 (Portuguese)",
		"Page 4 863 (Canadian-French)",
		"Page 5 865 (Nordic)",
		"Page 16 1252 (Latin I)",
		"Page 17 866 (Cyrillic #2)",
		"Page 18 852 (Latin 2)",
		"Page 19 858 (Euro)",
		"Page 21 862 (Hebrew DOS code)",
		"Page 22 864 (Arabic)",
		"Page 23 Thai42",
		"Page 24 1253 (Greek)",
		"Page 25 1254 (Turkish)",
		"Page 26 1257 (Baltic)",
		"Page 27 Farsi",
		"Page 28 1251 (Cyrillic)",
		"Page 29 737 (Greek)",
		"Page 30 775 (Baltic)",
		"Page 31 Thai14",
		"Page 33 1255 (Hebrew New code)",
		"Page 34 Thai 11",
		"Page 35 Thai 18",
		"Page 36 855 (Cyrillic)",
		"Page 37 857 (Turkish)",
		"Page 38 928 (Greek)",
		"Page 39 Thai 16",
		"Page 40 1256 (Arabic)",
		"Page 41 1258 (Vietnam)",
		"Page 42 KHMER(Cambodia)",
		"Page 47 1250 (Czech)",
		"KS5601 (double byte font)",
		"BIG5 (double byte font)",
		"GB2312 (double byte font)",
		"SHIFT-JIS (double byte font)"
	};

	private static final String[] PRINTER_ID_ITEMS = {
		"Firmware version",
		"Manufacturer",
		"Printer model",
		"Code page"
	};
	
	private static final String[] PRINT_SPEED_ITEMS = {
		"High speed",
		"Medium speed",
		"Low Speed"
	};
	
	private static final String[] PRINT_DENSITY_ITEMS = {
		"Light density",
		"Default density",
		"Dark density"
	};
	
	private static final String[] PRINT_COLOR_ITEMS = {
		"Black",
		"Red"
	};
	
	public static void showBluetoothDialog(Context context, final Set<BluetoothDevice> pairedDevices) {
		final String[] items = new String[pairedDevices.size()];
		int index = 0;
		for (BluetoothDevice device : pairedDevices) {
			items[index++] = device.getAddress();
		}

		new AlertDialog.Builder(context).setTitle("Paired Bluetooth printers")
				.setItems(items, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						MainActivity.mBixolonPrinter.connect(items[which]);
						
					}
				}).show();
	}
	
	@SuppressLint("NewApi")
	public
	static void showUsbDialog(final Context context, final Set<UsbDevice> usbDevices, final BroadcastReceiver usbReceiver) {
		final String[] items = new String[usbDevices.size()];
		int index = 0;
		for (UsbDevice device : usbDevices) {
			items[index++] = "Device name: " + device.getDeviceName() + ", Product ID: " + device.getProductId() + ", Device ID: " + device.getDeviceId();
		}

		new AlertDialog.Builder(context).setTitle("Connected USB printers")
				.setItems(items, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						MainPrinterActivity.mBixolonPrinter.connect((UsbDevice) usbDevices.toArray()[which]);
						
						// listen for new devices
						IntentFilter filter = new IntentFilter();
						filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
						filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
						context.registerReceiver(usbReceiver, filter);
					}
				}).show();
	}

	public static void showNetworkDialog(Context context, Set<String> ipAddressSet) {
		if (ipAddressSet != null) {
			 final String[] items = ipAddressSet.toArray(new String[ipAddressSet.size()]);
			
			new AlertDialog.Builder(context).setTitle("Connectable network printers")
			.setItems(items, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MainPrinterActivity.mBixolonPrinter.connect(items[which], 9100, 5000);
				}
			}).show();
		}
	}

	/*static void showPdf417Dialog(AlertDialog dialog, final Context context) {
		if (dialog == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.dialog_print_pdf417, null);

			dialog = new AlertDialog.Builder(context).setView(layout).setTitle("PDF417")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							EditText editText = (EditText) layout.findViewById(R.id.editText1);
							String data = editText.getText().toString();

							editText = (EditText) layout.findViewById(R.id.editText2);
							String string = editText.getText().toString();
							if (string.length() == 0) {
								Toast.makeText(context, "Please enter the width again.", Toast.LENGTH_SHORT).show();
								return;
							}
							int width = Integer.parseInt(string);

							editText = (EditText) layout.findViewById(R.id.editText3);
							string = editText.getText().toString();
							if (string.length() == 0) {
								Toast.makeText(context, "Please enter the height again.", Toast.LENGTH_SHORT).show();
								return;
							}
							int height = Integer.parseInt(string);
							
							int alignment = BixolonPrinter.ALIGNMENT_LEFT;
							RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup1);
							switch (radioGroup.getCheckedRadioButtonId()) {
							case R.id.radio1:
								alignment = BixolonPrinter.ALIGNMENT_CENTER;
								break;
								
							case R.id.radio2:
								alignment = BixolonPrinter.ALIGNMENT_RIGHT;
								break;
							}

							CheckBox checkBox = (CheckBox) layout.findViewById(R.id.checkBox1);
							if (checkBox.isChecked()) {
								MainPrinterActivity.mBixolonPrinter.printPdf417(data, alignment, width, height, false);
								MainPrinterActivity.mBixolonPrinter.formFeed(true);
							} else {
								MainPrinterActivity.mBixolonPrinter.printPdf417(data, alignment, width, height, true);
							}

						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).create();
		}
		dialog.show();
	}*/

	/*static void showQrCodeDialog(AlertDialog dialog, final Context context) {
		if (dialog == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.dialog_print_qrcode, null);

			dialog = new AlertDialog.Builder(context).setView(layout).setTitle("QR Code")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							EditText editText = (EditText) layout.findViewById(R.id.editText1);
							String data = editText.getText().toString();

							int model = BixolonPrinter.QR_CODE_MODEL2;
							RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup1);
							if (radioGroup.getCheckedRadioButtonId() == R.id.radio0) {
								model = BixolonPrinter.QR_CODE_MODEL1;
							}

							editText = (EditText) layout.findViewById(R.id.editText2);
							String string = editText.getText().toString();
							if (string.length() == 0) {
								Toast.makeText(context, "Please enter the size again.", Toast.LENGTH_SHORT).show();
								return;
							}
							int size = Integer.parseInt(string);
							
							int alignment = BixolonPrinter.ALIGNMENT_LEFT;
							radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup2);
							switch (radioGroup.getCheckedRadioButtonId()) {
							case R.id.radio3:
								alignment = BixolonPrinter.ALIGNMENT_CENTER;
								break;
								
							case R.id.radio4:
								alignment = BixolonPrinter.ALIGNMENT_RIGHT;
								break;
							}

							CheckBox checkBox = (CheckBox) layout.findViewById(R.id.checkBox1);
							if (checkBox.isChecked()) {
								MainPrinterActivity.mBixolonPrinter.printQrCode(data, alignment, model, size, false);
								MainPrinterActivity.mBixolonPrinter.formFeed(true);
							} else {
								MainPrinterActivity.mBixolonPrinter.printQrCode(data, alignment, model, size, true);
							}
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).create();
		}
		dialog.show();
	}
*//*
	static void showMaxiCodeDialog(AlertDialog dialog, Context context) {
		if (dialog == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.dialog_print_maxicode, null);

			dialog = new AlertDialog.Builder(context).setView(layout).setTitle("Maxi Code")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							EditText editText = (EditText) layout.findViewById(R.id.editText1);
							String data = editText.getText().toString();

							int mode = BixolonPrinter.MAXI_CODE_MODE2;
							RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup1);
							switch (radioGroup.getCheckedRadioButtonId()) {
							case R.id.radio0:
								mode = BixolonPrinter.MAXI_CODE_MODE2;
								byte[] header = new byte[] {
										'[', ')', '>', 0x1e, '0', '1', 0x1d, '9', '6', '1',
										'2', '3', '4', '5', '6', '7', '8', '9', 0x1d, '0',
										'0', '7', 0x1d, '2', '5', '0', 0x1d};
								data = new String(header) + data;
								break;

							case R.id.radio1:
								mode = BixolonPrinter.MAXI_CODE_MODE3;
								header = new byte[] {
										'[', ')', '>', 0x1e, '0', '1', 0x1d, '9', '6', 'F',
										'A', 'B', 'C', 'D', 'E', 0x1d, '0', '0', '7', 0x1d,
										'2', '5', '0', 0x1d};
								data = new String(header) + data;
								break;

							case R.id.radio2:
								mode = BixolonPrinter.MAXI_CODE_MODE4;
								header = new byte[] {'E', 0x1d, '7', 0x1d, '5', 0x1d};
								data = new String(header) + data;
								break;
							}
							
							int alignment = BixolonPrinter.ALIGNMENT_LEFT;
							radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup2);
							switch (radioGroup.getCheckedRadioButtonId()) {
							case R.id.radio4:
								alignment = BixolonPrinter.ALIGNMENT_CENTER;
								break;
								
							case R.id.radio5:
								alignment = BixolonPrinter.ALIGNMENT_RIGHT;
								break;
							}

							MainPrinterActivity.mBixolonPrinter.printMaxiCode(data, alignment, mode, true);
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).create();
		}
		dialog.show();
	}
*/
	/*static void showDataMatrixDialog(AlertDialog dialog, Context context) {
		if (dialog == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.dialog_print_datamatrix, null);

			dialog = new AlertDialog.Builder(context).setView(layout).setTitle("DATAMATRIX")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							EditText editText = (EditText) layout.findViewById(R.id.editText1);
							String data = editText.getText().toString();

							editText = (EditText) layout.findViewById(R.id.editText2);
							int size = Integer.parseInt(editText.getText().toString());
							
							int alignment = BixolonPrinter.ALIGNMENT_LEFT;
							RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radioGroup1);
							switch (radioGroup.getCheckedRadioButtonId()) {
							case R.id.radio1:
								alignment = BixolonPrinter.ALIGNMENT_CENTER;
								break;
								
							case R.id.radio2:
								alignment = BixolonPrinter.ALIGNMENT_RIGHT;
								break;
							}

							MainPrinterActivity.mBixolonPrinter.printDataMatrix(data, alignment, size, true);
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).create();
		}
		dialog.show();
	}
*/
	static void showCodePageDialog(AlertDialog dialog, Context context, final Handler handler) {
		if (dialog == null) {
			dialog = new AlertDialog.Builder(context).setTitle("Code page")
					.setItems(CODE_PAGE_ITEMS, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_437_USA);
								break;
							case 1:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_KATAKANA);
								break;
							case 2:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_850_MULTILINGUAL);
								break;
							case 3:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_860_PORTUGUESE);
								break;
							case 4:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_863_CANADIAN_FRENCH);
								break;
							case 5:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_865_NORDIC);
								break;
							case 6:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1252_LATIN1);
								break;
							case 7:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_866_CYRILLIC2);
								break;
							case 8:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_852_LATIN2);
								break;
							case 9:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_858_EURO);
								break;
							case 10:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_862_HEBREW_DOS_CODE);
								break;
							case 11:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_864_ARABIC);
								break;
							case 12:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_THAI42);
								break;
							case 13:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1253_GREEK);
								break;
							case 14:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1254_TURKISH);
								break;
							case 15:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1257_BALTIC);
								break;
							case 16:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_FARSI);
								break;
							case 17:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1251_CYRILLIC);
								break;
							case 18:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_737_GREEK);
								break;
							case 19:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_775_BALTIC);
								break;
							case 20:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_THAI14);
								break;
							case 21:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1255_HEBREW_NEW_CODE);
								break;
							case 22:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_THAI11);
								break;
							case 23:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_THAI18);
								break;
							case 24:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_855_CYRILLIC);
								break;
							case 25:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_857_TURKISH);
								break;
							case 26:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_928_GREEK);
								break;
							case 27:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_THAI16);
								break;
							case 28:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1256_ARABIC);
								break;
							case 29:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1258_VIETNAM);
								break;
							case 30:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_KHMER_CAMBODIA);
								break;
							case 31:
								MainPrinterActivity.mBixolonPrinter.setSingleByteFont(BixolonPrinter.CODE_PAGE_1250_CZECH);
								break;
							case 32:
								handler.obtainMessage(MainPrinterActivity.MESSAGE_START_WORK).sendToTarget();
								new Handler().postDelayed(new Runnable() {
									
									public void run() {
										MainPrinterActivity.mBixolonPrinter.setDoubleByteFont(BixolonPrinter.DOUBLE_BYTE_FONT_KS5601);
									}
								}, 1000);
								break;
							case 33:
								handler.obtainMessage(MainPrinterActivity.MESSAGE_START_WORK).sendToTarget();
								new Handler().postDelayed(new Runnable() {
									
									public void run() {
										MainPrinterActivity.mBixolonPrinter.setDoubleByteFont(BixolonPrinter.DOUBLE_BYTE_FONT_BIG5);
									}
								}, 1000);
								break;
							case 34:
								handler.obtainMessage(MainPrinterActivity.MESSAGE_START_WORK).sendToTarget();
								new Handler().postDelayed(new Runnable() {
									
									public void run() {
										MainPrinterActivity.mBixolonPrinter.setDoubleByteFont(BixolonPrinter.DOUBLE_BYTE_FONT_GB2312);
									}
								}, 1000);
								break;
							case 35:
								handler.obtainMessage(MainPrinterActivity.MESSAGE_START_WORK).sendToTarget();
								new Handler().postDelayed(new Runnable() {
									
									public void run() {
										MainPrinterActivity.mBixolonPrinter.setDoubleByteFont(BixolonPrinter.DOUBLE_BYTE_FONT_SHIFT_JIS);
									}
								}, 1000);
								break;
							}

						}
					}).create();
		}
		dialog.show();
	}

	static void showPrinterIdDialog(AlertDialog dialog, Context context) {
		if (dialog == null) {
			dialog = new AlertDialog.Builder(context).setTitle("Get printer ID")
					.setItems(PRINTER_ID_ITEMS, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								MainPrinterActivity.mBixolonPrinter.getPrinterId(BixolonPrinter.PRINTER_ID_FIRMWARE_VERSION);
								break;
							case 1:
								MainPrinterActivity.mBixolonPrinter.getPrinterId(BixolonPrinter.PRINTER_ID_MANUFACTURER);
								break;
							case 2:
								MainPrinterActivity.mBixolonPrinter.getPrinterId(BixolonPrinter.PRINTER_ID_PRINTER_MODEL);
								break;
							case 3:
								MainPrinterActivity.mBixolonPrinter.getPrinterId(BixolonPrinter.PRINTER_ID_CODE_PAGE);
								break;
							}

						}
					}).create();

		}
		dialog.show();
	}

	private static int mSpeed = BixolonPrinter.PRINT_SPEED_HIGH;
	static void showPrintSpeedDialog(AlertDialog dialog, final Context context) {
		if (dialog == null) {
			dialog = new AlertDialog.Builder(context).setTitle("Print speed")
				.setSingleChoiceItems(PRINT_SPEED_ITEMS, 0, new DialogInterface.OnClickListener() {
				
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							mSpeed = BixolonPrinter.PRINT_SPEED_HIGH;
							break;
						case 1:
							mSpeed = BixolonPrinter.PRINT_SPEED_MEDIUM;
							break;
						case 2:
							mSpeed = BixolonPrinter.PRINT_SPEED_LOW;
							break;
						}
					}
				}).setPositiveButton("OK", new DialogInterface.OnClickListener() {
	
					public void onClick(DialogInterface dialog, int which) {
						MainPrinterActivity.mBixolonPrinter.setPrintSpeed(mSpeed);
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).create();
		}
		mSpeed = BixolonPrinter.PRINT_SPEED_HIGH;
		dialog.show();
	}

	private static int mDensity = BixolonPrinter.PRINT_DENSITY_DEFAULT;
	static void showPrintDensityDialog(AlertDialog dialog, final Context context) {
		if (dialog == null) {
			dialog = new AlertDialog.Builder(context).setTitle("Print density")
				.setSingleChoiceItems(PRINT_DENSITY_ITEMS, 1, new DialogInterface.OnClickListener() {
				
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							mDensity = BixolonPrinter.PRINT_DENSITY_LIGHT;
							break;
						case 1:
							mDensity = BixolonPrinter.PRINT_DENSITY_DEFAULT;
							break;
						case 2:
							mDensity = BixolonPrinter.PRINT_DENSITY_DARK;
							break;
						}
					}
				}).setPositiveButton("OK", new DialogInterface.OnClickListener() {
	
					public void onClick(DialogInterface dialog, int which) {
						MainPrinterActivity.mBixolonPrinter.setPrintDensity(mDensity);
						
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).create();
		}
		mDensity = BixolonPrinter.PRINT_DENSITY_DEFAULT;
		dialog.show();
	}

	/*static void showDirectIoDialog(AlertDialog dialog, final Context context) {
		if (dialog == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.dialog_direct_io, null);

			dialog = new AlertDialog.Builder(context).setView(view).setTitle("Direct IO")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							EditText editText = (EditText) view.findViewById(R.id.editText1);
							String string = editText.getText().toString();
							StringTokenizer stringTokenize = new StringTokenizer(string);

							ArrayList<Byte> arrayList = new ArrayList<Byte>();
							while (stringTokenize.hasMoreTokens()) {
								try {
									byte b = Byte.parseByte(stringTokenize.nextToken(), 16);
									arrayList.add(b);
								} catch (NumberFormatException e) {
									e.printStackTrace();
									Toast.makeText(context, "Invalid command!", Toast.LENGTH_SHORT).show();
									return;
								}
							}

							CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
							boolean hasResponse = checkBox.isChecked();

							if (arrayList.size() > 0) {
								ByteBuffer buffer = ByteBuffer.allocate(arrayList.size());
								for (int i = 0; i < arrayList.size(); i++) {
									buffer.put(arrayList.get(i));
								}

								MainPrinterActivity.mBixolonPrinter.executeDirectIo(buffer.array(), hasResponse);
							}

						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).create();
		}
		dialog.show();
	}*/

	/*static void showPowerSavingModeDialog(AlertDialog dialog, final Context context) {
		if (dialog == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.dialog_power_saving_mode, null);

			dialog = new AlertDialog.Builder(context).setView(view).setTitle("Set page mode")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
							boolean enabled = checkBox.isChecked();
							EditText editText = (EditText) view.findViewById(R.id.editText1);
							String string = editText.getText().toString();
							if (string.length() == 0) {
								Toast.makeText(context, "Please enter the time again.", Toast.LENGTH_SHORT).show();
								return;
							}
							int time = Integer.parseInt(string);
							MainPrinterActivity.mBixolonPrinter.setPowerSavingMode(enabled, time);
						}

					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).create();
		}
		dialog.show();
	}*/
	
	static void showBsCodePageDialog(AlertDialog dialog, Context context) {
		if (dialog == null) {
			String[] items = new String[CODE_PAGE_ITEMS.length - 4];
			for (int i = 0; i < items.length; i++) {
				items[i] = CODE_PAGE_ITEMS[i];
			}
			dialog = new AlertDialog.Builder(context).setItems(items, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_437_USA);
						break;
					case 1:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_KATAKANA);
						break;
					case 2:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_850_MULTILINGUAL);
						break;
					case 3:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_860_PORTUGUESE);
						break;
					case 4:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_863_CANADIAN_FRENCH);
						break;
					case 5:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_865_NORDIC);
						break;
					case 6:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_1252_LATIN1);
						break;
					case 7:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_866_CYRILLIC2);
						break;
					case 8:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_852_LATIN2);
						break;
					case 9:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_858_EURO);
						break;
					case 10:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_862_HEBREW_DOS_CODE);
						break;
					case 11:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_864_ARABIC);
						break;
					case 12:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_THAI42);
						break;
					case 13:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_1253_GREEK);
						break;
					case 14:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_1254_TURKISH);
						break;
					case 15:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_1257_BALTIC);
						break;
					case 16:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_FARSI);
						break;
					case 17:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_1251_CYRILLIC);
						break;
					case 18:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_737_GREEK);
						break;
					case 19:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_775_BALTIC);
						break;
					case 20:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_THAI14);
						break;
					case 21:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_1255_HEBREW_NEW_CODE);
						break;
					case 22:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_THAI11);
						break;
					case 23:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_THAI18);
						break;
					case 24:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_855_CYRILLIC);
						break;
					case 25:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_857_TURKISH);
						break;
					case 26:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_928_GREEK);
						break;
					case 27:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_THAI16);
						break;
					case 28:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_1256_ARABIC);
						break;
					case 29:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_1258_VIETNAM);
						break;
					case 30:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_KHMER_CAMBODIA);
						break;
					case 31:
						MainPrinterActivity.mBixolonPrinter.setBsCodePage(BixolonPrinter.CODE_PAGE_1250_CZECH);
						break;
					}
				}
			}).create();
		}
		dialog.show();
	}
	
	static void showPrintColorDialog(AlertDialog dialog, Context context) {
		if (dialog == null) {
			dialog = new AlertDialog.Builder(context).setTitle("Print color").setItems(PRINT_COLOR_ITEMS, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						MainPrinterActivity.mBixolonPrinter.setPrintColor(BixolonPrinter.COLOR_BLACK);
						break;
						
					case 1:
						MainPrinterActivity.mBixolonPrinter.setPrintColor(BixolonPrinter.COLOR_RED);
						break;
					}
				}
			}).create();
		}
		dialog.show();
	}
}

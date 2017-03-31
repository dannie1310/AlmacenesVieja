package com.grupohi.almacenv1.lib;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.telephony.TelephonyManager;
import android.util.Log;


public class Utils {
	

	public static String getJSONString(String url) {
		String jsonString = null;
		HttpURLConnection linkConnection = null;
		try {
			URL linkurl = new URL(url);
			linkConnection = (HttpURLConnection) linkurl.openConnection();
			int responseCode = linkConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream linkinStream = linkConnection.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int j = 0;
				while ((j = linkinStream.read()) != -1) {
					baos.write(j);
				}
				byte[] data = baos.toByteArray();
				jsonString = new String(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", e.getMessage());
			return null;
		} finally {
			if (linkConnection != null) {
				linkConnection.disconnect();
			}
		}
		return jsonString;
	}

	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public static String getFechaHora(){
		java.util.Date dt = new java.util.Date();
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String fechaHora = sdf.format(dt);
	    return  fechaHora;
	}
	public static JSONObject jsonHttp(String url, List<NameValuePair> nameValue) {
		InputStream is = null;
		StringBuilder sb = null;
		String result_post = null;
		try {
			/*HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValue));
			
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();*/
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection" + e.toString());
		}

		// convert response to string
		try {			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = "0";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result_post = sb.toString();
			Log.d("Sincronizando",result_post);

		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		try {
			Log.e("Mensaje http", " " + result_post);
			return new JSONObject(result_post);
		} catch (JSONException e1) {
			Log.e("Error", " " + e1.getMessage()+")");
			Log.e("Error el lib util", "No Food Found(" + result_post.toString()+")");
			//Utils.setErrorhttp(result_post.toString());
			// Toast.makeText(getApplicationContext(), "No Food Found",
			// Toast.LENGTH_LONG).show();
			return null;
		} catch (ParseException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	public static String quitaEspacios(String texto) {
        java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
        StringBuilder buff = new StringBuilder();
        while (tokens.hasMoreTokens()) {
            buff.append(" ").append(tokens.nextToken());
        }
        return buff.toString().trim();
    }
	public String foliomobile(Context contex) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String currentDateandTime = sdf.format(new Date());
		Log.e("tiempo decimal", currentDateandTime);
		//Log.e("exadecimal",   Hex.getHex(currentDateandTime));
		Log.e("exadecimal to ",  Long.toHexString(Long.parseLong(currentDateandTime)));
		return Long.toHexString(Long.parseLong(currentDateandTime));
		/*int len=6;
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		  StringBuilder sb = new StringBuilder( len );
		   for( int i = 0; i < len; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		 Log.e("IMEI","="+getIMEI(contex) )  ;
		 Log.e("IMEI","="+sb.toString() )  ;
	   return getIMEI(contex) + "" + sb.toString();*/
		
    }

public String getIMEI(Context context){
    TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE); 
    String imei = mngr.getDeviceId();
    return imei;
}
public String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	        
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();
	        
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
public static void copyDataBase(Context mActivity) throws IOException {
    InputStream myInput = new FileInputStream(new File("/data/data/" + mActivity.getPackageName() + "/databases/" + "almacen"));
    File files = new File("/sdcard/files/");
    files.mkdirs();
    String outFileName = "/sdcard/files/aman36.sqlite";
    OutputStream myOutput = new FileOutputStream(outFileName);
    byte[] buffer = new byte[1024];
    int bufferLength;
    while ((bufferLength = myInput.read(buffer)) > 0) {
        myOutput.write(buffer, 0, bufferLength);
    }
    myOutput.flush();
    myOutput.close();
    myInput.close();
}

}

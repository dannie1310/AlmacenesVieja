package com.grupohi.almacenv1.printer;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Build;
import android.widget.Toast;
import com.grupohi.almacenv1.*;
@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	private WifiP2pManager manager;
	private Channel channel;
	private Activity activity;

	private List<WifiP2pDevice> peerList = new ArrayList<WifiP2pDevice>();

	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, Activity activity) {
		super();
		this.manager = manager;
		this.channel = channel;
		this.activity = activity;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				Toast.makeText(activity, "Wifi Direct mode is enabled", Toast.LENGTH_SHORT).show();
			} else {
				peerList.clear();
			}
			Toast.makeText(activity, "P2P state changed - " + state, Toast.LENGTH_SHORT).show();
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			if (manager != null) {
				manager.requestPeers(channel, new PeerListListener() {

					@Override
					public void onPeersAvailable(WifiP2pDeviceList peers) {
						peerList.clear();
						peerList.addAll(peers.getDeviceList());
						if (peerList.size() == 0) {
							Toast.makeText(activity, "No devices found", Toast.LENGTH_SHORT).show();
							return;
						}
					}
				});
			}
			Toast.makeText(activity, "P2P peers changed", Toast.LENGTH_SHORT).show();
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			if (manager == null) {
				return;
			}

			NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

			if (networkInfo.isConnected()) {
				manager.requestConnectionInfo(channel, new ConnectionInfoListener() {

					@Override
					public void onConnectionInfoAvailable(WifiP2pInfo info) {
						
						String text = "Am I the Group Owner? " + ((info.isGroupOwner == true) ? "yes" : "no" ) + "\n";
						text += "Group Owner IP - " + info.groupOwnerAddress.getHostAddress() + "\n";
						
						if (info.groupFormed && info.isGroupOwner) {
							text += "This device will act as a server.";
						} else if (info.groupFormed) {
							text += "This device will act as a client.";
							
						}
						
						Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
						MainPrinterActivity.mBixolonPrinter.connect(info.groupOwnerAddress.getHostAddress(), 9100, 5000);
					}
				});
			} else {
				peerList.clear();
			}
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			WifiP2pDevice device = (WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
			String text = device.deviceName + "\n";

			text += "Peer status :" + device.status + "\n";
			switch (device.status) {
			case WifiP2pDevice.AVAILABLE:
				text += "Available";
			case WifiP2pDevice.INVITED:
				text += "Invited";
			case WifiP2pDevice.CONNECTED:
				text += "Connected";
			case WifiP2pDevice.FAILED:
				text += "Failed";
			case WifiP2pDevice.UNAVAILABLE:
				text += "Unavailable";
			default:
				text += "Unknown";
			}
			
			Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
		}
	}
}

package com.grupohi.almacenv1.lib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class MyTask extends AsyncTask<String, Void, String> {

	private Activity activity;
	private ProgressDialog dialog;
	private AsyncTaskCompleteListener callback;

	public MyTask(Activity act) {
		this.activity = act;
		this.callback = (AsyncTaskCompleteListener)act;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		dialog = new ProgressDialog(activity);
		dialog.setMessage("Espere...");
		dialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		return Utils.getJSONString(params[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (null != dialog && dialog.isShowing()) {
			dialog.dismiss();
		}
		callback.onTaskComplete(result);
	}

}

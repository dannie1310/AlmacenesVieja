package com.grupohi.almacenv1.lib;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements AsyncTaskCompleteListener {
	private static final String rssFeed = "https://www.dropbox.com/s/rhk01nqlyj5gixl/jsonparsing.txt?dl=1";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);

	}

	public void btnclick(View view) {
		new MyTask(this).execute(rssFeed);
	}

	@Override
	public void onTaskComplete(String result) {
		System.out.println("calling....");
		System.out.println("result :: "+ result);
	}
}
package com.example.switchview_example;

import com.appspheregroup.android.swichview.SwitchView;
import com.appspheregroup.android.swichview.SwitchView.OnSwitchChangeListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements OnSwitchChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SwitchView switchView =(SwitchView) findViewById(R.id.switchView1);
		switchView.setOnSwitchChangeListener(this);
		// Set default On/Off
		// switchView.setSwitchOn(false);
		// switchView.setTextOn("On");
		// switchView.setTextOff("Off");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onSwitchChanged(View view, boolean isOn) {
		Toast.makeText(getApplicationContext(), (isOn)?"Switch On":"Switch Off", Toast.LENGTH_SHORT).show();
	}

}

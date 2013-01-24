Android-Switch-button
=====================

A Switch is a two-state toggle switch widget that can select between two options.

======================
Exe.
layout XML file

     <com.appspheregroup.android.swichview.SwitchView
        android:id="@+id/switchView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:background_switch="@drawable/button_switch_facebook"
        app:text_off="text off"
        app:text_on="text on"
        app:background_act=""
        app:background_inact=""/>

    ***********************************************
      text_on - String type, it is Shown alway on SwitchStateOn background. 
      text_off - String type, it is Shown alway on SwitchStateOff background.
      background_switch - thumb image.
      background_act and background_inact - It sopport only NinePatchDrawable type.

Java code file

    SwitchView switchView =(SwitchView) findViewById(R.id.switchView1);
  	switchView.setOnSwitchChangeListener(this);
		// ------Set default On/Off ------//
		// switchView.setSwitchOn(false);
		// switchView.setTextOn("On");
		// switchView.setTextOff("Off");

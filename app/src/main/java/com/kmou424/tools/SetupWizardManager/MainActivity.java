package com.kmou424.tools.SetupWizardManager;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.content.Intent;
import android.content.ComponentName;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Context;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.CardView;

public class MainActivity extends Activity {
	
	//Object Initial Start
	private Button start_setupwizard_button;
	private Button go_setupwizard_info;
	private Button refresh;
	private CardView setupwizard_motion_card;
	private Context context;
	private String setupwizard_package_name;
	private String setupwizard_status_summary_header;;
	private TextView setupwizard_status_summary;
	private TextView setupwizard_status_text;
	
	public void initial() {
		start_setupwizard_button = (Button)INeedAObject(R.id.start_setupwizard);
		go_setupwizard_info = (Button)INeedAObject(R.id.setupwizard_go_info);
		refresh = (Button)INeedAObject(R.id.setupwizard_refresh);
		setupwizard_motion_card = (CardView)INeedAObject(R.id.setupwizard_motion_card);
		context = MainActivity.this;
		setupwizard_package_name = "com.google.android.setupwizard";
		setupwizard_status_summary_header = getString(R.string.setupwizard_status_summary);
		setupwizard_status_summary = (TextView)INeedAObject(R.id.setupwizard_status_summary);
		setupwizard_status_text = (TextView)INeedAObject(R.id.setupwizard_status_text);
	}
	//Object Initial End
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		//Prepare
		initial();
		
		//Here is something which needs to update
		updateSetupWizardStatus();
		click();
    }
	
	/*
	Set OnClickListener
	*/
	public void click() {
		go_setupwizard_info.setOnClickListener(new View.OnClickListener(){
			public void onClick(View p1) {
				Intent intent = new Intent();
				intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				intent.setData(Uri.parse("package:" + setupwizard_package_name));
				startActivity(intent);
			}
		});
		refresh.setOnClickListener(new View.OnClickListener() {
			public void onClick(View p1) {
				updateSetupWizardStatus();
			}
		});
		start_setupwizard_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View p1) {
				startSetupWizard();
			}
		});
	}
	
	/*
	Feture: Refresh
	Q:When will run?
	A:OnCreate() & "Refrash" Button is clicked
	*/
	public void updateSetupWizardStatus() {
		if(context.getPackageManager().getApplicationEnabledSetting(setupwizard_package_name) ==  PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER){
			setupwizard_status_text.setText(setupwizard_status_summary_header + getString(R.string.setupwizard_disabled_text));
			setupwizard_status_text.setTextColor(Color.RED);
			setupwizard_status_summary.setText(getString(R.string.summary_text_on_disabled));
			setupwizard_motion_card.setVisibility(View.GONE);
		} else {
			setupwizard_status_text.setText(setupwizard_status_summary_header + getString(R.string.setupwizard_enabled_text));
			setupwizard_status_text.setTextColor(Color.GREEN);
			setupwizard_status_summary.setText(getString(R.string.summary_text_on_enabled));
			//If you don't want show this Button when SetupWizard is active, you can delete "//".
			//go_setupwizard_info.setVisibility(View.GONE);
			setupwizard_motion_card.setVisibility(View.VISIBLE);
		}
	}
	
	/*
	StartSetupWizard
	Extend from old version
	*/
    public void startSetupWizard() {
		new AlertDialog.Builder(MainActivity.this)
			.setTitle("Warning!")
			.setMessage("少年，你真的想要让开机向导重来一次吗？")
			.setOnCancelListener(new AlertDialog.OnCancelListener(){
				public void onCancel(DialogInterface p1){
					
				}
			})
			.setPositiveButton("是的", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface p1, int p2){
					ComponentName com = new ComponentName(setupwizard_package_name ,"com.google.android.setupwizard.SetupWizardTestActivity");
					Intent intent = new Intent();
					intent.setComponent(com);
					startActivity(intent);
					finish();
				}
			})
			.setNegativeButton("我后悔了", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface p1 ,int p2){}
			})
			.show();
	}
	
	/*
	@param id: android:id
	*/
	public Object INeedAObject(int id) {
		return (Object)findViewById(id);
	}
}

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
import java.util.List;
import android.content.pm.PackageInfo;

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
	
	private String SETUP_WIZARD_PACKAGE_AVAILABLE;
	private String SETUP_WIZARD_PACKAGE_NOT_FOUND;
	
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
		
		SETUP_WIZARD_PACKAGE_AVAILABLE = "setup_wizard_package_available";
		SETUP_WIZARD_PACKAGE_NOT_FOUND = "setup_wizard_package_not_found";
	}
	//Object Initial End
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		//Prepare
		initial();
		
		//Here is something which needs to update
		if (checkAppInstalled(setupwizard_package_name)) {
			updateSetupWizardStatus(SETUP_WIZARD_PACKAGE_AVAILABLE);
			click(SETUP_WIZARD_PACKAGE_AVAILABLE);
		} else {
			updateSetupWizardStatus(SETUP_WIZARD_PACKAGE_NOT_FOUND);
			click(SETUP_WIZARD_PACKAGE_NOT_FOUND);
		}
    }
	private boolean checkAppInstalled(String package_name) {
		if (package_name== null || package_name.isEmpty()) {
			return false;
		}
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> info = packageManager.getInstalledPackages(0);
		if(info == null || info.isEmpty())
			return false;
		for ( int i = 0; i < info.size(); i++ ) {
			if(package_name.equals(info.get(i).packageName)) {
                return true;
			}
		}
		return false;
	}
	/*
	Set OnClickListener
	@param isInstalled: Package com.google.android.setupwizard is installed or not
	      Choice:
		  1. MainActivity.SETUP_WIZARD_PACKAGE_AVAILABLE
		  2. MainActivity.SETUP_WIZARD_PACKAGE_NOT_FOUND
	*/
	public void click(String isInstalled) {
		if (isInstalled.equals(this.SETUP_WIZARD_PACKAGE_AVAILABLE)) {
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
						updateSetupWizardStatus(SETUP_WIZARD_PACKAGE_AVAILABLE);
					}
				});
			start_setupwizard_button.setOnClickListener(new View.OnClickListener() {
					public void onClick(View p1) {
						startSetupWizard();
					}
				});
		} else if (isInstalled.equals(this.SETUP_WIZARD_PACKAGE_NOT_FOUND)) {
			refresh.setOnClickListener(new View.OnClickListener() {
				public void onClick(View p1) {
					finish();
				}
			});
		}
	}
	
	/*
	Feture: Refresh
	Q:When will run?
	A:OnCreate() & "Refrash" Button is clicked
	
	 @param isInstalled: Package com.google.android.setupwizard is installed or not
	 Choice:
	 1. MainActivity.SETUP_WIZARD_PACKAGE_AVAILABLE
	 2. MainActivity.SETUP_WIZARD_PACKAGE_NOT_FOUND
	*/
	public void updateSetupWizardStatus(String isInstalled) {
		if (isInstalled.equals(this.SETUP_WIZARD_PACKAGE_AVAILABLE)) {
			if (context.getPackageManager().getApplicationEnabledSetting(setupwizard_package_name) ==  PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER){
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
		} else if (isInstalled.equals(this.SETUP_WIZARD_PACKAGE_NOT_FOUND)) {
			setupwizard_status_text.setText(setupwizard_status_summary_header + getString(R.string.setupwizard_not_found_text));
			setupwizard_status_text.setTextColor(Color.RED);
			setupwizard_status_summary.setText(getString(R.string.summary_text_not_found));
			refresh.setText(getString(R.string.button_quit_text));
			setupwizard_motion_card.setVisibility(View.GONE);
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

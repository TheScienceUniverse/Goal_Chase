package dev.the01guy.goal_chase.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.util.UUID;

class DeviceInformation {
	private Context context;
	String deviceId = "";
	private final int PERMISSION_READ_STATE = 123;
	private SharedPreferences settings = null;

	DeviceInformation (Context context) {
		this.context = context;
		this.settings = context.getSharedPreferences ("SETTINGS", Context.MODE_PRIVATE);
		this.deviceId = getAndroidId() + getUUId() + getSerialId() + getIMEI();
		// Toast.makeText (context, "" + getAndroidId() + getUUId() + getSerialId() + getIMEI(), Toast.LENGTH_SHORT).show();
	}

	private String getAndroidId() {
		return Settings.Secure.getString (this.context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	private String getSerialId() {
/*
		if (this.settings.getBoolean ("READ_PHONE_STATE", false)) {
			if (Build.VERSION.SDK_INT >= 26) {
				SerialId = Build.getSerial();
			} else {
				SerialId = Build.SERIAL;
			}
		}
*/
		return "";
	}

	private String getUUId() {
		String UUId = this.settings.getString ("UUId", "");

		if (UUId == "") {
			UUId = UUID.randomUUID().toString();
			SharedPreferences.Editor editor = this.settings.edit();
			editor.putString("UUId", UUId);
			editor.apply();
		}

		return UUId;
	}

	private String getIMEI() {
//		String IMEI = "";
/*
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService (Context.TELEPHONY_SERVICE);
		boolean readPhoneState = this.settings.getBoolean ("READ_PHONE_STATE", false);

		if (readPhoneState && telephonyManager != null) {
			if (Build.VERSION.SDK_INT >= 26) {
				if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
					IMEI = telephonyManager.getMeid();
				} else if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
					IMEI = telephonyManager.getImei();
				}
			} else {
				IMEI = telephonyManager.getDeviceId();
			}
		}
*/
		return "";
	}
}
package com.moutamid.maintenanceinspectionapp.utilis;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Constants {

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String REMEBER_ME = "REMEBER_ME";
    public static final String PASS_EQUIPMENT = "PASS_EQUIPMENT";
    public static final String STASH_USER = "STASH_USER";
    public static final String siteID = "siteID";
    public static final String GET_ALL_SITES = "https://inspectx-host.lunarsoft.co.za/api/Sites/GetAll";
    public static final String GET_ALL_LOCATIONS = "https://inspectx-host.lunarsoft.co.za/api/Locations/GetAll";
    public static final String Authenticate = "https://inspectx-host.lunarsoft.co.za/api/TokenAuth/Authenticate";
    public static final String NEW_INSPECTION = "https://inspectx-host.lunarsoft.co.za/api/InspectionForms/NewInspection";
    public static final String GET_EQUIPMENT = "https://inspectx-host.lunarsoft.co.za/api/Equipment/GetBySerialNumber?serialNumber=";
	public static void checkApp(Activity activity) {
        String appName = "maintenanceinspectionapp";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuilder stringBuffer = new StringBuilder();
            while (true) {
                try {
                    if ((input = in != null ? in.readLine() : null) == null) break;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

}

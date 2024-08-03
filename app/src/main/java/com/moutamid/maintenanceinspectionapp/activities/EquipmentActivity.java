package com.moutamid.maintenanceinspectionapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.fxn.stash.Stash;
import com.moutamid.maintenanceinspectionapp.databinding.ActivityEquipmentBinding;
import com.moutamid.maintenanceinspectionapp.models.EquipmentModel;
import com.moutamid.maintenanceinspectionapp.utilis.Constants;
import com.moutamid.maintenanceinspectionapp.utilis.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EquipmentActivity extends AppCompatActivity {
    ActivityEquipmentBinding binding;
    private static final int CAMERA_REQUEST_CODE = 1001;
    private CodeScanner mCodeScanner;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    private static final String TAG = "EquipmentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        CodeScannerView scannerView = binding.scannerView;
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            Log.d(TAG, "onCreate: " + result.getText().toString());
            progressDialog.show();
            getEquipment(result.getText().toString());
        }));
        scannerView.setOnClickListener(v -> {
            mCodeScanner.startPreview();
            startActivity(new Intent(this, NewFormActivity.class));
            finish();
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            mCodeScanner.startPreview();
        }

    }

    private void getEquipment(String result) {
        String url = Constants.GET_EQUIPMENT + result;
        Log.d(TAG, "getEquipment: " + url);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "onCreate:  " + response.toString());
                    try {
                        JSONObject object = response.getJSONObject("result");
                        EquipmentModel equipment = new EquipmentModel(
                                object.getString("id"),
                                object.getString("locationId"),
                                object.getString("serialNumber"),
                                object.getString("description"),
                                object.getBoolean("isActive")
                        );
                        Stash.put(Constants.PASS_EQUIPMENT, equipment);
                        startActivity(new Intent(this, NewFormActivity.class));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, "ERROR : " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("Accept", "text/plain");
                String token = "Bearer " + Stash.getString(Constants.ACCESS_TOKEN);
                map.put("Authorization", token);
                return map;
            }
        };
        requestQueue.add(objectRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCodeScanner.startPreview();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}
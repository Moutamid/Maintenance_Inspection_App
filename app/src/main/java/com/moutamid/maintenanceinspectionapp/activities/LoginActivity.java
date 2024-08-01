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
import com.moutamid.maintenanceinspectionapp.adapters.SitesAdapter;
import com.moutamid.maintenanceinspectionapp.models.SitesModel;
import com.moutamid.maintenanceinspectionapp.models.TenantModel;
import com.moutamid.maintenanceinspectionapp.models.UserModel;
import com.moutamid.maintenanceinspectionapp.utilis.Constants;
import com.moutamid.maintenanceinspectionapp.MainActivity;
import com.moutamid.maintenanceinspectionapp.databinding.ActivityLoginBinding;
import com.moutamid.maintenanceinspectionapp.utilis.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 1001;
    private CodeScanner mCodeScanner;
    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        CodeScannerView scannerView = binding.scannerView;
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {

        }));
        scannerView.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            mCodeScanner.startPreview();
        }

        binding.login.setOnClickListener(v -> {
            if (valid()) {
                progressDialog.show();
                authenticateUser();
            }
        });
    }

    private void authenticateUser() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("password", binding.password.getEditText().getText().toString());
            requestBody.put("userNameOrEmailAddress", binding.username.getEditText().getText().toString());
            requestBody.put("rememberClient", binding.remember.isChecked());
            requestBody.put("tenancyName", binding.tenancy.getEditText().getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Constants.Authenticate, requestBody,
                response -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "onCreate:  " + response.toString());
                    try {
                        JSONObject result = response.getJSONObject("result");
                        String accessToken = result.getString("accessToken");
                        int userId = result.getInt("userId");
                        UserModel userModel = new UserModel(userId, requestBody.getString("userNameOrEmailAddress"),
                                requestBody.getString("password"), requestBody.getString("tenancyName"), requestBody.getBoolean("rememberClient"));
                        Stash.put(Constants.STASH_USER, userModel);
                        Stash.put(Constants.ACCESS_TOKEN, accessToken);
                        Stash.put(Constants.REMEBER_ME, binding.remember.isChecked());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, String.valueOf(error.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("Accept", "text/plain");
            //    map.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjIiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiYWRtaW4iLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiOiJkZW1vQGx1bmFyc29mdC5jby56YSIsIkFzcE5ldC5JZGVudGl0eS5TZWN1cml0eVN0YW1wIjoiNUgzWFQ0UFdaUENZRlc1VFJaN1NNUjZNUDZBRzRJSlYiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJBZG1pbiIsImh0dHA6Ly93d3cuYXNwbmV0Ym9pbGVycGxhdGUuY29tL2lkZW50aXR5L2NsYWltcy90ZW5hbnRJZCI6IjEiLCJzdWIiOiIyIiwianRpIjoiNzA1MTE5NTQtZWE2YS00YzljLWJiZWEtY2E0M2YwZWZmN2I1IiwiaWF0IjoxNzIyNDI1MDczLCJuYmYiOjE3MjI0MjUwNzMsImV4cCI6MTcyMjUxMTQ3MywiaXNzIjoiSW5zcGVjdFgiLCJhdWQiOiJJbnNwZWN0WCJ9.E79Yj74PQOj9QCgM-RjXuUU0JvbwVifUHAKoykvmtZ8");
                return map;
            }
        };
        requestQueue.add(objectRequest);
    }

    private boolean valid() {
        if (binding.tenancy.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Tenancy is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.username.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
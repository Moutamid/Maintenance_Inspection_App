package com.moutamid.maintenanceinspectionapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fxn.stash.Stash;
import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.adapters.LocationsAdapter;
import com.moutamid.maintenanceinspectionapp.databinding.ActivityAreasBinding;
import com.moutamid.maintenanceinspectionapp.models.LocationModel;
import com.moutamid.maintenanceinspectionapp.models.SitesModel;
import com.moutamid.maintenanceinspectionapp.models.TenantModel;
import com.moutamid.maintenanceinspectionapp.utilis.Constants;
import com.moutamid.maintenanceinspectionapp.utilis.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AreasActivity extends AppCompatActivity {
    private static final String TAG = "AreasActivity";
    ActivityAreasBinding binding;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAreasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));

        String siteID = getIntent().getStringExtra(Constants.siteID);

        binding.back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        getSites(siteID);
    }

    private void getSites(String siteID) {
        Log.d(TAG, "getSites: " + siteID);
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("siteId", siteID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Constants.GET_ALL_LOCATIONS, requestBody,
                response -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "Locations:  " + response.toString());
                    try {
                        JSONArray items = response.getJSONObject("result").getJSONArray("items");
                        ArrayList<LocationModel> list = new ArrayList<>();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject object = items.getJSONObject(i);
                            LocationModel model = new LocationModel(
                                    object.getString("description"),
                                    object.getString("id"),
                                    object.getString("siteId")
                            );
                            list.sort(Comparator.comparing(sitesModel -> sitesModel.description));
                            list.add(model);
                        }
                        LocationsAdapter adapter = new LocationsAdapter(this, list);
                        binding.locationRc.setAdapter(adapter);
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
                String token = "Bearer " + Stash.getString(Constants.ACCESS_TOKEN);
                map.put("Authorization", token);
                return map;
            }
        };
        requestQueue.add(objectRequest);
    }

}
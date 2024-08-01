package com.moutamid.maintenanceinspectionapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fxn.stash.Stash;
import com.moutamid.maintenanceinspectionapp.adapters.SitesAdapter;
import com.moutamid.maintenanceinspectionapp.databinding.ActivityMainBinding;
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

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("maxResultCount", 2147483647);
            requestBody.put("skipCount", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.sitesRc.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        binding.sitesRc.setHasFixedSize(false);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Constants.GET_ALL_SITES, requestBody,
                response -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "onCreate:  " + response.toString());
                    try {
                        JSONArray items = response.getJSONObject("result").getJSONArray("items");
                        ArrayList<SitesModel> list = new ArrayList<>();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject object = items.getJSONObject(i);
                            SitesModel model = new SitesModel(
                                    object.getString("siteName"),
                                    new TenantModel(
                                        object.getJSONObject("tenant").getString("tenancyName"),
                                        object.getJSONObject("tenant").getString("name"),
                                        object.getJSONObject("tenant").getBoolean("isActive"),
                                        object.getJSONObject("tenant").getInt("id")
                                    ),
                                    object.getInt("tenantId"),
                                    object.getString("id")
                            );
                            list.sort(Comparator.comparing(sitesModel -> sitesModel.siteName));
                            list.add(model);
                        }
                        SitesAdapter adapter = new SitesAdapter(this, list);
                        binding.sitesRc.setAdapter(adapter);
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
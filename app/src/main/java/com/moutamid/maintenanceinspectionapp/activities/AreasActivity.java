package com.moutamid.maintenanceinspectionapp.activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.adapters.LocationsAdapter;
import com.moutamid.maintenanceinspectionapp.adapters.SitesAdapter;
import com.moutamid.maintenanceinspectionapp.databinding.ActivityAreasBinding;
import com.moutamid.maintenanceinspectionapp.models.SitesModel;
import com.moutamid.maintenanceinspectionapp.models.TenantModel;
import com.moutamid.maintenanceinspectionapp.utilis.Constants;

import java.util.ArrayList;

public class AreasActivity extends AppCompatActivity {
    ActivityAreasBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAreasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));

        String siteID = getIntent().getStringExtra(Constants.siteID);

        binding.back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        ArrayList<SitesModel> list = getSites();

        LocationsAdapter adapter = new LocationsAdapter(this, list);
        binding.locationRc.setAdapter(adapter);

    }

    private ArrayList<SitesModel> getSites() {
        ArrayList<SitesModel> list = new ArrayList<>();
        list.add(new SitesModel("Location A1", new TenantModel("demo", "demo", true,1), 1, "2011311a-d6c6-4ec7-8b92-d9ea53e0c517"));
        list.add(new SitesModel("Location A2", new TenantModel("demo", "demo", true,1), 1, "1dbc05fe-3951-4eef-8fd3-ad1e8aaafc1c"));
        list.add(new SitesModel("Location A3", new TenantModel("demo", "demo", true,1), 1, "ae2e1bd9-d0b5-4131-a48b-2791d60b00d3"));
        return list;
    }

}
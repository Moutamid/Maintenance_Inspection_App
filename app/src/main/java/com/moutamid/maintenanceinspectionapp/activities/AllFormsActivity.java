package com.moutamid.maintenanceinspectionapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fxn.stash.Stash;
import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.adapters.FormsAdapter;
import com.moutamid.maintenanceinspectionapp.databinding.ActivityAllFormsBinding;
import com.moutamid.maintenanceinspectionapp.models.InspectionModel;
import com.moutamid.maintenanceinspectionapp.utilis.Constants;

import java.util.ArrayList;
import java.util.UUID;

public class AllFormsActivity extends AppCompatActivity {
    ActivityAllFormsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllFormsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));

        binding.back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        binding.newForm.setOnClickListener(v -> {
            startActivity(new Intent(this, EquipmentActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<InspectionModel> recentList = Stash.getArrayList(Constants.RECENT_FORM, InspectionModel.class);

        binding.formRC.setHasFixedSize(false);
        binding.formRC.setLayoutManager(new LinearLayoutManager(this));

        if (recentList.isEmpty()) {
            binding.formRC.setVisibility(View.GONE);
            binding.nothingLayout.setVisibility(View.VISIBLE);
        } else {
            binding.formRC.setVisibility(View.VISIBLE);
            binding.nothingLayout.setVisibility(View.GONE);
        }

        FormsAdapter adapter = new FormsAdapter(this, recentList);
        binding.formRC.setAdapter(adapter);
    }
}
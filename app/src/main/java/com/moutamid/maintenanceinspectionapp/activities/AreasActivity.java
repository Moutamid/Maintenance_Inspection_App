package com.moutamid.maintenanceinspectionapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.databinding.ActivityAreasBinding;

public class AreasActivity extends AppCompatActivity {
    ActivityAreasBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAreasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
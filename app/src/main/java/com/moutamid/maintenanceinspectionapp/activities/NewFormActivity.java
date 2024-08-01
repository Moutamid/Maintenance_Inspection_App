package com.moutamid.maintenanceinspectionapp.activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.adapters.InspectionQuestionsAdapter;
import com.moutamid.maintenanceinspectionapp.databinding.ActivityNewFormBinding;
import com.moutamid.maintenanceinspectionapp.models.InspectionQuestions;

import java.util.ArrayList;

public class NewFormActivity extends AppCompatActivity {
    ActivityNewFormBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));

        binding.back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        ArrayList<InspectionQuestions> list = getList();
        binding.questionRC.setLayoutManager(new LinearLayoutManager(this));
        binding.questionRC.setHasFixedSize(false);


        InspectionQuestionsAdapter adapter = new InspectionQuestionsAdapter(this, list);
        binding.questionRC.setAdapter(adapter);
    }

    private ArrayList<InspectionQuestions> getList() {
        ArrayList<InspectionQuestions> list = new ArrayList<>();
        list.add(new InspectionQuestions("Is the equipment free from any visible damage or corrosion?"));
        list.add(new InspectionQuestions("Are all safety guards and covers securely in place?"));
        list.add(new InspectionQuestions("Has the equipment been properly maintained according to the manufacturer's schedule?"));
        list.add(new InspectionQuestions("Is the equipment's electrical wiring intact and free from wear?"));
        list.add(new InspectionQuestions("Are all warning labels and instructions clearly visible and legible?"));
        return list;
    }
}
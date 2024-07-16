package com.moutamid.maintenanceinspectionapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.moutamid.maintenanceinspectionapp.MainActivity;
import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.adapters.SliderAdapter;
import com.moutamid.maintenanceinspectionapp.databinding.ActivityWelcomeBinding;
import com.moutamid.maintenanceinspectionapp.models.SliderModal;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelcomeBinding binding;
    SliderAdapter adapter;
    Button skip;
    int size;
    private ViewPager viewPager;
    private LinearLayout dotsLL;
    private ArrayList<SliderModal> sliderModalArrayList;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        skip = findViewById(R.id.idBtnSkip);

        skip.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        viewPager = findViewById(R.id.idViewPager);
        dotsLL = findViewById(R.id.idLLDots);

        sliderModalArrayList = new ArrayList<>();

        sliderModalArrayList.add(new SliderModal(getString(R.string.streamlined_inspections), getString(R.string.desc_1), R.drawable.streamlined_inspections));
        sliderModalArrayList.add(new SliderModal(getString(R.string.comprehensive_record_keeping), getString(R.string.desc_2), R.drawable.photocopy));
        sliderModalArrayList.add(new SliderModal(getString(R.string.instant_reporting), getString(R.string.desc_3), R.drawable.instant_reporting));

        adapter = new SliderAdapter(WelcomeActivity.this, sliderModalArrayList);

        viewPager.setAdapter(adapter);
        size = sliderModalArrayList.size();
        addDots(size, 0);
        viewPager.addOnPageChangeListener(viewListener);
    }

    private void addDots(int size, int pos) {
        dots = new TextView[size];
        dotsLL.removeAllViews();
        for (int i = 0; i < size; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(".");
            dots[i].setTextSize(60);
            dots[i].setTextColor(getResources().getColor(R.color.black));
            dotsLL.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[pos].setTextColor(getResources().getColor(R.color.blueDark));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(size, position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
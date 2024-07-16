package com.moutamid.maintenanceinspectionapp.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;
import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.models.SliderModal;

import java.util.ArrayList;
public class SliderAdapter extends PagerAdapter {
    LayoutInflater layoutInflater;
    Context context;
    ArrayList<SliderModal> sliderModalArrayList;
    public SliderAdapter(Context context, ArrayList<SliderModal> sliderModalArrayList) {
        this.context = context;
        this.sliderModalArrayList = sliderModalArrayList;
    }

    @Override
    public int getCount() {
        return sliderModalArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView imageView = view.findViewById(R.id.idIV);
        TextView titleTV = view.findViewById(R.id.idTVtitle);
        TextView headingTV = view.findViewById(R.id.idTVheading);

        SliderModal modal = sliderModalArrayList.get(position);
        titleTV.setText(modal.getTitle());
        headingTV.setText(modal.getHeading());
        Glide.with(context).load(modal.getImgUrl()).into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // this is a destroy view method
        // which is use to remove a view.
        container.removeView((RelativeLayout) object);
    }
}

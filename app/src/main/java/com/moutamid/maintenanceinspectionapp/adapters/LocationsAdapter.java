package com.moutamid.maintenanceinspectionapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.activities.AllFormsActivity;
import com.moutamid.maintenanceinspectionapp.activities.AreasActivity;
import com.moutamid.maintenanceinspectionapp.activities.EquipmentActivity;
import com.moutamid.maintenanceinspectionapp.models.LocationModel;
import com.moutamid.maintenanceinspectionapp.models.SitesModel;

import java.util.ArrayList;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.SitesVH> {
    Context context;
    ArrayList<LocationModel> list;

    public LocationsAdapter(Context context, ArrayList<LocationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SitesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SitesVH(LayoutInflater.from(context).inflate(R.layout.sites_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SitesVH holder, int position) {
        LocationModel model = list.get(holder.getAdapterPosition());
        holder.name.setText(model.description);
        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, AllFormsActivity.class).putExtra("ID", model.id));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SitesVH extends RecyclerView.ViewHolder{
        TextView name;
        public SitesVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

}

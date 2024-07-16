package com.moutamid.maintenanceinspectionapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.maintenanceinspectionapp.R;

import java.util.ArrayList;

public class SitesAdapter extends RecyclerView.Adapter<SitesAdapter.SitesVH> {

    Context context;

    public SitesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SitesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SitesVH(LayoutInflater.from(context).inflate(R.layout.sites_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SitesVH holder, int position) {
        holder.name.setText("Site " + (holder.getAdapterPosition() + 1));

        holder.itemView.setOnClickListener(v -> {

        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class SitesVH extends RecyclerView.ViewHolder{
        TextView name;
        public SitesVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

}

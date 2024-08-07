package com.moutamid.maintenanceinspectionapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.activities.NewFormActivity;
import com.moutamid.maintenanceinspectionapp.models.InspectionModel;
import com.moutamid.maintenanceinspectionapp.utilis.Constants;

import java.util.ArrayList;

public class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.FormsVH> {

    Context context;
    ArrayList<InspectionModel> list;

    public FormsAdapter(Context context, ArrayList<InspectionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FormsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FormsVH(LayoutInflater.from(context).inflate(R.layout.forms, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FormsVH holder, int position) {
        InspectionModel model = list.get(holder.getAdapterPosition());

        holder.serialNumber.setText(model.equipmentModel.serialNumber);
        holder.equipment.setText(model.equipmentModel.description);
        holder.count.setText("Total Qs #" + model.questions.size());
        holder.grade.setText(model.inspectionGrade);

        holder.continueBtn.setOnClickListener(v -> {
            Stash.put(Constants.PASS_EQUIPMENT, model.equipmentModel);
            Stash.put(Constants.PASS_INSPECTION, model);
            context.startActivity(new Intent(context, NewFormActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FormsVH extends RecyclerView.ViewHolder{

        TextView equipment, count,serialNumber,grade;
        Button continueBtn;
        public FormsVH(@NonNull View itemView) {
            super(itemView);
            equipment = itemView.findViewById(R.id.equipment);
            count = itemView.findViewById(R.id.count);
            serialNumber = itemView.findViewById(R.id.serialNumber);
            grade = itemView.findViewById(R.id.grade);
            continueBtn = itemView.findViewById(R.id.continueBtn);
        }
    }

}

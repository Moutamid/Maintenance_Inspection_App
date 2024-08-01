package com.moutamid.maintenanceinspectionapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.models.InspectionQuestions;

import java.util.ArrayList;

public class InspectionQuestionsAdapter extends RecyclerView.Adapter<InspectionQuestionsAdapter.QuestionVH> {
    Context context;
    ArrayList<InspectionQuestions> list;

    public InspectionQuestionsAdapter(Context context, ArrayList<InspectionQuestions> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public QuestionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionVH(LayoutInflater.from(context).inflate(R.layout.forms, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionVH holder, int position) {
        InspectionQuestions model = list.get(holder.getAdapterPosition());
        String question = "Q" + (holder.getAdapterPosition() + 1) + "): " + model.questionText;
        holder.question.setText(question);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class QuestionVH extends RecyclerView.ViewHolder {
        TextView question;
        TextInputLayout comment;
        MaterialRadioButton yes, no;
        public QuestionVH(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            question = itemView.findViewById(R.id.question);
            yes = itemView.findViewById(R.id.yes);
            no = itemView.findViewById(R.id.no);
        }
    }

}

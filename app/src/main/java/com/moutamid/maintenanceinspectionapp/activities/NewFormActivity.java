package com.moutamid.maintenanceinspectionapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fxn.stash.Stash;
import com.moutamid.maintenanceinspectionapp.R;
import com.moutamid.maintenanceinspectionapp.databinding.ActivityNewFormBinding;
import com.moutamid.maintenanceinspectionapp.models.EquipmentModel;
import com.moutamid.maintenanceinspectionapp.models.InspectionModel;
import com.moutamid.maintenanceinspectionapp.models.InspectionQuestions;
import com.moutamid.maintenanceinspectionapp.models.UserModel;
import com.moutamid.maintenanceinspectionapp.utilis.Constants;
import com.moutamid.maintenanceinspectionapp.utilis.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NewFormActivity extends AppCompatActivity {
    private static final String TAG = "NewFormActivity";
    ActivityNewFormBinding binding;
    EquipmentModel equipmentModel;
    String grade = "";
    int current = 0;
    InspectionModel inspectionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));

        equipmentModel = (EquipmentModel) Stash.getObject(Constants.PASS_EQUIPMENT, EquipmentModel.class);
        inspectionModel = (InspectionModel) Stash.getObject(Constants.PASS_INSPECTION, InspectionModel.class);

        if (inspectionModel != null)
            grade = inspectionModel.inspectionGrade;

        binding.back.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        if (grade.isEmpty()) {
            showGradeDialog();
        } else {
            binding.grade.setText("Inspection Grade: " + grade);
            getQuestions();
        }

        // binding.grade.setOnClickListener(v -> showGradeDialog());

        binding.submit.setOnClickListener(v -> {
            if (current != inspectionModel.questions.size() - 1) {
                InspectionQuestions questions = inspectionModel.questions.get(current);
                questions.answer = binding.yes.isChecked();
                questions.comment = binding.comment.getEditText().getText().toString();
                inspectionModel.questions.set(current, questions);
                showQuestions(current + 1);
            } else {
                submitForm();
            }
        });

        binding.previous.setOnClickListener(v -> {
            showQuestions(current - 1);
        });

        binding.yes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (current != inspectionModel.questions.size() - 1) {
                String text = isChecked ? "Next" : "Skip";
                binding.submit.setText(text);
            }
        });

    }

    private void submitForm() {
        String url = Constants.SUBMIT_FORM + inspectionModel.id;
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("id", inspectionModel.id);
            JSONArray inspectionQuestions = new JSONArray();
            for (InspectionQuestions questions : inspectionModel.questions) {
                JSONObject object = new JSONObject();
                object.put("id", questions.id);
                object.put("answer", questions.answer);
                object.put("answeredById", questions.answeredById);
                object.put("answerDate", questions.answerDate);
                object.put("comment", questions.comment);
                object.put("group", questions.group);
                object.put("inspectionFormId", questions.inspectionFormId);
                object.put("inspectionGrade", questions.inspectionGrade);
                object.put("ordinal", questions.ordinal);
                object.put("questionText", questions.questionText);
                inspectionQuestions.put(object);
            }
            requestBody.put("inspectionQuestions", inspectionQuestions);

            Log.d(TAG, "submitForm: " + requestBody.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        onBackPressed();
    }

    private void showGradeDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_grade);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();

        Button next = dialog.findViewById(R.id.next);
        RadioGroup gradeGroup = dialog.findViewById(R.id.grade);

        next.setOnClickListener(v -> {
            int checkedRadioButtonId = gradeGroup.getCheckedRadioButtonId();
            if (checkedRadioButtonId != -1) {
                RadioButton radioButton = dialog.findViewById(checkedRadioButtonId);
                if (radioButton != null) {
                    grade = radioButton.getText().toString();
                    Log.d(TAG, "showGradeDialog: " + grade);
                    dialog.dismiss();
                    binding.grade.setText("Inspection Grade: " + grade);
                    getQuestions();
//                    showQuestions(0);
                }
            } else {
                Log.d("Selected Radio Button", "No selection made");
                Toast.makeText(this, "Please Select Inspection Grade", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getQuestions() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("inspectionGrade", grade.toLowerCase());
            requestBody.put("serialNumber", equipmentModel.serialNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Constants.NEW_INSPECTION, requestBody,
                response -> {
                    progressDialog.dismiss();
                    Log.d(TAG, "Form:  " + response.toString());
                    try {
                        JSONObject result = response.getJSONObject("result");
                        JSONArray inspectionQuestions = result.getJSONArray("inspectionQuestions");
                        String id = result.getString("id");
                        ArrayList<InspectionQuestions> list = new ArrayList<>();
                        for (int i = 0; i < inspectionQuestions.length(); i++) {
                            JSONObject object = inspectionQuestions.getJSONObject(i);
                            InspectionQuestions questions = new InspectionQuestions();
                            questions.answer = false;
                            questions.answerDate = null;
                            questions.answeredById = userModel.userId;
                            questions.comment = "";
                            questions.group = object.getString("group");
                            questions.inspectionFormId = id;
                            questions.inspectionGrade = object.getString("inspectionGrade");
                            questions.ordinal = object.getInt("ordinal");
                            questions.questionText = object.getString("questionText");
                            questions.id = object.getString("id");
                            list.add(questions);
                        }
                        list.sort(Comparator.comparingInt(value -> value.ordinal));
                        inspectionModel = new InspectionModel();
                        inspectionModel.id = id;
                        inspectionModel.equipmentModel = equipmentModel;
                        inspectionModel.inspectionGrade = grade;
                        inspectionModel.questions = list;

                        ArrayList<InspectionModel> recentList = Stash.getArrayList(Constants.RECENT_FORM, InspectionModel.class);
                        boolean exists = false;
                        for (int i = 0; i < recentList.size(); i++) {
                            if (Objects.equals(recentList.get(i).id, inspectionModel.id)) {
                                recentList.set(i, inspectionModel);
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            recentList.add(inspectionModel);
                        }
                        Stash.put(Constants.RECENT_FORM, recentList);
                        showQuestions(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
            progressDialog.dismiss();
            Log.d(TAG, "getQuestions: " + error.getLocalizedMessage());
            Log.d(TAG, "getQuestions: " + error.networkResponse.statusCode);
            Log.d(TAG, "getQuestions: " + error.networkResponse);
// TODO           Toast.makeText(this,  "Error : " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            onBackPressed();

            getDummy();

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("Accept", "text/plain");
                String token = "Bearer " + Stash.getString(Constants.ACCESS_TOKEN);
                map.put("Authorization", token);
                return map;
            }
        };
        requestQueue.add(objectRequest);
    }

    private void getDummy() {
        if (inspectionModel == null) {
            String[] questions_dummy = new String[]{
                    "Is the equipment free from any visible damage or corrosion?",
                    "Are all safety guards and covers securely in place?",
                    "Has the equipment been properly maintained according to the manufacturer's schedule?",
                    "Is the equipment's electrical wiring intact and free from wear?",
                    "Are all warning labels and instructions clearly visible and legible?"
            };
            ArrayList<InspectionQuestions> list = new ArrayList<>();
            for (int i = 0; i < questions_dummy.length; i++) {
                InspectionQuestions questions = new InspectionQuestions();
                questions.answer = false;
                questions.answerDate = null;
                questions.answeredById = 1;
                questions.comment = "";
                questions.group = "Dummy Group";
                questions.inspectionFormId = UUID.randomUUID().toString();
                questions.inspectionGrade = grade;
                questions.ordinal = i;
                questions.questionText = questions_dummy[i];
                questions.id = UUID.randomUUID().toString();
                list.add(questions);
            }
            list.sort(Comparator.comparingInt(value -> value.ordinal));
            inspectionModel = new InspectionModel();
            inspectionModel.id = UUID.randomUUID().toString();
            inspectionModel.equipmentModel = equipmentModel;
            inspectionModel.inspectionGrade = grade;
            inspectionModel.questions = list;
        }
        ArrayList<InspectionModel> recentList = Stash.getArrayList(Constants.RECENT_FORM, InspectionModel.class);
        boolean exists = false;
        for (int i = 0; i < recentList.size(); i++) {
            if (Objects.equals(recentList.get(i).id, inspectionModel.id)) {
                recentList.set(i, inspectionModel);
                exists = true;
                break;
            }
        }
        if (!exists) {
            recentList.add(inspectionModel);
        }
        Stash.put(Constants.RECENT_FORM, recentList);
        showQuestions(0);
    }

    private void showQuestions(int pos) {
        if (pos < inspectionModel.questions.size()) {
            InspectionQuestions model = inspectionModel.questions.get(pos);
            current = pos;
            if (pos > 0) {
                binding.previous.setVisibility(View.VISIBLE);
            } else {
                binding.previous.setVisibility(View.GONE);
            }
            binding.question.setText(model.questionText);
            binding.comment.getEditText().setText(model.comment);

            binding.yes.setChecked(model.answer);
            binding.no.setChecked(!model.answer);

            String text = pos == inspectionModel.questions.size() - 1 ? "Submit" : (model.answer ? "Next" : "Skip");
            binding.submit.setText(text);
            binding.indicator.setMax(inspectionModel.questions.size());
            binding.indicator.setProgress(pos + 1, true);
            binding.indicatorText.setText((pos + 1) + "/" + inspectionModel.questions.size());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Stash.clear(Constants.PASS_INSPECTION);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
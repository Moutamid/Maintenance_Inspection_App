package com.moutamid.maintenanceinspectionapp.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InspectionModel {
    public String id;
    public ArrayList<InspectionQuestions> questions;

    public InspectionModel(String id, ArrayList<InspectionQuestions> questions) {
        this.id = id;
        this.questions = questions;
    }
}

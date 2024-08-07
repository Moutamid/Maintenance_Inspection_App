package com.moutamid.maintenanceinspectionapp.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InspectionModel {
    public String id;
    public String inspectionGrade;
    public EquipmentModel equipmentModel;
    public ArrayList<InspectionQuestions> questions;

    public InspectionModel() {
    }

}

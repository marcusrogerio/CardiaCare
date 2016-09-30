package ru.cardiacare.cardiacare;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class LaboratoryStudies extends AppCompatActivity {

    String organizationName = "Ivanov Corporation";
    Integer hemoglobin = 0;
    Float erythrocyte = new Float(0.0);
    Integer hematocrit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratory_studies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText etOrganizationName = (EditText) findViewById(R.id.etOrganizationName);
        etOrganizationName.setText(organizationName);
        EditText etHemoglobin = (EditText) findViewById(R.id.etHemoglobin);
        etHemoglobin.setText(hemoglobin.toString());
        EditText etErythrocyte = (EditText) findViewById(R.id.etErythrocyte);
        etErythrocyte.setText(erythrocyte.toString());
        EditText etHematocrit = (EditText) findViewById(R.id.etHematocrit);
        etHematocrit.setText(hematocrit.toString());
    }
}

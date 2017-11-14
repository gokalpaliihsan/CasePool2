package com.example.aliihsangokalp.casepool;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CaseListActivity extends AppCompatActivity {


    FloatingActionButton newcase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_list);

        newcase = findViewById(R.id.newcase);



    }



  public void newCase (View view){

        Intent intent = new Intent(getApplicationContext(),UploadCaseActivity.class);
        startActivity(intent);

  }
}

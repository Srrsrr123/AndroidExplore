package com.example.asd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InsertRowActivity extends AppCompatActivity {

     TextView Cash,Date;
     Button insertRowFrom;
    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener dates = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

            myCalendar.set(Calendar.YEAR,year);
            myCalendar.set(Calendar.MONTH,monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            updateLabel();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_row);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        insertRowFrom = (Button) findViewById(R.id.insertRowFrom);
        Cash = (TextView) findViewById(R.id.Cash);
        Date = (TextView) findViewById(R.id.Date);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Inser Budget");
        }
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(InsertRowActivity.this,dates,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get((Calendar.DAY_OF_MONTH))).show();
            }
        });
    }

    public void insertRow(View view) {

        TextView Cash = findViewById(R.id.Cash);
        TextView Date = findViewById(R.id.Date);

        if(Cash.getText().toString().trim().equals("") || Date.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "Details Empty", Toast.LENGTH_SHORT).show();
        }else{
            UsersDatabaseAdapter.insertEntry(Cash.getText().toString().trim(),Date.getText().toString());
            Intent myIntent = new Intent(InsertRowActivity.this, MainActivity.class);
            InsertRowActivity.this.startActivity(myIntent);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateLabel(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        Date.setText(sdf.format(myCalendar.getTime()));

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    }
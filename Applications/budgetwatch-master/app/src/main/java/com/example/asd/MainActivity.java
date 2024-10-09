package com.example.asd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    UsersDatabaseAdapter usersDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UsersDatabaseAdapter usersDatabaseAdapter = new UsersDatabaseAdapter(getApplicationContext());
    }

    public void insertRowActivity(View view) {
        Intent myIntent = new Intent(MainActivity.this, InsertRowActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void deleteRowActivity(View view) {
        Intent myIntent = new Intent(MainActivity.this, deleteRowActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

}


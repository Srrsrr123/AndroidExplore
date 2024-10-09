package com.example.asd;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.ims.ImsMmTelManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.TextViewCompat;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CustomListAdapterDeleteRows extends BaseAdapter {
    Context c;
    ArrayList<UserModel> users;

    public CustomListAdapterDeleteRows(Context c,ArrayList<UserModel> users){
        this.c = c;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(c).inflate(R.layout.listviewdelete_row,viewGroup,false);
        }
        final TextView mtext = (TextView)view.findViewById(R.id.textView1);
        final TextView mtext2 = (TextView)view.findViewById(R.id.textView2);
        Button deleteBtn = (Button) view.findViewById(R.id.button1);
        Button updateBtn = (Button)view.findViewById(R.id.button2);

        final UserModel user = (UserModel)this.getItem(i);
        mtext.setText(user.getCash());
        mtext2.setText(user.getDate());


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsersDatabaseAdapter.deleteEntry(user.getID());
                users.remove(i);
                notifyDataSetChanged();
            }

        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li =  LayoutInflater.from(c);
                final View prompts = li.inflate(R.layout.prompt,null);
                final AlertDialog.Builder abr = new AlertDialog.Builder(c);
                abr.setView(prompts);
                final EditText Cash = (EditText) prompts.findViewById(R.id.txtcash);
                final TextView Date = (TextView) prompts.findViewById(R.id.txtdate);

                final Calendar myCalendar = Calendar.getInstance();

                final DatePickerDialog.OnDateSetListener dates = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        myCalendar.set(Calendar.YEAR,year);
                        myCalendar.set(Calendar.MONTH,monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        String myFormat = "MM/dd/yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                        Date.setText(sdf.format(myCalendar.getTime()));


                    }
                };

                Date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(c, dates, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get((Calendar.DAY_OF_MONTH))).show();
                    }
                });


                abr.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        mtext.setText(Cash.getText());
                        mtext2.setText(Date.getText());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.cancel();
                    }
                });
                abr.show();

               UsersDatabaseAdapter.updateEntry(user.getID(),mtext.getText().toString(),mtext2.getText().toString());
               Toast.makeText(c,"Update Entry Successful",Toast.LENGTH_SHORT).show();
            //   Intent intent = new Intent(CustomListAdapterDeleteRows.this, MainActivity.class);
            }
        });


        return view;
    }


}

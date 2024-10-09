package com.example.asd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

public class UsersDatabaseAdapter {
    static ArrayList<UserModel> users = new ArrayList<>();
    static final String DATABASE_NAME = "BudgetDB.db";
    static final String TABLE_NAME = "USERS";
    static final int DATABASE_VERSION = 1;
    static final String KEY_ID = "id";
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table " + TABLE_NAME + "("+ KEY_ID + " integer primary key autoincrement,Cash integer, Date VARCHAR); ";
    private static final String TAG = "UsersDatabaseAdapter:";

    // Variable to hold the database instance
    public static SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private static DataBaseHelper dbHelper;

    public UsersDatabaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method to open the Database
    public UsersDatabaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Method to close the Database
    public void close() {
        db.close();
    }

    // method returns an Instance of the Database
    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    // method to insert a record in Table
    public static String insertEntry(String cash, String date) {

        try {


            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("cash", cash);
            newValues.put("date", date);

            // Insert the row into your table
            db = dbHelper.getWritableDatabase();
            long result = db.insert(TABLE_NAME, null, newValues);
            toast("User Info Saved! Total Row Count is " + getRowCount());
            db.close();

        } catch (Exception ex) {
        }
        return "ok";
    }

    private static void toast(String s) {
    }


    // method to get the password  of userName
    public static int getRowCount() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        toast("Row Count is " + cursor.getCount());
        db.close();
        return cursor.getCount();
    }

    public static ArrayList<UserModel> getRows() throws JSONException {

        users.clear();
        UserModel user;
        db = dbHelper.getReadableDatabase();
        Cursor projCursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        while (projCursor.moveToNext()) {

            user = new UserModel();
            user.setID(projCursor.getString(projCursor.getColumnIndex(KEY_ID)));
            user.setCash(projCursor.getString(projCursor.getColumnIndex("Cash")));
            user.setDate(projCursor.getString(projCursor.getColumnIndex("Date")));

            users.add(user);
        }
        projCursor.close();
        return users;
    }

    // method to delete a Record of UserName
    public static int deleteEntry(String ID) {
        String where = "ID=?";
        int numberOFEntriesDeleted = db.delete(TABLE_NAME, where, new String[]{ID});
        toast("Number fo Entry Deleted Successfully : " + numberOFEntriesDeleted);
        return numberOFEntriesDeleted;

    }

    public static int updateEntry(String id, String cash, String Date){
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("cash",cash);
        values.put("date", Date);

        return db.update(TABLE_NAME,values, KEY_ID+"= ?",new String[]{String.valueOf(id)});
    }





}

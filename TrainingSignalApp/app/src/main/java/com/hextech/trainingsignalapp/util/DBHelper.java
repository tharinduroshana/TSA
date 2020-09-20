package com.hextech.trainingsignalapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TSA_DB.db";
    public static final String TABLE_NAME = "TSA_DB";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ('symbol' VARCHAR(10) PRIMARY KEY)";
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static boolean insertEntry(Context context, String symbol) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("symbol", symbol);

        long newRowId = db.insert(TABLE_NAME, null, values);
        return newRowId != -1;
    }

    public static ArrayList<String> getAllRecords(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"symbol"};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);

        ArrayList<String> records = new ArrayList<>();

        while (cursor.moveToNext()) {
            String symbol = cursor.getString(cursor.getColumnIndex("symbol"));
            records.add(symbol);
        }

        return records;
    }

    public static void deleteRecord(Context context, String symbol) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = "symbol" + " LIKE " + symbol;
        String[] selectionArgs = null;
        int deletedRows = db.delete(TABLE_NAME, selection, selectionArgs);
        Log.i("SmartTime", "Record deleted!");
    }
}

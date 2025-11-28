package com.example.bmi_mvvm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BMIDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bmi_history.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_BMI_HISTORY = "bmi_history";
    public static final String COLUMN_DATE = "record_date";
    public static final String COLUMN_BMI = "bmi";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public BMIDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BMI_HISTORY + " (" +
                COLUMN_DATE + " TEXT PRIMARY KEY, " +
                COLUMN_BMI + " REAL NOT NULL, " +
                COLUMN_UPDATED_AT + " INTEGER NOT NULL" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BMI_HISTORY);
        onCreate(db);
    }
}

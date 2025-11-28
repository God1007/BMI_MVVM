package com.example.bmi_mvvm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BMIHistoryRepository {

    private final BMIDatabaseHelper dbHelper;

    public BMIHistoryRepository(Context context) {
        dbHelper = new BMIDatabaseHelper(context.getApplicationContext());
    }

    public void saveTodayResult(double bmiValue) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BMIDatabaseHelper.COLUMN_DATE, today);
        values.put(BMIDatabaseHelper.COLUMN_BMI, bmiValue);
        values.put(BMIDatabaseHelper.COLUMN_UPDATED_AT, System.currentTimeMillis());

        db.insertWithOnConflict(
                BMIDatabaseHelper.TABLE_BMI_HISTORY,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public List<BMIRecord> getAllRecords() {
        List<BMIRecord> records = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(
                BMIDatabaseHelper.TABLE_BMI_HISTORY,
                new String[]{BMIDatabaseHelper.COLUMN_DATE, BMIDatabaseHelper.COLUMN_BMI, BMIDatabaseHelper.COLUMN_UPDATED_AT},
                null,
                null,
                null,
                null,
                BMIDatabaseHelper.COLUMN_DATE + " ASC"
        )) {
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndexOrThrow(BMIDatabaseHelper.COLUMN_DATE));
                float bmi = cursor.getFloat(cursor.getColumnIndexOrThrow(BMIDatabaseHelper.COLUMN_BMI));
                long updatedAt = cursor.getLong(cursor.getColumnIndexOrThrow(BMIDatabaseHelper.COLUMN_UPDATED_AT));
                records.add(new BMIRecord(date, bmi, updatedAt));
            }
        }

        return records;
    }
}

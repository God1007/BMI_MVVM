package com.example.bmi_mvvm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelper 实现，专门管理 BMI 历史记录的数据库表结构。
 * 提供创建与升级逻辑，仓库层可以通过它获取可读写的数据库实例。
 */
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
        // 创建记录日期、BMI 值和时间戳的表，日期作为主键避免同一天重复插入。
        db.execSQL("CREATE TABLE " + TABLE_BMI_HISTORY + " (" +
                COLUMN_DATE + " TEXT PRIMARY KEY, " +
                COLUMN_BMI + " REAL NOT NULL, " +
                COLUMN_UPDATED_AT + " INTEGER NOT NULL" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 简单粗暴的升级策略：丢弃旧表并重新创建。
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BMI_HISTORY);
        onCreate(db);
    }
}

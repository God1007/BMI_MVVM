package com.example.bmi_mvvm; // 指定包名，定义仓库类的命名空间

import android.content.ContentValues; // 导入 ContentValues 以插入数据
import android.content.Context; // 导入 Context 访问数据库
import android.database.Cursor; // 导入 Cursor 读取查询结果
import android.database.sqlite.SQLiteDatabase; // 导入 SQLiteDatabase 操作数据库

import java.text.SimpleDateFormat; // 导入 SimpleDateFormat 格式化日期
import java.util.ArrayList; // 导入 ArrayList 用于存储记录
import java.util.Date; // 导入 Date 获取当前日期
import java.util.List; // 导入 List 接口
import java.util.Locale; // 导入 Locale 指定日期格式区域

/**
 * 封装历史记录的读写操作，负责将计算结果写入 SQLite 并读取列表。
 */
public class BMIHistoryRepository { // 定义仓库类

    private final BMIDatabaseHelper dbHelper; // 数据库助手用于获取可写数据库

    public BMIHistoryRepository(Context context) { // 构造函数接收 Context
        dbHelper = new BMIDatabaseHelper(context); // 初始化数据库助手
    } // 构造函数结束

    public void saveTodayResult(double bmi) { // 保存当天的 BMI 结果
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // 获取可写数据库
        ContentValues values = new ContentValues(); // 创建 ContentValues 存储字段
        values.put("date", getTodayDate()); // 写入当前日期
        values.put("bmi", bmi); // 写入 BMI 数值
        db.insertWithOnConflict("bmi_history", null, values, SQLiteDatabase.CONFLICT_REPLACE); // 如果同一天已有记录则覆盖
        db.close(); // 关闭数据库连接
    } // saveTodayResult 结束

    public List<BMIRecord> getHistory() { // 读取历史记录列表
        List<BMIRecord> records = new ArrayList<>(); // 创建列表存储结果
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // 获取可读数据库
        Cursor cursor = db.query("bmi_history", null, null, null, null, null, "id ASC"); // 按 id 升序查询所有记录
        while (cursor.moveToNext()) { // 遍历结果集
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date")); // 读取日期字段
            float bmi = cursor.getFloat(cursor.getColumnIndexOrThrow("bmi")); // 读取 BMI 字段
            records.add(new BMIRecord(date, bmi)); // 将记录加入列表
        } // 遍历结束
        cursor.close(); // 关闭游标
        db.close(); // 关闭数据库
        return records; // 返回历史列表
    } // getHistory 结束

    private String getTodayDate() { // 获取今日日期字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // 创建日期格式化器
        return sdf.format(new Date()); // 将当前日期格式化成字符串
    } // getTodayDate 结束
} // BMIHistoryRepository 类结束

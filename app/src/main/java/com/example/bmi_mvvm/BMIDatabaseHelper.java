package com.example.bmi_mvvm; // 指定包名，声明数据库助手所在命名空间

import android.content.Context; // 导入 Context 以便访问数据库
import android.database.sqlite.SQLiteDatabase; // 导入 SQLiteDatabase 操作数据库
import android.database.sqlite.SQLiteOpenHelper; // 导入 SQLiteOpenHelper 简化数据库创建与升级

/**
 * SQLiteOpenHelper 实现，创建用于存储历史 BMI 记录的表。
 */
public class BMIDatabaseHelper extends SQLiteOpenHelper { // 继承 SQLiteOpenHelper 实现数据库管理

    private static final String DATABASE_NAME = "bmi_history.db"; // 数据库文件名
    private static final int DATABASE_VERSION = 1; // 数据库版本号

    private static final String TABLE_CREATE = // 创建表的 SQL 语句
            "CREATE TABLE bmi_history (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "date TEXT NOT NULL, " +
            "bmi REAL NOT NULL" +
            ")"; // 建表语句结束

    public BMIDatabaseHelper(Context context) { // 构造函数接收 Context
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // 调用父类构造器设置数据库信息
    } // 构造函数结束

    @Override
    public void onCreate(SQLiteDatabase db) { // 首次创建数据库时回调
        db.execSQL(TABLE_CREATE); // 执行建表语句
    } // onCreate 结束

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // 数据库升级时回调
        // 当前版本简单删除旧表重新创建，实际项目可根据版本差异迁移
        db.execSQL("DROP TABLE IF EXISTS bmi_history"); // 删除旧表
        onCreate(db); // 重新创建新表
    } // onUpgrade 结束
} // BMIDatabaseHelper 类结束

package com.example.bmi_mvvm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class ReportActivity extends AppCompatActivity {

    private TextView result, category, details, advice;
    private ImageView image;
    private BMIReportViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // 绑定控件
        result = findViewById(R.id.report_result);
        category = findViewById(R.id.report_category);
        details = findViewById(R.id.report_details);
        advice = findViewById(R.id.report_advice);
        image = findViewById(R.id.report_image);
        Button openHistory = findViewById(R.id.open_history_button);

        // 初始化 ViewModel
        viewModel = new ViewModelProvider(this).get(BMIReportViewModel.class);

        // ✅ Step 1: 从 Intent 或 SharedPreferences 获取数据（防止第一次打开空数据）
        Intent intent = getIntent();
        SharedPreferences sp = getSharedPreferences(BMIModel.PREF_NAME, MODE_PRIVATE);

        // 如果 Intent 中某个值是 null，就从 SharedPreferences 兜底加载
        String bmi = getOrDefault(intent.getStringExtra("bmi"), sp.getString(BMIModel.KEY_BMI, ""));
        String bmiCategory = getOrDefault(intent.getStringExtra("bmi_category"), sp.getString(BMIModel.KEY_CATEGORY, ""));
        String height = getOrDefault(intent.getStringExtra("height"), sp.getString(BMIModel.KEY_HEIGHT, ""));
        String weight = getOrDefault(intent.getStringExtra("weight"), sp.getString(BMIModel.KEY_WEIGHT, ""));
        String age = getOrDefault(intent.getStringExtra("age"), sp.getString(BMIModel.KEY_AGE, ""));
        String gender = getOrDefault(intent.getStringExtra("gender"), sp.getString(BMIModel.KEY_GENDER, ""));

        // ✅ Step 2: 构造新的 Intent 给 ViewModel（确保字段齐全）
        Intent fixedIntent = new Intent();
        fixedIntent.putExtra("bmi", bmi);
        fixedIntent.putExtra("bmi_category", bmiCategory);
        fixedIntent.putExtra("height", height);
        fixedIntent.putExtra("weight", weight);
        fixedIntent.putExtra("age", age);
        fixedIntent.putExtra("gender", gender);

        // ✅ Step 3: 调用 ViewModel 加载数据
        viewModel.loadReportData(fixedIntent, this);

        // ✅ Step 4: 绑定 LiveData
        viewModel.getBmi().observe(this, value ->
                result.setText(getString(R.string.YourBMI) + " " + value)
        );

        viewModel.getCategory().observe(this, cat ->
                category.setText(getString(R.string.Category) + " " + cat)
        );

        viewModel.getDetails().observe(this, text ->
                details.setText(text)
        );

        viewModel.getAdvice().observe(this, text ->
                advice.setText(text)
        );

        viewModel.getImageRes().observe(this, resId ->
                image.setImageResource(resId)
        );

        viewModel.getError().observe(this, msg -> {
            result.setText(getString(R.string.error_occurred) + " " + msg);
            advice.setText("");
        });

        openHistory.setOnClickListener(v ->
                startActivity(new Intent(ReportActivity.this, HistoryActivity.class))
        );
    }

    /**
     * 辅助方法：如果 primary 为空则返回 fallback
     */
    private String getOrDefault(String primary, String fallback) {
        return (primary == null || primary.isEmpty()) ? fallback : primary;
    }
}

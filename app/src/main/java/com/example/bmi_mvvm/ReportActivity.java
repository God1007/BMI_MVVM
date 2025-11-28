package com.example.bmi_mvvm; // 指定包名，声明报告页面类的命名空间

import android.os.Bundle; // 导入 Bundle 处理状态
import android.content.Intent; // 导入 Intent 以便跳转到历史页面
import android.widget.Button; // 导入 Button 处理按钮点击
import android.widget.ImageView; // 导入 ImageView 显示图片
import android.widget.TextView; // 导入 TextView 显示文本

import androidx.appcompat.app.AppCompatActivity; // 导入 AppCompatActivity 作为活动基类
import androidx.lifecycle.ViewModelProvider; // 导入 ViewModelProvider 获取 ViewModel

/**
 * 展示 BMI 计算结果的 Activity，负责接收数据并显示。
 */
public class ReportActivity extends AppCompatActivity { // 定义报告页面类

    private TextView reportResult, reportCategory, reportDetails, reportAdvice; // 结果展示的文本控件
    private ImageView reportImage; // 根据分类显示图片
    private Button openHistoryButton; // 跳转历史记录的按钮
    private BMIReportViewModel viewModel; // 报告页面的 ViewModel

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 生命周期回调：页面创建
        super.onCreate(savedInstanceState); // 调用父类初始化
        setContentView(R.layout.activity_report); // 设置报告页面布局

        reportResult = findViewById(R.id.report_result); // 绑定 BMI 数值文本
        reportCategory = findViewById(R.id.report_category); // 绑定 BMI 分类文本
        reportDetails = findViewById(R.id.report_details); // 绑定详情文本
        reportAdvice = findViewById(R.id.report_advice); // 绑定建议文本
        reportImage = findViewById(R.id.report_image); // 绑定报告图片视图
        openHistoryButton = findViewById(R.id.open_history_button); // 绑定跳转历史按钮

        viewModel = new ViewModelProvider(this).get(BMIReportViewModel.class); // 获取 ViewModel 实例

        if (getIntent().hasExtra("bmi")) { // 如果 Intent 携带了实时计算结果
            BMIReportData data = new BMIReportData( // 构建报告数据对象
                    getIntent().getStringExtra("bmi"), // 读取 BMI 数值
                    getIntent().getStringExtra("bmi_category"), // 读取 BMI 分类
                    getIntent().getStringExtra("height"), // 读取身高
                    getIntent().getStringExtra("weight"), // 读取体重
                    getIntent().getStringExtra("age"), // 读取年龄
                    getIntent().getStringExtra("gender") // 读取性别
            ); // 构造函数结束
            viewModel.saveReport(data); // 保存并更新 LiveData
        } else { // 如果没有传入数据
            viewModel.loadReport(); // 加载上一次报告
        } // 条件判断结束

        viewModel.getReportData().observe(this, this::renderReport); // 观察报告数据变化并渲染界面

        openHistoryButton.setOnClickListener(v -> // 点击按钮时跳转到历史记录
                startActivity(new Intent(ReportActivity.this, HistoryActivity.class))
        );
    } // onCreate 结束

    private void renderReport(BMIReportData data) { // 根据报告数据更新 UI
        if (data == null) return; // 若无数据直接返回

        reportResult.setText(getString(R.string.bmi_result) + data.getBmiValue()); // 显示 BMI 数值
        reportCategory.setText(getString(R.string.Category) + " " + data.getBmiCategory()); // 显示 BMI 分类
        reportDetails.setText(getString(R.string.details_format, data.getHeight(), data.getWeight(), data.getAge(), data.getGender())); // 显示详细信息
        reportAdvice.setText(R.string.advice_message); // 显示通用建议

        String category = data.getBmiCategory(); // 取出分类字符串
        if (category.contains(getString(R.string.bmi_category_underweight))) { // 判断是否偏瘦
            reportImage.setImageResource(R.drawable.bot_thin); // 设置偏瘦图片
        } else if (category.contains(getString(R.string.bmi_category_overweight)) || // 判断是否超重或肥胖
                category.contains(getString(R.string.bmi_category_obese))) {
            reportImage.setImageResource(R.drawable.bot_fat); // 设置偏胖图片
        } else { // 其他情况视为正常
            reportImage.setImageResource(R.drawable.bot_fit); // 设置正常图片
        } // 分类判断结束
    } // renderReport 结束
} // ReportActivity 类结束

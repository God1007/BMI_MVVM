package com.example.bmi_mvvm; // 指定包名，确保类归属于应用命名空间

import android.os.Bundle; // 导入 Bundle 以处理状态恢复
import android.view.View; // 导入 View 以便控制可见性
import android.widget.TextView; // 导入 TextView 用于显示提示

import androidx.appcompat.app.AppCompatActivity; // 导入 AppCompatActivity 作为活动基类
import androidx.core.content.ContextCompat; // 导入 ContextCompat 以兼容方式获取颜色
import androidx.lifecycle.ViewModelProvider; // 导入 ViewModelProvider 获取 ViewModel

import com.github.mikephil.charting.charts.LineChart; // 导入 LineChart 控件类
import com.github.mikephil.charting.components.Description; // 导入 Description 以设置图表说明
import com.github.mikephil.charting.components.XAxis; // 导入 XAxis 处理 X 轴
import com.github.mikephil.charting.components.YAxis; // 导入 YAxis 处理 Y 轴
import com.github.mikephil.charting.data.Entry; // 导入 Entry 表示数据点
import com.github.mikephil.charting.data.LineData; // 导入 LineData 作为图表数据容器
import com.github.mikephil.charting.data.LineDataSet; // 导入 LineDataSet 表示数据集
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter; // 导入轴标签格式化器
import com.google.android.material.appbar.MaterialToolbar; // 导入 MaterialToolbar 处理顶部栏

import java.util.ArrayList; // 导入 ArrayList 存储数据点
import java.util.List; // 导入 List 接口

/**
 * 展示 BMI 历史趋势的 Activity，负责初始化图表并监听数据变化。
 */
public class HistoryActivity extends AppCompatActivity { // 定义 HistoryActivity 继承 AppCompatActivity

    private LineChart lineChart; // 折线图控件引用
    private TextView emptyView; // 无数据时的提示视图
    private HistoryViewModel viewModel; // 历史数据的 ViewModel

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 生命周期回调：页面创建
        super.onCreate(savedInstanceState); // 调用父类完成基础初始化
        setContentView(R.layout.activity_history); // 设置历史页面布局

        MaterialToolbar toolbar = findViewById(R.id.history_toolbar); // 绑定顶部工具栏
        toolbar.setNavigationOnClickListener(v -> finish()); // 点击返回箭头关闭页面

        lineChart = findViewById(R.id.bmi_line_chart); // 获取折线图控件
        emptyView = findViewById(R.id.history_empty_view); // 获取空视图提示控件

        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class); // 通过 ViewModelProvider 获取 ViewModel
        viewModel.getHistory().observe(this, this::renderHistory); // 观察历史数据变化并渲染

        configureChart(); // 配置折线图外观
    } // onCreate 结束

    private void configureChart() { // 配置图表样式的方法
        lineChart.getAxisRight().setEnabled(false); // 隐藏右侧 Y 轴
        YAxis leftAxis = lineChart.getAxisLeft(); // 获取左侧 Y 轴对象
        leftAxis.setAxisMinimum(0f); // 设置 Y 轴最小值为 0
        leftAxis.setGranularity(0.5f); // 设置 Y 轴刻度间隔

        XAxis xAxis = lineChart.getXAxis(); // 获取 X 轴对象
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 将 X 轴放到底部
        xAxis.setGranularity(1f); // 设置 X 轴刻度间隔为 1
        xAxis.setDrawGridLines(false); // 关闭垂直网格线

        Description description = new Description(); // 创建描述对象
        description.setText(getString(R.string.history_chart_description)); // 设置描述文本
        lineChart.setDescription(description); // 应用描述到图表
    } // configureChart 结束

    private void renderHistory(List<BMIRecord> records) { // 根据历史记录渲染图表
        if (records == null || records.isEmpty()) { // 如果没有数据
            lineChart.setVisibility(View.GONE); // 隐藏图表
            emptyView.setVisibility(View.VISIBLE); // 显示空状态提示
            return; // 直接返回不继续绘制
        } // 空数据判断结束

        List<Entry> entries = new ArrayList<>(); // 存放图表数据点的列表
        List<String> labels = new ArrayList<>(); // 存放横轴标签的列表
        for (int i = 0; i < records.size(); i++) { // 遍历历史记录
            BMIRecord record = records.get(i); // 取出当前记录
            entries.add(new Entry(i, record.getBmi())); // 添加数据点：索引作 X，BMI 作 Y
            labels.add(record.getDate()); // 保存日期标签
        } // for 循环结束

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.history_chart_label)); // 创建数据集并设置图例标题
        dataSet.setColor(ContextCompat.getColor(this, R.color.purple_700)); // 设置线条颜色
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.purple_700)); // 设置拐点颜色
        dataSet.setLineWidth(2f); // 设置线条宽度
        dataSet.setCircleRadius(4f); // 设置拐点半径
        dataSet.setValueTextSize(10f); // 设置数值文字大小
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 使用平滑曲线模式

        LineData lineData = new LineData(dataSet); // 使用数据集构建 LineData
        lineChart.setData(lineData); // 将数据设置到图表
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels)); // 设置横轴标签格式化器
        lineChart.invalidate(); // 刷新图表以显示最新数据

        lineChart.setVisibility(View.VISIBLE); // 显示图表
        emptyView.setVisibility(View.GONE); // 隐藏空提示
    } // renderHistory 结束
} // HistoryActivity 类结束

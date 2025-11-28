package com.example.bmi_mvvm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示 BMI 历史趋势的 Activity，负责初始化图表并监听数据变化。
 */
public class HistoryActivity extends AppCompatActivity {

    private LineChart lineChart;
    private TextView emptyView;
    private HistoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // 返回按钮直接关闭页面。
        MaterialToolbar toolbar = findViewById(R.id.history_toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        lineChart = findViewById(R.id.bmi_line_chart);
        emptyView = findViewById(R.id.history_empty_view);

        // 使用 ViewModel 获取历史数据并监听变化。
        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        viewModel.getHistory().observe(this, this::renderHistory);

        configureChart();
    }

    /**
     * 配置折线图的样式与坐标轴，隐藏右侧轴并设置刻度。
     */
    private void configureChart() {
        lineChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(0.5f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText(getString(R.string.history_chart_description));
        lineChart.setDescription(description);
    }

    /**
     * 根据历史数据生成图表 Entry；如果没有数据则显示空视图提示。
     */
    private void renderHistory(List<BMIRecord> records) {
        if (records == null || records.isEmpty()) {
            lineChart.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            return;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            BMIRecord record = records.get(i);
            entries.add(new Entry(i, record.getBmi()));
            labels.add(record.getDate());
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.history_chart_label));
        dataSet.setColor(ContextCompat.getColor(this, R.color.purple_700));
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.purple_700));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart.invalidate();

        lineChart.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }
}

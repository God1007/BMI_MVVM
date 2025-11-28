package com.example.bmi_mvvm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private EditText heightET, weightET, ageET;
    private RadioGroup genderRadioGroup;
    private Button reportBtn;
    private ImageView imageView;

    private BMIViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化控件
        heightET = findViewById(R.id.heightET);
        weightET = findViewById(R.id.weightET);
        ageET = findViewById(R.id.yearsold);
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        reportBtn = findViewById(R.id.reportBtn);
        imageView = findViewById(R.id.imageView3);

        // 注册 ImageView 的 Context Menu
        registerForContextMenu(imageView);

        // 初始化 ViewModel
        viewModel = new ViewModelProvider(this).get(BMIViewModel.class);

        // 加载保存数据
        String[] data = viewModel.loadSavedData();
        heightET.setText(data[0]);
        weightET.setText(data[1]);
        ageET.setText(data[2]);
        if (data[3].equals("男")) genderRadioGroup.check(R.id.male_radio);
        else genderRadioGroup.check(R.id.female_radio);

        // LiveData 观察
        viewModel.getBmi().observe(this, bmi -> {
            if (bmi != null) {
                String category = viewModel.getCategory().getValue();
                String height = viewModel.getHeight().getValue();
                String weight = viewModel.getWeight().getValue();
                String age = viewModel.getAge().getValue();
                String gender = viewModel.getGender().getValue();

                // ✅【新增】保存数据到 SharedPreferences，确保 ReportActivity 第一次也能拿到完整数据
                getSharedPreferences("bmi_data", MODE_PRIVATE)
                        .edit()
                        .putString("bmi", bmi)
                        .putString("bmi_category", category)
                        .putString("height", height)
                        .putString("weight", weight)
                        .putString("age", age)
                        .putString("gender", gender)
                        .commit(); // ⚠️ 使用 commit() 同步保存，防止第一次跳转丢失

                // ✅ 然后再跳转 ReportActivity
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                intent.putExtra("bmi", bmi);
                intent.putExtra("bmi_category", category);
                intent.putExtra("height", height);
                intent.putExtra("weight", weight);
                intent.putExtra("age", age);
                intent.putExtra("gender", gender);
                startActivity(intent);
            }
        });


        // 点击按钮计算 BMI
        reportBtn.setOnClickListener(v -> {
            String height = heightET.getText().toString();
            String weight = weightET.getText().toString();
            String age = ageET.getText().toString();
            int genderId = genderRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedGender = findViewById(genderId);
            String gender = selectedGender.getText().toString();

            viewModel.calculateBMI(height, weight, age, gender, this);
        });
    }

    // 顶部菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return handleMenuClick(item, this);
    }

    // ImageView Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return handleMenuClick(item, this);
    }

    // 菜单统一处理函数
    private boolean handleMenuClick(MenuItem item, Context context) {
        int id = item.getItemId();
        if (id == R.id.menu_bmi_wiki) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://zh.wikipedia.org/wiki/BMI"));
            context.startActivity(intent);
            return true;
        } else if (id == R.id.menu_exit) {
            finish();
            return true;
        }
        return false;
    }
}

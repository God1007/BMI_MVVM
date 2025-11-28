package com.example.bmi_mvvm; // 指定应用的包名，确保类在正确命名空间下

import android.content.Context; // 导入 Context 用于启动其他组件或获取资源
import android.content.Intent; // 导入 Intent 以便在活动间传递数据和跳转
import android.net.Uri; // 导入 Uri 以支持打开网页链接
import android.os.Bundle; // 导入 Bundle 处理活动状态保存与恢复
import android.text.Editable; // 导入 Editable 接口以监听文本变化
import android.text.TextWatcher; // 导入 TextWatcher 监听输入框的文本变更
import android.view.ContextMenu; // 导入 ContextMenu 以创建长按菜单
import android.view.Menu; // 导入 Menu 用于创建顶部菜单
import android.view.MenuItem; // 导入 MenuItem 处理菜单点击事件
import android.view.View; // 导入 View 作为通用的 UI 基类
import android.widget.Button; // 导入 Button 控件类
import android.widget.EditText; // 导入 EditText 输入框类
import android.widget.ImageView; // 导入 ImageView 用于显示图片
import android.widget.RadioButton; // 导入 RadioButton 控件类
import android.widget.RadioGroup; // 导入 RadioGroup 处理单选按钮组
import androidx.appcompat.app.AlertDialog; // 导入 AlertDialog 以展示说明弹窗
import androidx.appcompat.app.AppCompatActivity; // 导入 AppCompatActivity 作为活动基类
import androidx.lifecycle.ViewModelProvider; // 导入 ViewModelProvider 获取 ViewModel 实例

/**
 * 应用的主界面，负责收集用户输入并驱动 BMI 计算、结果展示和导航。
 */
public class MainActivity extends AppCompatActivity { // 定义 MainActivity 继承 AppCompatActivity

    private EditText heightET, weightET, ageET; // 声明身高、体重、年龄输入框
    private RadioGroup genderRadioGroup; // 声明性别单选按钮组
    private Button reportBtn; // 声明生成报告的按钮
    private Button bmiInfoButton; // 声明展示 BMI 说明的按钮
    private Button historyBtn; // 声明查看历史的按钮
    private ImageView imageView; // 声明用于触发上下文菜单的图片控件

    private BMIViewModel viewModel; // 声明 ViewModel，用于处理数据逻辑

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 生命周期方法，活动创建时调用
        super.onCreate(savedInstanceState); // 调用父类方法完成基础初始化
        setContentView(R.layout.activity_main); // 设置布局文件显示主界面

        heightET = findViewById(R.id.heightET); // 关联布局中的身高输入框
        weightET = findViewById(R.id.weightET); // 关联布局中的体重输入框
        ageET = findViewById(R.id.yearsold); // 关联布局中的年龄输入框
        genderRadioGroup = findViewById(R.id.gender_radio_group); // 关联布局中的性别单选组
        reportBtn = findViewById(R.id.reportBtn); // 关联生成报告按钮
        bmiInfoButton = findViewById(R.id.bmi_info_button); // 关联 BMI 说明按钮
        historyBtn = findViewById(R.id.historyBtn); // 关联历史记录按钮
        imageView = findViewById(R.id.imageView3); // 关联用于打开上下文菜单的图片

        registerForContextMenu(imageView); // 为图片注册上下文菜单，支持长按菜单

        viewModel = new ViewModelProvider(this).get(BMIViewModel.class); // 通过 ViewModelProvider 获取 ViewModel

        String[] data = viewModel.loadSavedData(); // 从模型加载上次保存的输入数据
        heightET.setText(data[0]); // 恢复身高输入框的值
        weightET.setText(data[1]); // 恢复体重输入框的值
        ageET.setText(data[2]); // 恢复年龄输入框的值
        if (data[3].equals(getString(R.string.male))) genderRadioGroup.check(R.id.male_radio); // 如果保存的是男性则选中男性按钮
        else genderRadioGroup.check(R.id.female_radio); // 否则选中女性按钮

        setupPersistenceListeners(); // 配置输入变化监听，实现实时保存

        viewModel.getBmi().observe(this, bmi -> { // 观察 BMI 数据变化，触发跳转到报告页
            if (bmi != null) { // 仅在 BMI 有值时才处理
                String category = viewModel.getCategory().getValue(); // 读取当前 BMI 分类
                String height = viewModel.getHeight().getValue(); // 读取身高数据
                String weight = viewModel.getWeight().getValue(); // 读取体重数据
                String age = viewModel.getAge().getValue(); // 读取年龄数据
                String gender = viewModel.getGender().getValue(); // 读取性别数据

                Intent intent = new Intent(MainActivity.this, ReportActivity.class); // 创建跳转到报告页的 Intent
                intent.putExtra("bmi", bmi); // 将 BMI 数值放入 Intent
                intent.putExtra("bmi_category", category); // 将 BMI 分类放入 Intent
                intent.putExtra("height", height); // 将身高放入 Intent
                intent.putExtra("weight", weight); // 将体重放入 Intent
                intent.putExtra("age", age); // 将年龄放入 Intent
                intent.putExtra("gender", gender); // 将性别放入 Intent
                startActivity(intent); // 启动报告页
            } // if 结束
        }); // 观察者结束


        reportBtn.setOnClickListener(v -> { // 点击“生成报告”按钮时的回调
            String height = heightET.getText().toString(); // 获取身高输入的字符串
            String weight = weightET.getText().toString(); // 获取体重输入的字符串
            String age = ageET.getText().toString(); // 获取年龄输入的字符串
            int genderId = genderRadioGroup.getCheckedRadioButtonId(); // 获取当前选中的性别按钮 id
            RadioButton selectedGender = findViewById(genderId); // 根据 id 找到具体的单选按钮
            String gender = selectedGender.getText().toString(); // 读取按钮上的文本作为性别

            viewModel.calculateBMI(height, weight, age, gender, this); // 调用 ViewModel 进行 BMI 计算与校验
        }); // 设置点击监听结束

        historyBtn.setOnClickListener(v -> // 点击历史按钮时的回调
                startActivity(new Intent(MainActivity.this, HistoryActivity.class)) // 打开历史记录页面
        ); // 监听设置结束

        bmiInfoButton.setOnClickListener(v -> // 点击 BMI 说明按钮时的回调
                new AlertDialog.Builder(MainActivity.this) // 创建 AlertDialog 构建器
                        .setTitle(R.string.what_is_bmi) // 设置标题为“什么是 BMI”
                        .setMessage(R.string.bmi_info_text) // 设置正文说明文字
                        .setPositiveButton(android.R.string.ok, null) // 设置“确定”按钮，不需要额外回调
                        .show() // 显示弹窗
        ); // 监听设置结束
    } // onCreate 结束

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 创建顶部菜单时调用
        getMenuInflater().inflate(R.menu.main_menu, menu); // 将菜单布局填充到菜单对象
        return true; // 返回 true 表示菜单已创建
    } // onCreateOptionsMenu 结束

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 顶部菜单项被点击时调用
        return handleMenuClick(item, this); // 将处理逻辑统一交给 handleMenuClick
    } // onOptionsItemSelected 结束

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) { // 创建上下文菜单时调用
        super.onCreateContextMenu(menu, v, menuInfo); // 先调用父类实现
        getMenuInflater().inflate(R.menu.main_menu, menu); // 复用同一套菜单项
    } // onCreateContextMenu 结束

    @Override
    public boolean onContextItemSelected(MenuItem item) { // 上下文菜单选项被点击时调用
        return handleMenuClick(item, this); // 复用统一的菜单点击处理
    } // onContextItemSelected 结束

    private boolean handleMenuClick(MenuItem item, Context context) { // 统一处理顶部菜单和上下文菜单的点击
        int id = item.getItemId(); // 获取被点击菜单项的 id
        if (id == R.id.menu_bmi_wiki) { // 如果点击了“BMI 百科”
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.bmi_wiki_url))); // 创建跳转到百科网页的 Intent
            context.startActivity(intent); // 打开浏览器访问链接
            return true; // 返回 true 表示事件已处理
        } else if (id == R.id.menu_history) { // 如果点击了“历史记录”
            context.startActivity(new Intent(context, HistoryActivity.class)); // 跳转到历史记录页面
            return true; // 返回 true 表示事件已处理
        } else if (id == R.id.menu_exit) { // 如果点击了“退出”
            finish(); // 结束当前活动
            return true; // 返回 true 表示事件已处理
        } // if-else 结束
        return false; // 未匹配任何菜单项时返回 false
    } // handleMenuClick 结束

    /**
     * 为输入控件添加监听，实时将用户输入保存到本地。
     */
    private void setupPersistenceListeners() { // 设置持久化监听的方法
        TextWatcher watcher = new TextWatcher() { // 创建文本监听器，对三个输入框复用
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // 文本变化前无需处理

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { // 文本变化时回调
                persistInputs(); // 每次变化都保存当前输入
            } // onTextChanged 结束

            @Override
            public void afterTextChanged(Editable s) {} // 文本变化后无需处理
        }; // TextWatcher 定义结束

        heightET.addTextChangedListener(watcher); // 为身高输入框添加监听
        weightET.addTextChangedListener(watcher); // 为体重输入框添加监听
        ageET.addTextChangedListener(watcher); // 为年龄输入框添加监听

        genderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> persistInputs()); // 性别选项变化时也立即保存
    } // setupPersistenceListeners 结束

    /**
     * 读取当前输入框与性别选项，将其交给 ViewModel 做本地持久化。
     */
    private void persistInputs() { // 持久化当前输入的方法
        int genderId = genderRadioGroup.getCheckedRadioButtonId(); // 获取当前选中的性别按钮 id
        RadioButton selectedGender = findViewById(genderId); // 根据 id 找到对应的单选按钮
        String gender = selectedGender != null ? selectedGender.getText().toString() : getString(R.string.male); // 防空处理后获取性别文本
        viewModel.persistInputs( // 将输入传递给 ViewModel 保存
                heightET.getText().toString(), // 传递身高字符串
                weightET.getText().toString(), // 传递体重字符串
                ageET.getText().toString(), // 传递年龄字符串
                gender // 传递性别字符串
        ); // 方法调用结束
    } // persistInputs 结束
} // MainActivity 类结束

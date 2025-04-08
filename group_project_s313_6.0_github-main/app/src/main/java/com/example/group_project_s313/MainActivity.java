package com.example.group_project_s313;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        // 获取语言设置
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(newBase);
        String lang = prefs.getString("app_language", "zh");

        // 设置语言上下文
        Context context = LocaleHelper.setLocale(newBase, lang);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化导航控制器
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
        }

        // 设置语言切换按钮点击事件
        Button langSwitchBtn = findViewById(R.id.btn_switch_language);
        if (langSwitchBtn != null) {
            langSwitchBtn.setOnClickListener(v -> {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String lang = prefs.getString("app_language", "zh");
                String newLang = lang.equals("zh") ? "en" : "zh";

                // 保存新语言设置
                prefs.edit().putString("app_language", newLang).apply();

                // 重启当前 Activity，确保语言变化生效
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            });
        }
    }
}
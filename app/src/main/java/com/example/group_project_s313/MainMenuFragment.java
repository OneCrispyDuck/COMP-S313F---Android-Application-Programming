package com.example.group_project_s313;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class MainMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 跳转到路线查询功能
        view.findViewById(R.id.btn_route).setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_to_route));

        // 切换语言按钮（只在当前布局存在）
        Button btnSwitchLanguage = view.findViewById(R.id.btn_switch_language);
        if (btnSwitchLanguage != null) {
            btnSwitchLanguage.setOnClickListener(v -> switchLanguage());
        }
    }

    private void switchLanguage() {
        // 获取当前语言
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String currentLang = prefs.getString("app_language", "zh");
        String newLang = currentLang.equals("zh") ? "en" : "zh";

        // 应用新语言并保存
        LocaleHelper.setLocale(requireContext(), newLang);

        // 重启 Activity 以应用语言变更
        Intent intent = requireActivity().getIntent();
        requireActivity().finish();
        startActivity(intent);
    }
}
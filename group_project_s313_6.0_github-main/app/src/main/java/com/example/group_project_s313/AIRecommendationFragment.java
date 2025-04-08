package com.example.group_project_s313;

import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.*;
import org.json.*;

import java.io.IOException;
import java.util.*;

public class AIRecommendationFragment extends Fragment {

    private TextView textViewAI;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ai, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewAI = view.findViewById(R.id.textViewAI);
        Button btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        if (getArguments() != null) {
            String destination = getArguments().getString("destination");
            double lat = getArguments().getDouble("dest_lat", 0.0);
            double lng = getArguments().getDouble("dest_lng", 0.0);
            fetchAIRecommendations(destination, lat, lng);
        }
    }

    private void fetchAIRecommendations(String destinationName, double lat, double lng) {
        String apiUrl = "https://api.chatanywhere.org/v1/chat/completions";
        String apiKey = "sk-oc1GhqILDr49mChvU5vEIaHmRzc42LPYhMsmsWPpo1YpEeTF"; // ⚠️ 建议放入安全配置中

        try {
            String lang = getResources().getConfiguration().getLocales().get(0).getLanguage();

            String locationInfo = "";
            if (lat != 0.0 && lng != 0.0) {
                locationInfo = String.format(Locale.US, "(纬度: %.5f，经度: %.5f)", lat, lng);
            }

            // 设置 AI 提示
            String systemPrompt = lang.equals("zh") ?
                    "你是一个专业的旅游推荐助手，会根据用户目的地推荐路线和内容。" :
                    "You are a professional travel assistant. Recommend based on the user's destination.";

            String userPrompt = lang.equals("zh") ?
                    "请推荐 " + destinationName + locationInfo + " 附近值得一去的景点、地道美食和特色活动。" :
                    "Please recommend must-see attractions, local foods, and special activities near " + destinationName + " " + locationInfo + ".";

            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system").put("content", systemPrompt));
            messages.put(new JSONObject().put("role", "user").put("content", userPrompt));

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 1500);

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    updateUI(lang.equals("zh") ?
                            "AI推荐获取失败，请检查网络连接。" :
                            "Failed to get AI recommendation. Please check your network.");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        updateUI(lang.equals("zh") ?
                                "服务暂时不可用，错误码：" + response.code() :
                                "Service is temporarily unavailable. Error code: " + response.code());
                        return;
                    }

                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray choices = json.getJSONArray("choices");
                        if (choices.length() > 0) {
                            String content = choices.getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");

                            String header = lang.equals("zh") ?
                                    "🌟 AI智能推荐 🌟\n\n" :
                                    "🌟 AI Recommendation 🌟\n\n";

                            updateUI(header + content);
                        }
                    } catch (JSONException e) {
                        updateUI(lang.equals("zh") ?
                                "数据解析失败。" :
                                "Failed to parse AI response.");
                    }
                }
            });

        } catch (Exception e) {
            updateUI("请求构建失败：" + e.getMessage());
        }
    }

    private void updateUI(String content) {
        requireActivity().runOnUiThread(() -> textViewAI.setText(content));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        client.dispatcher().cancelAll();
    }
}
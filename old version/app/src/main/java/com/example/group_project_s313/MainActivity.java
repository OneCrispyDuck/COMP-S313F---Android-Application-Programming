package com.example.group_project_s313;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.PolyUtil;

import okhttp3.*;
import org.json.*;
import java.io.IOException;
import java.util.*;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private AutoCompleteTextView editTextStart, editTextEnd;
    private TextView textViewResult;
    private GoogleMap mMap;
    private OkHttpClient client = new OkHttpClient();
    private List<String> stopNames = new ArrayList<>();
    private Map<String, String> stopNameToIdMap = new HashMap<>();

    private String googleApiKey = "AIzaSyDeZLXmQFT4kQTVQh6MUw4--1xYvRqRzy8"; // ⚠️ 替换为你的 API Key

    private TextView textViewAI; // AI 推荐的 TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.textViewResult);
        textViewAI = findViewById(R.id.textViewAI); // 绑定 AI 推荐 TextView
        textViewResult.setMovementMethod(new ScrollingMovementMethod());
        textViewAI.setMovementMethod(new ScrollingMovementMethod()); // 让 AI 推荐可以滚动

        editTextStart = findViewById(R.id.editTextStart);
        editTextEnd = findViewById(R.id.editTextEnd);
        Button buttonSearch = findViewById(R.id.buttonSearch);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        fetchStopNames(); // 获取站点数据

        buttonSearch.setOnClickListener(v -> searchBusRoutes());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    /**
     * 获取 KMB 站点数据（用于自动补全）
     */
    private void fetchStopNames() {
        String stopUrl = "https://data.etabus.gov.hk/v1/transport/kmb/stop/";
        Request stopRequest = new Request.Builder().url(stopUrl).build();

        client.newCall(stopRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONArray stopArray = new JSONObject(response.body().string()).getJSONArray("data");
                    for (int i = 0; i < stopArray.length(); i++) {
                        JSONObject stopObject = stopArray.getJSONObject(i);
                        String stopId = stopObject.getString("stop");
                        String stopNameTc = stopObject.optString("name_tc", "");

                        if (!stopNameTc.isEmpty()) {
                            stopNames.add(stopNameTc);
                            stopNameToIdMap.put(stopNameTc, stopId);
                        }
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                                android.R.layout.simple_dropdown_item_1line, stopNames);
                        editTextStart.setAdapter(adapter);
                        editTextEnd.setAdapter(adapter);
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 查询 Google Maps 公交路线
     */
    private void searchBusRoutes() {
        String origin = editTextStart.getText().toString().trim();
        String destination = editTextEnd.getText().toString().trim();

        if (!stopNameToIdMap.containsKey(origin) || !stopNameToIdMap.containsKey(destination)) {
            textViewResult.setText("請輸入有效的 KMB 站名");
            return;
        }

        fetchGoogleTransitRoute(origin, destination);
        fetchAIRecommendations(destination); // 将 AI 推荐调用移至此处
    }

    /**
     * 通过 Google Maps API 查询公交路线，并获取预计到达时间
     */
    private void fetchGoogleTransitRoute(String origin, String destination) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin
                + "&destination=" + destination
                + "&mode=transit"
                + "&language=zh-HK"
                + "&key=" + googleApiKey;

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> textViewResult.setText("無法獲取數據，請檢查網絡"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONArray routes = jsonResponse.optJSONArray("routes");

                    if (routes == null || routes.length() == 0) {
                        runOnUiThread(() -> textViewResult.setText("未找到合適的公交路線"));
                        return;
                    }

                    JSONObject route = routes.getJSONObject(0);
                    String encodedPolyline = route.getJSONObject("overview_polyline").getString("points");
                    List<LatLng> routePoints = PolyUtil.decode(encodedPolyline);

                    JSONArray steps = route.getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                    StringBuilder busInfo = new StringBuilder();
                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

                    for (int i = 0; i < steps.length(); i++) {
                        JSONObject step = steps.getJSONObject(i);
                        if (step.has("transit_details")) {
                            JSONObject transitDetails = step.getJSONObject("transit_details");
                            String busNumber = transitDetails.getJSONObject("line").getString("short_name");
                            String departureStop = transitDetails.getJSONObject("departure_stop").getString("name");
                            String arrivalStop = transitDetails.getJSONObject("arrival_stop").getString("name");

                            LatLng startPoint = new LatLng(
                                    transitDetails.getJSONObject("departure_stop").getJSONObject("location").getDouble("lat"),
                                    transitDetails.getJSONObject("departure_stop").getJSONObject("location").getDouble("lng"));

                            LatLng endPoint = new LatLng(
                                    transitDetails.getJSONObject("arrival_stop").getJSONObject("location").getDouble("lat"),
                                    transitDetails.getJSONObject("arrival_stop").getJSONObject("location").getDouble("lng"));

                            boundsBuilder.include(startPoint);
                            boundsBuilder.include(endPoint);

                            // **新增：获取预计到达时间**
                            String arrivalTime = "未知";
                            if (transitDetails.has("arrival_time")) {
                                arrivalTime = transitDetails.getJSONObject("arrival_time").getString("text");
                            }

                            busInfo.append("🚌 搭乘路线：").append(busNumber).append("\n")
                                    .append("🚏 上车站：").append(departureStop).append("\n")
                                    .append("🏁 下车站：").append(arrivalStop).append("\n")
                                    .append("⏳ 预计到达时间：").append(arrivalTime).append("\n\n");

                            runOnUiThread(() -> {
                                mMap.addMarker(new MarkerOptions().position(startPoint)
                                        .title("上车站：" + departureStop)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                mMap.addMarker(new MarkerOptions().position(endPoint)
                                        .title("下车站：" + arrivalStop)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            });
                        }
                    }

                    runOnUiThread(() -> {
                        mMap.clear();
                        if (!routePoints.isEmpty()) {
                            mMap.addPolyline(new PolylineOptions().addAll(routePoints).color(0xFF0000FF).width(10));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 150));
                        }
                        textViewResult.setText(busInfo.toString());
                    });

                } catch (JSONException e) {
                    runOnUiThread(() -> textViewResult.setText("解析數據出錯"));
                }
            }
        });
    }
    /**
     * 调用 ChatAnywhere API 获取 AI 推荐
     */
    private void fetchAIRecommendations(String destinationName) {
        String apiUrl = "https://api.chatanywhere.org/v1/chat/completions";
        String chatAnywhereApiKey = "sk-oc1GhqILDr49mChvU5vEIaHmRzc42LPYhMsmsWPpo1YpEeTF";

        try {
            JSONObject systemMsg = new JSONObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", "你是一个旅游推荐助手");

            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", "请推荐" + destinationName + "附近的美食和旅游景点");

            JSONArray messages = new JSONArray();
            messages.put(systemMsg);
            messages.put(userMsg);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 1000); // 关键点：增加生成内容长度

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + chatAnywhereApiKey)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() ->
                            textViewAI.setText("AI推荐失败: " + e.getMessage())
                    );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        runOnUiThread(() ->
                                textViewAI.setText("错误码: " + response.code())
                        );
                        return;
                    }

                    try {
                        String jsonStr = response.body().string();
                        JSONObject json = new JSONObject(jsonStr);
                        JSONArray choices = json.getJSONArray("choices");

                        if (choices.length() > 0) {
                            JSONObject firstChoice = choices.getJSONObject(0);
                            JSONObject message = firstChoice.getJSONObject("message");
                            String content = message.getString("content");

                            // 打印完整内容到日志
                            Log.d("AI_Response", "完整内容: " + content);

                            runOnUiThread(() -> {
                                textViewAI.setText("🎯 AI推荐：\n" + content);
                                textViewAI.post(() -> {
                                    // 自动滚动到底部（可选）
                                    ScrollView scrollView = (ScrollView) textViewAI.getParent();
                                    scrollView.fullScroll(View.FOCUS_DOWN);
                                });
                            });
                        }
                    } catch (JSONException e) {
                        runOnUiThread(() ->
                                textViewAI.setText("解析错误")
                        );
                    }
                }
            });

        } catch (JSONException e) {
            runOnUiThread(() ->
                    textViewAI.setText("请求构建错误")
            );
        }
    }
}
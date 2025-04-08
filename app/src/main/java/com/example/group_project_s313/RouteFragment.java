package com.example.group_project_s313;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.*;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.PolyUtil;

import okhttp3.*;

import org.json.*;

import java.io.IOException;
import java.util.*;

public class RouteFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView textViewResult;
    private Button btnSearch, btnShowAI;
    private Spinner spinnerMode;

    private LatLng originLatLng;
    private LatLng destLatLng;
    private String destName = "";
    private String selectedMode = "transit"; // 默认公交

    private static final String GOOGLE_API_KEY = "AIzaSyDeZLXmQFT4kQTVQh6MUw4--1xYvRqRzy8"; // 请替换为你的实际 Key
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化 Google Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), GOOGLE_API_KEY);
        }

        textViewResult = view.findViewById(R.id.textViewResult);
        btnSearch = view.findViewById(R.id.buttonSearch);
        btnShowAI = view.findViewById(R.id.btn_show_ai);
        spinnerMode = view.findViewById(R.id.spinner_transport_mode);
        btnShowAI.setEnabled(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.transport_modes,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMode.setAdapter(adapter);

        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: selectedMode = "transit"; break;
                    case 1: selectedMode = "driving"; break;
                    case 2: selectedMode = "walking"; break;
                    case 3: selectedMode = "subway"; break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMode = "transit";
            }
        });

        AutocompleteSupportFragment startFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_start);
        startFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));
        startFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                originLatLng = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getContext(), "起点选择失败", Toast.LENGTH_SHORT).show();
            }
        });

        AutocompleteSupportFragment endFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_end);
        endFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));
        endFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destLatLng = place.getLatLng();
                destName = place.getName();
                btnShowAI.setEnabled(true);
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getContext(), "终点选择失败", Toast.LENGTH_SHORT).show();
            }
        });

        btnSearch.setOnClickListener(v -> {
            if (originLatLng == null || destLatLng == null) {
                textViewResult.setText(getString(R.string.select_start_end));
                return;
            }
            fetchRoute(originLatLng, destLatLng, selectedMode);
        });

        btnShowAI.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(destName)) {
                Bundle bundle = new Bundle();
                bundle.putString("destination", destName);
                bundle.putDouble("dest_lat", destLatLng.latitude);
                bundle.putDouble("dest_lng", destLatLng.longitude);
                Navigation.findNavController(v).navigate(R.id.action_route_to_ai, bundle);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void fetchRoute(LatLng origin, LatLng destination, String mode) {
        textViewResult.setText(getString(R.string.searching_route));

        String originStr = origin.latitude + "," + origin.longitude;
        String destStr = destination.latitude + "," + destination.longitude;

        String realMode = mode.equals("subway") ? "transit" : mode;

        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        urlBuilder.append("origin=").append(originStr)
                .append("&destination=").append(destStr)
                .append("&mode=").append(realMode)
                .append("&language=").append(getCurrentLanguageTag()); // ✅ 动态语言

        if (mode.equals("subway")) {
            urlBuilder.append("&transit_mode=subway");
        }

        urlBuilder.append("&key=").append(GOOGLE_API_KEY);
        String url = urlBuilder.toString();

        Log.d("RouteRequestURL", url); // ✅ 打印调试

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                showError("网络请求失败：" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    JSONArray routes = json.optJSONArray("routes");
                    if (routes == null || routes.length() == 0) {
                        showError(getString(R.string.route_not_found));
                        return;
                    }

                    JSONObject route = routes.getJSONObject(0);
                    JSONObject leg = route.getJSONArray("legs").getJSONObject(0);
                    JSONArray steps = leg.getJSONArray("steps");

                    String duration = leg.getJSONObject("duration").getString("text");
                    String distance = leg.getJSONObject("distance").getString("text");

                    StringBuilder sb = new StringBuilder();
                    sb.append("🕒 ").append(getString(R.string.total_time)).append("：").append(duration).append("\n");
                    sb.append("📏 ").append(getString(R.string.total_distance)).append("：").append(distance).append("\n\n");

                    List<LatLng> path = PolyUtil.decode(
                            route.getJSONObject("overview_polyline").getString("points"));

                    for (int i = 0; i < steps.length(); i++) {
                        JSONObject step = steps.getJSONObject(i);
                        String travelMode = step.getString("travel_mode");

                        if ("TRANSIT".equals(travelMode)) {
                            JSONObject transit = step.getJSONObject("transit_details");
                            String busNo = transit.getJSONObject("line").optString("short_name", "Line");
                            String vehicle = transit.getJSONObject("line")
                                    .getJSONObject("vehicle").optString("type", "Transit");
                            String dep = transit.getJSONObject("departure_stop").getString("name");
                            String arr = transit.getJSONObject("arrival_stop").getString("name");

                            String emoji = "SUBWAY".equalsIgnoreCase(vehicle) ? "🚇" : "🚌";

                            sb.append(emoji).append(" ").append(vehicle).append(" ").append(busNo).append("\n")
                                    .append("📍 ").append(getString(R.string.boarding)).append("：").append(dep).append("\n")
                                    .append("🏁 ").append(getString(R.string.alighting)).append("：").append(arr).append("\n\n");
                        } else {
                            String htmlInstruction = step.getString("html_instructions");
                            String plainText = android.text.Html.fromHtml(htmlInstruction).toString();
                            sb.append("➡️ ").append(plainText).append("\n\n");
                        }
                    }

                    requireActivity().runOnUiThread(() -> {
                        mMap.clear();

                        mMap.addPolyline(new PolylineOptions().addAll(path).color(0xFF0066FF).width(10));

                        if (!path.isEmpty()) {
                            String startTitle = getString(R.string.marker_start);
                            String endTitle = getString(R.string.marker_end);

                            mMap.addMarker(new MarkerOptions().position(path.get(0)).title(startTitle));
                            mMap.addMarker(new MarkerOptions().position(path.get(path.size() - 1)).title(endTitle));

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (LatLng point : path) {
                                builder.include(point);
                            }
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                        }

                        textViewResult.setText(sb.toString());
                    });

                } catch (Exception e) {
                    showError("解析路线失败：" + e.getMessage());
                }
            }
        });
    }

    private void showError(String msg) {
        requireActivity().runOnUiThread(() -> textViewResult.setText(msg));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    // ✅ 新增：获取当前系统语言的语言标签
    private String getCurrentLanguageTag() {
        Locale locale;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        return locale.toLanguageTag(); // 如 "en", "zh-CN"
    }
}
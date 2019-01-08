package com.example.lizeyu.eventsearch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchForm extends Fragment {

    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private LocationManager lm;
    private Button clear_btn;
    private Button search_btn;
    private TextView locationKey;
    private TextView keyword;
    private EditText distance;
    private Activity mainActivity;
    private double latitude;
    private double longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.search_form, container, false);
        mainActivity = this.getActivity();
        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        final Spinner spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.unit, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(0);

        final AppCompatAutoCompleteTextView autoCompleteTextView =
                rootView.findViewById(R.id.auto_complete_edit_text);
        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        distance = rootView.findViewById(R.id.distance);
        locationKey = rootView.findViewById(R.id.editText2);
        final RadioGroup rg = rootView.findViewById(R.id.radioGroup);
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        lm = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double longitude = location.getLongitude();
        final double latitude = location.getLatitude();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1) {
                    locationKey.setEnabled(false);
                } else {
                    locationKey.setEnabled(true);
                }
            }
        });
        final TextInputLayout keyword_wrapper = rootView.findViewById(R.id.keyword_wrapper);
        final TextInputLayout keyword_wrapper2 = rootView.findViewById(R.id.keyword_wrapper2);

        search_btn = rootView.findViewById(R.id.button);
        search_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean valid_keyword = !TextUtils.isEmpty(autoCompleteTextView.getText().toString().trim());
                boolean valid_location = (rg.getCheckedRadioButtonId() == R.id.radioButton1) ||
                        (rg.getCheckedRadioButtonId() == R.id.radioButton2 && !TextUtils.isEmpty(locationKey.getText().toString().trim()));
                if (valid_keyword && valid_location) {
                    Intent intent = new Intent(mainActivity, DisplayListActivity.class);
                    intent.putExtra("keyword", autoCompleteTextView.getText().toString());
                    intent.putExtra("category", spinner.getSelectedItem().toString());
                    intent.putExtra("distance", distance.getText().toString());
                    intent.putExtra("unit", spinner2.getSelectedItem().toString());
                    intent.putExtra("lat", Double.toString(latitude));
                    intent.putExtra("lon", Double.toString(longitude));
                    if (rg.getCheckedRadioButtonId() == R.id.radioButton1)
                        intent.putExtra("location", "local");
                    else {
                        intent.putExtra("location", "other");
                        intent.putExtra("locationKey", locationKey.getText().toString());
                    }
                    startActivity(intent);
                } else {
                    if (!valid_keyword) {
                        keyword_wrapper.setErrorEnabled(true);
                        keyword_wrapper.setError("Please enter mandatory field");
                    }
                    if (!valid_location) {
                        keyword_wrapper2.setErrorEnabled(true);
                        keyword_wrapper2.setError("Please enter mandatory field");
                    }
                    Toast.makeText(SearchForm.this.getContext(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                }
            }
        });
        clear_btn = rootView.findViewById(R.id.button2);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                keyword_wrapper.setError(null);
                keyword_wrapper.setErrorEnabled(false);
                keyword_wrapper2.setError(null);
                keyword_wrapper2.setErrorEnabled(false);
                spinner.setSelection(0);
                spinner2.setSelection(0);
                locationKey.setText("");
                distance.setText("10");
                rg.check(R.id.radioButton1);
                autoCompleteTextView.setText("");

            }
        });
        return rootView;
    }

    private void makeApiCall(String text) {
        ApiCall.make(this.getActivity(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
//                    JSONObject responseObject = new JSONObject(response);
//                    JSONArray array = responseObject.getJSONArray("results");
                    if (response != "") {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject row = array.getJSONObject(i);
                            stringList.add(row.getString("name"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
//                if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                final double longitude = location.getLongitude();
                final double latitude = location.getLatitude();
        }

    }
}

package com.se340.smartshoveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.location.Location;
import android.location.LocationManager;
import android.content.Context;
import android.app.Activity;

public class submitLawnCareRequest extends AppCompatActivity implements LocationListener {

    private ProgressDialog pDialog;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Geocoder geocoder;
    List<Address> addresses;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_lawn_care_request);

        final EditText etUserAddress = findViewById(R.id.etUserAddress);
        final CheckBox cbCutGrass = findViewById(R.id.checkBoxCutGrass);
        final CheckBox cbTrim = findViewById(R.id.checkBoxTrim);
        final EditText etNotes = findViewById(R.id.workNotes);
        final Button btnSubmit = findViewById(R.id.btnSubmitLawnRequest);
        final Button btnFillToday = findViewById(R.id.btnToday);
        final EditText date = findViewById(R.id.etDate);
        final Button GPS = findViewById(R.id.btnGPS);

        //Check if app has permission for GPS
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(location);

        geocoder = new Geocoder(this, Locale.getDefault());

        GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String address = addresses.get(0).getAddressLine(0);

                    etUserAddress.setText(address);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        });


        btnFillToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String today = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
                date.setText(today);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String address = etUserAddress.getText().toString();
                final boolean cutGrass = cbCutGrass.isChecked();
                final boolean trim = cbTrim.isChecked();
                final String description = etNotes.getText().toString();

                //show loading spinner
                pDialog.setMessage("Sending request...");
                showDialog();

                //create response listener
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonRes = new JSONObject(response);
                            boolean success = jsonRes.getBoolean("success");

                            if (success) {
                                JSONObject user = jsonRes.getJSONObject("user");
/*
                                db.addUser(
                                        user.getString(Constant.KEY_UID),
                                        user.getString(Constant.KEY_NAME),
                                        user.getString(Constant.KEY_EMAIL)
                                );
*/
                                hideDialog();
                                Intent goToDashboardIntent = new Intent(submitLawnCareRequest.this, UserHomeActivity.class);
                                submitLawnCareRequest.this.startActivity(goToDashboardIntent);

                                //finish();

                            } else {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideDialog();
                        }
                    }


                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        AlertDialog.Builder builder = new AlertDialog.Builder(submitLawnCareRequest.this);
                        builder.setMessage("Request failed").setNegativeButton("Retry", null).create()
                                .show();
                        // Toast.makeText(SmartShoveler.getAppContext(), "Bad Password", Toast.LENGTH_LONG).show();
                    }
                };

                //send request
                OrderRequest request = new OrderRequest(address, description, responseListener, errorListener);

                RequestQueue queue = Volley.newRequestQueue(submitLawnCareRequest.this);
                queue.add(request);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location)
    {
        Double longitude = location.getLongitude();
        Double latitude = location.getLatitude();

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}


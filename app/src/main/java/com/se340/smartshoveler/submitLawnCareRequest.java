package com.se340.smartshoveler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class submitLawnCareRequest extends AppCompatActivity {

    private ProgressDialog pDialog;

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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}


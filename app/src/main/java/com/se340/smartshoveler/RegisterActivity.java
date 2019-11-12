package com.se340.smartshoveler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import android.app.AlertDialog;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.se340.smartshoveler.helpers.SmartShoveler;

import android.app.ProgressDialog;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(RegisterActivity.this, MainActivity.class);
        RegisterActivity.this.startActivity(homeIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText etFullname = findViewById(R.id.etName);
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnRegister = findViewById(R.id.btnRegister);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etFullname.getText().toString();
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                pDialog.setMessage("Signing Up ...");
                showDialog();

                //create response listener
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonRes = new JSONObject(response);
                            boolean success = jsonRes.getBoolean("success");

                            if (success) {
                                Intent backToLoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(backToLoginIntent);
                                hideDialog();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Failled").setNegativeButton("Retry", null).create()
                                        .show();
                                hideDialog();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Failled").setNegativeButton("Retry", null).create()
                                .show();
                    }
                };

                //send request 
                RegisterRequest request = new RegisterRequest(name, email, password, responseListener, errorListener);

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(request);

            }
        });

        final TextView loginLink = findViewById(R.id.tvRegisterToLogin);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(registerIntent);
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

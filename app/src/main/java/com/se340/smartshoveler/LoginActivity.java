package com.se340.smartshoveler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.se340.smartshoveler.helpers.SmartShoveler;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText etUsername = findViewById(R.id.etUsername);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnLogin = findViewById(R.id.btnLogin);
        final ProgressBar signinprogressBar = findViewById(R.id.signinProgressBar);
        signinprogressBar.setVisibility(View.GONE);

        final TextView registerLink = findViewById(R.id.tvRegosterHere);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                signinprogressBar.setVisibility(View.VISIBLE);

                //create response listener
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonRes = new JSONObject(response);
                            boolean success = jsonRes.getBoolean("success");

                            if (success) {
                                signinprogressBar.setVisibility(View.GONE);
                                Intent goToDashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                                LoginActivity.this.startActivity(goToDashboardIntent);
                            } else {
                                signinprogressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Bad Password", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            signinprogressBar.setVisibility(View.GONE);
                        }
                    }
                };

                //send request
                LoginRequest request = new LoginRequest(username, password, responseListener, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        signinprogressBar.setVisibility(View.GONE);
                        Toast.makeText(SmartShoveler.getAppContext(), "Bad Password", Toast.LENGTH_LONG).show();
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(request);
            }
        });

    }
}

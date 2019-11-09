package com.se340.smartshoveler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.se340.smartshoveler.helpers.Constant;
import com.se340.smartshoveler.helpers.Functions;
import com.se340.smartshoveler.helpers.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText etUsername = findViewById(R.id.etUsername);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnLogin = findViewById(R.id.btnLogin);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // session manager
        session = new SessionManager(getApplicationContext());

        // check user is already logged in
        if (session.isLoggedIn()) {
            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(i);
            finish();
        }

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

                //show loading spinner
                pDialog.setMessage("Signing In ...");
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
                                Intent goToDashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                                //goToDashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                session.setLogin(true);

                                LoginActivity.this.startActivity(goToDashboardIntent);

                                //finish();

                            } else {
                                hideDialog();
                                Toast.makeText(getApplicationContext(), "Bad Password", Toast.LENGTH_LONG).show();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Login Failled").setNegativeButton("Retry", null).create()
                                .show();
                        // Toast.makeText(SmartShoveler.getAppContext(), "Bad Password", Toast.LENGTH_LONG).show();
                    }
                };

                //send request
                LoginRequest request = new LoginRequest(username, password, responseListener, errorListener);

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
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

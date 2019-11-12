package com.se340.smartshoveler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        final EditText etEmail = findViewById(R.id.etResetEmail);
        final Button btnReset = findViewById(R.id.btnResetAccount);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    
            btnReset.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        final String email = etEmail.getText().toString();

        //show loading spinner
        pDialog.setMessage("Resetting Your Account...");
        showDialog();

        //create response listener
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonRes = new JSONObject(response);
                    boolean success = jsonRes.getBoolean("success");

                    if (success) {
                        hideDialog();
                        Intent goToDashboardIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        ResetPasswordActivity.this.startActivity(goToDashboardIntent);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                builder.setMessage("Reset Failled").setNegativeButton("Retry", null).create()
                        .show();
                // Toast.makeText(SmartShoveler.getAppContext(), "Bad Password", Toast.LENGTH_LONG).show();
            }
        };

        //send request
        ResetRequest request = new ResetRequest(email, responseListener, errorListener);

        RequestQueue queue = Volley.newRequestQueue(ResetPasswordActivity.this);
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

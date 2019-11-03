package com.se340.smartshoveler;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.se340.smartshoveler.com.se340.smartshoveler.helpers.Constant;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_URL = Constant.BASE_URL+"/api/v1/users/account/login";
    private Map<String, String> params;
    public LoginRequest(String username, String password, Response.Listener<String> listener){
        super(Method.POST, LOGIN_URL, listener, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Bad Password", Toast.LENGTH_LONG).show();
            }
        });
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

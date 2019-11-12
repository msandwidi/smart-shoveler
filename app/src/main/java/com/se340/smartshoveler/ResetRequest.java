package com.se340.smartshoveler;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.se340.smartshoveler.helpers.Constant;

import java.util.HashMap;
import java.util.Map;

public class ResetRequest extends StringRequest {
    private static final String RESET_URL = Constant.BASE_URL + "/api/v1/users/account/reset";
    private Map<String, String> params;

    public ResetRequest(String email, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, RESET_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("email", email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

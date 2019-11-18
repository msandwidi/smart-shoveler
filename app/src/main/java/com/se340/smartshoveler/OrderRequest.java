package com.se340.smartshoveler;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.se340.smartshoveler.helpers.Constant;

import java.util.HashMap;
import java.util.Map;

public class OrderRequest extends StringRequest {
    private static final String LOGIN_URL = Constant.BASE_URL + "/api/v1/orders";
    private Map<String, String> params;

    public OrderRequest(String address, String description, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("address", address);
        params.put("description", description);
        //params.put("email", email);
        //params.put("isRecurrent", String.valueOf(isRecurrent));
    }

    //address, email, description, type, isrecurrent

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

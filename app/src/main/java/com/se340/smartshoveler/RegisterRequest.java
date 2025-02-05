package com.se340.smartshoveler;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.se340.smartshoveler.helpers.Constant;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_URL = Constant.BASE_URL+"/api/v1/users/account/signup";
    private Map<String, String> params;
    public RegisterRequest (String name, String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListner){
        super(Method.POST, REGISTER_URL, listener, errorListner);
        params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

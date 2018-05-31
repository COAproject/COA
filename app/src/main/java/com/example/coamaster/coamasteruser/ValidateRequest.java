package com.example.coamaster.coamasteruser;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateRequest extends StringRequest {

    final static private String URL = "http://coamaster.dothome.co.kr/user_validate.php";
    private Map<String, String> parameters;

    public ValidateRequest(String user_id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_id", user_id);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}

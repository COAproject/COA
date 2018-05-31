package com.example.coamaster.coamasteruser;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FindidRequest extends StringRequest {

    final static private String URL = "http://coamaster.dothome.co.kr/user_emailvalidate.php";
    private Map<String, String> parameters;

    public FindidRequest(String user_email, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_email", user_email);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}

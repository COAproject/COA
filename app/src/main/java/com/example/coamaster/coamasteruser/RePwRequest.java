package com.example.coamaster.coamasteruser;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RePwRequest extends StringRequest {

    final static private String URL = "http://coamaster.dothome.co.kr/android/password.php";
    private Map<String, String> parameters;

    public RePwRequest(String user_id,String repw, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_id", user_id);
        parameters.put("repw", repw);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}

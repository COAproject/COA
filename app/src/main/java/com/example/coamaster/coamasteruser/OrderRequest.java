package com.example.coamaster.coamasteruser;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OrderRequest extends StringRequest {

    final static private String URL = "http://coamaster.dothome.co.kr/android/shopping.php";
    private Map<String, String> parameters;

    public OrderRequest(String user_id, String request, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_id", user_id);
        parameters.put("request", request);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}

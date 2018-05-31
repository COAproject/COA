package com.example.coamaster.coamasteruser;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://coamaster.dothome.co.kr/user_register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String user_id, String user_pw, String user_name, String user_email,  String Token, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_id", user_id);
        parameters.put("user_pw", user_pw);
        parameters.put("user_name", user_name);
        parameters.put("user_email", user_email);
        parameters.put("Token", Token);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}

package com.gyecommerce.zamoraonline;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ConsultRequest extends StringRequest {

    private Map<String, String> parametros;
    public ConsultRequest (String numero, String rutaCodigo, Response.Listener<String> listener, Response.ErrorListener errorListen){
        super (Request.Method.POST, rutaCodigo, listener, errorListen);

        parametros = new HashMap<>();
        parametros.put("numero",numero+"");
    }

    protected Map<String, String> getParams(){
        return parametros;
    }
}


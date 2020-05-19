package com.gyecommerce.zamoraonline;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ConsultInfoProduct extends StringRequest {
    //private static final String rutaCodigo = "https://triptrick.000webhostapp.com/TiendaZamoraOnline/ConsultaProductos.php";
    //private static final String rutaCodigo = "https://gyecommerce-com.preview-domain.com/TiendaOnline/ConsultaProductos.php";
    private static final String rutaCodigo = "https://www.gyecommerce.com/TiendaOnline/ConsultaProductos.php";

    private Map<String, String> parametros;
    public ConsultInfoProduct (Response.Listener<String> listener){
        super (Request.Method.POST, rutaCodigo, listener, null);
        parametros = new HashMap<>();
    }

    protected Map<String, String> getParams(){
        return parametros;
    }
}

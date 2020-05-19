package com.gyecommerce.zamoraonline;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InitInformation extends StringRequest {

    //private static final String rutaCodigo = "https://triptrick.000webhostapp.com/TiendaZamoraOnline/RegistroPedido.php";
    //private static final String rutaCodigo = "https://gyecommerce-com.preview-domain.com/TiendaOnline/RegistroPedido.php";
    private static final String rutaCodigo = "https://www.gyecommerce.com/TiendaOnline/RegistroPedido.php";

    private Map<String, String> parametros;
    public InitInformation (String nombre, String apellido, String numero, boolean condWs, int numart, int[] arr_ncantidad, double PrecioTotal, String fecha, Response.Listener<String> listener, Response.ErrorListener errorListen){
        super (Request.Method.POST, rutaCodigo, listener, errorListen);

        parametros = new HashMap<>();
        parametros.put("nombre",nombre);
        parametros.put("p_apellido",apellido);
        parametros.put("numero",numero+"");
        parametros.put("condWs",condWs+"");
        parametros.put("precioTotal",PrecioTotal+"");
        parametros.put("fecha",fecha);
        parametros.put("numart",numart+"");

        for (int i = 0; i <numart; i++) {
            String str_key = "n_prod_"+i;
            parametros.put(str_key,arr_ncantidad[i]+"");

        }

    }

    protected Map<String, String> getParams(){
        return parametros;
    }
}
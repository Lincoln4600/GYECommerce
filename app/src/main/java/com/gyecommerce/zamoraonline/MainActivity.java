package com.gyecommerce.zamoraonline;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    private String[] arr_prodName;
    private String[] image_descr;
    private String[] arr_pathImg;
    private String[] arr_CatProd;
    private double[] arr_costProd;
    private int Numart = 0, compraMinima, costeTransporte;
    private String numWhatssap, mailContacto, webPage;
    private int[] NummaxPro;
    private boolean Check;
    //private static final String login_url = "https://triptrick.000webhostapp.com/TiendaZamoraOnline/ConsultaProductos.php";
    //private static final String login_url = "https://gyecommerce-com.preview-domain.com/TiendaOnline/ConsultaProductos.php";
    private static final String login_url = "https://www.gyecommerce.com/TiendaOnline/ConsultaProductos.php";

    private int MY_SOCKET_TIMEOUT_MS = 2000;
    View parentLayout;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferencias = getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.clear();
        editor.commit();
        parentLayout = findViewById(android.R.id.content);

    }

    public void onStart(){
        super.onStart();
        if (Numart == 0) {
            SharedPreferences preferencias = getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.clear();
            editor.commit();
            this.progressDialog = ProgressDialog.show(this, "Bienvenido a la tienda Online",
                    "espere por favor...", true, false);

            SetInitValues();
        }
    }

    public void onStop(){
        super.onStop();

    }

    public void BtnSelectCompra(View view) {


        if (Numart == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Fallo con la conexion a internet");

            builder.setPositiveButton("Volver a intentar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //perform any action
                    progressDialog = ProgressDialog.show(MainActivity.this, "Bienvenido a la tienda Online",
                            "espere por favor...", true, false);

                    RetryValuesSelectCompra();

                }
            });

            builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            //creating alert dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }else
        {

            Intent intent = new Intent(MainActivity.this, Listadocompra.class);
            startActivity(intent);
            progressDialog = ProgressDialog.show(this, "Cargando productos",
                    "espere por favor...", true, false);
            finish();
        }
    }

    public void BtnEstadoCompra(View view) {

        if (Numart == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Fallo con la conexion a internet");

            builder.setPositiveButton("Volver a intentar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //perform any action
                    progressDialog = ProgressDialog.show(MainActivity.this, "Bienvenido a la tienda Online",
                            "espere por favor...", true, false);

                    RetryValuesEstadoCompra();

                }
            });

            builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            //creating alert dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }else
        {

            finish();
            Intent intent = new Intent(this, Estadopedido.class);
            startActivity(intent);
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void SetInitValues (){

        requestQueue = Volley.newRequestQueue(this);
        Response.Listener listen = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject Json = new JSONObject(response);
                    Check = Json.getBoolean("check");
                    Numart = Json.getInt("nProductos");
                    compraMinima = Json.getInt("compraMinima");
                    costeTransporte = Json.getInt("CostoTransporte");
                    numWhatssap = Json.getString("numWhatssap");
                    mailContacto = Json.getString("mailContacto");
                    webPage = Json.getString("webpage");
                    JSONArray arrNombre = Json.getJSONArray("nombre_producto");
                    JSONArray arrDescr = Json.getJSONArray("descripcion_producto");
                    JSONArray arrPrecio = Json.getJSONArray("precio");
                    JSONArray arrMaxPro = Json.getJSONArray("cantidad_max_producto");
                    JSONArray arrStrPath = Json.getJSONArray("path_imagen");
                    JSONArray arrStrCatProd = Json.getJSONArray("categoria_producto");
                    String DBName = "DataPedido";
                    SharedPreferences preferencias = getSharedPreferences(DBName,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencias.edit();
                    editor.putInt("numart",Numart);
                    arr_prodName = new String[Numart];
                    image_descr = new String[Numart];
                    arr_costProd = new double[Numart];
                    arr_pathImg = new String[Numart];
                    arr_CatProd = new String[Numart];
                    NummaxPro = new int[Numart];
                    for (int i = 0; i <Numart; i++) {
                        arr_prodName[i] = arrNombre.getString(i);
                        image_descr[i] = arrDescr.getString(i);
                        arr_costProd[i] = arrPrecio.getDouble(i);
                        NummaxPro[i] = arrMaxPro.getInt(i);
                        arr_pathImg[i] = arrStrPath.getString(i);
                        arr_CatProd[i] = arrStrCatProd.getString(i);
                        String keyname = "Prod_"+i;
                        String keydescr = "ProdDescr_"+i;
                        String keyprodcost = "ProdCost_"+i;
                        String keyMaxProd = "ProdMax_"+i;
                        String keyPathImg = "PathImg_"+i;
                        String keyCatProd = "CatProd_"+i;
                        editor.putString(keyname,arr_prodName[i]);
                        editor.putString(keydescr,image_descr[i]);
                        editor.putFloat(keyprodcost,(float) arr_costProd[i]);
                        editor.putInt(keyMaxProd,NummaxPro[i]);
                        editor.putString(keyPathImg,arr_pathImg[i]);
                        editor.putString(keyCatProd,arr_CatProd[i]);
                    }
                    editor.putInt("NumProduct",GetNumberProduct(arr_costProd));
                    editor.putInt("CompraMinima",compraMinima);
                    editor.putInt("costeTransporte",costeTransporte);
                    editor.putString("numWhatssap",numWhatssap);
                    editor.putString("mailContacto",mailContacto);
                    editor.putString("webpage",webPage);
                    editor.commit();
                    if (MainActivity.this.progressDialog != null) {
                        MainActivity.this.progressDialog.dismiss();
                    }

                    setContentView(R.layout.activity_main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListen = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                        .setAction("Actionkjhl", null).show();

                MainActivity.this.progressDialog.dismiss();
            }
        };
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, login_url, listen,errorListen);

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

    public void RetryValuesSelectCompra (){

        requestQueue = Volley.newRequestQueue(this);
        Response.Listener listen = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject Json = new JSONObject(response);
                    Check = Json.getBoolean("check");
                    Numart = Json.getInt("nProductos");
                    compraMinima = Json.getInt("compraMinima");
                    numWhatssap = Json.getString("numWhatssap");
                    mailContacto = Json.getString("mailContacto");
                    webPage = Json.getString("webpage");
                    JSONArray arrNombre = Json.getJSONArray("nombre_producto");
                    JSONArray arrDescr = Json.getJSONArray("descripcion_producto");
                    JSONArray arrPrecio = Json.getJSONArray("precio");
                    JSONArray arrMaxPro = Json.getJSONArray("cantidad_max_producto");
                    JSONArray arrStrPath = Json.getJSONArray("path_imagen");
                    JSONArray arrStrCatProd = Json.getJSONArray("categoria_producto");
                    String DBName = "DataPedido";
                    SharedPreferences preferencias = getSharedPreferences(DBName,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencias.edit();
                    editor.putInt("numart",Numart);
                    arr_prodName = new String[Numart];
                    image_descr = new String[Numart];
                    arr_costProd = new double[Numart];
                    arr_pathImg = new String[Numart];
                    arr_CatProd = new String[Numart];
                    NummaxPro = new int[Numart];
                    for (int i = 0; i <Numart; i++) {
                        arr_prodName[i] = arrNombre.getString(i);
                        image_descr[i] = arrDescr.getString(i);
                        arr_costProd[i] = arrPrecio.getDouble(i);
                        NummaxPro[i] = arrMaxPro.getInt(i);
                        arr_pathImg[i] = arrStrPath.getString(i);
                        arr_CatProd[i] = arrStrCatProd.getString(i);
                        String keyname = "Prod_"+i;
                        String keydescr = "ProdDescr_"+i;
                        String keyprodcost = "ProdCost_"+i;
                        String keyMaxProd = "ProdMax_"+i;
                        String keyPathImg = "PathImg_"+i;
                        String keyCatProd = "CatProd_"+i;
                        editor.putString(keyname,arr_prodName[i]);
                        editor.putString(keydescr,image_descr[i]);
                        editor.putFloat(keyprodcost,(float) arr_costProd[i]);
                        editor.putInt(keyMaxProd,NummaxPro[i]);
                        editor.putString(keyPathImg,arr_pathImg[i]);
                        editor.putString(keyCatProd,arr_CatProd[i]);
                    }
                    editor.putInt("NumProduct",GetNumberProduct(arr_costProd));
                    editor.putInt("CompraMinima",compraMinima);
                    editor.putString("numWhatssap",numWhatssap);
                    editor.putString("mailContacto",mailContacto);
                    editor.putString("webpage",webPage);
                    editor.commit();
                    if (MainActivity.this.progressDialog != null) {
                        MainActivity.this.progressDialog.dismiss();
                    }

                    Intent intent = new Intent(MainActivity.this, Listadocompra.class);
                    startActivity(intent);
                    progressDialog = ProgressDialog.show(MainActivity.this, "Cargando productos",
                            "espere por favor...", true, false);
                    finish();

                    setContentView(R.layout.activity_main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListen = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                        .setAction("Actionkjhl", null).show();

                MainActivity.this.progressDialog.dismiss();
            }
        };
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, login_url, listen,errorListen);

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

    public void RetryValuesEstadoCompra (){

        requestQueue = Volley.newRequestQueue(this);
        Response.Listener listen = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject Json = new JSONObject(response);
                    Check = Json.getBoolean("check");
                    Numart = Json.getInt("nProductos");
                    compraMinima = Json.getInt("compraMinima");
                    numWhatssap = Json.getString("numWhatssap");
                    mailContacto = Json.getString("mailContacto");
                    webPage = Json.getString("webpage");
                    JSONArray arrNombre = Json.getJSONArray("nombre_producto");
                    JSONArray arrDescr = Json.getJSONArray("descripcion_producto");
                    JSONArray arrPrecio = Json.getJSONArray("precio");
                    JSONArray arrMaxPro = Json.getJSONArray("cantidad_max_producto");
                    JSONArray arrStrPath = Json.getJSONArray("path_imagen");
                    JSONArray arrStrCatProd = Json.getJSONArray("categoria_producto");
                    String DBName = "DataPedido";
                    SharedPreferences preferencias = getSharedPreferences(DBName,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencias.edit();
                    editor.putInt("numart",Numart);
                    arr_prodName = new String[Numart];
                    image_descr = new String[Numart];
                    arr_costProd = new double[Numart];
                    arr_pathImg = new String[Numart];
                    arr_CatProd = new String[Numart];
                    NummaxPro = new int[Numart];
                    for (int i = 0; i <Numart; i++) {
                        arr_prodName[i] = arrNombre.getString(i);
                        image_descr[i] = arrDescr.getString(i);
                        arr_costProd[i] = arrPrecio.getDouble(i);
                        NummaxPro[i] = arrMaxPro.getInt(i);
                        arr_pathImg[i] = arrStrPath.getString(i);
                        arr_CatProd[i] = arrStrCatProd.getString(i);
                        String keyname = "Prod_"+i;
                        String keydescr = "ProdDescr_"+i;
                        String keyprodcost = "ProdCost_"+i;
                        String keyMaxProd = "ProdMax_"+i;
                        String keyPathImg = "PathImg_"+i;
                        String keyCatProd = "CatProd_"+i;
                        editor.putString(keyname,arr_prodName[i]);
                        editor.putString(keydescr,image_descr[i]);
                        editor.putFloat(keyprodcost,(float) arr_costProd[i]);
                        editor.putInt(keyMaxProd,NummaxPro[i]);
                        editor.putString(keyPathImg,arr_pathImg[i]);
                        editor.putString(keyCatProd,arr_CatProd[i]);
                    }
                    editor.putInt("NumProduct",GetNumberProduct(arr_costProd));
                    editor.putInt("CompraMinima",compraMinima);
                    editor.putString("numWhatssap",numWhatssap);
                    editor.putString("mailContacto",mailContacto);
                    editor.putString("webpage",webPage);
                    editor.commit();
                    if (MainActivity.this.progressDialog != null) {
                        MainActivity.this.progressDialog.dismiss();
                    }

                    finish();
                    Intent intent = new Intent(MainActivity.this, Estadopedido.class);
                    startActivity(intent);

                    setContentView(R.layout.activity_main);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListen = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                        .setAction("Actionkjhl", null).show();

                MainActivity.this.progressDialog.dismiss();
            }
        };
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, login_url, listen,errorListen);

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

    public int GetNumberProduct(double[] arr_costProd){
        int NumProduct = 0;

        for (int i = 0; i <Numart; i++) {
            if(arr_costProd[i] > 0){
                NumProduct++;
            }
        }
        return NumProduct;
    }

}

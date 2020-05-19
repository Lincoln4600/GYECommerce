package com.gyecommerce.zamoraonline;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Estadopedido extends AppCompatActivity {

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    private EditText EtNumero;
    private TextView TvMessage;
    private String [] arr_prodName, arr_pathImg;
    private int nPedido, IDOrden,numart;
    private LinearLayout LyScroll_pedidos, pedidos_layout;
    private double PrecioTot;
    String str_numero, str_nombre, str_apellidos, str_fecha, str_estado;
    boolean CellExist, CancelarOk;
    String message;
    View parentLayout;
    TextView txt_estado;
    Button btnCancelarPedido;
    //private static final String rutaEstadoPedido = "https://triptrick.000webhostapp.com/TiendaZamoraOnline/ConsultarPedido.php";
    //private static final String rutaCancelarPedido = "https://triptrick.000webhostapp.com/TiendaZamoraOnline/CancelarPedido.php";
    //private static final String rutaEstadoPedido = "https://gyecommerce-com.preview-domain.com/TiendaOnline/ConsultarPedido.php";
    //private static final String rutaCancelarPedido = "https://gyecommerce-com.preview-domain.com/TiendaOnline/CancelarPedido.php";
    private static final String rutaEstadoPedido = "https://www.gyecommerce.com/TiendaOnline/ConsultarPedido.php";
    private static final String rutaCancelarPedido = "https://www.gyecommerce.com/TiendaOnline/CancelarPedido.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentLayout = findViewById(android.R.id.content);
        setContentView(R.layout.activity_estadopedido);


        EtNumero = (EditText)findViewById(R.id.et_celular);
        TvMessage = (TextView)findViewById(R.id.tv_message);
        txt_estado = findViewById(R.id.txt_estadoPedido);
        LyScroll_pedidos = (LinearLayout)findViewById(R.id.ly_scrollEstadoPedidos);
        btnCancelarPedido = findViewById(R.id.btn_CancelarPedido);
        btnCancelarPedido.setEnabled(false);
        btnCancelarPedido.setTextColor(Color.GRAY);
        GetInitValue();


    }

    public void BtnAtras(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void BtnBuscar(View view) {

        str_numero = EtNumero.getText().toString();
        progressDialog = ProgressDialog.show(this, "Buscando su compra", null, true, true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject Json = new JSONObject(response);
                    CellExist = Json.getBoolean("checkCell");
                    if (CellExist)
                    {
                        str_nombre = Json.getString("Nombre");
                        str_apellidos = Json.getString("Apellidos");
                        PrecioTot = Json.getDouble("PrecioTot");
                        IDOrden = Json.getInt("IDOrden");
                        str_fecha = Json.getString("Fecha");
                        nPedido = Json.getInt("nPedido");
                        str_estado = Json.getString("estado");
                        JSONArray arrID = Json.getJSONArray("arrID");
                        JSONArray arrCantidad = Json.getJSONArray("arrCantidad");

                        for (int i = 0; i <nPedido; i++) {
                            int ID_producto = arrID.getInt(i);
                            int nCantidad = arrCantidad.getInt(i);
                            boolean bShow = ShowPedidos (ID_producto, nCantidad);

                        }
                        TextView tv_precioTot = (TextView)findViewById(R.id.tv_strEstTotPrecio);
                        tv_precioTot.setText("USD "+String.format("%.2f",(float)PrecioTot));
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressDialog.dismiss();
                        TvMessage.setText("Hola " + str_nombre + " " + str_apellidos +",\n"
                                +"Tu orden (#"+ IDOrden + ") del día "+ str_fecha+ " está:\n");
                        EtNumero.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        txt_estado.setText(str_estado);
                        txt_estado.setTextSize(15);
                        if (str_estado.equals("En proceso"))
                        {
                            txt_estado.setTextColor(Color.RED);
                            btnCancelarPedido.setEnabled(true);
                            btnCancelarPedido.setTextColor(Color.BLACK);
                            btnCancelarPedido.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CancelarPedido(v);
                                }
                            });
                        }

                        if (str_estado.equals("Repartiendo"))
                            txt_estado.setTextColor(Color.YELLOW);
                        if (str_estado.equals("Entragado"))
                            txt_estado.setTextColor(Color.GREEN);
                    }
                    else{
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressDialog.dismiss();
                        TvMessage.setText("No existe pedido con ese numero");
                    }
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
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Estadopedido.this.progressDialog.dismiss();
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        ConsultRequest InitRegister = new ConsultRequest(str_numero,rutaEstadoPedido, respuesta, errorListen);

        requestQueue.add(InitRegister);
    }

    public void CancelarPedido(View view) {

            str_numero = EtNumero.getText().toString();
            progressDialog = ProgressDialog.show(this, "Cancelando su pedido", null, true, true);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Response.Listener<String> respuesta = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject Json = new JSONObject(response);
                        CancelarOk = Json.getBoolean("success");
                        if (CancelarOk)
                        {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(Estadopedido.this).create();
                            alertDialog.setTitle("Aviso");
                            alertDialog.setMessage("Su pedido ha sido cancelado");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            btnCancelarPedido.setEnabled(false);
                                            btnCancelarPedido.setTextColor(Color.GRAY);
                                        }
                                    });
                            alertDialog.show();
                            LyScroll_pedidos.removeAllViews();
                            TextView tv_precioTot = (TextView)findViewById(R.id.tv_strEstTotPrecio);
                            tv_precioTot.setText("USD 0.0");
                            TvMessage.setText("");
                            txt_estado.setText("");
                            EtNumero.setText("");



                        }
                        else{
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(Estadopedido.this).create();
                            alertDialog.setTitle("Aviso");
                            alertDialog.setMessage("No se ha podido cancelar su pedido");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
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

                    Estadopedido.this.progressDialog.dismiss();
                }
            };

            requestQueue = Volley.newRequestQueue(this);
            ConsultRequest InitRegister = new ConsultRequest(str_numero,rutaCancelarPedido, respuesta, errorListen);

            requestQueue.add(InitRegister);
    }

    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public boolean ShowPedidos (int ID, int nCantidad)
    {
        pedidos_layout = new LinearLayout(this);
        LinearLayout.LayoutParams paramLinearLayout2 = new LinearLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f);
        pedidos_layout.setBackgroundColor(Color.RED);
        pedidos_layout.setLayoutParams(paramLinearLayout2);
        pedidos_layout.setOrientation(LinearLayout.HORIZONTAL);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(3, Color.BLACK);
        drawable.setCornerRadius(8);
        pedidos_layout.setBackgroundDrawable(drawable);

        TextView text_pedido = new TextView(this);
        LinearLayout.LayoutParams paramProName = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        paramProName.gravity = Gravity.CENTER;
        paramProName.setMargins(50,0, 0, 0);
        text_pedido.setLayoutParams(paramProName);
        String Text = nCantidad + " " + arr_prodName[ID-1];
        text_pedido.setTextSize(20);
        text_pedido.setTextColor(Color.BLACK);
        text_pedido.setText("\u2022 "+Text);

        ImageView imgVw = new ImageView(this);
        LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(
                200,
                150,
                1.0f);
        paramsImage.setMargins(2, 2, 2, 2);
        paramsImage.gravity = Gravity.CENTER_VERTICAL;
        paramsImage.gravity = Gravity.CENTER_HORIZONTAL;
        imgVw.setLayoutParams(paramsImage);
        Picasso.get().load(arr_pathImg[ID-1]).into(imgVw);


        pedidos_layout.addView(text_pedido);
        pedidos_layout.addView(imgVw);

        LyScroll_pedidos.addView(pedidos_layout);
        return true;
    }
    public void GetInitValue()
    {
        SharedPreferences preferencias = getSharedPreferences("DataPedido", Context.MODE_PRIVATE);

        numart = preferencias.getInt("numart", 0);
        arr_prodName = new String[numart];
        arr_pathImg = new String[numart];
        for (int i = 0; i <numart; i++)
        {
            String key_nombre = "Prod_"+i;
            String keyPathImg = "PathImg_"+i;
            arr_prodName[i] = preferencias.getString(key_nombre, "NO tiene nombre");
            arr_pathImg[i] = preferencias.getString(keyPathImg, "") ;
        }
    }
}

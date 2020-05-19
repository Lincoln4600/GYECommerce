package com.gyecommerce.zamoraonline;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Ordencompra extends AppCompatActivity {

    private EditText EtNombre, EtApellido, EtNumero;
    private TextView text_msg;
    private CheckBox CbCondWs;
    private double Totprecio;
    String Mensaje, str_nombre, str_apellido, fecha, str_numero, msg;
    private boolean CondWs, RegisterOk, CellExist, DataOk;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    private int[] arr_ncantidad;
    private int numart;
    private int IDpedido;
    String message;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordencompra);

        Bundle b = this.getIntent().getExtras();
        parentLayout = findViewById(android.R.id.content);
        String key = "arr_ncantPedidos";
        arr_ncantidad = b.getIntArray(key);
        Totprecio = getIntent().getDoubleExtra("precioTot", 0.00);
        numart = getIntent().getIntExtra("N_productos", 0);

        EtNombre = (EditText) findViewById(R.id.et_nombre);
        EtApellido = (EditText) findViewById(R.id.et_apellido);
        CbCondWs = (CheckBox) findViewById(R.id.cb_ws);
        EtNumero = (EditText) findViewById(R.id.et_telefono);
        text_msg = (TextView) findViewById(R.id.tv_error);

    }

    public void BtnPedir(View view) {

        RegistroPedido();
        /*String str1, str2, str3, str4;
        String DateDeliver = GetDayDeliver();
        str1 = "Su pedido consiste en:\n";
        str2 = String.valueOf(nFrutilla) + " mermeladas de frutilla\n";
        str3 = String.valueOf(nPera) + " mermeladas de pera\n";
        str4 = "Precio total a pagar: $" + String.format("%.2f",(float)preciototal)+"\n"
                + DateDeliver + "\n";
        if(nFrutilla>0 && nPera>0)
        {
            Mensaje = str1 + str2 + str3 + str4;
        }
        else if (nFrutilla>0)
        {
            Mensaje = str1 + str2 + str4;
        }
        else if (nPera>0) {
            Mensaje = str1 + str3 + str4;
        }
        else
            Mensaje = str1 + str4;


        String.format("%.2f",(float)preciototal);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmacion de la orden");
        builder.setMessage(Mensaje);
        builder.setPositiveButton("Confirmar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RegistroPedido();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();*/

    }

    public void BtnAtras(View view) {
        Intent intent = new Intent(this, ConfirmacionCompra.class);
        startActivity(intent);
    }

    public String GetDayDeliver() {
        Calendar cal = Calendar.getInstance();
        int dayofWeek = cal.get(Calendar.DAY_OF_WEEK);
        int dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
        int monthofYear = cal.get(Calendar.MONTH) + 1;
        String str_dayofWeek, str_monthofYear, str_Deliver;

        switch (monthofYear) {
            case 1:
                str_monthofYear = "Enero";
                break;
            case 2:
                str_monthofYear = "Febrero";
                break;
            case 3:
                str_monthofYear = "Marzo";
                break;
            case 4:
                str_monthofYear = "Abril";
                break;
            case 5:
                str_monthofYear = "Mayo";
                break;
            case 6:
                str_monthofYear = "Junio";
                break;
            case 7:
                str_monthofYear = "Julio";
                break;
            case 8:
                str_monthofYear = "Agosto";
                break;
            case 9:
                str_monthofYear = "Septiembre";
                break;
            case 10:
                str_monthofYear = "Octubre";
                break;
            case 11:
                str_monthofYear = "Noviembre";
                break;
            default:
                str_monthofYear = "Diciembre";
        }

        switch (dayofWeek) {
            case 2:
                str_dayofWeek = "Lunes";
                break;
            case 3:
                str_dayofWeek = "Martes";
                break;
            case 4:
                str_dayofWeek = "Miercoles";
                break;
            case 5:
                str_dayofWeek = "Jueves";
                break;
            case 6:
                str_dayofWeek = "Viernes";
                break;
            case 7:
                str_dayofWeek = "Sabado";
                break;
            default:
                str_dayofWeek = "Domingo";
        }

        boolean firstDeliver = ((dayofWeek >= Calendar.SUNDAY) && (dayofWeek < Calendar.WEDNESDAY));
        if (firstDeliver)
            str_Deliver = "Pedido hecho el " + str_dayofWeek + " " + dayofMonth + " de " + str_monthofYear + "\n" +
                    "Se entregara el próximo miercoles";
        else
            str_Deliver = "Pediodo hecho el " + str_dayofWeek + " " + dayofMonth + " de " + str_monthofYear + "\n" +
                    "Se entregara el próximo domingo";

        return str_Deliver;

    }

    public String GetDate() {
        String fecha;
        Calendar cal = Calendar.getInstance();
        int dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
        int monthofYear = cal.get(Calendar.MONTH) + 1;
        fecha = dayofMonth + "/" + monthofYear + "/2020";
        return fecha;
    }

    public boolean AllDataOk() {
        String msgNombre, msgApellido, msgTelefono1, msgTelefono2;

        DataOk = true;
        if (TextUtils.isEmpty(str_nombre)) {

            msgNombre = "* No has introducido el nombre\n";
            EtNombre.setHint("Nombre *");
            EtNombre.setHintTextColor(Color.RED);
            DataOk = false;
        } else {
            msgNombre = "";
            EtNombre.setText(str_nombre);
        }

        if (TextUtils.isEmpty(str_apellido)) {

            msgApellido = "* No has introducido el apellido\n";
            EtApellido.setHint("Apellido *");
            EtApellido.setHintTextColor(Color.RED);
            DataOk = false;
        } else {
            msgApellido = "";
            EtApellido.setText(str_apellido);
        }

        if (TextUtils.isEmpty(str_numero)) {

            msgTelefono1 = "* No has introducido el número de teléfono\n";
            msgTelefono2 = "";
            EtNumero.setHint("Número de teléfono *");
            EtNumero.setHintTextColor(Color.RED);
            DataOk = false;
        } else {
            if (str_numero.length() != 10 ) {
                msgTelefono1 = "";
                msgTelefono2 = "* El número de teléfono no es correcto\n";
                EtNumero.setHint(str_numero);
                EtNumero.setTextColor(Color.RED);
                DataOk = false;
            } else {
                msgTelefono1 = "";
                msgTelefono2 = "";
                EtNumero.setText(str_numero);

            }
        }

        msg = msgNombre + msgApellido + msgTelefono1 + msgTelefono2;

        text_msg.setText(msg);

        return DataOk;

    }

    public void RegistroPedido() {
        str_nombre = EtNombre.getText().toString();
        str_apellido = EtApellido.getText().toString();
        CondWs = CbCondWs.isChecked();
        str_numero = EtNumero.getText().toString();
        fecha = GetDate();
        if (AllDataOk()) {

            progressDialog = ProgressDialog.show(this, "Ordenando compra", null, true, false);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Response.Listener<String> respuesta = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject Json = new JSONObject(response);
                        CellExist = Json.getBoolean("CelExist");
                        RegisterOk = Json.getBoolean("Success");
                        IDpedido = Json.getInt("ID_pedido");
                        if (!CellExist) {
                            if (RegisterOk) {
                                Bundle b = new Bundle();
                                String key = "arr_ncantPedidos";
                                b.putIntArray(key, arr_ncantidad);
                                Intent intent = new Intent(Ordencompra.this, OrdenFinalizada.class);
                                intent.putExtras(b);
                                intent.putExtra("IDPedido", IDpedido);
                                intent.putExtra("Nombre", str_nombre);
                                intent.putExtra("Apellido", str_apellido);
                                intent.putExtra("RegistroOk",true);
                                progressDialog.dismiss();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                startActivity(intent);
                                finish();
                            } else
                                Toast.makeText(Ordencompra.this, "Error Orden", Toast.LENGTH_LONG).show();
                        } else
                        {
                            progressDialog.dismiss();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            text_msg.setText("Ya existe un pedido activo con este número de telefono");
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
                    Ordencompra.this.progressDialog.dismiss();
                }
            };

            requestQueue = Volley.newRequestQueue(this);
            InitInformation InitRegister = new InitInformation(str_nombre, str_apellido, str_numero, CondWs, numart, arr_ncantidad, Totprecio, fecha, respuesta, errorListen);

            requestQueue.add(InitRegister);
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ConfirmacionCompra extends AppCompatActivity {

    private int numart, costeTranporte;
    private int[] arr_ncantidad;
    private String [] arr_prodName, arr_pathImg;
    private LinearLayout pedidos_layout, LyScroll_pedidos, transporte_layout;
    private LinearLayout[] arr_pedidosLayout;
    private double Totprecio;
    private ImageButton[] arr_btnMenudelete;
    private double[] arr_costProd;
    private boolean addTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmacion_compra);

        SharedPreferences preferencias = getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        addTrans = preferencias.getBoolean("AddTrans",false);
        costeTranporte = getIntent().getIntExtra("costeTranporte", 0);

        GetInitValue();
        LyScroll_pedidos = new LinearLayout(this);
        arr_btnMenudelete = new ImageButton[numart];
        LyScroll_pedidos = (LinearLayout) findViewById(R.id.ly_scrollEstadoPedidos);

        ShowPedidos();

        Button btn_cancelar = (Button) findViewById(R.id.btn_cancelar);
        Button btn_continuar = (Button)findViewById(R.id.btn_continuar);

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmacionCompra.this);
                builder.setMessage("Desea cancelar el pedido o seguir comprando?");

                builder.setPositiveButton("Cancelar pedido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ConfirmacionCompra.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        //perform any action

                    }
                });

                builder.setNegativeButton("Seguir comprando", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        TextView tv_precioTot = findViewById(R.id.tv_strEstTotPrecio);
                        tv_precioTot.setText("USD "+String.format("%.2f",(float)(Totprecio- costeTranporte)));

                        SharedPreferences preferencias = getSharedPreferences("DataPedido",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferencias.edit();
                        String strTotPrecio = "TotPrecio";
                        editor.putFloat(strTotPrecio,(float)(Totprecio-costeTranporte));
                        editor.commit();


                        Intent intent = new Intent(ConfirmacionCompra.this, Listadocompra.class);
                        startActivity(intent);
                        finish();
                    }
                });
                //creating alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TextView tv_Totprecio = findViewById(R.id.tv_TotPrecio);
                //String str_precioTot = tv_Totprecio.getText().toString();

                SharedPreferences preferencias = getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
                int minValue = preferencias.getInt("CompraMinima", 0);
                //String[] arr_strPrecioTot = str_precioTot.split(" ");
                //String num = arr_strPrecioTot[1];
                //num = num.replaceAll(",",".");
                //double num_ = Double.valueOf(num);
                if (Totprecio > minValue || addTrans){
                    Bundle b = new Bundle();
                    String key = "arr_ncantPedidos";
                    b.putIntArray(key, arr_ncantidad);
                    Intent intent = new Intent(v.getContext(), Ordencompra.class);
                    intent.putExtras(b);
                    intent.putExtra("N_productos", numart);
                    intent.putExtra("precioTot", Totprecio);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(v.getContext(), MinimaCompra.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        for (int i = 0; i <numart; i++)
        {
             final int k = i;
             arr_btnMenudelete[i].setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                LyScroll_pedidos.removeView(arr_pedidosLayout[k]);
                                                                Totprecio = Totprecio - (arr_costProd[k] * arr_ncantidad[k]);
                                                                arr_ncantidad[k] = 0;

                                                                TextView tv_precioTot = findViewById(R.id.tv_strEstTotPrecio);
                                                                tv_precioTot.setText("USD "+String.format("%.2f",(float)Totprecio));

                                                                SharedPreferences preferencias = getSharedPreferences("DataPedido",Context.MODE_PRIVATE);
                                                                String str_nCantidad = "nCantidad_"+k;
                                                                SharedPreferences.Editor editor = preferencias.edit();
                                                                editor.putInt(str_nCantidad,0);
                                                                editor.commit();
                                                                String strTotPrecio = "TotPrecio";
                                                                editor.putFloat(strTotPrecio,(float)Totprecio);
                                                                editor.commit();

                                                            }
                                                        }
                );
        }


    }

    public void ShowPedidos()
    {
        arr_pedidosLayout = new LinearLayout[numart];
        for (int i = 0; i <numart; i++)
        {
            arr_btnMenudelete[i] = new ImageButton(this);

            if(arr_ncantidad[i]>0) {

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

                ImageButton btn_menudelete = new ImageButton(this);
                btn_menudelete.setBackgroundResource(android.R.drawable.ic_menu_delete);
                LinearLayout.LayoutParams paramsBtnMenuDelete = new LinearLayout.LayoutParams(
                        200,
                        70,
                        1.0f);
                paramsBtnMenuDelete.setMargins(10,0, 15, 0);
                paramsBtnMenuDelete.gravity = Gravity.CENTER;
                btn_menudelete.setLayoutParams(paramsBtnMenuDelete);
                arr_btnMenudelete[i] = btn_menudelete;

                TextView text_pedido = new TextView(this);
                LinearLayout.LayoutParams paramProName = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f);
                paramProName.gravity = Gravity.CENTER;
                paramProName.setMargins(0,0, 0, 0);
                text_pedido.setLayoutParams(paramProName);
                String Text = arr_ncantidad[i] + " " + arr_prodName[i];
                text_pedido.setTextSize(20);
                text_pedido.setTextColor(Color.BLACK);
                text_pedido.setText("\u2022 "+Text);

                ImageView imgVw = new ImageView(this);
                LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(
                        200,
                        150,
                        1.0f);
                paramsImage.setMargins(2, 2, 10, 2);
                paramsImage.gravity = Gravity.RIGHT;
                paramsImage.gravity = Gravity.RIGHT;
                imgVw.setLayoutParams(paramsImage);
                Picasso.get().load(arr_pathImg[i]).into(imgVw);

                pedidos_layout.addView(btn_menudelete);
                pedidos_layout.addView(text_pedido);
                pedidos_layout.addView(imgVw);
                arr_pedidosLayout[i] = pedidos_layout;

                LyScroll_pedidos.addView(pedidos_layout);
            }
        }

        if (addTrans) {

            transporte_layout = new LinearLayout(this);
            LinearLayout.LayoutParams paramTransLayout = new LinearLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1.0f);
            transporte_layout.setBackgroundColor(Color.RED);
            transporte_layout.setLayoutParams(paramTransLayout);
            transporte_layout.setOrientation(LinearLayout.HORIZONTAL);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(3, Color.BLACK);
            drawable.setCornerRadius(8);
            transporte_layout.setBackgroundDrawable(drawable);

            TextView text_transporte = new TextView(this);
            LinearLayout.LayoutParams paramProName = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f);
            paramProName.gravity = Gravity.CENTER;
            paramProName.setMargins(80,0, 0, 0);
            text_transporte.setLayoutParams(paramProName);
            text_transporte.setTextSize(20);
            text_transporte.setTextColor(Color.BLACK);
            text_transporte.setText("\u2022  TRANSPORTE");

            ImageView imgVw = new ImageView(this);
            LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(
                    200,
                    150,
                    1.0f);
            paramsImage.setMargins(2, 2, 10, 2);
            paramsImage.gravity = Gravity.RIGHT;
            paramsImage.gravity = Gravity.RIGHT;
            imgVw.setLayoutParams(paramsImage);
            imgVw.setImageResource(R.drawable.auto);

            transporte_layout.addView(text_transporte);
            transporte_layout.addView(imgVw);

            LyScroll_pedidos.addView(transporte_layout);
            Totprecio = Totprecio + costeTranporte;
            SharedPreferences preferencias = getSharedPreferences("DataPedido",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            String strTotPrecio = "TotPrecio";
            editor.putFloat(strTotPrecio,(float)Totprecio);
            editor.commit();
        }

        TextView tv_precioTot = new TextView(this);
        tv_precioTot = findViewById(R.id.tv_strEstTotPrecio);
        tv_precioTot.setText("USD "+String.format("%.2f",(float)Totprecio));

    }
    public void GetInitValue()
    {
        SharedPreferences preferencias = getSharedPreferences("DataPedido", Context.MODE_PRIVATE);

        numart = preferencias.getInt("numart", 0);
        Totprecio = (double)preferencias.getFloat("TotPrecio", 0);
        arr_ncantidad = new int[numart];
        arr_prodName = new String[numart];
        arr_pathImg = new String[numart];
        arr_costProd = new double[numart];
        for (int i = 0; i <numart; i++)
        {
            String str_nCantidad = "nCantidad_"+i;
            String key_nombre = "Prod_"+i;
            String keyPathImg = "PathImg_"+i;
            String keyprodcost = "ProdCost_"+i;
            arr_costProd[i] = (double)preferencias.getFloat(keyprodcost, 0);
            arr_ncantidad[i] = preferencias.getInt(str_nCantidad, 0);
            arr_prodName[i] = preferencias.getString(key_nombre, "NO tiene nombre");
            arr_pathImg[i] = preferencias.getString(keyPathImg, "") ;
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}

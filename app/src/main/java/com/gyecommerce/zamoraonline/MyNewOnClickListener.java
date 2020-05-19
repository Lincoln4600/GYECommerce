package com.gyecommerce.zamoraonline;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyNewOnClickListener implements View.OnClickListener
{
    int ncantidad, NumaxPro, cont;
    private boolean IncrTrue;
    double costproduct, Totprecio;
    Context context;
    private ImageButton btn_increase, btn_decrease;
    private String tag_lyname, tag_name;
    private View root;
    private TextView tv_precioTotal;
    public MyNewOnClickListener(double Totprecio, int cont, View root, String tag_lyname, String tag_name, ImageButton btn_increase, ImageButton btn_decrease, Context context, int NumaxPro, double costproduct, int ncantidad, TextView tv_Totprecio) {
        this.ncantidad = ncantidad;
        this.costproduct = costproduct;
        this.NumaxPro = NumaxPro;
        this.context = context;
        this.btn_increase = btn_increase;
        this.btn_decrease = btn_decrease;
        this.tag_name = tag_name;
        this.root = root;
        this.tag_lyname = tag_lyname;
        this.cont = cont;
        this.Totprecio = Totprecio;
        this.tv_precioTotal = tv_Totprecio;


    }

    @Override
    public void onClick(View v)
    {
        TextView tv_cantidad = root.findViewWithTag(tag_name);
        String str = tv_precioTotal.getText().toString();
        String[] arr = str.split(" ");
        String str_precioTot = arr[1];
        str_precioTot = str_precioTot.replace(',', '.');
        if(v == btn_increase){
            Totprecio = Double.parseDouble(str_precioTot);
            SetIncreasecantidad();
            tv_cantidad.setText(Integer.toString(ncantidad));
            String str_tv = "USD "+String.format("%.2f",(float)Totprecio);
            tv_precioTotal.setText(str_tv);

        }

        else{
            Totprecio = Double.parseDouble(str_precioTot);
            SetDecreasecantidad();
            tv_cantidad.setText(Integer.toString(ncantidad));
            String str_tv = "USD "+String.format("%.2f",(float)Totprecio);
            tv_precioTotal.setText(str_tv);
        }

        LinearLayout secondLayout = new LinearLayout(context);
        secondLayout = (LinearLayout)root.findViewWithTag(tag_lyname);
        if(ncantidad>0) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(8, Color.RED);
            drawable.setCornerRadius(8);
            secondLayout.setBackgroundDrawable(drawable);
        }
        else{
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(3, Color.BLACK);
            drawable.setCornerRadius(8);
            secondLayout.setBackgroundDrawable(drawable);
        }
        guardarPreferencias();

    }

    public void SetIncreasecantidad()
    {

        if(ncantidad <= NumaxPro) {
            ncantidad = ncantidad + 1;
            Totprecio = Totprecio + costproduct;
        }
        else
            ncantidad = NumaxPro;
    }

    public void SetDecreasecantidad()
    {
        if(ncantidad > 0) {
            ncantidad = ncantidad - 1;
            Totprecio = Totprecio - costproduct;
        }else
            ncantidad = 0;
    }

    public void guardarPreferencias (){
        SharedPreferences preferencias = context.getSharedPreferences("DataPedido",Context.MODE_PRIVATE);
        String str_nCantidad = "nCantidad_"+cont;
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putInt(str_nCantidad,ncantidad);
        editor.commit();
        String strTotPrecio = "TotPrecio";
        editor.putFloat(strTotPrecio,(float)Totprecio);
        editor.commit();

    }

}

package com.gyecommerce.zamoraonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MinimaCompra extends AppCompatActivity {

    private int compraMinima;
    private int costeTranporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minima_compra);

        SharedPreferences preferencias = getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        compraMinima = preferencias.getInt("CompraMinima",10);
        costeTranporte = preferencias.getInt("costeTransporte",5);
        TextView txt_min = (TextView)findViewById(R.id.tv_valorMinimo);
        txt_min.setText("USD "+String.format("%.2f",(float)compraMinima));
        TextView txt_trans = (TextView)findViewById(R.id.tv_valorTransporte);
        txt_trans.setText("USD "+String.format("%.2f",(float)costeTranporte));
    }

    public void BtnCancelar (View view)
    {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void BtnVolver (View view)
    {
        Intent intent = new Intent(view.getContext(), Listadocompra.class);
        startActivity(intent);
        finish();
    }

    public void BtnAddTrans (View view)
    {
        Intent intent = new Intent(MinimaCompra.this, ConfirmacionCompra.class);
        SharedPreferences preferencias = getSharedPreferences("DataPedido",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean("AddTrans",true);
        editor.commit();
        intent.putExtra("costeTranporte", costeTranporte);
        startActivity(intent);
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

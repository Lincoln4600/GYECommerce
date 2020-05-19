package com.gyecommerce.zamoraonline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrdenFinalizada extends AppCompatActivity {

    private String Nombre, Apellido;
    private int IDpedido;
    private boolean ordenOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden_finalizada);

        IDpedido = getIntent().getIntExtra("IDPedido", 0);
        Nombre = getIntent().getStringExtra("Nombre");
        Apellido = getIntent().getStringExtra("Apellido");
        ordenOk = getIntent().getBooleanExtra("RegistroOk", false);

        Button btn_ok = (Button) findViewById(R.id.btn_okOrden);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

        Intent intent = new Intent(OrdenFinalizada.this, MainActivity.class);
        startActivity(intent);
        finish();

            }
        });

        TextView tv_message = findViewById(R.id.tv_correct);
        if(ordenOk){
            String message = "Gracias por tu compra \n"
            + Nombre + " " + Apellido + " \n"
            + "tu numero de orden es #" + IDpedido + ". Enseguida procederemos a preparártela.\n";
            tv_message.setText(message);
        }


    }
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

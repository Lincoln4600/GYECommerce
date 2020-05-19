package com.gyecommerce.zamoraonline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.gyecommerce.zamoraonline.ui.Todoproducto.TodoproductoFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;


public class Listadocompra extends AppCompatActivity {

    public static boolean fragment_todoProducto;

    private AppBarConfiguration mAppBarConfiguration;
    private int Numart;
    private boolean Check;
    private Fragment MyFragment;
    public ProgressBar mProgressBar;
    private Menu menu;
    private int numart;
    private int[] NummaxPro;
    private String [] arr_CatProd;
    private boolean dis_dulces, dis_galletas, dis_carnes, dis_frutas, dis_lacteos, dis_cereales, dis_suplementos, dis_otros;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fragment_todoProducto = true;



        MyFragment = getSupportFragmentManager().findFragmentById(R.id.nav_Todoproducto);

        setContentView(R.layout.activity_listadocompra);

        mProgressBar = findViewById(R.id.overlayProgressBar);
        mProgressBar.clearAnimation();
        mProgressBar.setVisibility(View.INVISIBLE);

        Initialize();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_Todoproducto, R.id.nav_Confiteria, R.id.nav_Frutas, R.id.nav_Carnes, R.id.nav_Cereales,R.id.nav_Lacteos, R.id.nav_Suplementos,
                R.id.nav_Enlatados, R.id.nav_Otros, R.id.nav_Contactanos, R.id.nav_TrabajaConNosotros)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(Listadocompra.this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(Listadocompra.this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        TextView tv_Totprecio = findViewById(R.id.tv_TotPrecio);
        tv_Totprecio.getText();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView tv_Totprecio = findViewById(R.id.tv_TotPrecio);
                String str_precioTot = tv_Totprecio.getText().toString();
                SharedPreferences preferencias = getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
                boolean addTrans = preferencias.getBoolean("AddTrans",false);
                int minValue = preferencias.getInt("CompraMinima", 0);
                int costeTranporte = preferencias.getInt("costeTransporte",5);
                String[] arr_strPrecioTot = str_precioTot.split(" ");
                String num = arr_strPrecioTot[1];
                num = num.replaceAll(",",".");
                double num_ = Double.valueOf(num);
                if(num_ == 0){
                    AlertDialog alertDialog = new AlertDialog.Builder(Listadocompra.this).create();
                    alertDialog.setTitle("Aviso");
                    alertDialog.setMessage("No ha realizado ningún pedido. Será dirigido a la pantalla principal");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Listadocompra.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                    alertDialog.show();
                }
                else if (num_ > minValue){
                    if(addTrans){
                        SharedPreferences.Editor editor = preferencias.edit();
                        editor.putBoolean("AddTrans",false);
                        String strTotPrecio = "TotPrecio";
                        editor.putFloat(strTotPrecio,(float)(num_ - costeTranporte));
                        editor.commit();

                    }
                    Intent intent = new Intent(view.getContext(), ConfirmacionCompra.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(view.getContext(), MinimaCompra.class);
                    startActivity(intent);
                }
                /*Intent intent = new Intent(view.getContext(), ConfirmacionCompra.class);
                startActivity(intent);*/
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Actionkjhl", null).show();*/

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void onStart(){
        super.onStart();
    }

    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*AlertDialog alertDialog = new AlertDialog.Builder(Listadocompra.this).create();
        alertDialog.setTitle("Aviso");
        alertDialog.setMessage("Su compra se perderá");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.show();*/
        //finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*AlertDialog alertDialog = new AlertDialog.Builder(Listadocompra.this).create();
        alertDialog.setTitle("Aviso");
        alertDialog.setMessage("Su compra se perderá");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.show();*/
        //finish();
    }
    @Override
    public void onBackPressed() {


        if(fragment_todoProducto) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Está a punto de salir de la aplicación. Su compra se perderá. ¿Está seguro?");

            builder.setPositiveButton("si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Listadocompra.super.onBackPressed();

                }
            });

            builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            //creating alert dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else
            Listadocompra.super.onBackPressed();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

     return true;

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    public void Initialize (){

        SharedPreferences preferencias = getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        numart = preferencias.getInt("numart", 0);
        NummaxPro = new int[numart];
        arr_CatProd = new String[numart];
        for (int i = 0; i <numart; i++)
        {
            String keyCatProd = "CatProd_"+i;
            arr_CatProd[i] = preferencias.getString(keyCatProd, "");
            String keyMaxProd = "ProdMax_"+i;
            NummaxPro[i] = preferencias.getInt(keyMaxProd, 0);

            if(NummaxPro[i] > 0 && (arr_CatProd[i].equals("DULCES") || arr_CatProd[i].equals("CHOCOLATES")))
                dis_dulces = false;
            else
                dis_dulces = true;

            if (NummaxPro[i] > 0 && ((arr_CatProd[i].equals("GALLETAS"))||(arr_CatProd[i].equals("CONSERVAS"))))
                dis_galletas = false;
            else
                dis_galletas = true;

            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("MARISCOS") || arr_CatProd[i].equals("POLLOS") || arr_CatProd[i].equals("CARNES")))
                dis_carnes = false;
            else
                dis_carnes = true;


            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("FRUTAS") || arr_CatProd[i].equals("VERDURAS")))
                dis_frutas = false;
            else
                dis_frutas = true;


            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("LACTEOS")))
                dis_lacteos = false;
            else
                dis_lacteos = true;

            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("ARROZ") || arr_CatProd[i].equals("CEREAL") || arr_CatProd[i].equals("GRANOS")))
                dis_cereales = false;
            else
                dis_cereales = true;


            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("SUPLEMENTOS")))
                dis_suplementos = false;
            else
                dis_suplementos = true;


            if (NummaxPro[i] > 0 && arr_CatProd[i].equals("OTROS"))
                dis_otros = false;
            else
                dis_otros = true;
        }
    }

}

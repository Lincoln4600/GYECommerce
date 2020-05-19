package com.gyecommerce.zamoraonline.ui.Todoproducto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.gyecommerce.zamoraonline.Listadocompra;
import com.gyecommerce.zamoraonline.MainActivity;
import com.gyecommerce.zamoraonline.MyNewOnClickListener;
import com.gyecommerce.zamoraonline.ProgressDialogFragment;
import com.gyecommerce.zamoraonline.R;
import com.gyecommerce.zamoraonline.ui.carnes.CarnesFragment;
import com.gyecommerce.zamoraonline.ui.cereales.CerealesFragment;
import com.gyecommerce.zamoraonline.ui.confiteria.ConfiteriaFragment;
import com.gyecommerce.zamoraonline.ui.enlatados.EnlatadosFragment;
import com.gyecommerce.zamoraonline.ui.frutas.FrutasFragment;
import com.gyecommerce.zamoraonline.ui.lacteos.LacteosFragment;
import com.gyecommerce.zamoraonline.ui.otros.OtrosFragment;
import com.gyecommerce.zamoraonline.ui.suplementos.SuplementosFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.RED;

public class TodoproductoFragment extends Fragment {

    private View root;
    private TodoproductoViewModel todoproductoViewModel;
    private double Totprecio = 0.0;
    private ImageView imgVw;
    private ImageView[] arr_imgVw;
    private Target target;
    private int numart, Constnumcols = 3, numcols, contRows, cont, contProd;
    private int[] NummaxPro, arr_ncantidad;
    private String[] arr_CatProd;
    private double[] arr_costProd;
    private ProgressDialog progressDialog;
    private List<Target> targets;
    private TableRow row;
    private ImageButton[] arr_btnIncrease, arr_btnDecrease;
    private LinearLayout second_layout, proof_layout;
    private boolean ExtraLayout;
    private TableLayout tl_output;
    private MyNewOnClickListener btnClick;
    private TextView tv_cant;
    boolean PreloadImage = false;
    TextView tv_Totprecio;
    private int NumProduct;
    private boolean newTable = true;
    private boolean dis_dulces, dis_galletas, dis_carnes, dis_frutas, dis_lacteos, dis_cereales, dis_suplementos, dis_otros;
    int a = 2;

    public ProgressBar mProgressBar;
    private ImageButton btn_dulces, btn_galletas, btn_carnes, btn_frutas, btn_lacteos, btn_cereales, btn_suplementos, btn_otros;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Listadocompra.fragment_todoProducto = true;
        root = inflater.inflate(R.layout.fragment_todoproducto, container, false);

        btn_dulces = (ImageButton) root.findViewById(R.id.btn_dulces);
        btn_galletas = (ImageButton) root.findViewById(R.id.btn_galletas);
        btn_carnes = (ImageButton) root.findViewById(R.id.btn_carnes);
        btn_frutas = (ImageButton) root.findViewById(R.id.btn_frutas);
        btn_lacteos = (ImageButton) root.findViewById(R.id.btn_lacteos);
        btn_cereales = (ImageButton) root.findViewById(R.id.btn_cereales);
        btn_suplementos = (ImageButton) root.findViewById(R.id.btn_suplementos);
        btn_otros = (ImageButton) root.findViewById(R.id.btn_otros);


        Initialize();
        InitializeFragment();
        FloatingActionButton fab = ((Listadocompra)getActivity()).findViewById(R.id.fab);
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(null); //should disable default animations
        p.setAnchorId(View.NO_ID); //should let you set visibility
        fab.setLayoutParams(p);
        fab.show(); //show
        //fab.hide(); //hide

        LinearLayout s = ((Listadocompra)getActivity()).findViewById(R.id.linearLayout3);
        s.setVisibility(root.VISIBLE);

        btn_dulces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar = ((Listadocompra)getActivity()).findViewById(R.id.overlayProgressBar);
                mProgressBar.setVisibility(View.VISIBLE);

                Navigation.findNavController(root).navigate(R.id.nav_Confiteria);

            }
        });

        btn_galletas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar = ((Listadocompra)getActivity()).findViewById(R.id.overlayProgressBar);
                mProgressBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(root).navigate(R.id.nav_Enlatados);
            }
        });

        btn_carnes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar = ((Listadocompra)getActivity()).findViewById(R.id.overlayProgressBar);
                mProgressBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(root).navigate(R.id.nav_Carnes);
            }
        });

        btn_frutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar = ((Listadocompra)getActivity()).findViewById(R.id.overlayProgressBar);
                mProgressBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(root).navigate(R.id.nav_Frutas);
            }
        });

        btn_lacteos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar = ((Listadocompra)getActivity()).findViewById(R.id.overlayProgressBar);
                mProgressBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(root).navigate(R.id.nav_Lacteos);
            }
        });

        btn_cereales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar = ((Listadocompra)getActivity()).findViewById(R.id.overlayProgressBar);
                mProgressBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(root).navigate(R.id.nav_Cereales);
            }
        });

        btn_suplementos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar = ((Listadocompra)getActivity()).findViewById(R.id.overlayProgressBar);
                mProgressBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(root).navigate(R.id.nav_Suplementos);
            }
        });

        btn_otros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar = ((Listadocompra)getActivity()).findViewById(R.id.overlayProgressBar);
                mProgressBar.setVisibility(View.VISIBLE);
                Navigation.findNavController(root).navigate(R.id.nav_Otros);
            }
        });

        return root;
    }

    public void Initialize (){

        SharedPreferences preferencias = getActivity().getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        numart = preferencias.getInt("numart", 0);
        Totprecio = (double)preferencias.getFloat("TotPrecio", 0);
        arr_CatProd = new String[numart];
        arr_ncantidad = new int[numart];
        NummaxPro = new int[numart];
        for (int i = 0; i <numart; i++)
        {
            String keyCatProd = "CatProd_"+i;
            arr_CatProd[i] = preferencias.getString(keyCatProd, "");
            String str_nCantidad = "nCantidad_"+i;
            arr_ncantidad[i] = preferencias.getInt(str_nCantidad, 0);
            String keyMaxProd = "ProdMax_"+i;
            NummaxPro[i] = preferencias.getInt(keyMaxProd, 0);
        }

        tv_Totprecio = ((Listadocompra)getActivity()).findViewById(R.id.tv_TotPrecio);
        tv_Totprecio.setText("USD "+String.format("%.2f",(float)Totprecio));
    }

    public void InitializeFragment(){

        boolean prod_dulces = false;
        boolean prod_galletas = false;
        boolean prod_carnes = false;
        boolean prod_frutas = false;
        boolean prod_lacteos = false;
        boolean prod_cereales = false;
        boolean prod_suplementos = false;
        boolean prod_otros = false;

        dis_dulces = true;
        dis_galletas = true;
        dis_carnes = true;
        dis_frutas = true;
        dis_lacteos = true;
        dis_cereales = true;
        dis_suplementos = true;
        dis_otros = true;
        for (int i = 0; i < numart; i++) {


            if(NummaxPro[i] > 0 && (arr_CatProd[i].equals("DULCES") || arr_CatProd[i].equals("CHOCOLATES"))) {
                if (arr_ncantidad[i] > 0)
                    prod_dulces = true;
                else
                    btn_dulces.setBackgroundResource(R.drawable.fragment_todoproducto_img_1);
                dis_dulces = false;
            }



            if (NummaxPro[i] > 0 && ((arr_CatProd[i].equals("ENLATADOS"))||(arr_CatProd[i].equals("CONSERVAS")))) {
                if (arr_ncantidad[i] > 0)
                    prod_galletas = true;
                else
                    btn_galletas.setBackgroundResource(R.drawable.fragment_todoproducto_img_4);
                dis_galletas = false;
            }



            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("MARISCOS") || arr_CatProd[i].equals("POLLOS") || arr_CatProd[i].equals("CARNES"))) {
                if (arr_ncantidad[i] > 0)
                    prod_carnes = true;
                else
                    btn_carnes.setBackgroundResource(R.drawable.fragment_todoproducto_img_5);
                dis_carnes = false;
            }


            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("FRUTAS") || arr_CatProd[i].equals("VERDURAS"))) {
                if (arr_ncantidad[i] > 0)
                    prod_frutas = true;
                else
                    btn_frutas.setBackgroundResource(R.drawable.fragment_todoproducto_img_2);
                dis_frutas = false;
            }


            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("LACTEOS"))) {
                if (arr_ncantidad[i] > 0)
                    prod_lacteos = true;
                else
                    btn_lacteos.setBackgroundResource(R.drawable.fragment_todoproducto_img_7);
                dis_lacteos = false;
            }

            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("ARROZ") || arr_CatProd[i].equals("GALLETAS") || arr_CatProd[i].equals("CEREAL") || arr_CatProd[i].equals("GRANOS"))) {
                if (arr_ncantidad[i] > 0)
                    prod_cereales = true;
                else
                    btn_cereales.setBackgroundResource(R.drawable.fragment_todoproducto_img_3);
                dis_cereales = false;
            }


            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("SUPLEMENTOS"))) {
                if (arr_ncantidad[i] > 0)
                    prod_suplementos = true;
                else
                    btn_suplementos.setBackgroundResource(R.drawable.fragment_todoproducto_img_8);
                dis_suplementos = false;
            }


            if (NummaxPro[i] > 0 && arr_CatProd[i].equals("OTROS")) {
                if (arr_ncantidad[i] > 0)
                    prod_otros = true;
                else
                    btn_otros.setBackgroundResource(R.drawable.fragment_todoproducto_img_6);
                dis_otros = false;
            }
        }

        if(dis_dulces)
            btn_dulces.setBackgroundResource(R.drawable.fragment_todoproducto_off_img_1);

        if(dis_galletas)
            btn_galletas.setBackgroundResource(R.drawable.fragment_todoproducto_off_img_4);

        if(dis_carnes)
            btn_carnes.setBackgroundResource(R.drawable.fragment_todoproducto_off_img_5);

        if(dis_frutas)
            btn_frutas.setBackgroundResource(R.drawable.fragment_todoproducto_off_img_2);

        if(dis_lacteos)
            btn_lacteos.setBackgroundResource(R.drawable.fragment_todoproducto_off_img_7);

        if(dis_cereales)
            btn_cereales.setBackgroundResource(R.drawable.fragment_todoproducto_off_img_3);

        if(dis_suplementos)
            btn_suplementos.setBackgroundResource(R.drawable.fragment_todoproducto_off_img_8);

        if(dis_otros)
            btn_otros.setBackgroundResource(R.drawable.fragment_todoproducto_off_img_6);

        if(prod_dulces)
            btn_dulces.setBackgroundResource(R.drawable.fragment_todoproducto_on_img_1);

        if(prod_galletas)
            btn_galletas.setBackgroundResource(R.drawable.fragment_todoproducto_on_img_4);

        if(prod_carnes)
            btn_carnes.setBackgroundResource(R.drawable.fragment_todoproducto_on_img_5);

        if(prod_frutas)
            btn_frutas.setBackgroundResource(R.drawable.fragment_todoproducto_on_img_2);

        if(prod_lacteos)
            btn_lacteos.setBackgroundResource(R.drawable.fragment_todoproducto_on_img_7);

        if(prod_cereales)
            btn_cereales.setBackgroundResource(R.drawable.fragment_todoproducto_on_img_3);

        if(prod_suplementos)
            btn_suplementos.setBackgroundResource(R.drawable.fragment_todoproducto_on_img_8);

        if(prod_otros)
            btn_otros.setBackgroundResource(R.drawable.fragment_todoproducto_on_img_6);
    }
}
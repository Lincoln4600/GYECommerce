package com.gyecommerce.zamoraonline.ui.cereales;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gyecommerce.zamoraonline.Listadocompra;
import com.gyecommerce.zamoraonline.MyNewOnClickListener;
import com.gyecommerce.zamoraonline.R;
import com.gyecommerce.zamoraonline.ui.Todoproducto.TodoproductoViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


public class CerealesFragment extends Fragment {

    private View root;
    private TodoproductoViewModel todoproductoViewModel;
    private double Totprecio = 0.0;
    private ImageView imgVw;
    private ImageView [] arr_imgVw;
    private Target target;
    private int numart, Constnumcols = 3, numcols, contRows, cont, contProd;
    private int[] NummaxPro, arr_ncantidad;
    private String [] arr_TagtextCant, arr_TagLay2, image_descr, arr_prodName, arr_pathImg, arr_CatProd;
    private double [] arr_costProd;
    private ProgressDialog progressDialog;
    private List<Target> targets;
    private TableRow row;
    private ImageButton[]  arr_btnIncrease, arr_btnDecrease;
    private LinearLayout second_layout, proof_layout;
    private LinearLayout[] arr_second_layout;
    private TextView[] arr_cant_pro;
    private boolean ExtraLayout;
    private TableLayout tl_output;
    private MyNewOnClickListener btnClick;
    private TextView tv_cant;
    boolean PreloadImage = false;
    TextView tv_Totprecio;
    private boolean newTable = true;
    private int NumProduct;
    public ProgressBar mProgressBar;
    int a;
    private boolean dist_cereales;


    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Listadocompra.fragment_todoProducto = false;

        root = inflater.inflate(R.layout.fragment_cereales, container, false);

        mProgressBar = ((Listadocompra)getActivity()).findViewById(R.id.overlayProgressBar);

        FloatingActionButton fab = ((Listadocompra)getActivity()).findViewById(R.id.fab);
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(null); //should disable default animations
        p.setAnchorId(View.NO_ID); //should let you set visibility
        fab.setLayoutParams(p);
        fab.show(); //show
        //fab.hide(); //hide

        LinearLayout s = ((Listadocompra)getActivity()).findViewById(R.id.linearLayout3);
        s.setVisibility(root.VISIBLE);

        return root;
    }

    @Override
    public void onActivityCreated  (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FillNumMaxProd();
        FillTagtName();
        FillProductDescr();
        FillProductName();
        FillCostProduct();
        FillImageArray();
        FillCatArray();
        GetNumberProductConf();

        if(dist_cereales){
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Aviso");
            alertDialog.setMessage("Por el momento no tenemos producto en esta categoria");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Navigation.findNavController(root).navigate(R.id.nav_Todoproducto);
                        }
                    });
            alertDialog.show();

        }



        arr_ncantidad = new int[numart];
        arr_imgVw = new ImageView[numart];
        arr_btnIncrease = new ImageButton[numart];
        arr_btnDecrease = new ImageButton[numart];
        targets = new ArrayList<Target>();
        tl_output = root.findViewById(R.id.tl_cereales);

        cont = 0;
        contProd = 0;
        contRows = 0;
        row = new TableRow(getActivity());
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                .0f);
        row.setLayoutParams(lp);
        for (int i = 0; i <numart; i++) {

            final int k = i;
            final int j = contProd;
            target = new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                    if (PreloadImage) {
                        arr_imgVw[k].setImageBitmap(bitmap);
                        if (j == (NumProduct - 1)) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (NummaxPro[k] > 0 && (arr_CatProd[k].equals("ARROZ") || arr_CatProd[k].equals("GALLETAS") || arr_CatProd[k].equals("CEREAL") || arr_CatProd[k].equals("GRANOS")))
                        {
                            FillTableLayout(k);
                            row.addView(second_layout);
                            arr_imgVw[k].setImageBitmap(bitmap);
                            if (j == (NumProduct - 1)) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    PreloadImage = true;
                    if (NummaxPro[k] > 0 && (arr_CatProd[k].equals("ARROZ") || arr_CatProd[k].equals("GALLETAS") || arr_CatProd[k].equals("CEREAL") || arr_CatProd[k].equals("GRANOS")))
                    {
                        FillTableLayout(k);
                        row.addView(second_layout);
                    }
                }
            };

            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("ARROZ") || arr_CatProd[i].equals("GALLETAS") ||arr_CatProd[i].equals("CEREAL") || arr_CatProd[i].equals("GRANOS")))
            {

                String url = arr_pathImg[k];
                Picasso.get().load(url).into(target);
                targets.add(target);
                contProd++;

                if(cont >= Constnumcols - 1){
                    tl_output.addView(row, contRows);
                    row = new TableRow(getActivity());
                    lp = new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT,
                            .0f);
                    row.setLayoutParams(lp);
                    cont = 0;
                    contRows++;
                }
                else{
                    cont++;
                }
            }

        }
        if (cont == (Constnumcols)) {
            numcols = Constnumcols;
        } else {
            ExtraLayout = true;
            numcols = cont;
        }

        if(ExtraLayout)
        {
            int nblanks = Constnumcols - numcols;
            for (int j = 0; j <nblanks; j++) {
                second_layout = new LinearLayout(getActivity());
                TableRow.LayoutParams paramLinearLayout2 = new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f);
                second_layout.setLayoutParams(paramLinearLayout2);
                second_layout.setOrientation(LinearLayout.VERTICAL);
                TextView tv_blank = new TextView(getActivity());
                LinearLayout.LayoutParams paramTxtBlank = new LinearLayout.LayoutParams(
                        0,
                        0,
                        1.0f);
                tv_blank.setLayoutParams(paramTxtBlank);
                second_layout.addView(tv_blank);
                row.addView(second_layout);
            }
            tl_output.addView(row, contRows);
        }

        InitializeTextView (root);
        for (int i = 0; i <numart; i++)
        {
            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("ARROZ") || arr_CatProd[i].equals("GALLETAS") || arr_CatProd[i].equals("CEREAL") || arr_CatProd[i].equals("GRANOS")))
            {
                btnClick = new MyNewOnClickListener(Totprecio, i, root, arr_TagLay2[i], arr_TagtextCant[i], arr_btnIncrease[i], arr_btnDecrease[i], getActivity(), NummaxPro[i], arr_costProd[i], arr_ncantidad[i], tv_Totprecio);
                arr_btnIncrease[i].setOnClickListener(btnClick);
                arr_btnDecrease[i].setOnClickListener(btnClick);
            }

        }

    }

    public void onStart () {
        super.onStart();
    }

    public void onStop(){
        super.onStop();

    }

    public void onDestroyView(){
        super.onDestroyView();

    }

    public void onDestroy(){
        super.onDestroy();
    }

    public void onResume (){
        super.onResume();
    }

    public void FillTableLayout (final int k){


        second_layout = new LinearLayout(getActivity());
        TableRow.LayoutParams paramLinearLayout2 = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f);
        second_layout.setBackgroundColor(Color.RED);
        second_layout.setLayoutParams(paramLinearLayout2);
        second_layout.setOrientation(LinearLayout.VERTICAL);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(3, Color.BLACK);
        drawable.setCornerRadius(8);
        second_layout.setBackgroundDrawable(drawable);
        second_layout.setTag(arr_TagLay2[k]);

        arr_imgVw[k] = new ImageView(getActivity());
        LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(
                200,
                200,
                1.0f);
        paramsImage.setMargins(2, 2, 2, 2);
        paramsImage.gravity = Gravity.CENTER_VERTICAL;
        paramsImage.gravity = Gravity.CENTER_HORIZONTAL;
        arr_imgVw[k].setLayoutParams(paramsImage);

        LinearLayout first_layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams paramLinearLayout1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        paramLinearLayout1.gravity = Gravity.CENTER_VERTICAL;
        paramLinearLayout1.gravity = Gravity.CENTER_HORIZONTAL;
        first_layout.setLayoutParams(paramLinearLayout1);
        first_layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton btn_increase = new ImageButton(getActivity());
        btn_increase.setImageResource(android.R.drawable.arrow_up_float);
        LinearLayout.LayoutParams paramsBtnIncrease = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        btn_increase.setLayoutParams(paramsBtnIncrease);
        arr_btnIncrease[k] = btn_increase;

        TextView cant_pro = new TextView(getActivity());
        LinearLayout.LayoutParams paramCantPro = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        paramCantPro.setMargins(0,0,0,2);
        cant_pro.setPadding(10,0,10,0);
        cant_pro.setTag(arr_TagtextCant[k]);
        cant_pro.setTextColor(Color.BLACK);
        cant_pro.setGravity(Gravity.CENTER);
        cant_pro.setTextSize(15);
        paramCantPro.gravity = Gravity.CENTER;
        GradientDrawable drawabletext = new GradientDrawable();
        drawabletext.setShape(GradientDrawable.RECTANGLE);
        drawabletext.setStroke(3, Color.rgb(0,191,255));
        drawabletext.setCornerRadius(8);
        cant_pro.setBackgroundDrawable(drawabletext);
        cant_pro.setLayoutParams(paramCantPro);
        arr_cant_pro[k] = cant_pro;


        ImageButton btn_decrease = new ImageButton(getActivity());
        btn_decrease.setImageResource(android.R.drawable.arrow_down_float);
        LinearLayout.LayoutParams paramsBtnDecrease = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        btn_decrease.setLayoutParams(paramsBtnDecrease);
        arr_btnDecrease[k] = btn_decrease;

        TextView pro_name = new TextView(getActivity());
        LinearLayout.LayoutParams paramProName = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        pro_name.setLayoutParams(paramProName);
        pro_name.setTextColor(Color.BLACK);
        pro_name.setText(arr_prodName[k]);
        paramProName.setMargins(10,0,0,0);

        TextView pro_desc = new TextView(getActivity());
        LinearLayout.LayoutParams paramProDesc = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        pro_desc.setLayoutParams(paramProDesc);
        pro_desc.setText(image_descr[k]);
        paramProDesc.setMargins(10,0,0,0);

        TextView Cost_name = new TextView(getActivity());
        LinearLayout.LayoutParams paramCostName = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        Cost_name.setLayoutParams(paramCostName);
        Cost_name.setText("$"+String.format("%.2f",(float)arr_costProd[k]));
        paramCostName.setMargins(30,0,0,0);
        Cost_name.setTypeface(null, Typeface.BOLD);
        Cost_name.setTextSize(20);
        Cost_name.setTextColor(Color.BLACK);

        first_layout.addView(btn_decrease);
        first_layout.addView(cant_pro);
        first_layout.addView(btn_increase);
        second_layout.addView(arr_imgVw[k]);
        second_layout.addView(pro_name);
        second_layout.addView(pro_desc);
        second_layout.addView(Cost_name);
        second_layout.addView(first_layout);
        arr_second_layout[k] = second_layout;

    }

    public void FillProductDescr (){
        image_descr = new String[numart]; //declare with size
        SharedPreferences preferencias = getActivity().getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        for (int i = 0; i <numart; i++)
        {
            String key_descr = "ProdDescr_"+i;
            image_descr[i] = preferencias.getString(key_descr, "NO tiene descripcion");
        }

    }

    public void FillProductName (){


        SharedPreferences preferencias = getActivity().getSharedPreferences("DataPedido", Context.MODE_PRIVATE);

        numart = preferencias.getInt("numart", 0);
        arr_prodName = new String[numart];
        for (int i = 0; i <numart; i++)
        {
            String key_nombre = "Prod_"+i;
            arr_prodName[i] = preferencias.getString(key_nombre, "NO tiene nombre");
        }

    }

    public void FillCostProduct(){

        SharedPreferences preferencias = getActivity().getSharedPreferences("DataPedido", Context.MODE_PRIVATE);

        numart = preferencias.getInt("numart", 0);
        arr_costProd = new double[numart];
        for (int i = 0; i <numart; i++)
        {
            String keyprodcost = "ProdCost_"+i;
            arr_costProd[i] = (double)preferencias.getFloat(keyprodcost, 0);
        }
    }

    public void FillTagtName (){
        String TagTextName, TagLayout2Name;
        arr_TagtextCant = new String[numart];
        arr_TagLay2 = new String[numart];
        arr_second_layout = new LinearLayout[numart];
        arr_cant_pro = new TextView[numart];
        for (int i = 0; i <numart; i++)
        {
            TagLayout2Name = "ly_ID_Conf_"+ String.valueOf(i);
            TagTextName = "Tv_ID_Conf_" + String.valueOf(i);
            arr_TagtextCant[i] = TagTextName;
            arr_TagLay2[i] = TagLayout2Name;
        }
    }

    public void FillNumMaxProd (){

        SharedPreferences preferencias = getActivity().getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        numart = preferencias.getInt("numart", 0);
        NummaxPro = new int[numart];
        for (int i = 0; i <numart; i++)
        {
            String keyMaxProd = "ProdMax_"+i;
            NummaxPro[i] = preferencias.getInt(keyMaxProd, 0);
        }
    }

    public void FillImageArray (){
        arr_pathImg = new String[numart];
        SharedPreferences preferencias = getActivity().getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        for (int i = 0; i <numart; i++)
        {
            String keyPathImg = "PathImg_"+i;
            arr_pathImg[i] = preferencias.getString(keyPathImg, "");
        }
    }

    public void FillCatArray (){
        arr_CatProd = new String[numart];
        SharedPreferences preferencias = getActivity().getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        for (int i = 0; i <numart; i++)
        {
            String keyCatProd = "CatProd_"+i;
            arr_CatProd[i] = preferencias.getString(keyCatProd, "");
        }
    }

    public void GetNumberProductConf() {

        NumProduct = 0;
        dist_cereales = true;
        for (int i = 0; i < numart; i++) {
            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("ARROZ") || arr_CatProd[i].equals("GALLETAS") || arr_CatProd[i].equals("CEREAL") || arr_CatProd[i].equals("GRANOS")))
            {
                dist_cereales = false;
                NumProduct++;
            }
        }
    }

    public void InitializeTextView (View view) {

        LinearLayout secondLayout = new LinearLayout(view.getContext());
        SharedPreferences preferencias = getActivity().getSharedPreferences("DataPedido",Context.MODE_PRIVATE);
        Totprecio = (double)preferencias.getFloat("TotPrecio", 0);
        for (int i = 0; i < numart; i++) {

            if (NummaxPro[i] > 0 && (arr_CatProd[i].equals("ARROZ") || arr_CatProd[i].equals("GALLETAS") || arr_CatProd[i].equals("CEREAL") || arr_CatProd[i].equals("GRANOS")))
            {
                secondLayout = arr_second_layout[i];
                String str_nCantidad = "nCantidad_"+i;
                arr_ncantidad[i] = preferencias.getInt(str_nCantidad, 0);
                tv_cant = arr_cant_pro[i];
                tv_cant.setText(Integer.toString(arr_ncantidad[i]));

                if(arr_ncantidad[i]>0) {
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
            }
        }
        tv_Totprecio = ((Listadocompra)getActivity()).findViewById(R.id.tv_TotPrecio);
        tv_Totprecio.setText("USD "+String.format("%.2f",(float)Totprecio));
    }
}
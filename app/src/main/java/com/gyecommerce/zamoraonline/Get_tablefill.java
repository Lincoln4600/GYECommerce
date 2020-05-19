package com.gyecommerce.zamoraonline;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class Get_tablefill {

    Context context;
    private String[] image_descr, arr_TagtextCant, arr_TagLay2, arr_prodName, arr_pathImg;
    private ImageButton[]  arr_btnIncrease, arr_btnDecrease;
    private int NummaxPro[];
    private int numart, Constnumcols = 3, numrows, numcols;
    private ImageView imgVw;
    private TableRow row;
    private int cont;
    private LinearLayout second_layout;
    private boolean ExtraLayout;
    private double[] arr_costProd;
    boolean Check = true;
    private View root;
    private Bitmap [] ImageIcon;
    RequestQueue requestQueue;

    public Get_tablefill(View root, Context context, int numart){
        this.context=context;
        this.numart = numart;
        this.root = root;
    }

    public TableLayout fillTable (){

        TableLayout tl_output = root.findViewById(R.id.tl_enlatados);
        tl_output.setWeightSum((float)numrows);
        FillImageArray();
        FillProductDescr();
        FillProductName();
        FillTagtName();
        FillNumMaxProd();
        FillCostProduct();
        arr_btnIncrease = new ImageButton[numart];
        arr_btnDecrease = new ImageButton[numart];

        numrows = (int)Math.ceil((double)numart/(double)Constnumcols);
        cont = 0;
        for (int i = 0; i <numrows; i++) {
            row = new TableRow(context);
            FillTableRow();
            tl_output.addView(row, i);
        }
        return tl_output;

    }

    public void FillTableRow ()
    {

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f);
        row.setLayoutParams(lp);

        if((numart-cont)>3)
        {
            numcols = Constnumcols;
        }
        else
        {
            ExtraLayout = true;
            numcols = numart - cont;
        }

        for (int i = 0; i <numcols; i++)
        {
            second_layout = new LinearLayout(context);
            TableRow.LayoutParams paramLinearLayout2 = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    4.0f);
            second_layout.setBackgroundColor(Color.RED);
            second_layout.setLayoutParams(paramLinearLayout2);
            second_layout.setOrientation(LinearLayout.VERTICAL);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(3, Color.BLACK);
            drawable.setCornerRadius(8);
            second_layout.setBackgroundDrawable(drawable);
            second_layout.setTag(arr_TagLay2[cont]);

            TextView pro_name = new TextView(context);
            LinearLayout.LayoutParams paramProName = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f);
            pro_name.setLayoutParams(paramProName);
            pro_name.setTextColor(Color.BLACK);
            pro_name.setText(arr_prodName[cont]);
            paramProName.setMargins(10,0,0,0);

            TextView pro_desc = new TextView(context);
            LinearLayout.LayoutParams paramProDesc = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f);
            pro_desc.setLayoutParams(paramProDesc);
            pro_desc.setText(image_descr[cont]);
            paramProDesc.setMargins(10,0,0,0);

            TextView Cost_name = new TextView(context);
            LinearLayout.LayoutParams paramCostName = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f);
            Cost_name.setLayoutParams(paramCostName);
            Cost_name.setText("$"+String.format("%.2f",(float)arr_costProd[cont]));
            paramCostName.setMargins(30,0,0,0);
            Cost_name.setTypeface(null, Typeface.BOLD);
            Cost_name.setTextSize(20);
            Cost_name.setTextColor(Color.BLACK);

            imgVw = new ImageView(context);
            LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(
                    200,
                    200,
                    1.0f);
            paramsImage.setMargins(2,2,2,2);
            paramsImage.gravity = Gravity.CENTER_VERTICAL;
            paramsImage.gravity = Gravity.CENTER_HORIZONTAL;
            imgVw.setLayoutParams(paramsImage);

            //String url = arr_pathImg[0];
            //Picasso.get().load(R.drawable.logo_app).into(imgVw);
            //imgVw.setImageResource(R.drawable.logo_app);
            imgVw = LoadImgImView(imgVw, cont);

            LinearLayout first_layout = new LinearLayout(context);
            LinearLayout.LayoutParams paramLinearLayout1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f);
            paramLinearLayout1.gravity = Gravity.CENTER_VERTICAL;
            paramLinearLayout1.gravity = Gravity.CENTER_HORIZONTAL;
            first_layout.setLayoutParams(paramLinearLayout1);
            first_layout.setOrientation(LinearLayout.HORIZONTAL);

                ImageButton btn_increase = new ImageButton(context);
                btn_increase.setImageResource(android.R.drawable.arrow_up_float);
                LinearLayout.LayoutParams paramsBtnIncrease = new LinearLayout.LayoutParams(
                        60,
                        60, 1.0f);
                btn_increase.setLayoutParams(paramsBtnIncrease);
                arr_btnIncrease[cont] = btn_increase;

                TextView cant_pro = new TextView(context);
                LinearLayout.LayoutParams paramCantPro = new LinearLayout.LayoutParams(
                        100,
                        60,
                        1.0f);
                paramCantPro.setMargins(0,0,0,2);
                cant_pro.setTag(arr_TagtextCant[cont]);
                cant_pro.setTextColor(Color.BLACK);
                cant_pro.setGravity(Gravity.CENTER);
                paramCantPro.gravity = Gravity.CENTER;
                GradientDrawable drawabletext = new GradientDrawable();
                drawabletext.setShape(GradientDrawable.RECTANGLE);
                drawabletext.setStroke(3, Color.rgb(0,191,255));
                drawabletext.setCornerRadius(8);
                cant_pro.setBackgroundDrawable(drawabletext);
                cant_pro.setLayoutParams(paramCantPro);


                ImageButton btn_decrease = new ImageButton(context);
                btn_decrease.setImageResource(android.R.drawable.arrow_down_float);
                LinearLayout.LayoutParams paramsBtnDecrease = new LinearLayout.LayoutParams(
                        60,
                        60, 1.0f);
                btn_decrease.setLayoutParams(paramsBtnDecrease);
                arr_btnDecrease[cont] = btn_decrease;

                first_layout.addView(btn_decrease);
                first_layout.addView(cant_pro);
                first_layout.addView(btn_increase);

            second_layout.addView(imgVw);
            second_layout.addView(pro_name);
            second_layout.addView(pro_desc);
            second_layout.addView(Cost_name);
            second_layout.addView(first_layout);
            row.addView(second_layout);
            cont++;


        }
        if(ExtraLayout)
        {
            int nblanks = Constnumcols - numcols;
            for (int j = 0; j <nblanks; j++) {
                second_layout = new LinearLayout(context);
                TableRow.LayoutParams paramLinearLayout2 = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f);
                second_layout.setLayoutParams(paramLinearLayout2);
                second_layout.setOrientation(LinearLayout.VERTICAL);
                TextView tv_blank = new TextView(context);
                LinearLayout.LayoutParams paramTxtBlank = new LinearLayout.LayoutParams(
                        0,
                        0,
                        1.0f);
                tv_blank.setLayoutParams(paramTxtBlank);
                second_layout.addView(tv_blank);
                row.addView(second_layout);
            }
        }
    }

    public void FillImageArray (){
        arr_pathImg = new String[numart];
        SharedPreferences preferencias = context.getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        for (int i = 0; i <numart; i++)
        {
            String keyPathImg = "PathImg_"+i;
            arr_pathImg[i] = preferencias.getString(keyPathImg, "");
        }
    }

    public void FillProductDescr (){
        String Description;
        image_descr = new String[numart]; //declare with size
        SharedPreferences preferencias = context.getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        for (int i = 0; i <numart; i++)
        {
            String key_descr = "ProdDescr_"+i;
            image_descr[i] = preferencias.getString(key_descr, "NO tiene descripcion");
        }

    }

    public void FillProductName (){


        SharedPreferences preferencias = context.getSharedPreferences("DataPedido", Context.MODE_PRIVATE);

        numart = preferencias.getInt("numart", 0);
        arr_prodName = new String[numart];
        for (int i = 0; i <numart; i++)
        {
            String key_nombre = "Prod_"+i;
            arr_prodName[i] = preferencias.getString(key_nombre, "NO tiene nombre");
        }

    }

    public void FillCostProduct(){

        SharedPreferences preferencias = context.getSharedPreferences("DataPedido", Context.MODE_PRIVATE);

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
        for (int i = 0; i <numart; i++)
        {
            TagLayout2Name = "ly_ID_"+ String.valueOf(i+1);
            TagTextName = "Tv_ID_" + String.valueOf(i+1);
            arr_TagtextCant[i] = TagTextName;
            arr_TagLay2[i] = TagLayout2Name;
        }
    }

    public void FillNumMaxProd (){

        SharedPreferences preferencias = context.getSharedPreferences("DataPedido", Context.MODE_PRIVATE);

        numart = preferencias.getInt("numart", 0);
        NummaxPro = new int[numart];
        for (int i = 0; i <numart; i++)
        {
            String keyMaxProd = "ProdMax_"+i;
            NummaxPro[i] = preferencias.getInt(keyMaxProd, 0);
        }
    }

    public ImageButton[] ReturnBtnIncrease(){
        return arr_btnIncrease;
    }
    public ImageButton[] ReturnBtnDecrease(){
        return arr_btnDecrease;
    }
    public int[] ReturnNummaxPro() {return NummaxPro;}
    public String[] ReturnTagTextName(){return arr_TagtextCant;}
    public String[] ReturnTagLayoutName(){return arr_TagLay2;}
    public double[] ReturnCostProduct(){return arr_costProd;}

    public ImageView LoadImgImView (ImageView imgview_entry, int cont){

        ImageView imgview_exit = imgview_entry;
        String url;
        if (cont > 2)
            url = arr_pathImg[0];
        else
            url = arr_pathImg[cont];

        new Picasso.Builder(context).build();
        Picasso.get().load(url).into(imgview_exit);

        //Picasso.get().load(url).into(new MyTarget(imgview_entry));

        return imgview_exit;
    }


}

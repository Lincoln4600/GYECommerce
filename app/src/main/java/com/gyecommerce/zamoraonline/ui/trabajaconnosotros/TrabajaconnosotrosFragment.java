package com.gyecommerce.zamoraonline.ui.trabajaconnosotros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gyecommerce.zamoraonline.Listadocompra;
import com.gyecommerce.zamoraonline.MyNewOnClickListener;
import com.gyecommerce.zamoraonline.R;
import com.gyecommerce.zamoraonline.ui.Todoproducto.TodoproductoViewModel;
import com.squareup.picasso.Target;

import java.util.List;

public class TrabajaconnosotrosFragment extends Fragment {

    private View root;
    private TodoproductoViewModel todoproductoViewModel;
    private double Totprecio = 0.0;
    private ImageView imgVw;
    private ImageView[] arr_imgVw;
    private Target target;
    private int numart, Constnumcols = 3, numcols, contRows, cont, contProd;
    private int[] NummaxPro, arr_ncantidad;
    private String[] arr_TagtextCant, arr_TagLay2, image_descr, arr_prodName, arr_pathImg;
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
    int a = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Listadocompra.fragment_todoProducto = false;

        root = inflater.inflate(R.layout.fragment_trabajaconnosotros, container, false);

        FloatingActionButton fab = ((Listadocompra)getActivity()).findViewById(R.id.fab);

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(null); //should disable default animations
        p.setAnchorId(View.NO_ID); //should let you set visibility
        fab.setLayoutParams(p);
        //fab.show(); //show
        fab.hide(); //hide

        LinearLayout s = ((Listadocompra)getActivity()).findViewById(R.id.linearLayout3);
        s.setVisibility(root.GONE);

        TextView txt_whatssap = root.findViewById(R.id.txt_whatssap_tr);
        SharedPreferences preferencias = getActivity().getSharedPreferences("DataPedido", Context.MODE_PRIVATE);
        String numWhatssap = preferencias.getString("numWhatssap", "");
        txt_whatssap.setText(numWhatssap);

        TextView txt_mail = root.findViewById(R.id.txt_mail_tr);
        String mailContacto = preferencias.getString("mailContacto", "");
        txt_mail.setText(mailContacto);

        TextView txt_web = root.findViewById(R.id.txt_web_tr);
        String webpage = preferencias.getString("webpage", "");
        txt_web.setText(webpage);


        return root;
    }
}
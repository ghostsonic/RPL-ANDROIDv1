package com.example.lachewendy.rpl_androidv1;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uny2.clases.DatosConfiguracion;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.lachewendy.rpl_androidv1.R.color.colorPrimary;

public class MenuRPLActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    TextView txtTitutuloToolbar;
    TextView txtUsuarioToolbar;
    ImageView imgMenu;
    private ImageButton entrada;
    private ImageButton ubicacion;
    private ImageButton recoleccion;
    private ImageButton conte;
    private ImageButton salidas;
  //  private ImageButton perfiles;
   // private ImageButton configuracion;
    private LinearLayout lnSalidas;
    //DATOS de LOGIN
    private String usuario;
    private int idUsuario;
    private ArrayList<String> permisos;
    private String perfil;
    static final int tiempoPeticion = 35000;
    private TableRow t1,t2,t3,t4,t5;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, this.MODE_PRIVATE);
        //System.out.println("USUARIO->>>"+prefs.getString("usuario",""));
        try {
            //Obtencion de Datos de inicio de sesion.
            usuario=prefs.getString("usuario", "").toUpperCase();
            perfil=prefs.getString("perfil","").toUpperCase();

            idUsuario = Integer.parseInt(prefs.getString("id", ""));
            DatosConfiguracion.idUsuario =prefs.getString("id", "");
            DatosConfiguracion.almacen =prefs.getString("almacen","");
            DatosConfiguracion.numEmpleado =prefs.getString("numeroempleado","");
            permisos = new ArrayList<>(Arrays.asList(prefs.getString("permisos", "").split(",")));
           //Seteando el titulo de ActionBar y obtencion de datos pasados de la actividad anterior
           // this.setTitle("MENU DE "+perfil);
            setContentView(R.layout.activity_menurpl);

            //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
             //actionBar.setSubtitle(Html.fromHtml("<font color='#e8e8e8'>" + "USUARIO:" + usuario + "</font>"));
            //obtencion de referencia de elementos de layout por id y asignacion de evento onclicklistener
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            txtTitutuloToolbar = (TextView) findViewById(R.id.txtPrincipalToolbar);
            txtUsuarioToolbar = (TextView) findViewById(R.id.txtUsuariotoolbar);
            imgMenu = (ImageView) findViewById(R.id.imgMenu);
            t1 = (TableRow) findViewById(R.id.tr1);
            t2 = (TableRow) findViewById(R.id.tr2);
            t3 = (TableRow) findViewById(R.id.tr3);
            t4 = (TableRow) findViewById(R.id.tr4);
            t5 = (TableRow) findViewById(R.id.tr5);
            txtTitutuloToolbar.setText("MENU DE " + perfil);
            txtUsuarioToolbar.setText(Html.fromHtml("<font color='#e8e8e8'>" + "USUARIO:" + usuario + "</font>"));
            imgMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] items = getResources().getStringArray(R.array.opciones_menu);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MenuRPLActivity.this);
                    builder.setTitle("Seleccione una opción");
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    });
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                    openQuitDialog();
                                   /* Intent intent = new Intent().setClass(
                                            MenuRPLActivity.this, PerfilesActivity.class);
                                    startActivity(intent);*/
                                    break;
                                /*case 1:
                                    openQuitDialog();
                                    break;*/
                            }

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getWindow();
                }
            });

            entrada = (ImageButton) findViewById(R.id.btnEntradas);
            entrada.setOnClickListener(this);
            entrada.setVisibility(View.GONE);

            salidas = (ImageButton) findViewById(R.id.btnSalidas);
            salidas.setOnClickListener(this);
            salidas.setVisibility(View.GONE);

            ubicacion = (ImageButton) findViewById(R.id.btnUbicacion);
            ubicacion.setOnClickListener(this);
            ubicacion.setVisibility(View.GONE);
            recoleccion = (ImageButton) findViewById(R.id.btnRecoleccion);
            recoleccion.setOnClickListener(this);
            recoleccion.setVisibility(View.GONE);
            conte = (ImageButton) findViewById(R.id.btnConteo);
            conte.setOnClickListener(this);
            conte.setVisibility(View.GONE);
            /*perfiles = (ImageButton) findViewById(R.id.btnPerfiles);
            perfiles.setOnClickListener(this);
            perfiles.setVisibility(View.GONE);
            configuracion = (ImageButton) findViewById(R.id.btnConfig);
            configuracion.setOnClickListener(this);
            configuracion.setVisibility(View.GONE);*/
            despliegueOpciones();
        }catch (Exception ad){
            ad.printStackTrace();
        }

    }
    public void despliegueOpciones(){
        if(permisos.get(0).equals("1")){
            entrada.setVisibility(View.VISIBLE);
        }else{
            t1.setVisibility(View.GONE);
        }
        if(permisos.get(1).equals("1")){
            ubicacion.setVisibility(View.VISIBLE);
        }else{
            t3.setVisibility(View.GONE);
        }
        if(permisos.get(2).equals("1")){
            recoleccion.setVisibility(View.VISIBLE);
        }else{
            t4.setVisibility(View.GONE);
        }
        if(permisos.get(3).equals("1")){
            conte.setVisibility(View.VISIBLE);
        }else{
            t5.setVisibility(View.GONE);
        }
        if(permisos.get(5).equals("1")){
            salidas.setVisibility(View.VISIBLE);
        }else{
            t2.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.salirApp:
                openQuitDialog();
                break;
        }
        return true;
    }
    private void openQuitDialog(){
        AlertDialog.Builder quitDialog
                = new AlertDialog.Builder(MenuRPLActivity.this);
        quitDialog.setTitle("¿Quieres salir?");

        quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
               logout(3);
            }});

        quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }});

        quitDialog.show();
    }


    public void logout(int paso) {
        try {
            final JSONObject JSONobj = new JSONObject();
            JSONobj.clear();
            JSONArray JSONarr = new JSONArray();
            JSONobj.put("paso", paso);
            JSONobj.put("id_usuario", DatosConfiguracion.idUsuario);

            JSONarr.add(JSONobj.clone());
            final String Datos = URLEncoder.encode(JSONarr.toString());
            DatosConfiguracion datosConfiguracion = new DatosConfiguracion(getBaseContext());
            progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo validación...", true);
            RequestQueue queue = Volley.newRequestQueue(this);
            System.out.println("------*>"+datosConfiguracion.getEndpoint() + datosConfiguracion.getConsultaAutentificacion() + Datos);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, datosConfiguracion.getEndpoint() + datosConfiguracion.getConsultaAutentificacion() + Datos ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            System.out.println("RespuestaLogin->" + response);
                            try {
                                org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                                org.json.JSONObject jsoNobj = new org.json.JSONObject(jsonArray.get(0).toString());
                                if(jsoNobj.getString("operacion").equalsIgnoreCase("true")){
                                    Intent intent = new Intent().setClass(
                                            MenuRPLActivity.this,LoginActivity.class);
                                    startActivity(intent);

                                    DatosConfiguracion.almacen = "";
                                    SharedPreferences settings = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, MODE_PRIVATE);
                                    settings.edit().clear().commit();

                                    finish();
                                }else{
                                    AlertDialog.Builder quitDialog
                                            = new AlertDialog.Builder(MenuRPLActivity.this);
                                    quitDialog.setTitle("ERROR");
                                    quitDialog.setMessage(""+ Html.fromHtml(jsoNobj.getString("error")));
                                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });

                                    quitDialog.show();
                                }
                            }catch(Exception e){

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("ERROR->" + error);
                    progressDialog.dismiss();
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(MenuRPLActivity.this);
                    quitDialog.setTitle("ERROR");
                    quitDialog.setMessage("ERROR DE CONECTIVIDAD, INTENTE MAS TARDE.");
                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }});

                    quitDialog.show();
                }
            }){
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(tiempoPeticion, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringRequest);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.btnEntradas:
                //Toast.makeText(getApplicationContext(),"Entradas", Toast.LENGTH_LONG).show();
                startActivity(new Intent().setClass(MenuRPLActivity.this,RecepcionActivity.class));// EntradasAlmacenActivity.class));
                //overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                break;


            case R.id.btnUbicacion:
                intent = new Intent().setClass(
                        MenuRPLActivity.this,UbicacionActivity.class);
                //Toast.makeText(getApplicationContext(),"UBICACION", Toast.LENGTH_LONG).show();
                intent.putExtra("Option", "2");
                startActivity(intent);
               // overridePendingTransition(R.transition.zoom_forward_in, R.transition.zoom_forward_out);
                break;
            case R.id.btnRecoleccion:
                //intent = new Intent().setClass(MenuRPLActivity.this,RecoleccionActivity.class);
                intent = new Intent(MenuRPLActivity.this, RecoleccionUbicacionActivity.class);
                //Toast.makeText(getApplicationContext(),"RECOLECCION", Toast.LENGTH_LONG).show();
                intent.putExtra("Option", "3");
                startActivity(intent);
               // overridePendingTransition(R.transition.zoom_forward_in, R.transition.zoom_forward_out);
                break;
            case R.id.btnConteo:
                //Toast.makeText(getApplicationContext(),"CONTEO", Toast.LENGTH_LONG).show();
                //intent.putExtra("Option","4");
                intent = new Intent().setClass(
                        MenuRPLActivity.this,ConteoCiclicoActivity.class);
                startActivity(intent);
               // overridePendingTransition(R.transition.zoom_forward_in, R.transition.zoom_forward_out);
                break;
            case R.id.btnSalidas:
                intent = new Intent(this, SalidaActivity.class);
                startActivity(intent);
           /* case R.id.btnPerfiles:
                intent = new Intent().setClass(
                        MenuRPLActivity.this,PerfilesActivity.class);
                //Toast.makeText(getApplicationContext(),"PERFILES", Toast.LENGTH_LONG).show();
                //intent.putExtra("Option","5");
                startActivity(intent);
                overridePendingTransition(R.transition.zoom_forward_in, R.transition.zoom_forward_out);
                break;
            case R.id.btnConfig:
                startActivity(new Intent().setClass(MenuRPLActivity.this,ConfiguracionActivity.class));
                overridePendingTransition(R.transition.zoom_forward_in, R.transition.zoom_forward_out);
                break;*/
        }
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}

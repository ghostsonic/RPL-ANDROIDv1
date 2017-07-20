package com.example.lachewendy.rpl_androidv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.com.uny2.metodos.AlmacenesDbHelper;
import com.uny2.clases.DatosConfiguracion;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RecepcionActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayoutEntradaUno;
    private LinearLayout linearLayoutEntradaDos;
    private LinearLayout linearLayoutEntradaTres;
    private LinearLayout linearLayoutEntradaCuatro;
    private LinearLayout linearLayoutEntradaCinco;
    private LinearLayout linearLayoutEntradaSeis;

    private EditText edtNumeroPedido;
    private ProgressDialog progressDialog;
    private DatosConfiguracion datosConfiguracion;
    public int tiempoEspera = DatosConfiguracion.TIEMPO_PETICION;
    private TextView txtTituloToolbar;
    private TextView txtBackToolbar;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepcion_activity);
        findViewByid();
        txtTituloToolbar.setText("Recepción");
        listenerClick();

    }

    public void findViewByid(){
        linearLayoutEntradaUno = (LinearLayout) findViewById(R.id.linearEntradaUno);
        linearLayoutEntradaDos = (LinearLayout) findViewById(R.id.linearEntradaDos);
        linearLayoutEntradaTres =  (LinearLayout) findViewById(R.id.linearEntradaTres);
        linearLayoutEntradaCuatro = (LinearLayout) findViewById(R.id.linearEntradaCuatro);
        linearLayoutEntradaCinco = (LinearLayout) findViewById(R.id.linearEntradaCinco);
        linearLayoutEntradaSeis = (LinearLayout) findViewById(R.id.linearEntradaSeis);
        txtTituloToolbar = (TextView) findViewById(R.id.txtTituloToolbar);
        txtBackToolbar = (TextView) findViewById(R.id.txtBackToolbar);
    }

    public void listenerClick(){
        linearLayoutEntradaUno.setOnClickListener(this);
        linearLayoutEntradaDos.setOnClickListener(this);
        linearLayoutEntradaTres.setOnClickListener(this);
        linearLayoutEntradaCuatro.setOnClickListener(this);
        linearLayoutEntradaCinco.setOnClickListener(this);
        linearLayoutEntradaSeis.setOnClickListener(this);
        txtBackToolbar.setOnClickListener(this);
    }


    public void mostrarEntradaDialog(final int tipoEntrada, final int origenEntrada, final String titulo){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecepcionActivity.this);
        LayoutInflater li = LayoutInflater.from(RecepcionActivity.this);
        View promptsView = li.inflate(R.layout.entradas_prompt, null);
        TextView txtTitulo = (TextView) promptsView.findViewById(R.id.tvNumeroped);
        txtTitulo.setText("Escribe el número "+titulo+":");
        edtNumeroPedido = (EditText) promptsView.findViewById(R.id.txtNumeroPedido);
        edtNumeroPedido.requestFocus();

        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false)
                .setTitle("Escanear Código")
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("numeroPedido" + edtNumeroPedido.getText().toString());
                        try {
                            Long.parseLong(edtNumeroPedido.getText().toString());
                            solicitarPedido(edtNumeroPedido.getText().toString(), tipoEntrada, origenEntrada);
                        } catch (Exception e) {
                            AlertDialog.Builder quitDialog
                                    = new AlertDialog.Builder(RecepcionActivity.this);
                            quitDialog.setTitle("Formato de Codigo no valido");
                            quitDialog.setCancelable(false);
                            quitDialog.setMessage("El código de orden debe ser numérico");
                            quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mostrarEntradaDialog(tipoEntrada, origenEntrada, titulo);
                                }
                            });
                            quitDialog.show();
                        }

                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void solicitarPedido(final String numeroPedido, final int tipoEntrada, final int origenEntrada){
        preferences = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, MODE_PRIVATE);
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
        datosConfiguracion = new DatosConfiguracion(getBaseContext());
        final JSONObject JSONobj = new JSONObject();
        JSONobj.clear();
        final JSONArray JSONarr = new JSONArray();

        JSONobj.put("ordenPedido",numeroPedido);
        JSONobj.put("paso", 1);
        JSONobj.put("tipoEntrada", tipoEntrada);
        JSONobj.put("origenEntrada", origenEntrada);
        JSONobj.put("almacen", preferences.getString("almacen", ""));
        JSONarr.add(JSONobj.clone());
        System.out.println("hola" + JSONarr.toString());
        final String Datos = URLEncoder.encode(JSONarr.toString());
        RequestQueue queue = Volley.newRequestQueue(RecepcionActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioRecepcion(),// + Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println("TEXTOUNO" + response);
                        try{
                            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
                            if(jsonObject.has("operacion")){
                                AlertDialog.Builder quitDialog
                                        = new AlertDialog.Builder(RecepcionActivity.this);
                                quitDialog.setTitle("Atencion");
                                quitDialog.setMessage( Html.fromHtml(jsonObject.getString("error")));
                                quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                quitDialog.show();
                            }else{
                                try {
                                    Long.parseLong(numeroPedido);
                                    Intent intent = new Intent(RecepcionActivity.this, DarEntradaActivity.class);
                                    intent.putExtra("numeroPedido", numeroPedido);
                                    intent.putExtra("respuestaJSON", response);
                                    intent.putExtra("numEntrada", Integer.toString(tipoEntrada));
                                    startActivity(intent);
                                }catch(Exception e) {
                                    System.out.println("El elemento solicitado");
                                }

                            }
                           /* linearDetalleEntrada.setVisibility(View.VISIBLE);
                            respuestaEntrada = response;
                            llenarDetalle();*/
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR->" + error);
                progressDialog.dismiss();
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(RecepcionActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setMessage("Ha ocurrido un error. Vuelve a intentar");
                quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        solicitarPedido(numeroPedido, tipoEntrada, origenEntrada);
                    }
                });
                quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                quitDialog.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put("param", Datos);
                //System.out.println();
                //params.put(datosConfiguracion.getServicioRecepcion(), JSONarr.toString());
                params.put("", JSONarr.toString());
                System.out.println("Paramet->"+JSONarr.toString());
                return params;
            }
           /* @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json;");
                return params;
            }*/
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(tiempoEspera, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();


        if(id==txtBackToolbar.getId()){
            finish();
        }
        switch (id){
            case R.id.linearEntradaUno:
                mostrarEntradaDialog(1,2,getResources().getString(R.string.tipo_uno));
                break;
            case R.id.linearEntradaDos:
                mostrarEntradaDialog(2,1,getResources().getString(R.string.tipo_dos)); //2,2
                break;
            case R.id.linearEntradaTres:
                mostrarEntradaDialog(3,2,getResources().getString(R.string.tipo_tres)); //3,1a
                break;
            case R.id.linearEntradaCuatro:
                mostrarEntradaDialog(4,1,getResources().getString(R.string.tipo_cuatro));
                break;
            case R.id.linearEntradaCinco:
                mostrarEntradaDialog(5,2,getResources().getString(R.string.tipo_cinco));
                break;
            case R.id.linearEntradaSeis:
                mostrarEntradaDialog(6,1,getResources().getString(R.string.tipo_seis));
                break;
        }

    }
}

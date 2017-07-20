package com.example.lachewendy.rpl_androidv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class EntradasAlmacenActivity extends AppCompatActivity{

    LinearLayout linearDetalleEntrada;
    EditText edtNumeroPedido;
    ProgressDialog progressDialog;
    DatosConfiguracion datosConfiguracion;
    public int tiempoEspera = 35000;

    TextView txtNumOrden;
    TextView txtFechaPedido;
    TextView txtUnidadesTotales;
    TextView txtAlmacen;
    TextView txtProcendencia;
    TextView txtBackToolbar;
    TextView txtTituloToolbar;

    Button btnDarEntrada;
    Button btnCancelar;
    public static String respuestaEntrada;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_entrada);
        linearDetalleEntrada = (LinearLayout) findViewById(R.id.linearDetalleEntrada);
        txtNumOrden = (TextView) findViewById(R.id.txtNumOrdenEntrada);
        txtFechaPedido = (TextView) findViewById(R.id.txtFechaPedidoEntrada);
        txtUnidadesTotales = (TextView) findViewById(R.id.txtUnidadesTotalesEntrada);
        txtAlmacen = (TextView) findViewById(R.id.txtAlmacenEntrada);
        txtProcendencia = (TextView) findViewById(R.id.txtProcedenciaEntrada);
        txtBackToolbar = (TextView) findViewById(R.id.txtBackToolbar);
        txtTituloToolbar = (TextView) findViewById(R.id.txtTituloToolbar);

        btnDarEntrada = (Button) findViewById(R.id.btnDarEntrada);
        btnCancelar = (Button) findViewById(R.id.btnCancelarEntrada);

        linearDetalleEntrada.setVisibility(View.GONE);
        txtTituloToolbar.setText("ENTRADAS");
        eventos();


        mostrarDialogInicial();

    }
    public void mostrarDialogInicial(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntradasAlmacenActivity.this);
        LayoutInflater li = LayoutInflater.from(EntradasAlmacenActivity.this);
        View promptsView = li.inflate(R.layout.entradas_prompt, null);
        edtNumeroPedido = (EditText) promptsView.findViewById(R.id.txtNumeroPedido);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false)
                .setTitle("SOLICITAR ENTRADA")
                .setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("numeriPedido" + edtNumeroPedido.getText().toString());
                        solicitarPedido(edtNumeroPedido.getText().toString());
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
                        finish();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void solicitarPedido(final String numeroPedido){
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
        datosConfiguracion = new DatosConfiguracion(getBaseContext());
        final JSONObject JSONobj = new JSONObject();
        JSONobj.clear();
        JSONArray JSONarr = new JSONArray();
        JSONobj.put("ordenPedido",numeroPedido);
        JSONobj.put("paso", 1);
        JSONarr.add(JSONobj.clone());
        System.out.println("" + JSONarr.toString());
        String Datos = URLEncoder.encode(JSONarr.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("DAORT->" + Datos);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioRecepcion() + Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println("TEXTOUNO" + response);
                        try{
                            linearDetalleEntrada.setVisibility(View.VISIBLE);
                            respuestaEntrada = response;
                            llenarDetalle();
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
                        = new AlertDialog.Builder(EntradasAlmacenActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setMessage("Ha ocurrido un error. Vuelve a intentar");
                quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        solicitarPedido(numeroPedido);
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
            public String getBodyContentType() {
                return "application/json";
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(tiempoEspera, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void eventos(){

        txtBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDarEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEntradas = new Intent(EntradasAlmacenActivity.this, DarEntradaActivity.class);
                intentEntradas.putExtra("respuesta", respuestaEntrada);
                startActivity(intentEntradas);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void llenarDetalle() throws Exception{
        org.json.JSONArray respuesta = new org.json.JSONArray(respuestaEntrada);
        org.json.JSONObject detalle = new org.json.JSONObject(respuesta.get(0).toString());
        txtNumOrden.setText(detalle.getString("op"));
        txtFechaPedido.setText(detalle.getString("fecha_pedido"));
        txtUnidadesTotales.setText(detalle.getString("unidades_totales"));
        txtAlmacen.setText(detalle.getString("almacen"));
        txtProcendencia.setText(detalle.getString("procedencia"));
    }

}

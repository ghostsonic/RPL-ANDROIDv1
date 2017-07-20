package com.example.lachewendy.rpl_androidv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.com.uny2.metodos.AlmacenesDbHelper;
import com.uny2.clases.ConteoCiclico;
import com.uny2.clases.ConteoCiclicoAdapter;
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

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ConteoCiclicoActivity extends AppCompatActivity implements View.OnClickListener {
    boolean hiloEsperaEscaner;
    ListView listConteoCiclico;
    ProgressDialog progressDialog;
    EditText edtRefaccion;
    EditText edtCantidad;
    ImageButton btnEnviiarConteo;
    ImageButton btnOkConteo;
    ImageView imgClear;
    Button btnManual;
    RelativeLayout linearItem;
    ConteoCiclicoAdapter arrayAdapter;
    ArrayList<ConteoCiclico> arrayListContado;
    private ScaleGestureDetector mScaleDetector;
    DatosConfiguracion datosConfiguracion;
    InputMethodManager imm;
    int posicion;
    String respuesta;
    private static ConteoCiclicoActivity mInst = null;
    public static final String RECEIVE_DATA = "unitech.scanservice.data";
    public static final String START_SCANSERVICE = "unitech.scanservice.start";
    public static final String SCAN2KEY_SETTING = "unitech.scanservice.scan2key_setting";
    static final int tiempoPeticion = DatosConfiguracion.TIEMPO_PETICION;
    boolean listActive=false;
    private Menu menuMio;
    Double scaleList=1.0;
    MenuItem bedMenuItem;
    boolean check;
    String codigoUbicacion;
    String codigoProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hiloEsperaEscaner=false;
        mInst = this;
        setContentView(R.layout.activity_conteo_ciclico);
        posicion = 0;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        codigoUbicacion = "";
        this.setTitle(getResources().getString(R.string.conteo_ciclico));
        btnManual = (Button) findViewById(R.id.btnManual);
        linearItem = (RelativeLayout) findViewById(R.id.linearItem);
        btnEnviiarConteo = (ImageButton) findViewById(R.id.imgButtonEnviar);
        listConteoCiclico = (ListView) findViewById(R.id.listConteoCiclico);
        edtCantidad = (EditText) findViewById(R.id.edtCantidad);
        edtRefaccion = (EditText) findViewById(R.id.edtIngresarRefaccion);
        imgClear = (ImageView) findViewById(R.id.imgClear);
        btnOkConteo = (ImageButton) findViewById(R.id.imgButtonConfirmar);
        btnManual.setOnClickListener(this);
        btnManual.setEnabled(false);
        btnEnviiarConteo.setOnClickListener(this);
        btnOkConteo.setOnClickListener(this);
        edtRefaccion.requestFocus();
        edtCantidad.setFocusable(false);
        btnOkConteo.setFocusable(false);
        btnManual.setFocusable(false);
        btnOkConteo.clearFocus();
        btnManual.clearFocus();
        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtRefaccion.setText("");
                edtRefaccion.requestFocus();
                imgClear.setVisibility(View.GONE);
            }
        });

        edtRefaccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                System.out.println("cadena->" + s.length() + "asd" + count + "before+->" + before + "start->" + start);
                System.out.println(s.toString());
                if(s.length()>0){
                    if(hiloEsperaEscaner==false){
                        hiloEsperaEscaner=true;
                        new CountDownTimer(1000,100){
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                String codigoBueno = edtRefaccion.getHint().toString() + "," + codigoUbicacion;
                                System.out.println("CodigoUb->"+codigoBueno);
                                if(s.toString().equalsIgnoreCase(edtRefaccion.getHint().toString())|| s.toString().equalsIgnoreCase(codigoBueno)){
                                    String data = "";
                                    edtRefaccion.requestFocus();
                                    edtCantidad.setFocusable(false);
                                    edtCantidad.setFocusableInTouchMode(true);
                                    btnOkConteo.setFocusable(false);
                                    btnManual.setFocusable(false);
                                    btnOkConteo.clearFocus();
                                    btnManual.clearFocus();
                                    Intent sendIntent = new Intent(RECEIVE_DATA);
                                    sendIntent.putExtra("text",edtRefaccion.getText().toString());
                                    sendIntent.putExtra("activity","conteo");
                                    sendIntent.putExtra("ubicacion",codigoBueno);
                                    sendBroadcast(sendIntent);
                                }else{
                                    if(edtRefaccion.getHint().toString().contains("Seleccione")){
                                        setMensaje("¡Atención!", "Debes seleccionar una refacción antes de escanear");
                                    }else {
                                        AlertDialog.Builder quitDialog
                                                = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                                        quitDialog.setTitle("Código Incorrecto");
                                        quitDialog.setCancelable(false);
                                        quitDialog.setMessage("Favor de revisar el codigo de la refacción");
                                        quitDialog.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        quitDialog.show();
                                    }
                                }
                                edtRefaccion.setText("");
                                edtRefaccion.requestFocus();
                                hiloEsperaEscaner=false;
                            }
                        }.start();
                    }

                    /*
                    if(s.toString().equalsIgnoreCase(edtRefaccion.getHint().toString())) {
                        new CountDownTimer(200, 100) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                // do something after 1s
                            }

                            @Override
                            public void onFinish() {
                                // do something end times 5s
                                    if(s.length()==edtRefaccion.length()){
                                        String data = "";
                                        edtRefaccion.requestFocus();
                                        edtCantidad.setFocusable(false);
                                        edtCantidad.setFocusableInTouchMode(true);
                                        btnOkConteo.setFocusable(false);
                                        btnManual.setFocusable(false);
                                        btnOkConteo.clearFocus();
                                        btnManual.clearFocus();
                                        Intent sendIntent = new Intent(RECEIVE_DATA);
                                        sendIntent.putExtra("text",edtRefaccion.getText().toString());
                                        sendIntent.putExtra("activity","conteo");
                                        sendBroadcast(sendIntent);

                                }
                            }

                        }.start();

                    }*/

                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });


        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);



        //Deshabilitamos el scanner por entrada de texto y obligamos a que funcione por Broadcast
       /* Bundle bundle = new Bundle();
        bundle.putBoolean("scan2key", true);
        Intent mIntent = new Intent().setAction(SCAN2KEY_SETTING).putExtras(bundle);
        sendBroadcast(mIntent);*/

        //Iniciamos el ScanService Broadcast
        Bundle bundleScan = new Bundle();
        bundleScan.putBoolean("close", true);
        Intent mIntentScan = new Intent().setAction("unitech.scanservice.start").putExtras(bundleScan);
        sendBroadcast(mIntentScan);

//DEscomentar
       /* edtRefaccion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imm.hideSoftInputFromWindow(edtRefaccion.getWindowToken(), 0);
                edtRefaccion.requestFocus();
                return true;
            }
        });*/
        arrayListContado = new ArrayList<>();
        edtCantidad.setEnabled(false);

        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }
            @Override
            public boolean onScale(ScaleGestureDetector detector) {

                if (detector.getScaleFactor() > 1) {
                    if (scaleList < 1.10) {
                        scaleList=scaleList+0.01;
                        listConteoCiclico.setScaleX(Float.parseFloat(Double.toString(scaleList)));
                        listConteoCiclico.setScaleY(Float.parseFloat(Double.toString(scaleList)));

                    }
                } else {
                    if (scaleList > 1.0) {
                        scaleList=scaleList-0.01;
                        listConteoCiclico.setScaleX(Float.parseFloat(Double.toString(scaleList)));
                        listConteoCiclico.setScaleY(Float.parseFloat(Double.toString(scaleList)));
                    }
                }

                return false;
            }
        });

        pedirConteo();


    }




    public void pedirConteo() {

            final AlmacenesDbHelper isExistente = new AlmacenesDbHelper(getBaseContext());
            if(isExistente.conteoActivo()==0) {
                final JSONObject JSONobj = new JSONObject();
                JSONobj.clear();
                final JSONArray JSONarr = new JSONArray();
                DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
                JSONobj.put("id_Usuario",Integer.parseInt( DatosConfiguracion.idUsuario));
                JSONobj.put("almacen",DatosConfiguracion.almacen);
                JSONobj.put("paso",1);
                JSONarr.add(JSONobj.clone());
                System.out.println(""+JSONarr.toString());
                String Datos = URLEncoder.encode(JSONarr.toString());
                datosConfiguracion = new DatosConfiguracion(getBaseContext());
                progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
                RequestQueue queue = Volley.newRequestQueue(this);
                System.out.println("DAORT->" + Datos);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + datosConfiguracion.getConsultaConteo(),// + Datos,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                progressDialog.dismiss();
                                System.out.println("TEXTOUNO" + response);
                                try {
                                    org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                                    org.json.JSONObject json = new org.json.JSONObject(jsonArray.getString(0));
                                    if(!json.has("operacion")){
                                        respuesta = response;
                                        AlmacenesDbHelper insertCon = new AlmacenesDbHelper(getBaseContext());
                                        String respuestaInsert = insertCon.insertConteoCiclico(respuesta);
                                        respuesta =respuestaInsert;
                                        obtenerRefaccion(respuestaInsert);
                                    }else{
                                        AlertDialog.Builder quitDialog
                                                = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                                        quitDialog.setTitle("Atención");
                                        quitDialog.setCancelable(false);
                                        try {
                                            quitDialog.setMessage("" + Html.fromHtml(URLDecoder.decode(json.getString("error"), "UTF-8")));
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            quitDialog.setMessage("No se pudo obtener la respuesta");
                                        }
                                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });

                                        quitDialog.show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR->" + error);
                        progressDialog.dismiss();
                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                        quitDialog.setTitle("ERROR");
                        quitDialog.setCancelable(false);
                        quitDialog.setMessage("Ha ocurrido un error. Vuelve a intentar");
                        quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pedirConteo();
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
                        //params.put(datosConfiguracion.getServicioRecepcion(), Datos);
                        params.put("", JSONarr.toString());
                        //params.put("", JSONarr.toString());
                        return params;
                    }
                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(tiempoPeticion, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(stringRequest);
            }else{
                System.out.println("--->>>>> YA TIENE CONTEO");
                String retornoConteo = isExistente.resgresarConteo();
                respuesta=retornoConteo;
                ConteoCiclico conteoCiclico = new ConteoCiclico();
                //ArrayList<ConteoCiclico> listClass =conteoCiclico.toArrayListWS(retornoConteo, 3); //-> 17 marzo



                //UNO
                final JSONObject JSONobj = new JSONObject();
                JSONobj.clear();
                final JSONArray JSONarr = new JSONArray();
                DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
                JSONobj.put("id_Usuario",Integer.parseInt( DatosConfiguracion.idUsuario));
                JSONobj.put("almacen",DatosConfiguracion.almacen);
                JSONobj.put("paso",1);
                JSONarr.add(JSONobj.clone());
                System.out.println(""+JSONarr.toString());
                String Datos = URLEncoder.encode(JSONarr.toString());
                datosConfiguracion = new DatosConfiguracion(getBaseContext());
                progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
                RequestQueue queue = Volley.newRequestQueue(this);
                System.out.println("DAORT->" + Datos);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + datosConfiguracion.getConsultaConteo(),// + Datos,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                progressDialog.dismiss();
                                System.out.println("TEXTOUNO" + response);

                                ArrayList<ConteoCiclico> listClass =isExistente.actualizarConteo(response);
                                if(listClass.size()>0){
                                    arrayAdapter = new ConteoCiclicoAdapter(getBaseContext(), listClass);
                                    listConteoCiclico.setAdapter(arrayAdapter);
                                    listConteoCiclico.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    for(ConteoCiclico x:listClass){
                                        if(x.getEstatus().equals("1")){
                                            arrayListContado.add(new ConteoCiclico(x.getIdRow(),x.getDescripcion(),x.getSku(),x.getEstatus(),x.getUbicacion(),x.getCantidad(),x.isConfirmacion()));
                                        }
                                    }
                                    conteoPendiente();
                                }else{
                                    if(listClass.size()==0){
                                        isExistente.borrarConteo("CONTEOS_CICLICO");
                                        AlertDialog.Builder quitDialog
                                                = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                                        quitDialog.setTitle("Conteo Concluido");
                                        quitDialog.setCancelable(false);
                                        quitDialog.setMessage("No tienes conteo por el momento");
                                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        quitDialog.show();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR->" + error);
                        progressDialog.dismiss();
                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                        quitDialog.setTitle("ERROR");
                        quitDialog.setCancelable(false);
                        quitDialog.setMessage("Ha ocurrido un error. Vuelve a intentar");
                        quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pedirConteo();
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
                        //params.put(datosConfiguracion.getServicioRecepcion(), Datos);
                        params.put("", JSONarr.toString());
                        //params.put("", JSONarr.toString());
                        return params;
                    }
                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(tiempoPeticion, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(stringRequest);




                //Dos


            }

    }
    public void conteoPendiente(){
        listConteoCiclico.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);

                return false;
            }
        });
        listConteoCiclico.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Selection ---->" + arrayAdapter.getItem(position).getEstatus());
                codigoUbicacion = arrayAdapter.getItem(position).getUbicacion().replace("-","");
                if (arrayAdapter.getItem(position).getEstatus().equals("0")) {
                    view.setSelected(true);
                    posicion = position;
                    check = false;
                    edtCantidad.setEnabled(false);
                    edtRefaccion.requestFocus();
                    btnManual.setEnabled(true);
                    edtRefaccion.setText("");
                    edtRefaccion.setHint(arrayAdapter.getItem(position).getSku());
                    edtCantidad.setText("");
                    listConteoCiclico.setEnabled(false);
                    listActive=true;
                    bedMenuItem = menuMio.findItem(R.id.activarList);
                    bedMenuItem.setIcon(R.drawable.ic_lock);


                } else {
                    edtRefaccion.setHint(R.string.ingresa_codigo);
                    edtCantidad.setEnabled(false);
                    edtCantidad.setText("");
                }

            }
        });
        listConteoCiclico.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(arrayAdapter.getItem(position).getEstatus().equals("1")) {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                    quitDialog.setTitle("CONTAR");
                    String pieza = arrayAdapter.getItem(position).getDescripcion();
                    quitDialog.setMessage("Deseas hacer nuevamente el conteo de:\n.[" + pieza + "]");
                    quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            arrayAdapter.getItem(position).setEstatus("0");
                            arrayAdapter.getItem(position).setCantidad(0);
                            int i=0;
                            for (ConteoCiclico x : arrayListContado) {
                                if (arrayAdapter.getItem(position).getSku().equals(x.getSku())) {
                                    System.out.println(arrayAdapter.getItem(position).getSku()+"----"+x.getSku());
                                    arrayListContado.remove(i);
                                }
                                i++;
                            }
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });
                    quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    quitDialog.show();
                }
                return false;
            }
        });
    }
    public void obtenerRefaccion(String respuesta){

        System.out.println("OBTENIENDO REFACCIONES: "+respuesta);
        ConteoCiclico conteoCiclico = new ConteoCiclico();
        arrayAdapter = new ConteoCiclicoAdapter(getBaseContext(), conteoCiclico.toArrayListWS(respuesta, 2));
        listConteoCiclico.setAdapter(arrayAdapter);
        listConteoCiclico.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listConteoCiclico.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);

                return false;
            }
        });
        listConteoCiclico.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Selection ---->" + arrayAdapter.getItem(position).getEstatus());
                codigoUbicacion = arrayAdapter.getItem(position).getUbicacion().replace("-","");
                if (arrayAdapter.getItem(position).getEstatus().equals("0")) {
                    view.setSelected(true);
                    posicion = position;
                    edtCantidad.setEnabled(false);
                    edtRefaccion.setText("");
                    btnManual.setEnabled(true);
                    edtRefaccion.setHint(arrayAdapter.getItem(position).getSku());
                    edtCantidad.setText("");
                    listActive = true;
                    listConteoCiclico.setEnabled(false);
                    bedMenuItem = menuMio.findItem(R.id.activarList);
                    bedMenuItem.setIcon(R.drawable.ic_lock);
                } else {
                    edtRefaccion.setHint(R.string.ingresa_codigo);
                    edtCantidad.setEnabled(false);
                    edtCantidad.setText("");
                }
            }
        });
        listConteoCiclico.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (arrayAdapter.getItem(position).getEstatus().equals("1")) {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                    quitDialog.setTitle("CONTAR");
                    String pieza = arrayAdapter.getItem(position).getDescripcion();
                    quitDialog.setMessage("Deseas hacer nuevamente el conteo de:\n.[" + pieza + "]");
                    quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            arrayAdapter.getItem(position).setEstatus("0");
                            arrayAdapter.getItem(position).setCantidad(0);
                            int i = 0;
                            for (ConteoCiclico x : arrayListContado) {
                                if (arrayAdapter.getItem(position).getSku().equals(x.getSku())) {
                                    arrayListContado.remove(i);
                                }
                                i++;
                            }
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });

                    quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    quitDialog.show();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!isPrepareCount()) {
                    this.finish();
                } else {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                    quitDialog.setTitle("AVISO");
                    quitDialog.setMessage("¿Deseas salir?");

                    quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    quitDialog.show();
                }
                break;
            case R.id.activarList:
                if(listActive){
                    listActive=false;
                    listConteoCiclico.setEnabled(true);
                    item.setIcon(R.drawable.ic_lock_open);
                }else{
                    listActive=true;
                    listConteoCiclico.setEnabled(false);
                    item.setIcon(R.drawable.ic_lock);
                }
            break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if(!isPrepareCount()) {
            this.finish();
        }else{
            AlertDialog.Builder quitDialog
                    = new AlertDialog.Builder(ConteoCiclicoActivity.this);
            quitDialog.setTitle("AVISO");
            quitDialog.setMessage("¿Deseas Salir?");

            quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }});

            quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }});

            quitDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.imgButtonConfirmar:
               if(!edtRefaccion.getHint().equals(getResources().getString(R.string.ingresa_codigo))) {
                   if(this.arrayAdapter.getItem(posicion).getEstatus().equals("0")) {
                      // if (edtRefaccion.getHint().equals(edtRefaccion.getText().toString().trim())) {
                           if (edtCantidad.getText().toString().trim().length() > 0) {
                               final ConteoCiclico conteoCiclico = new ConteoCiclico();
                               System.out.println("POSICION->" + posicion + "ELemento->" + arrayAdapter.getItem(posicion).getDescripcion() + "Catnidad" + edtCantidad.getText().toString());
                               conteoCiclico.setSku(edtRefaccion.getText().toString());
                               AlertDialog.Builder quitDialog
                                       = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                               quitDialog.setTitle("Atención");
                               quitDialog.setMessage("¿El conteo realizado es correcto?");
                               quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       AlmacenesDbHelper insert=new AlmacenesDbHelper(getBaseContext());
                                       insert.updateLineaRefaccion(edtCantidad.getText().toString(), arrayAdapter.getItem(posicion).getIdRow(),1);
                                       arrayAdapter.getItem(posicion).setEstatus("1");
                                       arrayAdapter.getItem(posicion).setCantidad(Double.parseDouble(edtCantidad.getText().toString()));
                                       arrayListContado.add(new ConteoCiclico(arrayAdapter.getItem(posicion).getIdRow(), arrayAdapter.getItem(posicion).getDescripcion(), arrayAdapter.getItem(posicion).getSku(), arrayAdapter.getItem(posicion).getEstatus(), arrayAdapter.getItem(posicion).getUbicacion(), Double.parseDouble(edtCantidad.getText().toString()), false));
                                       arrayAdapter.notifyDataSetChanged();
                                       edtCantidad.setText("");
                                       edtCantidad.setEnabled(false);
                                       edtRefaccion.setText("");
                                       edtRefaccion.setHint(R.string.ingresa_codigo);
                                       listConteoCiclico.setEnabled(true);
                                       listActive=false;
                                       bedMenuItem = menuMio.findItem(R.id.activarList);
                                       bedMenuItem.setIcon(R.drawable.ic_lock_open);
                                   }
                               });
                               quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                   }
                               });
                               quitDialog.setCancelable(false);
                               quitDialog.show();
                           } else {
                               AlertDialog.Builder quitDialog
                                       = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                               quitDialog.setTitle("ERROR");
                               quitDialog.setMessage("Se requiere cantidad");
                               quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       listConteoCiclico.setEnabled(true);
                                   }
                               });

                               quitDialog.show();
                           }
                      /* } else {
                           AlertDialog.Builder quitDialog
                                   = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                           quitDialog.setTitle("ERROR");
                           quitDialog.setMessage("Código erróneo!!!");
                           quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                               }
                           });

                           quitDialog.show();
                       }*/
                   }
               }
               break;
           case R.id.imgButtonEnviar:
               if(arrayListContado.size()>0){
                   envioConte();
               }else{
                   AlertDialog.Builder quitDialog
                           = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                   quitDialog.setTitle("AVISO");
                   quitDialog.setMessage("Es necesario enviar al menos un conteo.");
                   quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                       }
                   });
                   quitDialog.show();
               }
               break;
           case R.id.btnManual:
               /*edtCantidad.setFocusableInTouchMode(true);
               if(btnManual.getText().toString().equalsIgnoreCase("Manual")){
                   edtCantidad.setEnabled(true);
                   btnManual.setText("OK");
               }else{
                   AlertDialog.Builder quitDialog
                           = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                   quitDialog.setTitle("Confirmación");
                   quitDialog.setMessage("¿Desea guardar la cantidad ingresada o cancelar el modo Manual?");
                   quitDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {

                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           try {
                               if (edtCantidad.getText().toString().equals("")) {
                                   setMensaje("Atención!", "Debes ingresar un numero de refacciones");
                               } else {
                                   btnManual.setText("MANUAL");
                                   edtCantidad.setEnabled(false);
                                   edtCantidad.setText(Double.toString(Double.parseDouble(edtCantidad.getText().toString())));
                               }
                           } catch (Exception e) {
                           }
                       }
                   });

                   quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           edtCantidad.setEnabled(false);
                           edtCantidad.setText("");
                           btnManual.setText("Manual");
                       }
                   });

                   quitDialog.show();
               }*/
               //edtCantidadRecoleccion.setFocusableInTouchMode(true);
               if (check) {
                   edtRefaccion.requestFocus();
                   edtCantidad.setEnabled(true);
                   // btnManual.setText("OK");
               } else {

                   try {
                       if (edtCantidad.getText().toString().equals("")) {
                           setMensaje("¡Atención!", "Debes escanear para confirmar el número de refacciones");
                       } else {
                           btnManual.setText("MANUAL");
                           edtCantidad.setEnabled(false);
                           edtCantidad.setText(Double.toString(Double.parseDouble(edtCantidad.getText().toString())));
                       }
                   } catch (Exception e) {
                   }
                    /*AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(RecoleccionActivity.this);
                    quitDialog.setTitle("Confirmación");
                    quitDialog.setMessage("¿Desea guardar la cantidad ingresada o cancelar el modo Manual?");
                    quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                if (edtCantidadRecoleccion.getText().toString().equals("")) {
                                    setMensaje("Atención!", "Debes ingresar un numero de refacciones");
                                } else {
                                    btnManual.setText("MANUAL");
                                    edtCantidadRecoleccion.setEnabled(false);
                                    edtCantidadRecoleccion.setText(Double.toString(Double.parseDouble(edtCantidadRecoleccion.getText().toString())));
                                }
                            } catch (Exception e) {
                            }
                        }
                    });

                    quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edtCantidadRecoleccion.setEnabled(false);
                            edtCantidadRecoleccion.setText("");
                            btnManual.setText("Manual");
                        }
                    });

                    quitDialog.show();*/
               }


               break;
       }
    }
     public void envioConte(){
         JSONObject JSONobj = new JSONObject();
         final JSONArray JSONarr = new JSONArray();
         JSONobj.clear();
         JSONobj.put("paso", 2);
         JSONarr.add(JSONobj.clone());
         for(ConteoCiclico x:arrayListContado) {
             JSONobj.clear();
             JSONobj.put("sku", x.getSku());
             JSONobj.put("descripcion",x.getDescripcion());
             JSONobj.put("estatus", x.getEstatus());
             JSONobj.put("ubicacion",x.getUbicacion());
             JSONobj.put("cantidad_contada", Double.toString(x.getCantidad()));
             DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
             JSONobj.put("id_Usuario",DatosConfiguracion.idUsuario);
             JSONobj.put("almacen",DatosConfiguracion.almacen);
             JSONobj.put("id_linea", x.getIdRow());
             JSONobj.put("ubicacion", x.getUbicacion());
             JSONarr.add(JSONobj.clone());
         }
         progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Enviando datos...", true);
         String Datos = URLEncoder.encode(JSONarr.toString());
         System.out.println("ENVIO---->>>>"+JSONarr.toString());
         RequestQueue queue = Volley.newRequestQueue(this);
         datosConfiguracion = new DatosConfiguracion();
         StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + datosConfiguracion.getConsultaConteo(), //+ Datos ,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         System.out.println("RespUEsTACONTEO"+response);
                         analizarRespuestaConteo(response);
                         progressDialog.dismiss();
                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 System.out.println("ERROR->" + error);
                 progressDialog.dismiss();
                 AlertDialog.Builder quitDialog
                         = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                 quitDialog.setTitle("ERROR");
                 quitDialog.setMessage("No fue posible obtener una respuesta.");
                 quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                     }
                 });

                 quitDialog.show();
             }
         }){
             @Override
             protected Map<String, String> getParams() {
                 Map<String, String> params = new HashMap<>();
                 //params.put(datosConfiguracion.getServicioRecepcion(), Datos);
                 params.put("", JSONarr.toString());
                 //params.put("", JSONarr.toString());
                 return params;
             }
             @Override
             public String getBodyContentType() {
                 return "application/json";
             }
         }
                 ;
// Add the request to the RequestQueue.
         stringRequest.setRetryPolicy(new DefaultRetryPolicy(tiempoPeticion, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         queue.add(stringRequest);
     }

    public void analizarRespuestaConteo(String respuesta){
        ArrayList<ConteoCiclico> respuestaArray=ConteoCiclico.toArrayListWS(respuesta, 4);

        AlmacenesDbHelper updateFin = new AlmacenesDbHelper(getBaseContext());
        for(ConteoCiclico x:respuestaArray){
            if(x.isConfirmacion()){

                int i=0;
               // for(ConteoCiclico y:arrayListContado){
                for(ConteoCiclico y:arrayListContado){
                    if(x.getDescripcion().equals(y.getDescripcion())){
                        System.out.println("Confirmacion->"+Boolean.toString(x.isConfirmacion()) + "IdRow"+y.getIdRow() );
                        System.out.println("ArrayListContado->"+arrayListContado.size()  );
                        updateFin.updateLineaRefaccion(Boolean.toString(x.isConfirmacion()), y.getIdRow(), 3);
                        //REvisar este proceso con arrayadapter y con arraylist y actualizar la lista.
                       // arrayAdapter.getItem(y.getIdRow()-1).setConfirmacion(true);
                        //arrayAdapter.remove(arrayAdapter.getItem(y.getIdRow() - 1));
                        arrayListContado.remove(y);
                        arrayAdapter.notifyDataSetChanged();
                        break;
                    }
                    i++;
                }
                  i=0;

                //for(ConteoCiclico y:arrayListContado){
              /*  for (int i = 0; i < arrayListContado.size(); i++) {
                    ConteoCiclico y = arrayListContado.get(i);
                    System.out.println("DEsCRIPcioN->"+arrayListContado.get(i).getDescripcion() +"SKU->" +arrayListContado.get(i).getSku());
                    System.out.println("Contienela Repuesta descripcion->"+respuestaArray.contains(arrayListContado.get(i).getDescripcion()));
                    System.out.println("Posicion dentro del arreglo de respuesta->"+respuestaArray.indexOf(arrayListContado.get(i).getDescripcion()));
                    System.out.println("Posicion dentro del arreglo de adapter->"+arrayListContado.indexOf(respuestaArray.get(i).getDescripcion()));
                    System.out.println(""+arrayListContado);

                    if (x.getDescripcion().equals(y.getDescripcion())) {
//                        updateFin.updateLineaRefaccion(Boolean.toString(x.isConfirmacion()), y.getIdRow(), 3);
                        System.out.println("***IDROW->" + y.getIdRow() + "DESCRIPCION CONTADO->" + y.getDescripcion() + "   ITem->" + arrayAdapter.getItem((y.getIdRow() - 1)).getDescripcion() + "Que pasa aqui->" +arrayAdapter.getPosition(y));
                        //arrayAdapter.getItem(y.getIdRow() - 1).setConfirmacion(true);
 //                       arrayAdapter.getItem(arrayAdapter.getPosition(y)).setConfirmacion(true);
                        //arrayAdapter.remove(arrayAdapter.getItem(y.getIdRow() - 1));
//                        arrayAdapter.remove(arrayAdapter.getItem(arrayAdapter.getPosition(y)));
//                        arrayListContado.remove(i);
                        arrayAdapter.notifyDataSetChanged();
                    }

                }*/

            }else{
                //PROVICISIONAL
               /* for (int i = 0; i < arrayListContado.size(); i++) {
                    ConteoCiclico y = arrayListContado.get(i);

                    if (x.getDescripcion().equals(y.getDescripcion())) {
                        /*updateFin.updateLineaRefaccion(Boolean.toString(x.isConfirmacion()), y.getIdRow(), 3);
                        arrayAdapter.getItem(y.getIdRow() - 1).setConfirmacion(true);
                        System.out.println("***-SINOIDROW->" + y.getIdRow() +"DESCRIPCION CONTADO->"+y.getDescripcion() +"   ITem->" + arrayAdapter.getItem((y.getIdRow() - 1)).getDescripcion()+"Que pasa aqui->"+(y.getIdRow() - 1));
                        arrayAdapter.remove(arrayAdapter.getItem(y.getIdRow() - 1));
                        arrayListContado.remove(i);
                        arrayAdapter.notifyDataSetChanged();

                        //updateFin.updateLineaRefaccion(Boolean.toString(x.isConfirmacion()), y.getIdRow(), 3);
                        System.out.println("fhgjjhghghhjkl->"+arrayAdapter.getPosition(y)+"***IDROW->" + y.getIdRow() + "DESCRIPCION CONTADO->" + y.getDescripcion() + "   ITem->" + arrayAdapter.getItem((y.getIdRow() - 1)).getDescripcion() + "Que pasa aqui->" + arrayAdapter.getPosition(y));
                        //arrayAdapter.getItem(y.getIdRow() - 1).setConfirmacion(true);
//                        arrayAdapter.getItem(arrayAdapter.getPosition(y)).setConfirmacion(true);
                        //arrayAdapter.remove(arrayAdapter.getItem(y.getIdRow() - 1));
//                        arrayAdapter.remove(arrayAdapter.getItem(arrayAdapter.getPosition(y)));
//                        arrayListContado.remove(i);
                       // arrayAdapter.notifyDataSetChanged();
                    }

                }*/
                //FIN PROVISIONAL
            }
        }

        ArrayList<ConteoCiclico> actualizadoArray=ConteoCiclico.toArrayListWS(updateFin.resgresarConteo(),5);
        if(actualizadoArray.size()>0) {
            arrayAdapter = new ConteoCiclicoAdapter(getBaseContext(), actualizadoArray);
            listConteoCiclico.setAdapter(arrayAdapter);
            listConteoCiclico.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            for (ConteoCiclico x : actualizadoArray) {
                if (x.getEstatus().equals("1")) {
                    arrayListContado.add(new ConteoCiclico(x.getIdRow(), x.getDescripcion(), x.getSku(), x.getEstatus(), x.getUbicacion(), x.getCantidad(), x.isConfirmacion()));
                }
            }
            arrayListContado.clear();
            conteoPendiente();
        }else{
            if(actualizadoArray.size()==0){
                updateFin.borrarConteo("CONTEOS_CICLICO");
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(ConteoCiclicoActivity.this);
                quitDialog.setTitle("Conteo Concluido");
                quitDialog.setMessage("Has terminado");
                quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                quitDialog.show();
            }
        }

    }
     public boolean isPrepareCount(){
         boolean respuesta=false;
         for(ConteoCiclico x:arrayListContado){
             respuesta=true;
         }
         return respuesta;
     }

    public void setMensaje(String titulo,String mensaje){
        AlertDialog.Builder quitDialog
                = new AlertDialog.Builder(ConteoCiclicoActivity.this);
        quitDialog.setTitle(titulo);
        quitDialog.setMessage(mensaje);
        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuMio =menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.candado_list, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onCreateOptionsMenu(menu);
    }


    public void setTextCodigoRefaccion(String text) {

        if (edtRefaccion.getHint().toString().contains("Seleccione")) {
            setMensaje("¡Atencion!","Debes Seleccionar el producto antes de scanear");
        }
        else{
            edtRefaccion.setText(text);
            edtRefaccion.requestFocus();
        }
        edtRefaccion.requestFocus();

    }
    public EditText getTexCantidadRef(){
        return edtCantidad;
    }
    public void setTextCantidad(String text) {
        edtCantidad.setText(text);
        listConteoCiclico.setClickable(false);
        imgClear.setVisibility(View.GONE);
        check = true;
    }
    public EditText getTexCodigoRefaccion(){
        return edtRefaccion;
    }
    public static ConteoCiclicoActivity instance()
    {
        return mInst;
    }



}

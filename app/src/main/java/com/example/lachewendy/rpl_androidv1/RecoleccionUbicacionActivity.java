package com.example.lachewendy.rpl_androidv1;

import android.support.v7.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import android.widget.Toast;

import com.RPLApplication;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.com.uny2.metodos.AlmacenesDbHelper;
import com.uny2.clases.ConteoCiclico;
import com.uny2.clases.DatosConfiguracion;
import com.uny2.clases.Recoleccion;
import com.uny2.clases.RecoleccionAdapter;
import com.uny2.clases.RecoleccionNuevoAdapter;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RecoleccionUbicacionActivity extends AppCompatActivity {

    boolean hiloEsperaEscaner;
    ListView listViewRecoleccion;
    ListView listSeleccion;
    EditText edtBuscador;
    Button btnSeleccionarTodos;
    Button btnRecolectarSeleccionados;
    Button btnCancelarSeleccionados;
    ProgressDialog progressDialog;
    DatosConfiguracion datosConfiguracion;
    Button btnOrden;
    double cantidad;
    Button btnLockRecoleccion;
    TextView btnBackRecoleccion;
    TextView txtTituloRecoleccion;
    TextView txtNumeroOrden;
    TextView txtFechaOrden;
    RecoleccionNuevoAdapter recoleccionAdapter;
    EditText edtRecoleccion;
    ImageView imgClear;
    EditText edtCantidadRecoleccion;
    ArrayList<Recoleccion> arrayListContado;
    org.json.JSONArray arrayCodigosGarantia;
    CountDownTimer contadorActualizar;

    private Button btnServicio;
    private Button btnEscanear;
    private Button btnCancelar;
    private TextView txtErrorMensaje;
    private Button btnConfirmarOrden;
    private EditText edtNumeroOrden;
    AlertDialog alertDialog;

    int posicion;
    boolean check = false;
    private ScaleGestureDetector mScaleDetector;
    private static RecoleccionUbicacionActivity mInst = null;
    public static final String TABLA = "RECOLECCIONNUEVO";
    public static final String RECEIVE_DATA = "unitech.scanservice.data";
    public static final String START_SCANSERVICE = "unitech.scanservice.start";
    public static final String SCAN2KEY_SETTING = "unitech.scanservice.scan2key_setting";
    boolean asignada;

    public static String ElegirRecoleccion;
    public static String codigoOrden;

    InputMethodManager imm;
    Double scaleList = 1.0;
    static final int tiempoPeticion = 55000;
    static int tiempoActualizarPeticion = 60000;
    String[] items;
    ArrayList<String> garantiaItems;
    String respuesta;
    ArrayList<Recoleccion> recoleccionArrayList;
    ArrayList<Recoleccion> pedirOrdenArrayList;
    HashMap<String, Double> valoresOriginales = new HashMap<String, Double>();
    boolean listActive = false;
    Button btnManual;
    ImageButton btnEnviar;
    ImageButton btnConfirmar;
    RelativeLayout relativePrincipal;
    RelativeLayout relativeOpciones;
    RelativeLayout relativeEscaner;
    EditText edtCodigoProducto;
    public static boolean isRPL;
    String[] outputStrArr;
    SharedPreferences sharedPreferencesRPL;
    SharedPreferences sharedpreferencesSelect;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorSelect;
    ArrayList<String> selectedItems;


    double cantidadRequerida;
    double cantidadContada;

    String codigoUbicacion;
    //String[] codigosUbicacion;
    String codigoUbicacionPropiedad;
    String codigoUbicacionConsigna;

    Button btnGenerarNegado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoleccion_ubicacion);
        mInst = this;
        cantidadRequerida = 0;
        cantidadContada = 0;
        //recoleccionArrayList = new ArrayList<Recoleccion>();
        hiloEsperaEscaner = false;
        codigoUbicacion = "";
        codigoUbicacionConsigna = "";
        codigoUbicacionPropiedad = "";
        btnBackRecoleccion = (TextView) findViewById(R.id.txtBackRecoleccion);
        btnOrden = (Button) findViewById(R.id.btnOrden);
        btnLockRecoleccion = (Button) findViewById(R.id.btnLockRecoleccion);
        btnManual = (Button) findViewById(R.id.btnManualRecoleccion);
        btnConfirmar = (ImageButton) findViewById(R.id.imgButtonConfirmar);
        btnEnviar = (ImageButton) findViewById(R.id.imgButtonEnviar);
        txtNumeroOrden = (TextView) findViewById(R.id.txtNumeroOrden);
        txtFechaOrden = (TextView) findViewById(R.id.txtFechaOrden);
        listViewRecoleccion = (ListView) findViewById(R.id.listRecoleccion);
        edtRecoleccion = (EditText) findViewById(R.id.edtCodigoRecoleccion);
        edtCantidadRecoleccion = (EditText) findViewById(R.id.edtCantidadRecoleccion);
        relativePrincipal = (RelativeLayout) findViewById(R.id.relativePrincipal);
        btnGenerarNegado = (Button) findViewById(R.id.btnGenerarNegado);
        imgClear = (ImageView) findViewById(R.id.imgClear);
        sharedPreferencesRPL = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_RPL, MODE_PRIVATE);
        sharedpreferencesSelect = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_SELECT, Context.MODE_PRIVATE);
        editor = sharedPreferencesRPL.edit();
        editorSelect = sharedpreferencesSelect.edit();

        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtRecoleccion.setText("");
                edtRecoleccion.requestFocus();
                imgClear.setVisibility(View.GONE);
            }
        });

        relativePrincipal.setVisibility(View.GONE);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        /**
         * Alerta para que el usuario escoja entre las opciones
         */
        AlmacenesDbHelper existeConteoPrevio = new AlmacenesDbHelper(getBaseContext());
        System.out.println("Cantidad Juntada->" + existeConteoPrevio.recoleccionActivado());
        arrayListContado = new ArrayList<>();
        if (existeConteoPrevio.recoleccionActivado() > 0) {


        } else {
            //alertInicial();
        }
        alertInicial();
        //Fin del ALERT


        edtRecoleccion.requestFocus();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        posicion = 0;

        btnManual.setEnabled(false);
        btnManual.setFocusable(false);
        btnGenerarNegado.setFocusable(false);
        btnManual.clearFocus();
        edtCantidadRecoleccion.setEnabled(false);


        edtRecoleccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (hiloEsperaEscaner == false) {
                        hiloEsperaEscaner = true;
                        new CountDownTimer(1000, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                String codigoConsigna = edtRecoleccion .getHint().toString() + ","+codigoUbicacionConsigna;
                                String codigoPropiedad = edtRecoleccion .getHint().toString() + ","+codigoUbicacionPropiedad;
                                System.out.println("CodigoUbicacion---->"+codigoUbicacion+ "lll->"+codigoConsigna+ "++"+codigoUbicacionConsigna+ "**"+codigoUbicacionPropiedad +  "codigo consigna->"+codigoConsigna +"++++++++---->"+s.toString().equalsIgnoreCase(codigoPropiedad) + "//////01.>"+s.toString().equalsIgnoreCase(codigoConsigna)+"Entrada"+s.toString());
                                if (s.toString().equalsIgnoreCase(edtRecoleccion .getHint().toString())  || s.toString().equalsIgnoreCase(codigoPropiedad)||s.toString().equalsIgnoreCase(codigoConsigna)) {
                                    String data = "";
                                    edtRecoleccion.requestFocus();
                                    edtCantidadRecoleccion.setFocusable(false);
                                    edtCantidadRecoleccion.setFocusableInTouchMode(true);
                                    btnConfirmar.setFocusable(false);
                                    btnManual.setFocusable(false);
                                    btnGenerarNegado.setFocusable(false);
                                    btnConfirmar.clearFocus();
                                    btnManual.clearFocus();
                                    try {
                                        cantidadContada = Double.parseDouble(edtCantidadRecoleccion.getText().toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        cantidadContada = 0;
                                    }
                                    System.out.println("valor orgininal: " + valoresOriginales.get(s.toString()));
                                    System.out.println("cantidadContada: " + cantidadContada);
                                    System.out.println("CodigoProveedo:r"+codigoUbicacion);
                                    System.out.println("Cantidadcontada->"+cantidadContada);
                                    System.out.println("valoresOrigginales->"+valoresOriginales.get(s.toString()));
//                                    if (cantidadContada + 1 < valoresOriginales.get(s.toString()) || cantidadContada + 1 == valoresOriginales.get(s.toString())) {
                                    Intent sendIntent = new Intent(RECEIVE_DATA);
                                    sendIntent.putExtra("text", edtRecoleccion.getText().toString());
                                    sendIntent.putExtra("activity", "recoleccion");
                                    sendIntent.putExtra("ubicacion", codigoUbicacionPropiedad.replace("-",""));
                                    sendIntent.putExtra("ubicacionC", codigoUbicacionConsigna.replace("-",""));
                                    sendBroadcast(sendIntent);
                                   /* } else {
                                        AlertDialog.Builder quitDialog
                                                = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                                        quitDialog.setTitle("Valor máximo");
                                        quitDialog.setCancelable(false);
                                        quitDialog.setMessage("No puedes recolectar mas piezas de las asignadas");
                                        quitDialog.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        quitDialog.show();
                                    }*/
                                } else {
                                    if(edtRecoleccion.getHint().toString().contains("Seleccione")){
                                        setMensaje("¡Atención!", "Debes seleccionar una refacción antes de escanear");
                                    }else {

                                        AlertDialog.Builder quitDialog
                                                = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                                        quitDialog.setTitle("Atención");
                                        quitDialog.setCancelable(false);
                                        quitDialog.setMessage("El código es incorrecto. Vuelve a intentarlo");
                                        quitDialog.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        quitDialog.show();
                                    }
                                }
                                edtRecoleccion.setText("");
                                edtRecoleccion.requestFocus();
                                hiloEsperaEscaner = false;
                            }
                        }.start();
                    }

                    //imgClear.setVisibility(View.VISIBLE);


                       /* String data = "";
                        edtRefaccion.requestFocus();
                        edtCantidad.setFocusable(false);
                        edtCantidad.setFocusableInTouchMode(true);
                        btnOkConteo.setFocusable(false);
                        btnManual.setFocusable(false);
                        btnOkConteo.clearFocus();
                        btnManual.clearFocus();
                        Intent sendIntent = new Intent(RECEIVE_DATA);
                        sendIntent.putExtra("text", edtRefaccion.getText().toString());
                        sendIntent.putExtra("activity", "conteo");
                        sendBroadcast(sendIntent);
                    }else{

                        System.out.println("Texxto->"+s.toString());
                        System.out.println("Longitudas->"+s.length() +"recoleccion->"+ edtRecoleccion.getText().length());

                            new CountDownTimer(700, 100) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    // do something after 1s
                                }

                                @Override
                                public void onFinish() {
                                    if(!s.toString().equals(edtRecoleccion.getHint().toString())  && errorEdtRecoleccion) {

                                        AlertDialog.Builder quitDialog
                                                = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                                        quitDialog.setTitle("Atención");
                                        quitDialog.setMessage("El código es incorrecto");
                                        quitDialog.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                edtRecoleccion.setText("");
                                                edtRecoleccion.requestFocus();
                                            }
                                        });
                                        quitDialog.show();
                                        errorEdtRecoleccion = false;
                                        //edtRecoleccion.;
                                    }
                                }
                            }.start();

                    }*/
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //System.out.println("CantidadEditable"+s.length()+"texto->"+edtRefaccion.length());
                /*if(!edtRecoleccion.getText().equals("")){
                    if(s.length()==edtRecoleccion.length()){
                        String data = "";
                        edtRecoleccion.requestFocus();
                        edtCantidadRecoleccion.setFocusable(false);
                        edtCantidadRecoleccion.setFocusableInTouchMode(true);
                        //btnOkConteo.setFocusable(false);
                        btnManual.setFocusable(false);
                        //btnOkConteo.clearFocus();
                        btnManual.clearFocus();
                        Intent sendIntent = new Intent(RECEIVE_DATA);
                        sendIntent.putExtra("text",edtRecoleccion.getText().toString());
                        sendIntent.putExtra("activity","recoleccion");
                        sendBroadcast(sendIntent);
                    }

                }*/
            }
        });

        eventos();

        //Deshabilitamos el scanner por entrada de texto y obligamos a que funcione por Broadcast
        /*Bundle bundle = new Bundle();
        bundle.putBoolean("scan2key", true);
        Intent mIntent = new Intent().setAction(SCAN2KEY_SETTING).putExtras(bundle);
        sendBroadcast(mIntent);*/

        //Iniciamos el ScanService Broadcast
        Bundle bundleScan = new Bundle();
        bundleScan.putBoolean("close", true);
        Intent mIntentScan = new Intent().setAction(START_SCANSERVICE).putExtras(bundleScan);
        sendBroadcast(mIntentScan);

        //CodigoGarantia
        arrayCodigosGarantia = new org.json.JSONArray();


    }

    public void alertInicial() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
        LayoutInflater li = LayoutInflater.from(RecoleccionUbicacionActivity.this);
        View promptsView = li.inflate(R.layout.fragment_determinar_recoleccion, null);
        btnServicio = (Button) promptsView.findViewById(R.id.btnServicio);
        btnEscanear = (Button) promptsView.findViewById(R.id.btnEscanear);
        btnCancelar = (Button) promptsView.findViewById(R.id.btnCancelar);
        txtErrorMensaje = (TextView) promptsView.findViewById(R.id.txtErrorMensaje);
        btnConfirmarOrden = (Button) promptsView.findViewById(R.id.btnConfirmar);
        edtNumeroOrden = (EditText) promptsView.findViewById(R.id.edtNumOrden);
        edtNumeroOrden.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtNumeroOrden, InputMethodManager.SHOW_IMPLICIT);
        try{
            DatosConfiguracion.almacen = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN,"almacen");
            if(DatosConfiguracion.almacen.equals("AMO1P")){
                btnEscanear.setVisibility(View.GONE);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        relativeOpciones = (RelativeLayout) promptsView.findViewById(R.id.relativeOpciones);
        relativeEscaner = (RelativeLayout) promptsView.findViewById(R.id.relativeEscanear);
        imm.hideSoftInputFromWindow(edtNumeroOrden.getWindowToken(), 0);


        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        if(recoleccionArrayList!= null){
            alertDialogBuilder.setNeutralButton("Salir", null);
        }else{
            alertDialogBuilder.setNeutralButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }

        final AlertDialog alertDialog = alertDialogBuilder.create();

        //Primera opcion de elegir servicios
        btnServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignada = true;
                relativePrincipal.setVisibility(View.VISIBLE);
                pedirServicio();
                //tiempoActualizar();
                alertDialog.dismiss();
                if(DatosConfiguracion.almacen.equals("AMO1P")){
                    isRPL = true;
                }else{
                    isRPL = false;
                }
                editor.putBoolean("rpl", isRPL);
                editor.commit();
            }
        });
        btnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignada = false;
                relativeOpciones.setVisibility(View.GONE);
                relativeEscaner.setVisibility(View.VISIBLE);
                edtNumeroOrden.requestFocus();

                if(DatosConfiguracion.almacen.equals("AMO1P")){
                    isRPL = true;
                }else{
                    isRPL = false;
                }
                editor.putBoolean("rpl", isRPL);
                editor.commit();
            }
        });

        //Fin de elegir servicios


        //opciones cuando seleccione escanear orden
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeOpciones.setVisibility(View.VISIBLE);
                edtNumeroOrden.setText("");
                relativeEscaner.setVisibility(View.GONE);
            }
        });

        btnConfirmarOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtNumeroOrden.getText().toString().isEmpty()) {
                    relativePrincipal.setVisibility(View.VISIBLE);
                    AlmacenesDbHelper eliminarTabla = new AlmacenesDbHelper(RecoleccionUbicacionActivity.this);
                    eliminarTabla.borrarConteo(TABLA);
                    escanearOrden();
                    alertDialog.dismiss();
                    System.out.println("Confirmar orden->" + edtNumeroOrden.getText());
                } else {
                    txtErrorMensaje.setText("Ingresa un código de orden para continuar");
                    txtErrorMensaje.setVisibility(View.VISIBLE);
                }


            }
        });
        //Fin de cuando selecciones orden
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }


    public static RecoleccionUbicacionActivity instance() {
        return mInst;
    }

    public void escanearOrden() {
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
        datosConfiguracion = new DatosConfiguracion(getBaseContext());
        final JSONObject JSONobj = new JSONObject();
        JSONobj.clear();
        final JSONArray JSONarr = new JSONArray();
        DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
        JSONobj.put("id_Usuario", Integer.parseInt(DatosConfiguracion.idUsuario));
        JSONobj.put("paso", 3);
        JSONobj.put("asignada",asignada );
        JSONobj.put("almacen", DatosConfiguracion.almacen);
        JSONobj.put("num_empleado", DatosConfiguracion.numEmpleado);
        JSONobj.put("orden_servicio", edtNumeroOrden.getText().toString());
        JSONarr.add(JSONobj.clone());
        System.out.println("" + JSONarr.toString());
        String Datos = URLEncoder.encode(JSONarr.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("DAORT->" + Datos);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + "ServicioRecoleccion",//?parametros=" + Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println("TEXTOUNO" + response);
                        try {
                            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
                            if (jsonObject.has("operacion")) {
                                AlertDialog.Builder quitDialog
                                        = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                                quitDialog.setTitle("Atencion");
                                quitDialog.setCancelable(false);
                                quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(jsonObject.getString("error"), "UTF-8")));
                                quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //finish();
                                        alertInicial();
                                    }
                                });
                                quitDialog.show();
                            } else {
                                AlmacenesDbHelper insertCon = new AlmacenesDbHelper(getBaseContext());
                                //Se obtiene la orden que despues de haber sido limpiada, almacenada, consultada y empaquetada se muestra la orden que desea trabajar.
                                String respuestaInsert = insertCon.insertNuevaRecoleccion(response);
                                System.out.println("lalala1111111");
                                System.out.println("Respuesta Base de Datos" + respuestaInsert);
                                obtenerRecoleccionOrden(respuestaInsert, true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AlertDialog.Builder quitDialog
                                    = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                            quitDialog.setTitle("Atención");
                            quitDialog.setCancelable(false);
                            quitDialog.setMessage("Ha ocurrido un error. Vuelve a intentarlo");
                            quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    escanearOrden();
                                }
                            });
                            quitDialog.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR->" + error);
                progressDialog.dismiss();
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setCancelable(false);
                quitDialog.setMessage("Ha ocurrido un error. Vuelve a intentar");
                quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        escanearOrden();
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
    }

    /**
     * Método que pide las refacciones sin necesidad de escanear la orden y muestra las que estan pendientes a recoelctar. el proceso
     * que realiza es de alamcenamiento prcesamiento y consulta de refacciones.
     */
    public void pedirServicio() {

        AlmacenesDbHelper isExistente = new AlmacenesDbHelper(getBaseContext());


        //   if(isExistente.recoleccionActivo()==0) { aqui
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
        datosConfiguracion = new DatosConfiguracion(getBaseContext());
        final JSONObject JSONobj = new JSONObject();
        JSONobj.clear();
        final JSONArray JSONarr = new JSONArray();
        DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
        JSONobj.put("id_Usuario", Integer.parseInt(DatosConfiguracion.idUsuario));
        JSONobj.put("almacen", DatosConfiguracion.almacen);
        JSONobj.put("asignada", asignada);
        JSONobj.put("num_empleado", DatosConfiguracion.numEmpleado);
        JSONobj.put("paso", 1);
        JSONarr.add(JSONobj.clone());
        System.out.println("" + JSONarr.toString());
        String Datos = URLEncoder.encode(JSONarr.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("DAORT->" + Datos);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioRecoleccion(),// + Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println("TEXTOUNO" + response);
                        try {
                            if (response.toString().length() > 2) {
                                org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                                org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
                                if (jsonObject.has("operacion")) {
                                    AlertDialog.Builder quitDialog
                                            = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                                    quitDialog.setTitle("Atención");
                                    quitDialog.setCancelable(false);
                                    quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(jsonObject.getString("error"), "UTF-8")));
                                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //finish();
                                            alertInicial();
                                        }
                                    });
                                    quitDialog.show();
                                } else {
                                    AlmacenesDbHelper insertCon = new AlmacenesDbHelper(getBaseContext());
                                    //Se obtiene los datos despues de haber sido procesados, almacenados, consultados y empaquetados para mostrarlos en la lista de seleccion
                                    String respuestaInsert = insertCon.insertNuevaRecoleccion(response);
                                    System.out.println("lalalalalala2");
                                    System.out.println("" + respuestaInsert);
                                    obtenerRecoleccionOrden(respuestaInsert, false);
                                }
                            } else {
                                AlertDialog.Builder quitDialog
                                        = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                                quitDialog.setTitle("ERROR");
                                quitDialog.setCancelable(false);
                                quitDialog.setMessage("Ha ocurrido un error con la conexión, intente nuevamente");
                                quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //finish();
                                        pedirServicio();
                                    }
                                });
                                quitDialog.show();


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR->" + error);
                progressDialog.dismiss();
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setCancelable(false);
                quitDialog.setMessage("Ha ocurrido un error. Vuelve a intentar");
                quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pedirServicio();
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
       /* }else{ aqui
            System.out.println("--->>>>> YA TIENE RECOLECCION");
            //eventos(); //descomentae por si no agarra otros eventos
            try {
                String ordenesActivas = isExistente.consultarOrdenRecoleccion();
                obtenerRecoleccionOrden(ordenesActivas);
                String retornoConteo = isExistente.resgresarRecoleccion();
                respuesta=retornoConteo;
                Recoleccion recoleccion = new Recoleccion();
                //obtenerRecoleccionOrden(retornoConteo);
                    ArrayList<Recoleccion> listClass =recoleccion.toArrayListWS(retornoConteo, 3);
                    recoleccionAdapter = new RecoleccionAdapter(getBaseContext(), listClass);
                    listViewRecoleccion.setAdapter(recoleccionAdapter);
                    listViewRecoleccion.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    for(Recoleccion x:listClass){
                        if(x.getEstatus() == 1){
                            arrayListContado.add(new Recoleccion(x.getIdRow(),x.getOrdenServicio(),x.getIdPegado(),x.getFechaPegado(),x.getSku(),
                                    x.getDescripcion(),x.getCantidad(),x.getCantidadRecolectada(),x.getUnidadMedida(),x.getUbicacion(),x.getEstatus(),
                                    x.isConfirmacion()));
                        }
                    }
                eventoPendiente();
            }catch (Exception ex){}

            /*
            String retornoConteo = isExistente.resgresarRecoleccion();
            respuesta=retornoConteo;
            Recoleccion recoleccion = new Recoleccion();
            try{
                obtenerRecoleccionOrden(retornoConteo);
                ArrayList<Recoleccion> listClass =recoleccion.toArrayListWS(retornoConteo, 3);
                recoleccionAdapter = new RecoleccionAdapter(getBaseContext(), listClass);
                listViewRecoleccion.setAdapter(recoleccionAdapter);
                listViewRecoleccion.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                for(Recoleccion x:listClass){
                    if(x.getEstatus() == 1){
                        arrayListContado.add(new Recoleccion(x.getIdRow(),x.getDescripcion(),x.getSku(),x.getEstatus(),x.getUbicacion(),x.getCantidad(),x.isConfirmacion()));
                    }
                }
                eventoPendiente();
            }catch(Exception e){
                e.printStackTrace();
            }*/

        //  } se agrego aqui
    }

    public void eventoPendiente() {
        listViewRecoleccion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);

                return false;
            }
        });
        listViewRecoleccion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cantidad = valoresOriginales.get(recoleccionAdapter.getItem(position).getSku());
                System.out.println("733SelectionEstatus Refaccion---->" + recoleccionAdapter.getItem(position).getEstatus());
               // codigoUbicacion = recoleccionAdapter.getItem(position).getUbicacion().replace("-","");
                System.out.println("Co++++dig2oUbicacion..-.--" + codigoUbicacion);
                try{
                    //codigosUbicacion = codigoUbicacion.split(",");
                    //codigoUbicacionConsigna = codigosUbicacion[0].replace("-", "");
                    //codigoUbicacionPropiedad = codigosUbicacion[1].replace("-","");
                    codigoUbicacionConsigna = recoleccionAdapter.getItem(position).getUbicacionC();
                    codigoUbicacionPropiedad = recoleccionAdapter.getItem(position).getUbicacionP();
                    System.out.println("CodigoUbicacionConsigna..-.--" + codigoUbicacionConsigna );
                    System.out.println("CodigoUbicacionPropiedad..-.--" + codigoUbicacionPropiedad );
                }catch (Exception e){
                    e.printStackTrace();
                    //codigoUbicacion = recoleccionAdapter.getItem(position).getUbicacion();
                    //System.out.println("CodigoUbicacion..-.--" + codigoUbicacion);
                }finally {

                    if (recoleccionAdapter.getItem(position).getEstatus() == 0) {
                        view.setSelected(true);
                        check = false;
                        posicion = position;
                        edtCantidadRecoleccion.setEnabled(false);
                        edtRecoleccion.requestFocus();
                        btnManual.setEnabled(true);
                        edtRecoleccion.setText("");
                        edtRecoleccion.setHint(recoleccionAdapter.getItem(position).getSku());
                        //codigoUbicacion = recoleccionAdapter.getItem(position).getUbicacion();
                        //System.out.println("CodigoUbicacion..-.--" + codigoUbicacion);
                        edtCantidadRecoleccion.setText("");
                        listViewRecoleccion.setEnabled(false);
                        listActive = true;
                        btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock));

                    } else {
                        edtRecoleccion.setHint(R.string.ingresa_codigo);
                        edtCantidadRecoleccion.setEnabled(false);
                        edtCantidadRecoleccion.setText("");
                    }
                }
            }
        });
        listViewRecoleccion.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (recoleccionAdapter.getItem(position).getEstatus() == 1) {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                    quitDialog.setTitle("EDITAR");
                    String pieza = recoleccionAdapter.getItem(position).getDescripcion();
                    quitDialog.setMessage("Deseas editar el conteo de:\n.[" + pieza + "]");
                    quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.out.println("768cantidad: " + cantidad);
                                    // recoleccionAdapter.getItem(position).setEstatus(0);
                                    // recoleccionAdapter.getItem(position).setCantidadRecolectada(0);
                                    int i = 0;
                                    for (Recoleccion x : arrayListContado) {
                                        if (recoleccionAdapter.getItem(position).getSku().equals(x.getSku())) {
                                            System.out.println(recoleccionAdapter.getItem(position).getSku() + "----" + x.getSku());
                                            //agregar metodo
                                            recoleccionAdapter.getItem(position).setCantidad(x.getCantidad() + x.getCantidadRecolectada());
                                            arrayListContado.remove(i);
                                            recoleccionAdapter.getItem(position).setEstatus(0);
                                            recoleccionAdapter.getItem(position).setFaltante(cantidad);
                                            recoleccionAdapter.getItem(position).setCantidadRecolectada(0);
                                            AlmacenesDbHelper insert = new AlmacenesDbHelper(getBaseContext());
                                            insert.updateLineaRecoleccionNuevo("0.0", x.getIdRow(), 2);
                                            recoleccionAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                        i++;
                                    }
                                    recoleccionAdapter.notifyDataSetChanged();
                                }
                            }

                    );
                    quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()

                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }

                    );

                    quitDialog.show();
                }
                return false;
            }
        });
    }

    AlertDialog alertDialogSeleccion = null;

    /**
     * Recibimos un JSOn de respuesta que contine el orden de pegado, fecha de pegado y id pegado
     * @param respuesta
     * @param escaner
     * @throws Exception
     */
    public void obtenerRecoleccionOrden(String respuesta, final boolean escaner) throws Exception {
        System.out.println("entra a obtenerRecoleccionOrden");
        org.json.JSONArray jsonArrayOrden = new org.json.JSONArray(respuesta);
        items = new String[jsonArrayOrden.length()];
        garantiaItems = new ArrayList<>();
        recoleccionArrayList = new ArrayList<>();

        //final String[] pegados = new String[jsonArrayOrden.length()];
        for (int i = 0; i < jsonArrayOrden.length(); i++) {
            org.json.JSONObject filas = new org.json.JSONObject(jsonArrayOrden.get(i).toString());
            items[i] = filas.getString("orden_servicio") + " / " + filas.getString("fecha_pegado");
            recoleccionArrayList.add(new Recoleccion(filas.getString("orden_servicio"), Integer.parseInt(filas.getString("id_pegado")), filas.getString("fecha_pegado")));
            //  pegados[i] = filas.getString("id_pegado");
            System.out.println("Elementos->" + items[i]);
        }
        btnLockRecoleccion.setVisibility(View.GONE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
        builder.setTitle("Seleccione la orden");
        builder.setNeutralButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (listViewRecoleccion.getVisibility() == View.GONE) {
                    System.out.println("DISMiIISISISIASIHFDSIAHFS");
                } else {
                    System.out.println("hace visible la lista");
                    listActive = true;
                    btnLockRecoleccion.setVisibility(View.VISIBLE);
                    //btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                    //listViewRecoleccion.setEnabled(true);
                }
            }
        });

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (escaner) {

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                    LayoutInflater li = LayoutInflater.from(RecoleccionUbicacionActivity.this);
                    View promptsView = li.inflate(R.layout.seleccion_recoleccion_list, null);
                    listSeleccion = (ListView) promptsView.findViewById(R.id.select_list);
                    edtBuscador = (EditText) promptsView.findViewById(R.id.edt_buscador);
                    edtBuscador.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(final CharSequence s, int start, int before, int count) {
                            if (s.length() > 0) {
                                if (hiloEsperaEscaner == false) {
                                    hiloEsperaEscaner = true;
                                    new CountDownTimer(1000, 100) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            // String codigoConsigna = edtRecoleccion.getHint().toString() + "," + codigoUbicacionConsigna;
                                            // String codigoPropiedad = edtRecoleccion.getHint().toString() + "," + codigoUbicacionPropiedad;
                                            System.out.println("cadena Introducida->"+s.toString());
                                            if (s.toString().length() > 0) {//.equalsIgnoreCase(edtBuscador.getHint().toString()) || s.toString().equalsIgnoreCase(codigoPropiedad) || s.toString().equalsIgnoreCase(codigoConsigna)) {
                                                String data = "";
                                                edtBuscador.requestFocus();

                                                /*System.out.println("valor orgininal: " + valoresOriginales.get(s.toString()));
                                                System.out.println("cantidadContada: " + cantidadContada);
                                                System.out.println("CodigoProveedo:r" + codigoUbicacion);
                                                System.out.println("Cantidadcontada->" + cantidadContada);
                                                System.out.println("valoresOrigginales->" + valoresOriginales.get(s.toString()));*/
                                                Intent sendIntent = new Intent(RECEIVE_DATA);
                                                sendIntent.putExtra("text", edtBuscador.getText().toString());
                                                sendIntent.putExtra("activity", "buscarRecoleccionPrevia");
                                                sendBroadcast(sendIntent);

                                            } else {
                                                if (edtBuscador.getHint().toString().contains("buscar")) {
                                                    //setMensaje("¡Atención!", "Debes seleccionar una refacción antes de escanear");
                                                    System.out.println("El codigo ha sido procesado, estoy en validacion seleccione");
                                                } else {
                                                    edtBuscador.setText("");
                                                    Toast.makeText(RecoleccionUbicacionActivity.this, "El código no se encuentra en la lista", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            //edtBuscador.setText("");
                                            edtBuscador.requestFocus();
                                            hiloEsperaEscaner = false;
                                        }
                                    }.start();
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    btnSeleccionarTodos = (Button) promptsView.findViewById(R.id.btnSeleccionarTodos);
                    btnRecolectarSeleccionados = (Button) promptsView.findViewById(R.id.btnRecolectarSeleccionados);
                    btnCancelarSeleccionados = (Button) promptsView.findViewById(R.id.btnCancelarSeleccionados);
                    imm.hideSoftInputFromWindow(edtNumeroOrden.getWindowToken(), 0);
                    btnSeleccionarTodos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("Lista de elementos->" + listSeleccion.getAdapter().getCount());
                            for (int i = 0; i < listSeleccion.getAdapter().getCount(); i++) {
                                listSeleccion.setItemChecked(i, true);
                            }
                            SparseBooleanArray checked = listSeleccion.getCheckedItemPositions();
                            selectedItems = new ArrayList<String>();
                            ArrayList<Recoleccion> selectedRecoleccion = new ArrayList<>();
                            for (int i = 0; i < checked.size(); i++) {
                                // Item position in adapter
                                int position = checked.keyAt(i);
                                // Add sport if it is checked i.e.) == TRUE!
                                if (checked.valueAt(i)) {
                                    recoleccionAdapter.getItem(position).setEstatus(0);
                                    selectedItems.add(recoleccionAdapter.getItem(position).getSku());
                                    //agregar filtro.
                                    selectedRecoleccion.add(recoleccionAdapter.getItem(position));
                                }
                            }

                            recoleccionAdapter = new RecoleccionNuevoAdapter(getBaseContext(), selectedRecoleccion);
                            listViewRecoleccion.setAdapter(recoleccionAdapter);
                            eventoListView();
                            btnLockRecoleccion.setVisibility(View.VISIBLE);
                            listViewRecoleccion.setVisibility(View.VISIBLE);

                            outputStrArr = new String[selectedItems.size()];

                            for (int i = 0; i < selectedItems.size(); i++) {
                                outputStrArr[i] = selectedItems.get(i);
                                System.out.println("Select->" + outputStrArr[i]);
                                editorSelect.putString("sku" + i, outputStrArr[i]);
                            }
                            editorSelect.putInt("cantidad", selectedItems.size());
                            editorSelect.commit();
                            alertDialogSeleccion.dismiss();
                        }
                    });
                    btnRecolectarSeleccionados.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SparseBooleanArray checked = listSeleccion.getCheckedItemPositions();
                            if (checked.size() == 0) {
                                Toast.makeText(RecoleccionUbicacionActivity.this, "Seleccione una o varias refacciones para recolectar", Toast.LENGTH_LONG).show();
                            } else {
                                selectedItems = new ArrayList<String>();
                                ArrayList<Recoleccion> selectedRecoleccion = new ArrayList<>();
                                for (int i = 0; i < checked.size(); i++) {
                                    // Item position in adapter
                                    int position = checked.keyAt(i);
                                    // Add sport if it is checked i.e.) == TRUE!
                                    if (checked.valueAt(i)) {
                                        recoleccionAdapter.getItem(position).setEstatus(0);
                                        selectedItems.add(recoleccionAdapter.getItem(position).getSku());
                                        selectedRecoleccion.add(recoleccionAdapter.getItem(position));
                                    }
                                }

                                recoleccionAdapter = new RecoleccionNuevoAdapter(getBaseContext(), selectedRecoleccion);
                                listViewRecoleccion.setAdapter(recoleccionAdapter);
                                eventoListView();
                                btnLockRecoleccion.setVisibility(View.VISIBLE);
                                listViewRecoleccion.setVisibility(View.VISIBLE);

                                outputStrArr = new String[selectedItems.size()];

                                for (int i = 0; i < selectedItems.size(); i++) {
                                    outputStrArr[i] = selectedItems.get(i);
                                    System.out.println("Select->" + outputStrArr[i]);
                                    editorSelect.putString("sku" + i, outputStrArr[i]);
                                }
                                editorSelect.putInt("cantidad", selectedItems.size());
                                editorSelect.commit();
                                alertDialogSeleccion.dismiss();
                            }

                        }
                    });
                    btnCancelarSeleccionados.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    alertDialogBuilder.setView(promptsView);

                    /*alertDialogBuilder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });*/
                    alertDialogSeleccion = alertDialogBuilder.create();

                    //Primera opcion de elegir servicios

                    btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                    listSeleccion.setEnabled(true);
                    String auxSplit = items[item];
                    String datos[] = auxSplit.split("/");
                    txtNumeroOrden.setText(datos[0]);
                    txtFechaOrden.setText(datos[1]);
                    AlmacenesDbHelper insertCon = new AlmacenesDbHelper(getBaseContext());
                    Recoleccion numOrden = recoleccionArrayList.get(item);
                    String respuestaOrden = insertCon.obtenerDescripcionOrdenRecoleccionNuevo(numOrden.getOrdenServicio(), numOrden.getIdPegado());
                    pedirOrdenArrayList = new ArrayList<Recoleccion>();
                    try {
                        org.json.JSONArray jsonArray = new org.json.JSONArray(respuestaOrden);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(i).toString());
                              /*  pedirOrdenArrayList.add(new Recoleccion(Integer.parseInt(jsonObject.getString("_id")),jsonObject.getString("descripcion"),jsonObject.getString("sku"),
                                        jsonObject.getInt("estatus"), jsonObject.getString("ubicacion"), Double.parseDouble(jsonObject.getString("cantidad")), jsonObject.getBoolean("confirmacion")));
                               */
                            pedirOrdenArrayList.add(new Recoleccion(Integer.parseInt(jsonObject.getString("_id")), jsonObject.getString("orden_servicio"),
                                    jsonObject.getInt("id_pegado"),
                                    jsonObject.getString("fecha_pegado"),
                                    jsonObject.getString("sku"), jsonObject.getString("descripcion"), jsonObject.getDouble("cantidad"),
                                    jsonObject.getDouble("cantidad_recolectada"),
                                    jsonObject.getString("unidad_medida"), jsonObject.getInt("estatus"),
                                    jsonObject.getBoolean("confirmacion"), jsonObject.getBoolean("garantia"), jsonObject.getString("ubicacionP"), jsonObject.getDouble("cantidadP"), jsonObject.getString("ubicacionC"), jsonObject.getDouble("cantidadC")));
                            valoresOriginales.put(jsonObject.getString("sku"), jsonObject.getDouble("cantidad"));


                        }
                        for (int i = 0; i < pedirOrdenArrayList.size() -1; i++) {
                            System.out.println("sku->" + pedirOrdenArrayList.get(i).getSku() + "->Cant" + pedirOrdenArrayList.get(i).getCantidad() + "CantP" + pedirOrdenArrayList.get(i).getCantidadP() + "CantC" + pedirOrdenArrayList.get(i).getCantidadC() + "ubicacionC" + pedirOrdenArrayList.get(i).getUbicacionC() + "UbicacionP" + pedirOrdenArrayList.get(i).getUbicacionP() + "->");
                        }
                        recoleccionAdapter = new RecoleccionNuevoAdapter(getBaseContext(), pedirOrdenArrayList, escaner);
                        listSeleccion.setAdapter(recoleccionAdapter);
                        listSeleccion.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        eventoListViewSeleccion();

                        //Fin de cuando selecciones orden
                        alertDialogSeleccion.setCancelable(false);
                        alertDialogSeleccion.setCanceledOnTouchOutside(false);
                        alertDialogSeleccion.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("NO TIENE SCANER");
                    listActive = true;
                    btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                    listViewRecoleccion.setEnabled(true);
                    String auxSplit = items[item];
                    String datos[] = auxSplit.split("/");
                    txtNumeroOrden.setText(datos[0]);
                    txtFechaOrden.setText(datos[1]);
                    AlmacenesDbHelper insertCon = new AlmacenesDbHelper(getBaseContext());
                    Recoleccion numOrden = recoleccionArrayList.get(item);
                    //Con la orden seleccionada se hara la consulta a la base para obtener el listado de productos y llenar la prelista para recolectar
                    String respuestaOrden = insertCon.obtenerDescripcionOrdenRecoleccionNuevo(numOrden.getOrdenServicio(), numOrden.getIdPegado());
                    pedirOrdenArrayList = new ArrayList<Recoleccion>();
                    try {
                        org.json.JSONArray jsonArray = new org.json.JSONArray(respuestaOrden);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(i).toString());
                              /*  pedirOrdenArrayList.add(new Recoleccion(Integer.parseInt(jsonObject.getString("_id")),jsonObject.getString("descripcion"),jsonObject.getString("sku"),
                                        jsonObject.getInt("estatus"), jsonObject.getString("ubicacion"), Double.parseDouble(jsonObject.getString("cantidad")), jsonObject.getBoolean("confirmacion")));
                               */
                            pedirOrdenArrayList.add(new Recoleccion(Integer.parseInt(jsonObject.getString("_id")), jsonObject.getString("orden_servicio"),
                                    jsonObject.getInt("id_pegado"),
                                    jsonObject.getString("fecha_pegado"),
                                    jsonObject.getString("sku"), jsonObject.getString("descripcion"), jsonObject.getDouble("cantidad"),
                                    jsonObject.getDouble("cantidad_recolectada"),
                                    jsonObject.getString("unidad_medida"),  jsonObject.getInt("estatus"),
                                    jsonObject.getBoolean("confirmacion"), jsonObject.getBoolean("garantia"), jsonObject.getString("ubicacionP"), jsonObject.getDouble("cantidadP"), jsonObject.getString("ubicacionC"), jsonObject.getDouble("cantidadC")));
                            valoresOriginales.put(jsonObject.getString("sku"), jsonObject.getDouble("cantidad"));

                        }
                        System.out.println("cantidad de valores originales: " + valoresOriginales.size());
                        //System.out.println("cantidad de 141303547 " + valoresOriginales.get("141303547"));
                        for (int i = 0; i < pedirOrdenArrayList.size() -1; i++) {
                            System.out.println("sku1->"+pedirOrdenArrayList.get(i).getSku()+"->Cant"+pedirOrdenArrayList.get(i).getCantidad()+"CantP"+pedirOrdenArrayList.get(i).getCantidadP() + "CantC"+pedirOrdenArrayList.get(i).getCantidadC()+"ubicacionC"+pedirOrdenArrayList.get(i).getUbicacionC()+"UbicacionP"+pedirOrdenArrayList.get(i).getUbicacionP()+"->");
                        }
                        recoleccionAdapter = new RecoleccionNuevoAdapter(getBaseContext(), pedirOrdenArrayList);
                        listViewRecoleccion.setAdapter(recoleccionAdapter);
                        eventoListView();
                        btnLockRecoleccion.setVisibility(View.VISIBLE);
                        listViewRecoleccion.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
        // alert.getWindow().setLayout(600,700);


    }


    public void eventoListView() {
        listViewRecoleccion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Selection ---->" + recoleccionAdapter.getItem(position).getEstatus());
                edtRecoleccion.setHint(recoleccionAdapter.getItem(position).getSku());
               // codigoUbicacion = recoleccionAdapter.getItem(position).getUbicacion();
                //System.out.println("CodigoUbicacion..-.--" + codigoUbicacion);
                try{
                    //codigosUbicacion = codigoUbicacion.split(",");
                    //codigoUbicacionConsigna = codigosUbicacion[0].replace("-", "");
                    //codigoUbicacionPropiedad = codigosUbicacion[1].replace("-","");
                    codigoUbicacionConsigna = recoleccionAdapter.getItem(position).getUbicacionC();
                    codigoUbicacionPropiedad = recoleccionAdapter.getItem(position).getUbicacionP();
                    System.out.println("CodigoUbicacionConsigna..-.--" + codigoUbicacionConsigna);
                    System.out.println("CodigoUbicacionPropiedad..-.--" + codigoUbicacionPropiedad);
                }catch (Exception e){
                    e.printStackTrace();
                    //codigoUbicacion = recoleccionAdapter.getItem(position).getUbicacion();
                    //System.out.println("CodigoUbicacion..-.--" + codigoUbicacion);
                }finally {

                    System.out.println("CodigoUbicacion..-.--" + codigoUbicacion);
                    if (("" + recoleccionAdapter.getItem(position).getEstatus()).equals("0")) {
                        view.setSelected(true);
                        posicion = position;
                        edtCantidadRecoleccion.setEnabled(false);
                        edtRecoleccion.requestFocus();
                        btnManual.setEnabled(true);
                        edtRecoleccion.setText("");
                        edtRecoleccion.setHint(recoleccionAdapter.getItem(position).getSku());
                        edtCantidadRecoleccion.setText("");
                        listViewRecoleccion.setEnabled(false);
                        cantidadRequerida = recoleccionAdapter.getItem(position).getCantidad();
                        cantidadContada = recoleccionAdapter.getItem(position).getCantidadRecolectada();
                        listActive = true;
                        btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock));
                        //activar el lock

                    } else {
                        edtRecoleccion.setHint(R.string.ingresa_codigo);
                        edtCantidadRecoleccion.setEnabled(false);
                        edtCantidadRecoleccion.setText("");
                    }
                }
            }
        });
    }

    public void eventoListViewSeleccion() {
        listSeleccion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (recoleccionAdapter.getItem(position).getEstatus() < 2) {
                    recoleccionAdapter.getItem(position).setEstatus(2);

                } else {
                    recoleccionAdapter.getItem(position).setEstatus(0);
                    //recoleccionAdapter.notifyDataSetChanged();

                }
                recoleccionAdapter.notifyDataSetChanged();
            }
        });
    }


    public void eventos() {


        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (!(Double.parseDouble(edtCantidadRecoleccion.getText().toString()) > (recoleccionAdapter.getItem(posicion).getCantidadP() + recoleccionAdapter.getItem(posicion).getCantidadC()))) {
                        final Double cantidadRecoleccion = Double.parseDouble(edtCantidadRecoleccion.getText().toString());
                        final Double cantidadRestante = valoresOriginales.get(recoleccionAdapter.getItem(posicion).getSku()) - cantidadRecoleccion;
                        System.out.println("cantidadRecoleccion: " + cantidadRecoleccion);
                        System.out.println("cantidadRestante: " + cantidadRestante);
                        System.out.println("valor original" + valoresOriginales.get(recoleccionAdapter.getItem(posicion).getSku()));
                        if ((cantidadRecoleccion <= valoresOriginales.get(recoleccionAdapter.getItem(posicion).getSku()))) {
                            if (!edtRecoleccion.getHint().equals(getResources().getString(R.string.ingresa_codigo))) {
                                if (("" + recoleccionAdapter.getItem(posicion).getEstatus()).equals("0")) {
                                    // if (edtRefaccion.getHint().equals(edtRefaccion.getText().toString().trim())) {
                                    if (edtCantidadRecoleccion.getText().toString().trim().length() > 0) {
                                        final ConteoCiclico conteoCiclico = new ConteoCiclico();
                                        //System.out.println("POSICION->" + posicion + "ELemento->" + arrayAdapter.getItem(posicion).getDescripcion() + "Catnidad" + edtCantidad.getText().toString());
                                        conteoCiclico.setSku(edtRecoleccion.getText().toString());
                                        AlertDialog.Builder quitDialog
                                                = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                                        quitDialog.setTitle("Atención");
                                        quitDialog.setMessage("¿El conteo realizado es correcto?");
                                        quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Como se veran afectadas las cantidadsdes de ubicacion.
                                                AlmacenesDbHelper insert = new AlmacenesDbHelper(getBaseContext());
                                                insert.updateLineaRecoleccionNuevo(edtCantidadRecoleccion.getText().toString(), Double.toString(cantidadRestante), recoleccionAdapter.getItem(posicion).getIdRow(), 1);
                                                recoleccionAdapter.getItem(posicion).setEstatus(1);
                                                recoleccionAdapter.getItem(posicion).setCantidad(cantidadRestante);
                                                recoleccionAdapter.getItem(posicion).setCantidadRecolectada(cantidadRecoleccion);
                                                recoleccionAdapter.getItem(posicion).setFaltante(cantidadRestante);
                                                //DESCOMENTAR ESTO 29/11/16
                                                System.out.println("GARANTIA?: " + recoleccionAdapter.getItem(posicion).isGarantia());
                                                System.out.println("edtCantidadRecoleccion: " + (int) Double.parseDouble(edtCantidadRecoleccion.getText().toString().trim()));
                                                if (recoleccionAdapter.getItem(posicion).isGarantia()) {//.contains(recoleccionAdapter.getItem(posicion).getIdPegado())) {
                                                    mostrarDialogInicial((int) Double.parseDouble(edtCantidadRecoleccion.getText().toString().trim()), recoleccionAdapter.getItem(posicion).getSku());
                                                }
                                                arrayListContado.add(new Recoleccion(recoleccionAdapter.getItem(posicion).getIdRow(), recoleccionAdapter.getItem(posicion).getOrdenServicio(),
                                                        recoleccionAdapter.getItem(posicion).getIdPegado(), recoleccionAdapter.getItem(posicion).getFechaPegado(),
                                                        recoleccionAdapter.getItem(posicion).getSku(), recoleccionAdapter.getItem(posicion).getDescripcion(),
                                                        recoleccionAdapter.getItem(posicion).getCantidad(), Double.parseDouble(edtCantidadRecoleccion.getText().toString()),
                                                        recoleccionAdapter.getItem(posicion).getUnidadMedida(), 1, cantidadRestante, false, Boolean.parseBoolean(recoleccionAdapter.getItem(posicion).getOrdenGarantia()),
                                                        recoleccionAdapter.getItem(posicion).getUbicacionP(), recoleccionAdapter.getItem(posicion).getCantidadP(),
                                                        recoleccionAdapter.getItem(posicion).getUbicacionC(), recoleccionAdapter.getItem(posicion).getCantidadC()));
                                                recoleccionAdapter.notifyDataSetChanged();
                                                edtCantidadRecoleccion.setText("");
                                                edtCantidadRecoleccion.setEnabled(false);
                                                edtRecoleccion.setText("");
                                                edtRecoleccion.setHint(R.string.ingresa_codigo);
                                                listViewRecoleccion.setEnabled(true);
                                                listActive = false;
                                                btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                                                eventoPendiente();
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
                                                = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                                        quitDialog.setTitle("ERROR");
                                        quitDialog.setCancelable(false);
                                        quitDialog.setMessage("Se requiere cantidad!!!");
                                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                listViewRecoleccion.setEnabled(true);
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
                        } else {
                            AlertDialog.Builder quitDialog
                                    = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                            quitDialog.setTitle("ERROR");
                            quitDialog.setCancelable(false);
                            quitDialog.setMessage("Modifica la cantidad, no puedes recolectar mas piezas de las pedidas");
                            quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listViewRecoleccion.setEnabled(true);
                                }
                            });
                            quitDialog.show();

                        }
                    }else{
                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                        quitDialog.setTitle("ERROR");
                        quitDialog.setCancelable(false);
                        quitDialog.setMessage("No puedes recolectar una cantidad mayor a la existencia");
                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listViewRecoleccion.setEnabled(true);
                            }
                        });

                        quitDialog.show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayListContado.size() > 0 && arrayListContado.size() == recoleccionAdapter.getCount()) {
                    System.out.println("Se debe enviar la recoleccion total");
                        AlertDialog.Builder confirmarAlert = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                        confirmarAlert.setCancelable(false);
                        confirmarAlert.setMessage("¿Estás seguro que deseas enviar las refacciones?");
                        confirmarAlert.setTitle("¡Atención!");
                        confirmarAlert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    envioRecoleccion(true);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        confirmarAlert.setNegativeButton("Cancelar", null);
                        confirmarAlert.create();
                        confirmarAlert.show();
                } else {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                    quitDialog.setTitle("AVISO!!");
                    quitDialog.setMessage("Es necesario recolectar todos los elementos");
                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    quitDialog.show();
                }
            }
        });


        btnOrden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //actualizarOrden();
                if (arrayListContado.size() == 0) {
                    alertInicial();
                }

            }
        });

        btnBackRecoleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                onBackPressed();
                /*if (!isPrepareCount()) {
                    //contadorActualizar.cancel();
                    onBackPressed();
                } else {

                }*/
            }
        });
//        edtRecoleccion.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                imm.hideSoftInputFromWindow(edtRecoleccion.getWindowToken(), 0);
//                edtRecoleccion.requestFocus();
//                return true;
//            }
//        });

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
                        scaleList = scaleList + 0.01;
                        listViewRecoleccion.setScaleX(Float.parseFloat(Double.toString(scaleList)));
                        listViewRecoleccion.setScaleY(Float.parseFloat(Double.toString(scaleList)));

                    }
                } else {
                    if (scaleList > 1.0) {
                        scaleList = scaleList - 0.01;
                        listViewRecoleccion.setScaleX(Float.parseFloat(Double.toString(scaleList)));
                        listViewRecoleccion.setScaleY(Float.parseFloat(Double.toString(scaleList)));
                    }
                }

                return false;
            }
        });


        listViewRecoleccion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);

                return false;
            }
        });

        btnLockRecoleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listActive) {
                    listActive = false;
                    listViewRecoleccion.setEnabled(true);
                    btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                } else {
                    listActive = true;
                    listViewRecoleccion.setEnabled(false);
                    btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock));
                }

            }
        });


        btnGenerarNegado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtCantidadRecoleccion.getText().toString().isEmpty()){
                    AlertDialog.Builder alertNegado = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                    alertNegado.setCancelable(false);
                    alertNegado.setTitle("Aviso");
                    alertNegado.setMessage("Debes validar la refacción para marcarla como no existente");
                    alertNegado.setNeutralButton("Entendido", null);
                    alertNegado.create();
                    alertNegado.show();
                }else{
                    if(check){
                        edtCantidadRecoleccion.setEnabled(false);
                        edtCantidadRecoleccion.setText(Double.toString(Double.parseDouble("0")));
                        /*Validar Negado*/
                        try{
                        final Double cantidadRecoleccion = 0.0;
                        final Double cantidadRestante = valoresOriginales.get(recoleccionAdapter.getItem(posicion).getSku()) - cantidadRecoleccion;
                        System.out.println("cantidadRecoleccion: " + cantidadRecoleccion);
                        System.out.println("cantidadRestante: " + cantidadRestante);
                        System.out.println("valor original" + valoresOriginales.get(recoleccionAdapter.getItem(posicion).getSku()));

                                if (("" + recoleccionAdapter.getItem(posicion).getEstatus()).equals("0")) {
                                    final ConteoCiclico conteoCiclico = new ConteoCiclico();
                                    //System.out.println("POSICION->" + posicion + "ELemento->" + arrayAdapter.getItem(posicion).getDescripcion() + "Catnidad" + edtCantidad.getText().toString());
                                    conteoCiclico.setSku(edtRecoleccion.getText().toString());
                                    AlmacenesDbHelper insert = new AlmacenesDbHelper(getBaseContext());
                                    insert.updateLineaRecoleccionNuevo(edtCantidadRecoleccion.getText().toString(), Double.toString(cantidadRestante), recoleccionAdapter.getItem(posicion).getIdRow(), 1);
                                    recoleccionAdapter.getItem(posicion).setEstatus(1);
                                    recoleccionAdapter.getItem(posicion).setCantidad(cantidadRestante);
                                    recoleccionAdapter.getItem(posicion).setCantidadRecolectada(cantidadRecoleccion);
                                    recoleccionAdapter.getItem(posicion).setFaltante(cantidadRestante);
                                    //DESCOMENTAR ESTO 29/11/16
                                    System.out.println("GARANTIA?: " +recoleccionAdapter.getItem(posicion).isGarantia());
                                    System.out.println("edtCantidadRecoleccion: " +(int) Double.parseDouble(edtCantidadRecoleccion.getText().toString().trim()));
                                    if (recoleccionAdapter.getItem(posicion).isGarantia()){//.contains(recoleccionAdapter.getItem(posicion).getIdPegado())) {
                                        mostrarDialogInicial((int) Double.parseDouble(edtCantidadRecoleccion.getText().toString().trim()),  recoleccionAdapter.getItem(posicion).getSku());
                                    }
                                    arrayListContado.add(new Recoleccion(recoleccionAdapter.getItem(posicion).getIdRow(), recoleccionAdapter.getItem(posicion).getOrdenServicio(),
                                            recoleccionAdapter.getItem(posicion).getIdPegado(), recoleccionAdapter.getItem(posicion).getFechaPegado(),
                                            recoleccionAdapter.getItem(posicion).getSku(), recoleccionAdapter.getItem(posicion).getDescripcion(),
                                            recoleccionAdapter.getItem(posicion).getCantidad(), Double.parseDouble(edtCantidadRecoleccion.getText().toString()),
                                            recoleccionAdapter.getItem(posicion).getUnidadMedida(), 1, cantidadRestante, false,Boolean.parseBoolean(recoleccionAdapter.getItem(posicion).getOrdenGarantia()),
                                            recoleccionAdapter.getItem(posicion).getUbicacionP(),recoleccionAdapter.getItem(posicion).getCantidadP(),
                                            recoleccionAdapter.getItem(posicion).getUbicacionC(),recoleccionAdapter.getItem(posicion).getCantidadC()));
                                    recoleccionAdapter.notifyDataSetChanged();
                                    edtCantidadRecoleccion.setText("");
                                    edtCantidadRecoleccion.setEnabled(false);
                                    edtRecoleccion.setText("");
                                    edtRecoleccion.setHint(R.string.ingresa_codigo);
                                    listViewRecoleccion.setEnabled(true);
                                    listActive = false;
                                    btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                                    eventoPendiente();
                                }


                    }catch(Exception e){
                        e.printStackTrace();
                    }
                        /*Fin Validar negado*/
                    }
                }
            }
        });

        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edtCantidadRecoleccion.setFocusableInTouchMode(true);
                if (check) {
                    edtCantidadRecoleccion.requestFocus();
                    edtCantidadRecoleccion.setEnabled(true);
                    // btnManual.setText("OK");
                } else {

                    try {

                        //Comentar para uso sin HANDHELD
                        if (edtCantidadRecoleccion.getText().toString().equals("")) {
                            setMensaje("Atención!", "Debes escanear para confirmar el número de refacciones");
                        } else {
                            btnManual.setText("MANUAL");
                            edtCantidadRecoleccion.setEnabled(false);
                            edtCantidadRecoleccion.setText(Double.toString(Double.parseDouble(edtCantidadRecoleccion.getText().toString())));
                        }
                        //SOlo la condicion sin el contenido del else
                    } catch (Exception e) {
                    }
                    /*AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
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
            }
        });
    }

    public void setCodigoRecoleccion(String text) {
        edtRecoleccion.setText(text);
        edtRecoleccion.setFocusable(true);
        edtRecoleccion.requestFocus();
    }

    public void setCantidadRecoleccion(String text) {
        edtCantidadRecoleccion.setText(text);
        edtRecoleccion.requestFocus();
        listViewRecoleccion.setClickable(false);
        imgClear.setVisibility(View.GONE);
        check = true;
    }

    public void focus() {
        edtRecoleccion.requestFocus();
    }

    public void buscadorPrevioFocus(){
        edtBuscador.requestFocus();
    }

    public EditText getCantidadRecoleccion() {
        return edtCantidadRecoleccion;
    }

    public EditText getCodigoRecoleccion() {
        return edtRecoleccion;
    }
    public EditText getCodigPrevioBuscador(){
        return edtBuscador;
    }

    public ArrayList<Recoleccion> getPrevioRecoleccionArrayList(){
        return pedirOrdenArrayList;
    }

    public void noEncontrada(){
        edtBuscador.setText("");
        Toast.makeText(this, "No se ha encontrado la refacción en la lista",Toast.LENGTH_LONG).show();
    }

    public void refreshRecoleccionList(int position){
        listSeleccion.setItemChecked(position,true);
        if (recoleccionAdapter.getItem(position).getEstatus() < 2) {
            recoleccionAdapter.getItem(position).setEstatus(2);
        } else {
            recoleccionAdapter.getItem(position).setEstatus(0);
            //recoleccionAdapter.notifyDataSetChanged();

        }
        recoleccionAdapter.notifyDataSetChanged();
        edtBuscador.setText("");
        //listSeleccion.deferNotifyDataSetChanged();
        //System.out.println("NotifyDataChanged"+listSeleccion.onRemoteAdapterConnected());
        //recoleccionAdapter.notifyDataSetChanged();

    }


    public void setMensaje(String titulo, String mensaje) {
        AlertDialog.Builder quitDialog
                = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
        quitDialog.setTitle(titulo);
        quitDialog.setMessage(mensaje);
        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.setCancelable(false);
        quitDialog.show();
    }

    public void envioRecoleccion(boolean completo) throws Exception {
        JSONObject JSONobj = new JSONObject();
        JSONObject jsonObjectSelect = new JSONObject();
        final JSONArray JSONarr = new JSONArray();
        JSONArray jsonArraySelect = new JSONArray();
        JSONArray jsonArrayFaltante = new JSONArray();
        JSONobj.clear();
//        JSONarr.add(JSONobj.clone());
        JSONobj.clear();

        JSONobj.put("paso", 2);
        JSONobj.put("completo", true);
        if (!isRPL) {
            JSONobj.put("almacen", DatosConfiguracion.almacen);
        }else{
            JSONobj.put("almacen", DatosConfiguracion.almacen);
        }
        JSONobj.put("rpl", sharedPreferencesRPL.getBoolean("rpl", true)); //isRPL
        DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
        JSONobj.put("id_usuario", Integer.parseInt(DatosConfiguracion.idUsuario));
        JSONobj.put("idp", arrayListContado.get(0).getIdPegado());
        JSONobj.put("orden_servicio", arrayListContado.get(0).getOrdenServicio());
        JSONobj.put("garantias", arrayCodigosGarantia);
        JSONobj.put("asignada",asignada );
        int size = sharedpreferencesSelect.getInt("cantidad", 0);
        if (outputStrArr != null || sharedpreferencesSelect.getString("sku0", "").length() > 0) {
            for (int i = 0; i < size; i++) {
                jsonObjectSelect.put("sku" + i, sharedpreferencesSelect.getString("sku" + i, "")); //selectedItems.get(i));
                //System.out.println(""+outputStrArr[i]);
            }
            jsonArraySelect.add(jsonObjectSelect);
            JSONobj.put("refacciones", jsonArraySelect);
        }
        //JSONarr.add(JSONobj.clone());
        for (Recoleccion x : arrayListContado) {
            //JSONobj.clear();
            JSONObject jsonObject = new JSONObject();
            if (x.getFaltante() > 0) {
                jsonObject.put("orden", x.getOrdenServicio());
                jsonObject.put("um", x.getUnidadMedida());
                jsonObject.put("sku", x.getSku());
                jsonObject.put("idp", arrayListContado.get(0).getIdPegado());
                jsonObject.put("faltantes", x.getFaltante()); //faltante
                if(x.isGarantia()){
                    System.out.println("ES GARANTIAFaltante?: "+x.isGarantia());
                }
                //JSONarr.add(JSONobj.clone());
                jsonArrayFaltante.add(jsonObject.clone());
            }
        }
        JSONobj.put("faltantes",jsonArrayFaltante); //faltante
        JSONarr.add(JSONobj.clone());
        System.out.println("ENVIO---->>>>" + JSONarr.toString());
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Enviando datos...", true);
        String Datos = URLEncoder.encode(JSONarr.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        datosConfiguracion = new DatosConfiguracion();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + "ServicioRecoleccion",//?parametros=" + Datos, //DatosConfiguracion.home+"recoleccion.php",//
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("RespUEsTARecoleccion" + response);
                        try {
                            analizarRespuestaRecoleccion(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR-====>" + error);
                progressDialog.dismiss();
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setCancelable(false);
                quitDialog.setMessage("No fue posible obtener una respuesta.");
                quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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

    }

    public void analizarRespuestaRecoleccion(String respuesta) throws Exception {
      /*  ArrayList<Recoleccion> respuestaArray=Recoleccion.toArrayListWS(respuesta, 4);
        AlmacenesDbHelper updateFin = new AlmacenesDbHelper(getBaseContext());
        for(Recoleccion x:respuestaArray){
            if(x.isConfirmacion()){
                int i=0;
                for(Recoleccion y:arrayListContado){
                    if(x.getDescripcion().equals(y.getDescripcion())){
                        updateFin.updateLineaRecoleccion(Boolean.toString(x.isConfirmacion()), y.getIdRow(), 3);
                        recoleccionAdapter.getItem(y.getIdRow()-1).setConfirmacion(true);
                        recoleccionAdapter.remove(recoleccionAdapter.getItem(y.getIdRow() - 1));
                        arrayListContado.remove(i);
                        recoleccionAdapter.notifyDataSetChanged();
                    }
                    i++;
                }
                i=0;
            }else{

            }
        }*/

        ArrayList<Recoleccion> respuestaArray = Recoleccion.toArrayListRWS(respuesta, 4); //validar que recibo

        AlmacenesDbHelper updateFin = new AlmacenesDbHelper(getBaseContext());
        for (Recoleccion x : respuestaArray) {
            if (x.isConfirmacion()) {

                int i = 0;
                for (Recoleccion y : arrayListContado) {
                    if (x.getDescripcion().equals(y.getDescripcion())) {
                        updateFin.updateLineaRecoleccionNuevo(Boolean.toString(x.isConfirmacion()), y.getIdRow(), 3);
                        //arrayAdapter.getItem(y.getIdRow()-1).setConfirmacion(true);
                        //arrayAdapter.remove(arrayAdapter.getItem(y.getIdRow() - 1));
                        //arrayListContado.remove(i);
                        //arrayAdapter.notifyDataSetChanged();
                    }
                    i++;
                }
                i = 0;

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

            } else {
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

       /* ArrayList<Recoleccion> actualizadoArray = Recoleccion.toArrayListRWS(updateFin.resgresarRecoleccionNuevo(), 5);
        if (actualizadoArray.size() > 0) {
            recoleccionAdapter = new RecoleccionNuevoAdapter(getBaseContext(), actualizadoArray);
            listViewRecoleccion.setAdapter(recoleccionAdapter);
            listViewRecoleccion.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            for (Recoleccion x : actualizadoArray) {
                System.out.println("Pendientes->"+x.getSku());
                if (x.getEstatus() == 1) {
                    arrayListContado.add(new Recoleccion(x.getIdRow(), x.getOrdenServicio(), x.getIdPegado(), x.getFechaPegado(), x.getSku(),
                            x.getDescripcion(), x.getCantidad(), x.getCantidadRecolectada(), x.getUnidadMedida(),"", x.getEstatus(),
                            x.isConfirmacion()));
                }
            }
            arrayListContado.clear();
            eventoPendiente();
        } else {*/
            org.json.JSONArray uno = new org.json.JSONArray(respuesta);
            org.json.JSONObject dos =new org.json.JSONObject(uno.getString(0));
            if(dos.getString("confirmacion").equalsIgnoreCase("true")){
               // if (actualizadoArray.size() == 0) {
                    updateFin.borrarConteo(TABLA);
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                    quitDialog.setTitle("Recoleccion Concluido");
                    quitDialog.setCancelable(false);
                    quitDialog.setMessage("Has terminado!");
                    quitDialog.setNeutralButton("ACEPTAR", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    quitDialog.show();
                //}
            }else{
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                quitDialog.setTitle("Atención");
                quitDialog.setCancelable(false);
                quitDialog.setMessage("No se ha logrado guardar la información, favor de intentarlo nuevamente");
                quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                quitDialog.show();
            }
            /*if (actualizadoArray.size() == 0) {
                updateFin.borrarConteo("RECOLECCION");
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                quitDialog.setTitle("Recoleccion Concluido");
                quitDialog.setCancelable(false);
                quitDialog.setMessage("Has terminado!");
                quitDialog.setNeutralButton("ACEPTAR", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                quitDialog.show();
            }*/
       // }


    }

    public boolean isPrepareCount() {
        boolean respuesta = false;
        for (Recoleccion x : arrayListContado) {
            respuesta = true;
        }
        return respuesta;
    }


    public void actualizarOrden() {
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
        datosConfiguracion = new DatosConfiguracion(getBaseContext());
        final JSONObject JSONobj = new JSONObject();
        JSONobj.clear();
        final JSONArray JSONarr = new JSONArray();
        DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
        JSONobj.put("id_Usuario", Integer.parseInt(DatosConfiguracion.idUsuario));
        JSONobj.put("paso", 1);
        JSONarr.add(JSONobj.clone());
        System.out.println("" + JSONarr.toString());
        String Datos = URLEncoder.encode(JSONarr.toString());


        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("DAORT->" + Datos);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + "ServicioRecoleccion",//?parametros=" + Datos, //DatosConfiguracion.home+"recoleccion.php",//
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        tiempoActualizarPeticion = 60000;
                        System.out.println("TEXTOUNO" + response);
                        try {
                            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
                            AlmacenesDbHelper insertCon = new AlmacenesDbHelper(getBaseContext());
                            insertCon.borrarConteo(TABLA);
                            String respuestaInsert = insertCon.insertNuevaRecoleccion(response);
                            System.out.println("lalalalalal3333333");
                            System.out.println("" + respuestaInsert);
                            obtenerRecoleccionOrden(respuestaInsert, false);
                        } catch (TimeoutException t) {
                            //actualizarOrden();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR->" + error);
                tiempoActualizarPeticion = 10000;
                progressDialog.dismiss();

                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setCancelable(false);
                quitDialog.setMessage("Ha ocurrido un error con la conexión. Vuelve a intentar");
                quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pedirServicio();
                    }
                });
                quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // finish();
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
    }

    @Override
    public void onBackPressed() {
        //Si hay conteo pendiente

        AlmacenesDbHelper almacenesDbHelper = new AlmacenesDbHelper(getBaseContext());

        if (almacenesDbHelper.consultarOrdenRecoleccionActiva().isEmpty()) {
           // System.out.println("No hay nada Termina");
           // AlmacenesDbHelper updateFin = new AlmacenesDbHelper(getBaseContext());
           // updateFin.borrarConteo(TABLA);
           // finish();
            condicionarSalida();
        } else {

            AlertDialog.Builder quitDialog
                    = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
            quitDialog.setTitle("AVISO!!!");
            quitDialog.setMessage("Haz iniciado recolección si regresas, este se perderá.\n¿Deseas salir?");

            quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("Nborrar");
                    AlmacenesDbHelper updateFin = new AlmacenesDbHelper(getBaseContext());
                    updateFin.borrarConteo(TABLA);
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

        //finish();
        //Sino
        //borra contenido en tablas


    }
    int articulos_garantia = 0;
    public void mostrarDialogInicial( int cantidad, final String codigoRefaccion){
        articulos_garantia = cantidad;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecoleccionUbicacionActivity.this);
        LayoutInflater li = LayoutInflater.from(RecoleccionUbicacionActivity.this);
        View promptsView = li.inflate(R.layout.pre_ubicacion_prompt, null);
        edtCodigoProducto = (EditText) promptsView.findViewById(R.id.txtNumeroPedido);
        TextView tvNumeroped = (TextView) promptsView.findViewById(R.id.tvNumeroped);
        Button btnValidarPre = (Button) promptsView.findViewById(R.id.btnValidarPre);
        Button btnCancelarPre = (Button) promptsView.findViewById(R.id.btnCancelarPre);
        TextView txtNumeroPedidoError = (TextView) promptsView.findViewById(R.id.txtNumeroPedidoError);
        tvNumeroped.setText("Ingresar codigo nuevo de esta refacción para garantía");
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false)
                .setTitle("Refacción con garantia");
        btnValidarPre.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 //cargarLista();
                                                 String codigoOrdenPedido = edtCodigoProducto.getText().toString();
//
                                                 garantiaItems.add(codigoOrdenPedido);
                                                 System.out.println("CodigoNuevo->" + codigoOrdenPedido);
                                                 System.out.println("CodigoViejo->" + codigoRefaccion);
                                                 articulos_garantia--;
                                                 if (articulos_garantia > 0) {
                                                     alertDialog.dismiss();
                                                     mostrarDialogInicial(articulos_garantia, codigoRefaccion);
                                                     System.out.println("articulos Garantia" + articulos_garantia);
                                                 } else {
                                                     try {
                                                         org.json.JSONObject codigoViejo = new org.json.JSONObject();
                                                         codigoViejo.put("codigo_viejo", codigoRefaccion);
                                                         org.json.JSONArray codigosNuevoJSONArray = new org.json.JSONArray();
                                                         for (int i = 0; i < garantiaItems.size(); i++) {
                                                             org.json.JSONObject codigoNuevo = new org.json.JSONObject();
                                                             codigoNuevo.put("codigo_nuevo", garantiaItems.get(i));
                                                             codigosNuevoJSONArray.put(codigoNuevo);
                                                         }
                                                         codigoViejo.put("codigos_garantia", codigosNuevoJSONArray);
                                                         arrayCodigosGarantia.put(codigoViejo);
                                                         System.out.println("GarantisJSON->" + arrayCodigosGarantia.toString());
                                                         garantiaItems.clear();
                                                     } catch (Exception e) {
                                                         e.printStackTrace();
                                                     } finally {
                                                         alertDialog.dismiss();
                                                     }
                                                 }
                                                 //arrayCodigosGarantia.add(codigoOrdenPedido);
                                             }
                                         }

        );
        btnCancelarPre.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View v) {
                                                  // overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
                                                  //finish();
                                                  alertDialog.dismiss();
                                              }
                                          }

        );
        alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void condicionarSalida(){
        if(listViewRecoleccion.getAdapter().getCount()>0){
            AlertDialog.Builder salidaUbicacion = new AlertDialog.Builder(this);
            salidaUbicacion.setCancelable(false);
            salidaUbicacion.setTitle("¡Atención!");
            salidaUbicacion.setMessage("¿Seguro que deseas salir?");
            salidaUbicacion.setNegativeButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlmacenesDbHelper updateFin = new AlmacenesDbHelper(getBaseContext());
                    updateFin.borrarConteo(TABLA);
                    finish();
                }
            });
            salidaUbicacion.setPositiveButton("No", null);
            salidaUbicacion.create();
            salidaUbicacion.show();
        }else{
            finish();
        }
    }


}




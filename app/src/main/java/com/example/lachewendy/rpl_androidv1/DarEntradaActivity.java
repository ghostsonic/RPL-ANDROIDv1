package com.example.lachewendy.rpl_androidv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.com.uny2.metodos.AlmacenesDbHelper;
import com.uny2.clases.ConteoCiclico;
import com.uny2.clases.ConteoCiclicoAdapter;
import com.uny2.clases.DatosConfiguracion;
import com.uny2.clases.Entrada;
import com.uny2.clases.EntradasAdapter;
import com.uny2.clases.Recoleccion;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DarEntradaActivity extends AppCompatActivity {

    private EntradasAdapter arrayAdapter;
    private ArrayList<Entrada> arrayListEntrada;
    private ScaleGestureDetector mScaleDetector;
    private ListView listEntrada;
    private Double scaleList = 1.0;
    private ImageView imgBackToolbar;
    private ImageView imgInfo;
    private TextView txtTituloToolbar;
    private TextView txtNumeroOrden;
    private Button btnLock;
    private DatosConfiguracion datosConfiguracion;
    private InputMethodManager imm;
    private ProgressDialog progressDialog;
    private EditText edtRefaccion;
    private EditText edtCantidad;
    private ImageButton btnEnviiarEntrada;
    private ImageButton btnOkConteo;
    private Button btnManual;
    private int posicion;
    private String respuesta;
    private String numeroOrden;
    public String codigoProveedor;

    public static DarEntradaActivity mInst = null;
    public static final String RECEIVE_DATA = "unitech.scanservice.data";
    public static final String START_SCANSERVICE = "unitech.scanservice.start";
    public static final String SCAN2KEY_SETTING = "unitech.scanservice.scan2key_setting";
    static final int tiempoPeticion = DatosConfiguracion.TIEMPO_PETICION;
    private boolean listActive = false;
    private String respuestaJSON;
    private int numEntrada;
    private boolean check = false;
    private int tipoEntrada;
    private int origenEntrada;
    private boolean hiloEsperaEscaner;
    public static double cantidadRequerida;

    private SharedPreferences sharedPreferences;
    private org.json.JSONArray uno;
    private org.json.JSONObject detallePedido;

    //avisos y alertas
    private String atencionStr;
    private String errorStr;
    private String entendidoStr;
    private String aceptarStr;
    private String cancelarStr;
    private String ocurrioErrorStr;
    private String limiteExcedidoStr;
    public int tiempoEspera = 35000;

    //Buscador
    EditText edtBuscador;
    ArrayList<Entrada> arrayListBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dar_entrada);
        atencionStr = getResources().getString(R.string.atencion);
        errorStr = getResources().getString(R.string.error);
        entendidoStr = getResources().getString(R.string.entendido);
        aceptarStr = getResources().getString(R.string.aceptar);
        cancelarStr = getResources().getString(R.string.cancelar);
        ocurrioErrorStr = getResources().getString(R.string.ocurrio_error);
        limiteExcedidoStr = getResources().getString(R.string.limite_excedido);
        mInst = this;
        posicion = 0;
        hiloEsperaEscaner = false;
        arrayListBuscar = new ArrayList<>();
        listEntrada = (ListView) findViewById(R.id.listEntrada);
        imgBackToolbar = (ImageView) findViewById(R.id.imgBackToolBar);
        imgInfo = (ImageView) findViewById(R.id.imgInfo);
        txtTituloToolbar = (TextView) findViewById(R.id.txtTituloToolbar);
        txtTituloToolbar.setTextSize(14);
        txtNumeroOrden = (TextView) findViewById(R.id.txtNumeroOrden);
        btnLock = (Button) findViewById(R.id.btnLock);
        btnManual = (Button) findViewById(R.id.btnManualEntrada);
        btnManual.setEnabled(false);
        edtBuscador = (EditText) findViewById(R.id.edt_buscador);
        btnEnviiarEntrada = (ImageButton) findViewById(R.id.imgButtonEnviar);
        btnOkConteo = (ImageButton) findViewById(R.id.imgButtonConfirmar);
        edtCantidad = (EditText) findViewById(R.id.edtCantidadEntrada);
        edtRefaccion = (EditText) findViewById(R.id.edtIngresarEntrada);
        edtRefaccion.requestFocus();
        edtCantidad.setFocusable(true);
        //btnOkConteo.
        //arrayListEntrada = new ArrayList<>();
        numeroOrden = getIntent().getStringExtra("numeroPedido");
        respuestaJSON = getIntent().getStringExtra("respuestaJSON");
        numEntrada = Integer.parseInt(getIntent().getStringExtra("numEntrada"));

        try {
            uno = new org.json.JSONArray(respuestaJSON);
            System.out.println("DetallePedido:" + uno.toString());
            if (uno.length() > 0) {
                detallePedido = new org.json.JSONObject(uno.get(0).toString());
            } else {
                System.out.println("No se obtuvieron resultados");
                finish();
            }

        } catch (Exception e) {
        }
        switch (numEntrada) {
            case 1:
                txtTituloToolbar.setText(getResources().getString(R.string.entrada_uno));
                tipoEntrada = 1;
                origenEntrada = 2;
                break;
            case 2:
                txtTituloToolbar.setText(getResources().getString(R.string.entrada_dos));
                tipoEntrada = 2;
                origenEntrada = 1;
                break;
            case 3:
                txtTituloToolbar.setText(getResources().getString(R.string.entrada_tres));
                tipoEntrada = 3;
                origenEntrada = 2;
                break;
            case 4:
                txtTituloToolbar.setText(getResources().getString(R.string.entrada_cuatro));
                tipoEntrada = 4;
                origenEntrada = 1;
                break;
            case 5:
                txtTituloToolbar.setText(getResources().getString(R.string.entrada_cinco));
                tipoEntrada = 5;
                origenEntrada = 2;
                break;
            case 6:
                txtTituloToolbar.setText(getResources().getString(R.string.entrada_seis));
                tipoEntrada = 6;
                origenEntrada = 1;
                break;
            default:
                txtTituloToolbar.setText(getString(R.string.entradas));
                break;
        }

        System.out.println("respuestaJSON" + respuestaJSON);
        txtNumeroOrden.setText("Numero de orden: " + numeroOrden);
        Bundle bundleScan = new Bundle();
        bundleScan.putBoolean("close", true);
        Intent mIntentScan = new Intent().setAction("unitech.scanservice.start").putExtras(bundleScan);
        sendBroadcast(mIntentScan);

//        edtRefaccion.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                imm.hideSoftInputFromWindow(edtRefaccion.getWindowToken(), 0);
//                edtRefaccion.requestFocus();
//                return true;
//            }
//        });
        edtCantidad.setEnabled(true);
        edtRefaccion.setEnabled(true);
        edtRefaccion.setFocusable(true);
        try {
            arrayListBuscar = Entrada.toArrayListWS(respuestaJSON, 2);
            arrayAdapter = new EntradasAdapter(getBaseContext(), Entrada.toArrayListWS(respuestaJSON, 2));
            listEntrada.setAdapter(arrayAdapter);
            listEntrada.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listEntrada.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mScaleDetector.onTouchEvent(event);

                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            AlertDialog.Builder quitDialog
                    = new AlertDialog.Builder(DarEntradaActivity.this);
            quitDialog.setTitle("Orden Vacía");
            quitDialog.setCancelable(false);
            quitDialog.setMessage("El número de orden, no contiene elementos para dar entrada.");
            quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            quitDialog.show();
        }
        eventosLista();
        buscador();
    }

    public void buscador(){
        edtBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, int start, int count, int after) {

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
                                    sendIntent.putExtra("activity", "buscadorEntrada");
                                    sendBroadcast(sendIntent);

                                } else {
                                    if (edtBuscador.getHint().toString().toLowerCase().contains("buscar")) {
                                        //setMensaje("¡Atención!", "Debes seleccionar una refacción antes de escanear");
                                        System.out.println("El codigo ha sido procesado, estoy en validacion seleccione");
                                    } else {
                                        edtBuscador.setText("");
                                        // Toast.makeText(DarEntradaActivity.this, "El código no se encuentra en la lista", Toast.LENGTH_LONG).show();
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
    }

    public double getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void eventos() {

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DarEntradaActivity.this);
                LayoutInflater li = LayoutInflater.from(DarEntradaActivity.this);
                View promptsView = li.inflate(R.layout.activity_detalle_entrada, null);
                TextView txtNumOrden = (TextView) promptsView.findViewById(R.id.txtNumOrdenEntrada);
                TextView txtFechaPedidoEntrada = (TextView) promptsView.findViewById(R.id.txtFechaPedidoEntrada);
                TextView txtUnidadesTotalesEntrada = (TextView) promptsView.findViewById(R.id.txtUnidadesTotalesEntrada);
                TextView txtAlmacenEntrada = (TextView) promptsView.findViewById(R.id.txtAlmacenEntrada);
                TextView txtProcedenciaEntrada = (TextView) promptsView.findViewById(R.id.txtProcedenciaEntrada);
                txtNumOrden.setText(numeroOrden);
                try {
                    txtFechaPedidoEntrada.setText(detallePedido.getString("fecha_pedido"));
                    txtUnidadesTotalesEntrada.setText(detallePedido.getString("unidades_totales"));
                    txtAlmacenEntrada.setText(detallePedido.getString("almacen"));
                    txtProcedenciaEntrada.setText(detallePedido.getString("procedencia"));
                } catch (Exception e) {
                }

                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setNeutralButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });

        imgBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayListEntrada.size() > 0 || arrayAdapter.getCount() > 0) {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(DarEntradaActivity.this);
                    quitDialog.setTitle("AVISO!!");
                    quitDialog.setCancelable(false);
                    quitDialog.setMessage("Tienes refacciones pendientes. ¿Seguro que deseas salir?");
                    quitDialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    quitDialog.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    quitDialog.show();
                } else {
                    finish();
                }
            }
        });
        edtRefaccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                // System.out.println("cadena->"+s.length()+"asd"+count+"before+->"+before);
                if (s.length() > 0) {
                    if (hiloEsperaEscaner == false) {
                        hiloEsperaEscaner = true;
                        new CountDownTimer(900, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                System.out.println("CODPROV->" + codigoProveedor);
                                System.out.println("Cadena->" + s.toString());

                                if (s.toString().equalsIgnoreCase(edtRefaccion.getHint().toString()) || (s.toString().equalsIgnoreCase(codigoProveedor))) {
                                    String data = "";
                                    edtRefaccion.requestFocus();
                                    edtCantidad.setFocusable(true);
                                    edtCantidad.setFocusableInTouchMode(true);
                                    btnOkConteo.setFocusable(false);
                                    btnManual.setFocusable(false);
                                    btnOkConteo.clearFocus();
                                    btnManual.clearFocus();
                                    Intent sendIntent = new Intent(RECEIVE_DATA);
                                    sendIntent.putExtra("text", edtRefaccion.getText().toString());
                                    sendIntent.putExtra("activity", "recepcion");
                                    sendBroadcast(sendIntent);
                                } else {
                                    if (edtRefaccion.getHint().toString().contains("Seleccione")) {
                                        setMensaje(atencionStr, "Debes seleccionar una refacción antes de escanear");
                                    } else {

                                        AlertDialog.Builder quitDialog
                                                = new AlertDialog.Builder(DarEntradaActivity.this);
                                        quitDialog.setTitle("Código Incorrecto");
                                        quitDialog.setCancelable(false);
                                        quitDialog.setMessage("Verifica el código escaneado");
                                        quitDialog.setNeutralButton(aceptarStr, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        quitDialog.show();
                                    }
                                }
                                edtRefaccion.setText("");
                                edtRefaccion.requestFocus();
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
                                                = new AlertDialog.Builder(RecoleccionActivity.this);
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
                /*if (!edtRefaccion.getText().equals("")) {
                    if (s.length() == edtRefaccion.length()) {
                        String data = "";
                        edtRefaccion.requestFocus();
                        edtCantidad.setFocusable(false);
                        edtCantidad.setFocusableInTouchMode(true);
                        btnOkConteo.setFocusable(false);
                        btnManual.setFocusable(false);
                        btnOkConteo.clearFocus();
                        btnManual.clearFocus();
                        Intent sendIntent = new Intent(RECEIVE_DATA);
                        sendIntent.putExtra("text", edtRefaccion.getText().toString());
                        sendIntent.putExtra("activity", "entrada");
                        sendBroadcast(sendIntent);
                    }

                }*/
            }
        });


        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


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
                        listEntrada.setScaleX(Float.parseFloat(Double.toString(scaleList)));
                        listEntrada.setScaleY(Float.parseFloat(Double.toString(scaleList)));

                    }
                } else {
                    if (scaleList > 1.0) {
                        scaleList = scaleList - 0.01;
                        listEntrada.setScaleX(Float.parseFloat(Double.toString(scaleList)));
                        listEntrada.setScaleY(Float.parseFloat(Double.toString(scaleList)));
                    }
                }

                return false;
            }
        });


        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check) {
                    edtCantidad.requestFocus();
                    edtCantidad.setEnabled(true);
                    // btnManual.setText("OK");
                } else {

                    try {

                        if (edtCantidad.getText().toString().equals("")) {
                            setMensaje(atencionStr, "Debes escanear para confirmar el número de refacciones");
                        } else {
                            btnManual.setText("MANUAL");
                            edtCantidad.setEnabled(true);
                            edtCantidad.setText(Double.toString(Double.parseDouble(edtCantidad.getText().toString())));
                        }
                    } catch (Exception e) {
                    }
                }


                //edtCantidad.setFocusableInTouchMode(true);
                /*if(btnManual.getText().toString().equalsIgnoreCase("Manual")){
                    edtCantidad.setEnabled(true);
                    btnManual.setText("OK");
                }else{
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(DarEntradaActivity.this);
                    quitDialog.setTitle("Confirmación");
                    quitDialog.setMessage("¿aa guardar la cantidad ingresada o cancelar el modo Manual?");
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
            }
        });
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listActive) {
                    listActive = false;
                    listEntrada.setEnabled(true);
                    edtBuscador.setEnabled(true);
                    btnLock.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                } else {
                    listActive = true;
                    listEntrada.setEnabled(false);
                    edtBuscador.setEnabled(false);
                    btnLock.setBackground(getResources().getDrawable(R.drawable.ic_lock));
                }

            }
        });


        btnEnviiarEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayListEntrada.size() > 0) {
                    if(tipoEntrada == 1 || tipoEntrada == 2 || tipoEntrada == 4 || tipoEntrada == 3 || tipoEntrada == 6){
                        AlertDialog.Builder confirmarAlert = new AlertDialog.Builder(DarEntradaActivity.this);
                        confirmarAlert.setCancelable(false);
                        confirmarAlert.setMessage("¿Estás seguro que deseas enviar las refacciones?");
                        confirmarAlert.setTitle("¡Atención!");
                        confirmarAlert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                envioRecoleccion();
                            }
                        });
                        confirmarAlert.setNegativeButton("Cancelar", null);
                        confirmarAlert.create();
                        confirmarAlert.show();
                       // envioRecoleccion();
                    }else
                    if(tipoEntrada == 5 && (arrayListEntrada.size() == arrayAdapter.getCount())){
                        AlertDialog.Builder confirmarAlert = new AlertDialog.Builder(DarEntradaActivity.this);
                        confirmarAlert.setCancelable(false);
                        confirmarAlert.setMessage("¿Estás seguro que deseas enviar las refacciones?");
                        confirmarAlert.setTitle("¡Atención!");
                        confirmarAlert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                envioRecoleccion();
                            }
                        });
                        confirmarAlert.setNegativeButton("Cancelar", null);
                        confirmarAlert.create();
                        confirmarAlert.show();
                        //envioRecoleccion();
                    }else{
                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(DarEntradaActivity.this);
                        quitDialog.setTitle("¡AVISO!");
                        quitDialog.setCancelable(false);
                        quitDialog.setMessage("Es necesario contar todas las refacciones.");
                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        quitDialog.show();
                    }


                    //envioRecoleccion();
                } else {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(DarEntradaActivity.this);
                    quitDialog.setTitle("AVISO!!");
                    quitDialog.setCancelable(false);
                    quitDialog.setMessage("Es necesario enviar al menos una recolección.");
                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    quitDialog.show();
                }
            }
        });

        btnOkConteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtRefaccion.getHint().equals(getResources().getString(R.string.ingresa_codigo))) {
                    if (arrayAdapter.getItem(posicion).getEstatus().equals("0")) {
                        try {
                            if (Double.parseDouble(edtCantidad.getText().toString()) > arrayAdapter.getItem(posicion).getUnidades()) {
                                AlertDialog.Builder quitDialog
                                        = new AlertDialog.Builder(DarEntradaActivity.this);
                                quitDialog.setTitle(atencionStr);
                                quitDialog.setMessage("No puedes dar entrada a más refacciones de las requeridas");
                                quitDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        listEntrada.setEnabled(true);
                                        listActive = false;
                                        btnLock.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                                        edtBuscador.setEnabled(true);
                                        eventosLista();
                                    }
                                });
                                quitDialog.setCancelable(false);
                                quitDialog.show();
                            } else {
                                // if (edtRefaccion.getHint().equals(edtRefaccion.getText().toString().trim())) {
                                if (edtCantidad.getText().toString().trim().length() > 0 || edtCantidad.getText().toString().equals("0.0")|| edtCantidad.getText().toString().equals("0")) {
                                    final Entrada entrada = new Entrada();
                                    //System.out.println("POSICION->" + posicion + "ELemento->" + arrayAdapter.getItem(posicion).getDescripcion() + "Catnidad" + edtCantidad.getText().toString());
                                    entrada.setSkuProveedor(edtRefaccion.getText().toString());
                                    AlertDialog.Builder quitDialog
                                            = new AlertDialog.Builder(DarEntradaActivity.this);
                                    quitDialog.setTitle(atencionStr);
                                    quitDialog.setMessage("¿El conteo realizado es correcto?");
                                    quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Double cant = Double.parseDouble(edtCantidad.getText().toString());
                                            edtBuscador.setEnabled(true);
                                            if(tipoEntrada == 1 || tipoEntrada == 2){
                                                AlmacenesDbHelper insert = new AlmacenesDbHelper(getBaseContext());
                                                Double unidContadas = Double.parseDouble(edtCantidad.getText().toString());
                                                Double unidades = arrayAdapter.getItem(posicion).getUnidades();
                                                insert.updateLineaRefaccion(edtCantidad.getText().toString(), arrayAdapter.getItem(posicion).getIdRow(), 1);
                                                arrayAdapter.getItem(posicion).setEstatus("1");
                                                arrayAdapter.getItem(posicion).setUnidadesContadas(unidContadas);
                                                arrayListEntrada.add(new Entrada(arrayAdapter.getItem(posicion).getDescripcion(), arrayAdapter.getItem(posicion).getSkuProveedor(), arrayAdapter.getItem(posicion).getSkuADO(), arrayAdapter.getItem(posicion).getEstatus(), arrayAdapter.getItem(posicion).getUnidadMedida(), arrayAdapter.getItem(posicion).getUnidades(), unidContadas, false, arrayAdapter.getItem(posicion).getNoLinea()));
                                                arrayAdapter.getItem(posicion).setUnidades(unidades - unidContadas);
                                                arrayAdapter.notifyDataSetChanged();
                                                edtCantidad.setText("");
                                                edtCantidad.setEnabled(true);
                                                edtRefaccion.setText("");
                                                edtRefaccion.setHint(R.string.ingresa_codigo);
                                                listEntrada.setEnabled(true);
                                                listActive = false;
                                                btnLock.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                                                eventosLista();
                                            }else{
                                            //if((tipoEntrada == 6|| tipoEntrada == 5 || tipoEntrada == 4 || tipoEntrada == 3) && (cant == arrayAdapter.getItem(posicion).getUnidades() )){
                                                AlmacenesDbHelper insert = new AlmacenesDbHelper(getBaseContext());
                                                Double unidContadas = Double.parseDouble(edtCantidad.getText().toString());
                                                Double unidades = arrayAdapter.getItem(posicion).getUnidades();
                                                insert.updateLineaRefaccion(edtCantidad.getText().toString(), arrayAdapter.getItem(posicion).getIdRow(), 1);
                                                arrayAdapter.getItem(posicion).setEstatus("1");
                                                arrayAdapter.getItem(posicion).setUnidadesContadas(unidContadas);
                                                arrayListEntrada.add(new Entrada(arrayAdapter.getItem(posicion).getDescripcion(), arrayAdapter.getItem(posicion).getSkuProveedor(), arrayAdapter.getItem(posicion).getSkuADO(), arrayAdapter.getItem(posicion).getEstatus(), arrayAdapter.getItem(posicion).getUnidadMedida(), arrayAdapter.getItem(posicion).getUnidades(), unidContadas, false));
                                                arrayAdapter.getItem(posicion).setUnidades(unidades - unidContadas);
                                                arrayAdapter.notifyDataSetChanged();
                                                edtCantidad.setText("");
                                                edtCantidad.setEnabled(true);
                                                edtRefaccion.setText("");
                                                edtRefaccion.setHint(R.string.ingresa_codigo);
                                                listEntrada.setEnabled(true);
                                                listActive = false;
                                                btnLock.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                                                eventosLista();
                                            }/*else{
                                                AlertDialog.Builder quitDialog
                                                        = new AlertDialog.Builder(DarEntradaActivity.this);
                                                quitDialog.setTitle(atencionStr);
                                                quitDialog.setMessage("No puedes dar entradas parciales");
                                                quitDialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                                quitDialog.setCancelable(false);
                                                quitDialog.show();
                                            }

                                        }*/
                                    }});
                                    quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    quitDialog.setCancelable(false);
                                    quitDialog.show();
                                } else {
                                    AlertDialog.Builder quitDialog
                                            = new AlertDialog.Builder(DarEntradaActivity.this);
                                    quitDialog.setTitle(errorStr);
                                    quitDialog.setMessage("!Se requiere cantidad!");
                                    quitDialog.setNeutralButton(entendidoStr, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            listEntrada.setEnabled(true);
                                        }
                                    });

                                    quitDialog.show();
                                }
                            }
                        } catch (Exception e) {
                            AlertDialog.Builder quitDialog
                                    = new AlertDialog.Builder(DarEntradaActivity.this);
                            quitDialog.setTitle("Atención");
                            quitDialog.setCancelable(false);
                            quitDialog.setMessage("Ingresa una cantidad válida");
                            quitDialog.setNeutralButton(entendidoStr, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listEntrada.setEnabled(true);
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
            }
        });
    }

    public void setTextCodigoEntrada(String text) {

        if (edtRefaccion.getHint().toString().contains("Seleccione")) {
            setMensaje(atencionStr, "Debes Seleccionar el producto antes de escanear");
        } else {
            //System.out.println("Activity->" + text);
            edtRefaccion.setText(text);
            edtRefaccion.requestFocus();
        }
        edtRefaccion.requestFocus();

    }


    //Inicio de enviar Conteo
  /*  public void envioRecoleccion() {
        JSONObject JSONobj = new JSONObject();
        JSONArray JSONarr = new JSONArray();
        sharedPreferences = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, MODE_PRIVATE);
        JSONobj.clear();
        JSONobj.put("paso", 2);
        JSONarr.add(JSONobj.clone());
        for (Entrada x : arrayListEntrada) {
            JSONobj.clear();
            System.out.println("Enviamos: numOden" + numeroOrden + "-skuADO" + x.getSkuADO() +
                    "-prov" + x.getSkuProveedor() + "-descripion" + x.getDescripcion() +
                    "-stock" + x.getUnidades() +
                    "origen" + origenEntrada + "tipoEntrada " + tipoEntrada +
                    "um" + x.getUnidadMedida() + "-stock2" + x.getUnidadesContadas());
            JSONobj.put("op", numeroOrden);//"15040819");
            JSONobj.put("skuADO", x.getSkuADO());//"030201202");
            JSONobj.put("skuPROV", x.getSkuProveedor());//"030201202");
            JSONobj.put("stock", x.getUnidadesContadas());
            JSONobj.put("um", x.getUnidadMedida());
            JSONobj.put("tpE", tipoEntrada);
            JSONobj.put("almacen", sharedPreferences.getString("almacen", ""));
            JSONobj.put("origenE", origenEntrada);
            JSONobj.put("estatus", x.getEstatus());
            DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
            JSONobj.put("idUser", DatosConfiguracion.idUsuario);
            JSONarr.add(JSONobj.clone());
        }

        System.out.println("");
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Enviando datos...", true);
        String Datos = URLEncoder.encode(JSONarr.toString());
        System.out.println("ENVIO---->>>>" + JSONarr.toString());
        System.out.println("Datos->"+Datos);
        RequestQueue queue = Volley.newRequestQueue(this);
        datosConfiguracion = new DatosConfiguracion();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioRecepcion() + Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("RespUEsTACONTEO" + response);
                        try {
                            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
                            if (jsonObject.has("operacion")) {
                                AlertDialog.Builder quitDialog
                                        = new AlertDialog.Builder(DarEntradaActivity.this);
                                quitDialog.setTitle(atencionStr);
                                quitDialog.setMessage(Html.fromHtml(jsonObject.getString("error")));
                                quitDialog.setNeutralButton(entendidoStr, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                quitDialog.show();
                            } else {
                                analizarRespuestaRecoleccion(response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR->" + error);
                progressDialog.dismiss();
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(DarEntradaActivity.this);
                quitDialog.setTitle(errorStr);
                quitDialog.setMessage("No fue posible obtener una respuesta.");
                quitDialog.setNeutralButton(entendidoStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(tiempoPeticion, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
*/

    public void envioRecoleccion() {
        JSONObject JSONobj = new JSONObject();
        final JSONArray JSONarr = new JSONArray();
        sharedPreferences = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, MODE_PRIVATE);
        JSONobj.clear();
        JSONobj.put("paso", 2);
        JSONarr.add(JSONobj.clone());
        for (Entrada x : arrayListEntrada) {
            JSONobj.clear();
            System.out.println("Enviamos: numOden" + numeroOrden + "-skuADO" + x.getSkuADO() +
                    "-prov" + x.getSkuProveedor() + "-descripion" + x.getDescripcion() +
                    "-stock" + x.getUnidades() +
                    "origen" + origenEntrada + "tipoEntrada " + tipoEntrada +
                    "um" + x.getUnidadMedida() + "-stock2" + x.getUnidadesContadas());
            JSONobj.put("op", numeroOrden);//"15040819");
            JSONobj.put("skuADO", x.getSkuADO());//"030201202");
            JSONobj.put("skuPROV", x.getSkuProveedor());//"030201202");
            JSONobj.put("stock", x.getUnidadesContadas());
            JSONobj.put("um", x.getUnidadMedida());
            JSONobj.put("tpE", tipoEntrada);
            JSONobj.put("almacen", sharedPreferences.getString("almacen", ""));
            JSONobj.put("origenE", origenEntrada);
            JSONobj.put("estatus", x.getEstatus());
            JSONobj.put("noLinea",x.getNoLinea());
            DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
            JSONobj.put("idUser", DatosConfiguracion.idUsuario);
            JSONarr.add(JSONobj.clone());
        }

        System.out.println("");
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Enviando datos...", true);
        final String Datos = URLEncoder.encode(JSONarr.toString());

        System.out.println("ENVIO---->>>>" + JSONarr.toString());
        //System.out.println("Datos->"+Datos);
        RequestQueue queue = Volley.newRequestQueue(this);
        datosConfiguracion = new DatosConfiguracion();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint()+ datosConfiguracion.getServicioRecepcion(), //+ Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("RespUEsTACONTEO" + response);
                        try {
                            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
                            if (jsonObject.has("operacion")) {
                                AlertDialog.Builder quitDialog
                                        = new AlertDialog.Builder(DarEntradaActivity.this);
                                quitDialog.setTitle(atencionStr);
                                quitDialog.setCancelable(false);
                                try {
                                    quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(jsonObject.getString("error"), "UTF-8")));
                                }catch(Exception e){
                                    quitDialog.setMessage("Ha ocurrido un problema con obtener los datos.");
                                    e.printStackTrace();
                                }
                                quitDialog.setNeutralButton(entendidoStr, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                quitDialog.show();
                            } else {
                                analizarRespuestaRecoleccion(response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR->" + error);
                progressDialog.dismiss();
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(DarEntradaActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setCancelable(false);
                quitDialog.setMessage("Ha ocurrido un error con la conexión. Vuelve a intentar");
                quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        envioRecoleccion();
                    }
                });
                quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
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
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json;");
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
        org.json.JSONArray respuestaJSON = new org.json.JSONArray(respuesta);
        int numeroRechazadas = 0;
        int numeroAceptadas = 0;
        boolean completo = true;
        for (int i = 0; i < respuestaJSON.length(); i++) {
            org.json.JSONObject confirmar = new org.json.JSONObject(respuestaJSON.get(i).toString());
            if (confirmar.getBoolean("estatus")) {
                numeroAceptadas++ ;
                for (int j = 0; j < arrayAdapter.getCount(); j++) {
                    if (arrayAdapter.getItem(j).getSkuADO().equalsIgnoreCase(confirmar.getString("sku"))) {
                        if (arrayAdapter.getItem(j).getUnidades() == 0) {
                            System.out.println("borrar elementos");
                            arrayAdapter.remove(arrayAdapter.getItem(j));
                            arrayAdapter.notifyDataSetChanged();
                        } else {
                            System.out.println("Despintar elemento");
                            arrayAdapter.getItem(j).setEstatus("0");
                            arrayAdapter.getItem(j).setUnidadesContadas(0);
//                arrayAdapter.getItem(position).setUnidadesContadas(0);
                        }
                    }
                }
//                arrayAdapter.getItem(position).setEstatus("0");
//                arrayAdapter.getItem(position).setUnidadesContadas(0);
//                int tamaño = arrayListEntrada.size();
//                for (int i = 0; i<tamaño;i++) {
//                    if (arrayAdapter.getItem(position).getSkuProveedor().equals(arrayListEntrada.get(i).getSkuProveedor())) {
//                        System.out.println(arrayAdapter.getItem(position).getSkuProveedor() + "----" + arrayListEntrada.get(i).getSkuProveedor());
//                        arrayAdapter.getItem(position).setUnidades(arrayListEntrada.get(i).getUnidades());
//                        arrayListEntrada.remove(arrayListEntrada.get(i));
//                        break;
//                    }
//                }
//                arrayAdapter.notifyDataSetChanged();


                for (int k = 0; k < arrayListEntrada.size(); k++) {
                    arrayListEntrada.remove(k);
                }
                arrayAdapter.notifyDataSetChanged();


            }else{
                numeroRechazadas++ ;
                completo = false;
            }
            /*if(completo){
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(DarEntradaActivity.this);
                quitDialog.setTitle("CORRECTO");
                quitDialog.setMessage("Las refacciones fueron recibidas correctamente");
                quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                quitDialog.show();
                //finish();
            }*/
        }
        boolean bandera = false;
        if (arrayListEntrada.size() == arrayAdapter.getCount()) {
            bandera = true;
        }
        if (arrayAdapter.getCount() == 0) {
            bandera = false;
        }
        if (completo) { //Revisar Condicion

                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(DarEntradaActivity.this);
                quitDialog.setTitle("CORRECTO");
            quitDialog.setCancelable(false);
                quitDialog.setMessage("Se recibieron "+numeroAceptadas+" refacciones, correctamente");
                quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                quitDialog.show();


        } else {

                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(DarEntradaActivity.this);
                quitDialog.setTitle("¡Atención!");
            quitDialog.setCancelable(false);
                quitDialog.setMessage("No se logró realizar la entrada de "+numeroRechazadas+ " refacciones. Intente nuevamente. Si el problema persiste consulte a sistemas");
                quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                quitDialog.show();

        }
    }
    //Fin de enviar conteo

    public EditText getTexCantidadRef() {
        return edtCantidad;
    }

    public void setTextCantidad(String text) {
        edtCantidad.setText(text);
    }

    public EditText getTexCodigoRefaccion() {
        return edtRefaccion;
    }

    public static DarEntradaActivity instance() {
        return mInst;
    }


    public void actualizarCodigoProveedor(final String codigo) {
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
        datosConfiguracion = new DatosConfiguracion(getBaseContext());
        final JSONObject JSONobj = new JSONObject();
        JSONobj.clear();
        JSONArray JSONarr = new JSONArray();
        JSONobj.put("ordenPedido", codigo);
        JSONobj.put("paso", 1);
        JSONarr.add(JSONobj.clone());
        System.out.println("" + JSONarr.toString());
        String Datos = URLEncoder.encode(JSONarr.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("DAORT->" + Datos);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, datosConfiguracion.getEndpoint() + Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println("ENTRADAACTIVITY-?>" + response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR->" + error);
                progressDialog.dismiss();
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(DarEntradaActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setCancelable(false);
                quitDialog.setMessage("Ha ocurrido un error. Vuelve a intentar");
                quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actualizarCodigoProveedor(codigo);
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


    public void eventosLista() {
        listEntrada.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);

                return false;
            }
        });
        listEntrada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Selection ---->" + arrayAdapter.getItem(position).getEstatus());
                edtBuscador.setEnabled(true);
                if (arrayAdapter.getItem(position).getEstatus().equals("0")) {
                    view.setSelected(true);
                    posicion = position;
                    check = false;
                    edtCantidad.setEnabled(true);
                    edtRefaccion.requestFocus();
                    btnManual.setEnabled(true);
                    edtRefaccion.setText("");
                    edtRefaccion.setHint(arrayAdapter.getItem(position).getSkuADO());
                    codigoProveedor = arrayAdapter.getItem(position).getSkuProveedor();
                    cantidadRequerida = arrayAdapter.getItem(position).getUnidades();
                    System.out.println("CodigoProv->" + codigoProveedor);
                    edtCantidad.setText("");
                    listEntrada.setEnabled(false);
                    listActive = true;
                    btnLock.setBackground(getResources().getDrawable(R.drawable.ic_lock));
                    //ic_lock


                } else {
                    edtRefaccion.setHint(R.string.ingresa_codigo);
                    edtCantidad.setEnabled(false);
                    edtCantidad.setText("");
                }

            }
        });
        listEntrada.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (arrayAdapter.getItem(position).getEstatus().equals("1")) {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(DarEntradaActivity.this);
                    quitDialog.setTitle("CONTAR");
                    quitDialog.setCancelable(false);
                    String pieza = arrayAdapter.getItem(position).getDescripcion();
                    quitDialog.setMessage("Deseas hacer nuevamente el conteo de:\n.[" + pieza + "]");
                    quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            arrayAdapter.getItem(position).setEstatus("0");
                            arrayAdapter.getItem(position).setUnidadesContadas(0);
                            int j = 0;
                            int tamaño = arrayListEntrada.size();
                            for (int i = 0; i < tamaño; i++) {
                                if (arrayAdapter.getItem(position).getSkuADO().equals(arrayListEntrada.get(i).getSkuADO())) {
                                    System.out.println(arrayAdapter.getItem(position).getSkuProveedor() + "----" + arrayListEntrada.get(i).getSkuProveedor());
                                    arrayAdapter.getItem(position).setUnidades(arrayListEntrada.get(i).getUnidades());
                                    arrayListEntrada.remove(arrayListEntrada.get(i));
                                    break;
                                }
                                j++;
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
                } else {
                    //Actualizac
                }
                return false;
            }
        });
    }

    public boolean isPrepareCount() {
        boolean respuesta = false;
        for (Entrada x : arrayListEntrada) {
            respuesta = true;
        }
        return respuesta;
    }

    public void setMensaje(String titulo, String mensaje) {
        AlertDialog.Builder quitDialog
                = new AlertDialog.Builder(DarEntradaActivity.this);
        quitDialog.setTitle(titulo);
        quitDialog.setMessage(mensaje);
        quitDialog.setCancelable(false);
        quitDialog.setNeutralButton(aceptarStr, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }

    public void setCodigoRecoleccion(String text) {
        edtRefaccion.setText(text);
        edtRefaccion.setFocusable(true);
        edtRefaccion.requestFocus();
    }

    public void setCantidadRecoleccion(String text) {
        edtCantidad.setText(text);
        edtRefaccion.requestFocus();
        //imgClear.setVisibility(View.GONE);
        check = true;
    }

    public String getCodigoProveedor() {
        return codigoProveedor;
    }

    public void focus() {
        edtRefaccion.requestFocus();
    }

    public EditText getCantidadRecoleccion() {
        return edtCantidad;
    }

    public EditText getCodigoRecoleccion() {
        return edtRefaccion;
    }

    //EditTextBuscador
    public EditText getCodigoBuscador(){
        return edtBuscador;
    }

    //Arreglo para buscar dentro de mi arreglo
    public ArrayList<Entrada> getArrayEntradaBuscar(){
        return arrayListBuscar;
    }

    //refrescar la lista
    public void refreshLista(int position){
        System.out.println("Selection ---->" + arrayAdapter.getItem(position).getEstatus());
        if (arrayAdapter.getItem(position).getEstatus().equals("0")) {
            //view.setSelected(true);
            posicion = position;
            check = false;
            edtCantidad.setEnabled(true);
            edtRefaccion.requestFocus();
            btnManual.setEnabled(true);
            edtRefaccion.setText("");
            edtRefaccion.setHint(arrayAdapter.getItem(position).getSkuADO());
            codigoProveedor = arrayAdapter.getItem(position).getSkuProveedor();
            cantidadRequerida = arrayAdapter.getItem(position).getUnidades();
            System.out.println("CodigoProv->" + codigoProveedor);
            edtCantidad.setText("");
            listEntrada.setEnabled(false);
            listActive = true;
            btnLock.setBackground(getResources().getDrawable(R.drawable.ic_lock));
            //ic_lock


        } else {
            edtRefaccion.setHint(R.string.ingresa_codigo);
            edtCantidad.setEnabled(false);
            edtCantidad.setText("");
        }
        edtBuscador.setText("");
    }

    public void noEncontrada(){
        edtBuscador.setText("");
        edtBuscador.setEnabled(true);
        AlertDialog.Builder alertaNoEncontrado = new AlertDialog.Builder(DarEntradaActivity.this);
        alertaNoEncontrado.setCancelable(false);
        alertaNoEncontrado.setTitle("¡Atención!");
        alertaNoEncontrado.setMessage("No se ha encontrado la refaccion en la lista");
        alertaNoEncontrado.setNeutralButton("Entendido",null);
        alertaNoEncontrado.create();
        alertaNoEncontrado.show();
        //Toast.makeText(this, "No se ha encontrado la refacción en la lista",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (arrayListEntrada.size() > 0 || arrayAdapter.getCount() > 0) {
            AlertDialog.Builder quitDialog
                    = new AlertDialog.Builder(DarEntradaActivity.this);
            quitDialog.setTitle("AVISO!!");
            quitDialog.setCancelable(false);
            quitDialog.setMessage("Tienes refacciones pendientes. ¿Seguro que deseas salir?");
            quitDialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            quitDialog.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            quitDialog.show();
        } else {
            finish();
        }

    }
}

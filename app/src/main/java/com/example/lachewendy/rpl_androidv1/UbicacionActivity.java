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
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.com.uny2.metodos.Animacion;
import com.uny2.clases.DatosConfiguracion;
import com.uny2.clases.Ubicacion;
import com.uny2.clases.UbicacionAdapter;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UbicacionActivity extends AppCompatActivity {


    AlertDialog alertDialog;
    //Toolbar
    ImageView txtBackToolbar;
    TextView txtTituloToolbar;
    ImageView imgInfo;
    Button btnLockRecoleccion;

    //Dialogo Inicial
    // EditText edtNumeroPedido;
    TextView tvNumeroped;
    TextView txtCodigoErroneo;
    TextView txtNumeroPedidoError;
    //Button btnValidar;
    Button btnCancelar;
    Button btnManual;

    //Dialog Cantidad
    //Button btnManual;

    int operacion;

    //Dialogo Segundo
    ProgressDialog progressDialog;
    DatosConfiguracion datosConfiguracion;
    Button btnValidarCantidad;

    //LayoutPrincipal
    UbicacionAdapter ubicacionAdapter;
    Ubicacion ubicacion;
    TextView txtNumOrden;
    ListView listViewUbicacion;
    EditText edtCodigoUbicacion;
    EditText edtCodigoProducto;
    EditText edtNumeroPedido;
    EditText edtCantidadUbicacionFondo;
    EditText edtCantidadUbicacion;
    Button btnUbicar;
    Button btnValidarPre;
    Button btnCancelarPre;

    SharedPreferences sharedPreferences;
    ArrayList<Ubicacion> arrayUbicacion;
    String codigoOrden = "";

    int posicion;
    boolean check = false;
    private ScaleGestureDetector mScaleDetector;
    private static UbicacionActivity mInst = null;

    public static final String RECEIVE_DATA = "unitech.scanservice.data";
    InputMethodManager imm;
    Double scaleList = 1.0;
    static final int  tiempoPeticion = DatosConfiguracion.TIEMPO_PETICION2; //450000; //35000
    static int tiempoActualizarPeticion = 60000;

    boolean listActive=false;
    ArrayList<Ubicacion> arrayListUbicacion;

    private String ubicacionStr;
    private String codigoADOStr;

    public static double cantidadRequerida;
    String ubicaStr = "";
    boolean hiloEsperaEscaner;

    Animacion animButtonParpadeante;

    String numOrdenPedido = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        mInst = this;
        posicion = 0;
        operacion = 0;
        hiloEsperaEscaner=false;
        txtBackToolbar = (ImageView) findViewById(R.id.imgBackToolBar);
        txtTituloToolbar = (TextView) findViewById(R.id.txtTituloToolbar);
        btnLockRecoleccion = (Button) findViewById(R.id.btnLock);
        txtNumOrden = (TextView) findViewById(R.id.txtNumOrden);
        listViewUbicacion = (ListView) findViewById(R.id.listViewUbicacion);
        imgInfo = (ImageView) findViewById(R.id.imgInfo);
        imgInfo.setImageResource(R.drawable.refresh);
        //imgInfo.setVisibility(View.GONE);
        writefile("Creado");
      /*  edtCodigoUbicacion = (EditText) findViewById(R.id.edtCodigoUbicacion);
        edtCodigoProducto = (EditText) findViewById(R.id.edtCodigoProducto);
        edtCantidadUbicacion = (EditText) findViewById(R.id.edtCantidadUbicacion);
        edtCantidadUbicacionFondo = (EditText) findViewById(R.id.edtCantidadUbicacion);
        */
        btnUbicar = (Button) findViewById(R.id.btnUbicar);
        btnUbicar.setEnabled(true);
        animButtonParpadeante = new Animacion(getBaseContext(),btnUbicar);
       /* edtCodigoUbicacion.requestFocus();
        edtCodigoUbicacion.setFocusable(false);
        edtCodigoProducto.setFocusable(false);*/

        eventos();
        arrayListUbicacion = new ArrayList<>();

        txtBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtTituloToolbar.setText(getResources().getString(R.string.ubicacion));

        Bundle bundleScan = new Bundle();
        bundleScan.putBoolean("close", true);
        Intent mIntentScan = new Intent().setAction("unitech.scanservice.start").putExtras(bundleScan);
        sendBroadcast(mIntentScan);

        listViewUbicacion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);

                return false;
            }
        });

        eventosLista();

        mostrarDialogInicial();
    }


    public  void eventos(){
        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarUbicacion(arrayUbicacion)){
                    pedirServicio(codigoOrden);
                }else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(UbicacionActivity.this);
                    alerta.setCancelable(false);
                    alerta.setMessage("Tus refacciones se encuentran en orden no es necesario actualizar");
                    alerta.setNeutralButton("Entedido", null);
                    alerta.create();
                    alerta.show();
                }
            }
        });

       /* edtCodigoProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // System.out.println("cadena->"+s.length()+"asd"+count+"before+->"+before);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //System.out.println("CantidadEditable"+s.length()+"texto->"+edtRefaccion.length());
                if (!edtCodigoProducto.getText().equals("")) {
                    if (s.length() == edtCodigoProducto.length()) {
                        String data = "";
                        edtCodigoProducto.requestFocus();
                        edtCantidadUbicacion.setFocusable(false);
                        edtCantidadUbicacion.setFocusableInTouchMode(true);
                        Intent sendIntent = new Intent(RECEIVE_DATA);
                        sendIntent.putExtra("text", edtCodigoProducto.getText().toString());
                        sendIntent.putExtra("activity", "ubicacion");
                        sendBroadcast(sendIntent);
                    }

                }
            }
        });
*/

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
                        scaleList=scaleList+0.01;
                        listViewUbicacion.setScaleX(Float.parseFloat(Double.toString(scaleList)));
                        listViewUbicacion.setScaleY(Float.parseFloat(Double.toString(scaleList)));

                    }
                } else {
                    if (scaleList > 1.0) {
                        scaleList=scaleList-0.01;
                        listViewUbicacion.setScaleX(Float.parseFloat(Double.toString(scaleList)));
                        listViewUbicacion.setScaleY(Float.parseFloat(Double.toString(scaleList)));
                    }
                }

                return false;
            }
        });


        //btnUbicar.setEnabled(false);
        btnLockRecoleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listActive) {
                    listActive = false;
                    listViewUbicacion.setEnabled(true);
                    btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                } else {
                    listActive = true;
                    listViewUbicacion.setEnabled(false);
                    btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock));
                }

            }
        });


        btnUbicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // animButtonParpadeante.stopAnimations();
                System.out.println("Envio- " + arrayListUbicacion.size() + "ll-" +ubicacionAdapter.getCount());
                //  if(arrayListUbicacion.size() == ){
                //envioRecoleccion();
                /*}else{
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(DarEntradaActivity.this);
                    quitDialog.setTitle("AVISO!!");
                    quitDialog.setMessage("Es necesario enviar al menos una recolección.");
                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    quitDialog.show();
                }*/

                //Descomentar
                if (arrayListUbicacion.size() == ubicacionAdapter.getCount()) {
                    System.out.println("ListoPara enviar");
                    AlertDialog.Builder confirmarAlert = new AlertDialog.Builder(UbicacionActivity.this);
                    confirmarAlert.setCancelable(false);
                    confirmarAlert.setMessage("¿Estás seguro que deseas ubicar las refacciones?");
                    confirmarAlert.setTitle("¡Atención!");
                    confirmarAlert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enviarPedido();
                        }
                    });
                    confirmarAlert.setNegativeButton("Cancelar", null);
                    confirmarAlert.create();
                    confirmarAlert.show();
                }else{
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(UbicacionActivity.this);
                    quitDialog.setTitle("¡Atención!");
                    quitDialog.setCancelable(false);
                    quitDialog.setMessage("Es necesario que cuentes todas las refacciones");
                    quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    quitDialog.show();
                }

            }
        });

    }


    public void validarCodigo(final String codigo, final String ubicacionCodigo){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UbicacionActivity.this);
        LayoutInflater li = LayoutInflater.from(UbicacionActivity.this);
        View promptsView = li.inflate(R.layout.ubicacion_prompt, null);
        edtCodigoProducto = (EditText) promptsView.findViewById(R.id.txtNumeroPedido);
        tvNumeroped = (TextView) promptsView.findViewById(R.id.tvNumeroped);
        // btnValidar = (Button) promptsView.findViewById(R.id.btnValidar);
        btnCancelar = (Button) promptsView.findViewById(R.id.btnCancelar);
        txtCodigoErroneo = (TextView) promptsView.findViewById(R.id.txtCodigoErroneo);

        edtCodigoProducto.setHint(codigo + ","+ubicacionCodigo);

        //--Z
        edtCodigoProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (hiloEsperaEscaner == false) {
                        hiloEsperaEscaner = true;
                        new CountDownTimer(1300, 100) { //estaba en 700
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                if(s.toString().length()>=edtCodigoProducto.getHint().length()){
                                    if (s.toString().contains(edtCodigoProducto.getHint().toString())) {
                                        String data = "";
                                        edtCodigoProducto.setFocusable(false);
                                        edtCodigoProducto.setFocusableInTouchMode(true);
                                        Intent sendIntent = new Intent(RECEIVE_DATA);
                                        sendIntent.putExtra("text", edtCodigoProducto.getText().toString());
                                        sendIntent.putExtra("activity", "ubicacion3");
                                        sendBroadcast(sendIntent);
                                    } else {
                                        if(edtCodigoProducto.getHint().toString().contains("Seleccione")){
                                            setMensaje("¡Atención!", "Debes seleccionar una refacción antes de escanear");
                                        }else {
                                            AlertDialog.Builder quitDialog
                                                    = new AlertDialog.Builder(UbicacionActivity.this);
                                            quitDialog.setTitle("Atención");
                                            quitDialog.setCancelable(false);
                                            quitDialog.setMessage("El código es incorrecto. Vuelve a intentarlo");
                                            quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    edtCodigoProducto.setText("");
                                                }
                                            });
                                            quitDialog.show();
                                        }
                                    }
                                }
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

        //Z--




        tvNumeroped.setText("Valida con el código de estante:");
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                listActive = true;
                listViewUbicacion.setEnabled(true);
                btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
            }
        });
       /* btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                   /* ubicacionStr = retval[1];
                    StringBuilder  ubicacion= new StringBuilder();
                    if(ubicacionStr.length()>0) {
                        ubicacion.append(ubicacionStr.substring(0, 3));
                        ubicacion.append("-");
                        ubicacion.append(ubicacionStr.substring(3,4));
                        ubicacion.append("-");
                        ubicacion.append(ubicacionStr.substring(4, 6));
                        ubicacion.append("-");
                        ubicacion.append(ubicacionStr.substring(6, 8));
                        ubicacionStr = ubicacion.toString();
                    }*/
             /*     validacionCodigo(codigo);
                }catch(Exception e){
                    txtCodigoErroneo.setVisibility(View.VISIBLE);
                    edtCodigoProducto.setText("");
                    txtCodigoErroneo.setText("Formato de código incorrecto");
                    edtCodigoProducto.requestFocus();
                }
            }
        });*/
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false)
                .setTitle("Validar Refacción");
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void validacionCodigo(String codigo){
        try{
            String retval[] = edtCodigoProducto.getText().toString().split(",", 2);
            System.out.println("CodigoValidacion->"+retval[0]+"codigo parama"+codigo );
            codigoADOStr = retval[0];
            ubicacionStr = retval[1];
            System.out.println("Uno-<" + codigoADOStr + "-<" + ubicacionStr);
            if(edtCodigoProducto.getText().toString().equalsIgnoreCase(codigo)){
                //if(codigoADOStr.equalsIgnoreCase(codigo) ){//&& ubicacionStr.equalsIgnoreCase(ubicacionStr)){
                //edtCodigoProducto.setText(codigoADOStr);
                //edtCodigoUbicacion.setText(ubicacionStr);
                alertDialog.dismiss();
                //edtCantidadUbicacion.requestFocus();


                        /*Contar cantidad*/

                determinarCantidad(codigoADOStr);

            }else{
                txtCodigoErroneo.setVisibility(View.VISIBLE);
                edtCodigoProducto.setText("");
                txtCodigoErroneo.setText("Código Erróneo");
                edtCodigoProducto.requestFocus();
            }
            System.out.println("-ubicacion>" + ubicacionStr);
        }catch(Exception e){
            txtCodigoErroneo.setVisibility(View.VISIBLE);
            edtCodigoProducto.setText("");
            txtCodigoErroneo.setText("Formato de código incorrecto");
            edtCodigoProducto.requestFocus();
        }
    }
    boolean isDisabled = false;

    public void determinarCantidad(String codigo){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UbicacionActivity.this);
        LayoutInflater li = LayoutInflater.from(UbicacionActivity.this);
        View promptsView = li.inflate(R.layout.ubicacion_cantidad_prompt, null);

        edtNumeroPedido = (EditText) promptsView.findViewById(R.id.edtNumeroPedido);
        edtCantidadUbicacion = (EditText) promptsView.findViewById(R.id.txtCantidadUbicacion);
        btnValidarCantidad = (Button) promptsView.findViewById(R.id.btnValidarCantidad);
        btnCancelar = (Button) promptsView.findViewById(R.id.btnCancelar);
        btnManual = (Button) promptsView.findViewById(R.id.btnManualUbicacion);
        //btnManual = (Button) findViewById(R.id.btnManual);
        edtCantidadUbicacion.setText("1.0");
        edtNumeroPedido.setHint(codigo);
        btnManual.setEnabled(true);
        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDisabled) {
                    edtCantidadUbicacion.setFocusable(false);
                    edtCantidadUbicacion.setFocusableInTouchMode(true);
                    edtCantidadUbicacion.clearFocus();
                    isDisabled = false;
                    btnManual.setBackgroundColor(getResources().getColor(R.color.btn_manual_disabled));
                } else {
                    btnManual.setEnabled(true);
                    btnManual.getBackground().setColorFilter(null);
                    btnManual.setBackgroundResource(0);
                    btnManual.setTextColor(getResources().getColor(R.color.blanco));
                    isDisabled = true;
                }

            }
        });

        edtNumeroPedido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (hiloEsperaEscaner == false) {
                        hiloEsperaEscaner = true;
                        new CountDownTimer(700, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                //if (s.toString().equalsIgnoreCase(edtNumeroPedido.getHint().toString())) {
                                if (s.toString().contains(edtNumeroPedido.getHint().toString())) {
                                    String data = "";
                                    edtNumeroPedido.requestFocus();
                                    edtCantidadUbicacion.setFocusable(false);
                                    edtCantidadUbicacion.setFocusableInTouchMode(false);
                                    edtCantidadUbicacion.clearFocus();
                                    btnCancelar.setFocusable(false);
                                    btnCancelar.setFocusableInTouchMode(false);
                                    btnCancelar.clearFocus();
                                    btnManual.clearFocus();
                                    btnValidarCantidad.setFocusable(false);
                                    btnValidarCantidad.setFocusableInTouchMode(false);
                                    btnValidarCantidad.clearFocus();
                                    Intent sendIntent = new Intent(RECEIVE_DATA);
                                    sendIntent.putExtra("text", edtNumeroPedido.getText().toString());
                                    sendIntent.putExtra("activity", "ubicacion2");
                                    sendBroadcast(sendIntent);
                                } else {
                                    AlertDialog.Builder quitDialog
                                            = new AlertDialog.Builder(UbicacionActivity.this);
                                    quitDialog.setTitle("Atención");
                                    quitDialog.setCancelable(false);
                                    quitDialog.setMessage("El código es incorrecto. Vuelve a intentarlo");
                                    quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            edtNumeroPedido.setText("");
                                        }
                                    });
                                    quitDialog.show();
                                }
                                edtNumeroPedido.setText("");
                                edtNumeroPedido.requestFocus();
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
           /*if (!edtNumeroPedido.getText().equals("")) {
                if (s.length() == edtNumeroPedido.length()) {
                    String data = "";
                    edtNumeroPedido.requestFocus();
                    edtCantidadUbicacion.setFocusable(false);
                    edtCantidadUbicacion.setFocusableInTouchMode(true);
                    Intent sendIntent = new Intent(RECEIVE_DATA);
                    sendIntent.putExtra("text", edtNumeroPedido.getText().toString());
                    sendIntent.putExtra("activity", "ubicacion2");
                    sendBroadcast(sendIntent);
                }

            }*/
            }
        });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                listActive = true;
                listViewUbicacion.setEnabled(true);
                btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));

            }
        });
        btnValidarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (edtRefaccion.getHint().equals(edtRefaccion.getText().toString().trim())) {
                System.out.println("cantidadNecesariaUbicacion->" + ubicacionAdapter.getItem(posicion).getUnidades());
                //if (edtCantidadUbicacion.getText().toString().trim().length() > 0) {
                try {
                    if (Double.parseDouble(edtCantidadUbicacion.getText().toString().trim()) == ubicacionAdapter.getItem(posicion).getUnidades()) {
                        final Ubicacion entrada = new Ubicacion();
                        //System.out.println("POSICION->" + posicion + "ELemento->" + arrayAdapter.getItem(posicion).getDescripcion() + "Catnidad" + edtCantidad.getText().toString());
                        entrada.setSkuProveedor(edtCodigoProducto.getText().toString());
                        // AlertDialog.Builder quitDialog
                        //         = new AlertDialog.Builder(UbicacionActivity.this);
                        //  quitDialog.setTitle("Atención");
                        //  quitDialog.setMessage("¿El conteo realizado es correcto?");
                        //  quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        //     @Override
                        //    public void onClick(DialogInterface dialog, int which) {
                        if (!(Double.parseDouble(edtCantidadUbicacion.getText().toString()) > ubicacionAdapter.getItem(posicion).getUnidades())) {
                            alertDialog.dismiss();
                            Double unidContadas = Double.parseDouble(edtCantidadUbicacion.getText().toString());
                            Double unidades = ubicacionAdapter.getItem(posicion).getUnidades();
                            ubicacionAdapter.getItem(posicion).setEstatus("1");
                            ubicacionAdapter.getItem(posicion).setUnidadesContadas(unidContadas);
                            arrayListUbicacion.add(new Ubicacion(ubicacionAdapter.getItem(posicion).getSkuProveedor(),
                                    ubicacionAdapter.getItem(posicion).getSkuADO(),
                                    ubicacionAdapter.getItem(posicion).getDescripcion(),
                                    ubicacionAdapter.getItem(posicion).getUnidades(),
                                    ubicacionAdapter.getItem(posicion).getUnidadMedida(),
                                    ubicacionAdapter.getItem(posicion).getUbicacion(),
                                    ubicacionAdapter.getItem(posicion).getUnidadesContadas(),
                                    ubicacionAdapter.getItem(posicion).getEstatus()));
                            ubicacionAdapter.getItem(posicion).setUnidades(unidades - unidContadas);
                            ubicacionAdapter.notifyDataSetChanged();
                            listViewUbicacion.setEnabled(true);
                            listActive = false;
                            btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                            eventosLista();
                            if (arrayListUbicacion.size() == ubicacionAdapter.getCount()) {
                                // animButtonParpadeante.starAnimations();
                                btnUbicar.setEnabled(true);
                            }
                        } else {
                            AlertDialog.Builder quitDialog
                                    = new AlertDialog.Builder(UbicacionActivity.this);
                            quitDialog.setTitle("Atención");
                            quitDialog.setMessage("¿Se ha realizado el conteo correctamente?");
                            quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listViewUbicacion.setEnabled(true);
                                    listActive = false;
                                    btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                                    eventosLista();
                                }
                            });
                            quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            quitDialog.setCancelable(false);
                            quitDialog.show();
                        }
                        //  }
                        //});
                        // quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        //     @Override
                        //     public void onClick(DialogInterface dialog, int which) {
                        //     }
                        // });
                        // quitDialog.setCancelable(false);
                        // quitDialog.show();
                    } else {
                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(UbicacionActivity.this);
                        quitDialog.setTitle("ERROR");
                        quitDialog.setCancelable(false);
                        quitDialog.setMessage("No puedes ubicar una cantidad diferente a la solicitada");
                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listViewUbicacion.setEnabled(true);
                            }
                        });

                        quitDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UbicacionActivity.this, "Ingresa una cantidad válida", Toast.LENGTH_LONG).show();
                    edtCantidadUbicacion.setText("0.0");
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
        });

        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false).setTitle("Validar Refacción");
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void enviarPedido(){
        JSONObject JSONobj = new JSONObject();
        final JSONArray JSONarr = new JSONArray();
        sharedPreferences = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, MODE_PRIVATE);
        JSONobj.clear();
        JSONobj.put("paso", 2);
        DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
        JSONobj.put("idUser", DatosConfiguracion.idUsuario);
        JSONobj.put("almacen", DatosConfiguracion.almacen);
        JSONobj.put("op", numOrdenPedido);

        JSONarr.add(JSONobj.clone());
        for (Ubicacion x : arrayListUbicacion) {
            JSONobj.clear();
            JSONobj.put("codADO", x.getSkuADO()); //035200305
            JSONobj.put("unidades",x.getUnidadesContadas());
            JSONarr.add(JSONobj.clone());
        }
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Enviando datos...", true);
        String Datos = URLEncoder.encode(JSONarr.toString());
        System.out.println("Datos->" + Datos);
        System.out.println("ENVIO---->>>>" + JSONarr.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        datosConfiguracion = new DatosConfiguracion();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioUbicacion(), //+ Datos, //DatosConfiguracion.home+"recoleccion.php",//
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("RespUEsTAUbicaicon" + response);
                        writefile("respuestaUbicacion" + response);
                        progressDialog.dismiss();
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.add(response);
                        try {
                            //analizarUbicacion(jsonArray);
                            response = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"), "UTF-8");
                            analizarUbicacion(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            writefile(e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR-====>" + error);
                writefile("ERROR-====>" + error);
                progressDialog.dismiss();
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(UbicacionActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setCancelable(false);
                quitDialog.setMessage("No fue posible obtener una respuesta.");
                quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
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


    public void eventosLista(){
        listViewUbicacion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                return false;
            }
        });
        listViewUbicacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (validarUbicacion(arrayUbicacion)){
                    System.out.println("Selection ---->" + ubicacionAdapter.getItem(position).getEstatus());
                //edtCodigoUbicacion.setHint(ubicacionAdapter.getItem(position).getUbicacion());
                //edtCodigoProducto.setHint(ubicacionAdapter.getItem(position).getSkuADO());
                //validarCodigo(ubicacionAdapter.getItem(position).getSkuADO(), ubicacionAdapter.getItem(position).getUbicacion());
                if (ubicacionAdapter.getItem(position).getEstatus().equals("0")) {
                    view.setSelected(true);
                    posicion = position;
                    check = false;
                    listViewUbicacion.setEnabled(false);
                    listActive = true;
                    btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock));
                    cantidadRequerida = ubicacionAdapter.getItem(position).getUnidades();
                    ubicaStr = ubicacionAdapter.getItem(position).getUbicacion();
                    if (ubicaStr.toLowerCase().contains("sin")) {
                        AlertDialog.Builder alUb = new AlertDialog.Builder(UbicacionActivity.this);
                        alUb.setCancelable(false);
                        alUb.setMessage("Esta pieza no puede ser contada por que no tiene ubicación. Establece su Ubicación desde JDE");
                        alUb.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listActive = true;
                                listViewUbicacion.setEnabled(true);
                                btnLockRecoleccion.setBackground(getResources().getDrawable(R.drawable.ic_lock_open));
                            }
                        });
                        alUb.setTitle("Atención");
                        alUb.create();
                        alUb.show();
                    } else {
                        try {
                            String retval[] = ubicacionAdapter.getItem(position).getUbicacion().split(" ", 2);

                            validarCodigo(ubicacionAdapter.getItem(position).getSkuADO(), retval[1].replace("-", ""));// ubicacionAdapter.getItem(position).getUbicacion());
                            System.out.println(retval[0]);
                            System.out.println("Ubicacion->" + retval.length + "-+>" + retval[1].replace("-", ""));
                            //determinarCantidad(ubicacionAdapter.getItem(position).getSkuADO());
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("UbicacionException->" + ubicacionAdapter.getItem(position).getUbicacion().replace("-", ""));
                            validarCodigo(ubicacionAdapter.getItem(position).getSkuADO(), ubicacionAdapter.getItem(position).getUbicacion().replace("-", ""));
                            //determinarCantidad(ubicacionAdapter.getItem(position).getSkuADO());
                        }
                    }
                }
            }else{
                    AlertDialog.Builder noContar = new AlertDialog.Builder(UbicacionActivity.this);
                    noContar.setCancelable(false);
                    noContar.setTitle("¡Atención!");
                    noContar.setMessage("Registre una ubicación dentro de JDEdwards para esta refacción antes de iniciar el proceso de ubicar");
                    noContar.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    noContar.create();
                    noContar.show();
                }

        }
    });
        listViewUbicacion.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (ubicacionAdapter.getItem(position).getEstatus().equals("1")) {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(UbicacionActivity.this);
                    quitDialog.setTitle("CONTAR");
                    String pieza = ubicacionAdapter.getItem(position).getDescripcion();
                    quitDialog.setCancelable(false);
                    quitDialog.setMessage("Deseas hacer nuevamente el conteo de:\n.[" + pieza + "]");
                    quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  btnUbicar.setEnabled(false);
                            /*if (animButtonParpadeante.isAnimate) {
                                animButtonParpadeante.stopAnimations();
                            }else{
                                animButtonParpadeante.starAnimations();
                            }*/
                            ubicacionAdapter.getItem(position).setEstatus("0");
                            ubicacionAdapter.getItem(position).setUnidadesContadas(0);
                            int j = 0;
                            int tamaño = arrayListUbicacion.size();
                            for (int i = 0; i < tamaño; i++) {
                                if (ubicacionAdapter.getItem(position).getSkuProveedor().equals(arrayListUbicacion.get(i).getSkuProveedor())) {
                                    System.out.println(ubicacionAdapter.getItem(position).getSkuProveedor() + "----" + arrayListUbicacion.get(i).getSkuProveedor());
                                    ubicacionAdapter.getItem(position).setUnidades(arrayListUbicacion.get(i).getUnidades());
                                    ubicacionAdapter.notifyDataSetChanged();
                                    arrayListUbicacion.remove(arrayListUbicacion.get(i));
                                    break;
                                }
                                j++;
                            }
                            ubicacionAdapter.notifyDataSetChanged();
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


    public void mostrarDialogInicial(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UbicacionActivity.this);
        LayoutInflater li = LayoutInflater.from(UbicacionActivity.this);
        View promptsView = li.inflate(R.layout.pre_ubicacion_prompt, null);
        edtCodigoProducto = (EditText) promptsView.findViewById(R.id.txtNumeroPedido);
        tvNumeroped = (TextView) promptsView.findViewById(R.id.tvNumeroped);
        btnValidarPre = (Button) promptsView.findViewById(R.id.btnValidarPre);
        btnCancelarPre = (Button) promptsView.findViewById(R.id.btnCancelarPre);
        txtNumeroPedidoError = (TextView) promptsView.findViewById(R.id.txtNumeroPedidoError);
        tvNumeroped.setText("Ubicar la orden de pedido:");
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false)
                .setTitle("UBICAR REFACCIONES");
        btnValidarPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cargarLista();
                String codigoOrdenPedido = edtCodigoProducto.getText().toString();
//                        pedirServicio(codigoOrdenPedido);
                System.out.println("++->" + codigoOrdenPedido.length());
                if (codigoOrdenPedido.length() == 0) {
                    txtNumeroPedidoError.setText("Debes ingresar un numero de orden para continuar");
                    txtNumeroPedidoError.setVisibility(View.VISIBLE);
                } else {
                    alertDialog.dismiss();
                    codigoOrden = codigoOrdenPedido;
                    pedirServicio(codigoOrdenPedido);
                }
            }
        });
        btnCancelarPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // overridePendingTransition(R.transition.fade_out, R.transition.fade_in);
                finish();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void pedirServicio(final String numOrden){
        numOrdenPedido = numOrden;
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
        datosConfiguracion = new DatosConfiguracion(getBaseContext());
        final JSONObject JSONobj = new JSONObject();
        JSONobj.clear();
        final JSONArray JSONarr = new JSONArray();
        DatosConfiguracion.idUsuario = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "id");
        JSONobj.put("idUser", Integer.parseInt(DatosConfiguracion.idUsuario));
        JSONobj.put("almacen", DatosConfiguracion.almacen);
        JSONobj.put("paso", 1);
        JSONobj.put("op", numOrden);
        JSONarr.add(JSONobj.clone());
        System.out.println("" + JSONarr.toString());
        String Datos = URLEncoder.encode(JSONarr.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("DAORT->" + Datos);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioUbicacion(),// + Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println("TEXTOUNO" + response);
                        writefile("pedirServicio->" + response);

                        try{
                            org.json.JSONArray jsonArray = new org.json.JSONArray(URLDecoder.decode(response, "UTF-8"));
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
                            if(!jsonObject.getBoolean("operacion")){
                                AlertDialog.Builder quitDialog
                                        = new AlertDialog.Builder(UbicacionActivity.this);
                                quitDialog.setTitle("Atencion");
                                quitDialog.setCancelable(false);
                                quitDialog.setMessage( Html.fromHtml(jsonObject.getString("error")));
                                quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intet = new Intent(UbicacionActivity.this, MenuRPLActivity.class);
                                        startActivity(intet);
                                        finish();
                                    }
                                });
                                quitDialog.show();
                            }else{
                                txtNumOrden.setText("Número de Pedido: "+numOrden);
                                obtenerOrdenUbicacion(response);
                            }

                        }catch(Exception e){
                            e.printStackTrace();
                            writefile(e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR->" + error);
                writefile("Error"+error);
                progressDialog.dismiss();
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(UbicacionActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setMessage("Ha ocurrido un error. Vuelve a intentar");
                quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pedirServicio(numOrden);
                    }
                });
                quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                quitDialog.setCancelable(false);
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

    public void obtenerOrdenUbicacion(String respuesta)throws JSONException{
        arrayUbicacion = new ArrayList<>();
        ubicacion = new Ubicacion();
        arrayUbicacion = Ubicacion.toArrayListWS(respuesta,2);
        if(validarUbicacion(arrayUbicacion)){
            imgInfo.setVisibility(View.GONE);
        }
        ubicacionAdapter = new UbicacionAdapter(UbicacionActivity.this,Ubicacion.toArrayListWS(respuesta,2));
        //2 marazo

        if(!validarUbicacion(Ubicacion.toArrayListWS(respuesta,2))){
            AlertDialog.Builder alertaUbicacion = new AlertDialog.Builder(UbicacionActivity.this);
            alertaUbicacion.setCancelable(false);
            alertaUbicacion.setMessage("Alguna(s) refaccion(es) no tienen ubicación, es necesario actualizar las ubicaciones en JDEdwards antes de iniciar el proceso de ubicar");
            alertaUbicacion.setPositiveButton("Entendido", null);
            alertaUbicacion.create();
            alertaUbicacion.show();
        }
        //fin 2marzo
        listViewUbicacion.setAdapter(ubicacionAdapter);
        listViewUbicacion.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }


    public void analizarUbicacion(String response) throws JSONException{
        String mensaje = "";
        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
            System.out.println("analizaUbicacion->"+jsonObject.toString());

            try {
                operacion = Integer.parseInt(jsonObject.getString(("operacion")));
            }catch(Exception e){
                e.printStackTrace();
                operacion = 3;
                if(jsonObject.has("error")){
                    mensaje = jsonObject.getString("error");
                }else{
                    mensaje = jsonObject.getString("errores");
                }
            }
            //Toast.makeText(getApplicationContext(), "operacion: " + operacion, Toast.LENGTH_LONG).show();
            String rOK = "";
            String rERROR = "";
            String error = "";
            String encabezado = "";
            try {
                if (jsonObject.has("refaccionesOK")) {
                    rOK = jsonObject.getString("refaccionesOK");
                }
                if (jsonObject.has("refaccionesERROR")) {
                    rERROR = jsonObject.getString("refaccionesERROR");
                }
                if (jsonObject.has("errores")) {
                    error = jsonObject.getString("errores");
                }
                if (jsonObject.has("error")) {
                    error = jsonObject.getString("error");
                }
                if (jsonObject.has("encabezado")) {
                    encabezado = jsonObject.getString("encabezado");
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            AlertDialog.Builder quitDialog
                    = new AlertDialog.Builder(UbicacionActivity.this);
            switch (operacion) {
                case 1:
                    quitDialog.setTitle("PROCESO FINALIZADO");
                    quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(encabezado + rOK, "UTF-8")));
                    quitDialog.setCancelable(false);
                    quitDialog.setNeutralButton("SALIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    quitDialog.show();
                    break;
                case 2:
                    quitDialog.setTitle("PROCESO FINALIZADO");
                    quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(encabezado + rOK + rERROR, "UTF-8")) );
                    quitDialog.setCancelable(false);
                    quitDialog.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    quitDialog.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enviarPedido();
                        }
                    });
                    quitDialog.show();
                    break;
                case 3:
                    quitDialog.setTitle("ERROR");
                    if(jsonObject.has("error")){
                        quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(jsonObject.getString("error"), "UTF-8") ));
                    }else{
                        quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(jsonObject.getString("errores"), "UTF-8") ));
                    }
                    quitDialog.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enviarPedido();
                        }
                    });
                    quitDialog.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    quitDialog.setCancelable(false);
                    quitDialog.show();
                    break;
                case 4:
                    quitDialog.setTitle("PROCESO FINALIZADO");
                    quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(encabezado + rERROR + error, "UTF-8") ));
                    quitDialog.setCancelable(false);
                    quitDialog.setNeutralButton("SALIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    quitDialog.show();
                    break;
                case 5:
                    quitDialog.setTitle("PROCESO FINALIZADO");
                    quitDialog.setMessage(Html.fromHtml( URLDecoder.decode(encabezado+ rOK +error, "UTF-8")));
                    quitDialog.setNeutralButton("SALIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    quitDialog.show();
                    break;
            }


//
        } catch (Exception ex) {
            ex.printStackTrace();
            AlertDialog.Builder quitDialog
                    = new AlertDialog.Builder(UbicacionActivity.this);
            quitDialog.setTitle("ERROR");
            quitDialog.setMessage(ex.getMessage());
            quitDialog.setCancelable(false);
            quitDialog.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            quitDialog.show();
            //Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }




    public void analizarlUbicacion(JSONArray jsonArray) throws  JSONException{
        int ubicadas = 0;
        String estatus = "Ha ocurrido un error";
        for (int i = 0; i < jsonArray.size(); i++) {
            System.out.println("JSONESTATUS->"+jsonArray.get(i));
            org.json.JSONArray jsonRespuesta =  new org.json.JSONArray(jsonArray.get(i).toString());
            System.out.println("JAONUNOIN"+jsonRespuesta.get(i).toString());
            org.json.JSONObject jsonEstatus = new org.json.JSONObject(jsonRespuesta.get(i).toString());
            System.out.println("ESTATUS"+jsonEstatus.getBoolean("operacion")); //estatus
            if(jsonEstatus.getBoolean("operacion")){
                ubicadas ++;
            }else{
                estatus = jsonEstatus.getString("error");
                ubicadas --;
            }
        }

        if(ubicadas == jsonArray.size()){
            AlertDialog.Builder quitDialog
                    = new AlertDialog.Builder(UbicacionActivity.this);
            quitDialog.setTitle("Refacciones Ubicadas");
            quitDialog.setCancelable(false);
            quitDialog.setMessage("Las refaccciones fueron enviadas correctamente");
            quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }});

            quitDialog.show();
        }else {
            AlertDialog.Builder quitDialog
                    = new AlertDialog.Builder(UbicacionActivity.this);
            quitDialog.setTitle("ERROR");
            quitDialog.setCancelable(false);
            quitDialog.setMessage(Html.fromHtml( estatus));
            quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }});

            quitDialog.show();
        }
    }


    public static UbicacionActivity instance()
    {
        return mInst;
    }
    public void setMensaje(String titulo,String mensaje){
        AlertDialog.Builder quitDialog
                = new AlertDialog.Builder(UbicacionActivity.this);
        quitDialog.setTitle(titulo);
        quitDialog.setMessage(mensaje);
        quitDialog.setCancelable(false);
        quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }

    public void setCodigoUbicacion(String text){
        edtNumeroPedido.setText(text);
        edtNumeroPedido.setFocusable(true);
        edtNumeroPedido.requestFocus();
    }

    public void setCantidadUbicacion(String text){
        edtCantidadUbicacion.setText(text);
        edtNumeroPedido.requestFocus();
        //imgClear.setVisibility(View.GONE);
        check = true;
    }

    public void focus(){
        edtNumeroPedido.requestFocus();
    }
    public EditText getCantidadRecoleccion(){
        return edtCantidadUbicacion;
    }

    public double getCantidadRequerida(){
        return cantidadRequerida;
    }

    public EditText getCodigoRecoleccion(){
        return edtCodigoProducto;
    }


    public EditText getCodigoUbicacion(){
        return edtNumeroPedido;
    }


    public void writefile(String message)
    {
        //File externalStorageDir = Environment.getExternalStorageDirectory();
        //File myFile = new File("/sdcard/Hola/CodigoBarras.txt");//externalStorageDir +"/MyAppFolder/" , "Log.txt");
        /*File txtFolder = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "ADO");
        //añadimos el nombre de la imagen
        File myFile = new File(txtFolder, "LogCodBarras.txt");
        if(myFile.exists())
        {
            try
            {

                FileOutputStream fostream = new FileOutputStream(myFile);
                OutputStreamWriter oswriter = new OutputStreamWriter(fostream);
                BufferedWriter bwriter = new BufferedWriter(oswriter);
                bwriter.write(message);
                bwriter.newLine();
                bwriter.close();
                oswriter.close();
                fostream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                myFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }*/
    }

    public boolean validarUbicacion(ArrayList<Ubicacion> listaUbicacion){
        boolean hasUbicacion = true;
        for (int i = 0; i < listaUbicacion.size() ; i++) {
            if(listaUbicacion.get(i).getUbicacion().contains("ubicacion")){
                hasUbicacion =  false;
            }
        }
        return hasUbicacion;
    }

}

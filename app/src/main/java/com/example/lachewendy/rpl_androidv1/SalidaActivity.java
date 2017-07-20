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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uny2.clases.DatosConfiguracion;
import com.uny2.clases.Salida;
import com.uny2.clases.SalidaSegundaAdapter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SalidaActivity extends AppCompatActivity {

    String nombreAlmacen;
    TextView txtTitulo;
    TextView txtBackToolbar;
    EditText edtFolioSalida;
    Button btnResumen, manualApoyo;
    InputMethodManager imm;
    ListView listView;
    ProgressDialog progressDialog;
    DatosConfiguracion datosConfiguracion;
    String usuario, idUsuario, almacen;
    SalidaSegundaAdapter salidaSegundaAdapter = null;
    public static SalidaActivity mInst = null;
    public int tiempoEspera = 120000;
    public static final String RECEIVE_DATA = "unitech.scanservice.data";
    public static final String START_SCANSERVICE = "unitech.scanservice.start";
    public static double cantidadRequerida;
    private boolean hiloEsperaEscaner;
    EditText edtProductoSalida = null;
    EditText edtCantidadSalida = null;
    ArrayList<Salida> salidaList = null;
    String folioFinal = "";
    TextView txtSalida;
    String codigoUbicacion;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    int operacion ;
    String datosSalida = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida);
        mInst = this;
        mostrarDialogInicial();
        operacion = 0;
        SharedPreferences prefs = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, this.MODE_PRIVATE);
        try {
            usuario = prefs.getString("usuario", "").toUpperCase();
            idUsuario = prefs.getString("id", "");
            almacen = prefs.getString("almacen", "");
            DatosConfiguracion.almacen = DatosConfiguracion.getPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, "almacen");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        txtTitulo = (TextView) findViewById(R.id.txtTituloToolbar);
        txtBackToolbar = (TextView) findViewById(R.id.txtBackToolbar);
        txtBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtTitulo.setText(getResources().getString(R.string.salidas));
        btnResumen = (Button) findViewById(R.id.btnResumenSalida);
        listView = (ListView) findViewById(R.id.list_salida);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        Bundle bundleScan = new Bundle();
        bundleScan.putBoolean("close", true);
        Intent mIntentScan = new Intent().setAction(START_SCANSERVICE).putExtras(bundleScan);
        sendBroadcast(mIntentScan);
        btnResumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean salida = false;
                for (Salida x : salidaList) {
                    if (x.getEstatus() == 1) {
                        salida = true;
                    }
                }
                if (salida) {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(SalidaActivity.this);
                    quitDialog.setTitle("PROCESAR SALIDA");
                    quitDialog.setCancelable(false);
                    quitDialog.setMessage("¿Estás seguro que las cantidades autorizadas son correctas?");
                    quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            JSONObject JSONobj = new JSONObject();
                            JSONobj.clear();
                            JSONArray JSONarr = new JSONArray();
                            JSONobj.put("paso", 2);
                            JSONobj.put("folio", folioFinal);
                            JSONobj.put("id_usuario", idUsuario);
                            JSONobj.put("almacen", nombreAlmacen);
                            JSONarr.add(JSONobj.clone());
                            for (Salida x : salidaList) {
                                if (x.getEstatus() == 1) {
                                    JSONobj.clear();
                                    JSONobj.put("sku_ADO", x.getSku());
//                                    JSONobj.put("descripcion", x.getDescripcion());
//                                    JSONobj.put("ubicacion", x.getUbicacion());
//                                    JSONobj.put("unidades", x.getCantidadaApoyo());
//                                    JSONobj.put("um", x.getUnidadMedida());
                                    JSONobj.put("autorizada", x.getAutorizado());
                                    JSONarr.add(JSONobj.clone());

                                }
                            }
                            String Datos = URLEncoder.encode(JSONarr.toString());
                            System.out.println("SAlidasEnvio->"+JSONarr.toString());
                            //dialog.dismiss();
                            procesarSalida(JSONarr.toString());
                        }
                    });
                    quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    quitDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "No se ha verificado ningun elemento.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static SalidaActivity instance() {
        return mInst;
    }
View hola;

    public void mostrarDialogInicial() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SalidaActivity.this);
        LayoutInflater li = LayoutInflater.from(SalidaActivity.this);
        hola = li.inflate(R.layout.salida_prompt, null);
        edtFolioSalida = (EditText) hola.findViewById(R.id.txtFolioSalida);
        radioSexGroup = (RadioGroup) hola.findViewById(R.id.radioAlmacen);
        alertDialogBuilder.setView(hola);
        alertDialogBuilder.setCancelable(false)
                .setTitle("CONSULTAR SALIDA")
                .setPositiveButton("BUSCAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedId = radioSexGroup.getCheckedRadioButtonId();
                        radioSexButton = (RadioButton) hola.findViewById(selectedId);
                        nombreAlmacen =almacen;
                        if (radioSexButton.getText().toString().equalsIgnoreCase("Propiedad")) {
                            System.out.println(""+nombreAlmacen.replace("" + nombreAlmacen.charAt(nombreAlmacen.length() - 1), "P"));
                            nombreAlmacen = nombreAlmacen.replace("" + nombreAlmacen.charAt(nombreAlmacen.length() - 1), "P");
                        } else if (radioSexButton.getText().toString().equalsIgnoreCase("Consigna")) {
                            System.out.println(""+nombreAlmacen.replace(""+nombreAlmacen.charAt(nombreAlmacen.length()-1), "C"));
                            nombreAlmacen = nombreAlmacen.replace("" + nombreAlmacen.charAt(nombreAlmacen.length() - 1), "C");
                        }
                        if (edtFolioSalida.getText().toString().isEmpty()) {
                            Toast.makeText(SalidaActivity.this, "El campo no puede quedar vacio", Toast.LENGTH_SHORT).show();
                            mostrarDialogInicial();
                        } else {
                            System.out.println("numeriPedido" + edtFolioSalida.getText().toString());
                            dialog.dismiss();
                            solicitarSalida(edtFolioSalida.getText().toString());
                        }
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

    public void solicitarSalida(final String folio) {
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
        datosConfiguracion = new DatosConfiguracion(getBaseContext());
        final JSONObject JSONobj = new JSONObject();
        JSONobj.clear();
        folioFinal = folio;
        final JSONArray JSONarr = new JSONArray();
        JSONobj.put("paso", 1);
        JSONobj.put("folio", folio);
        JSONobj.put("id_usuario", idUsuario);
        JSONobj.put("almacen", nombreAlmacen);
        JSONarr.add(JSONobj.clone());
        System.out.println("JSON->" + JSONarr.toString());
        String Datos = URLEncoder.encode(JSONarr.toString());
        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("DAORT->" + Datos);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioSalidas(), //+ Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            System.out.println("TEXTOUNO" + response);
                            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
                            System.out.println("TEXTOJSONOBJECT->"+jsonObject.toString());
                            if (!Boolean.parseBoolean(jsonObject.get("operacion").toString())) {
                                AlertDialog.Builder quitDialog
                                        = new AlertDialog.Builder(SalidaActivity.this);
                                quitDialog.setTitle("Atencion");
                                quitDialog.setCancelable(false);
                                quitDialog.setMessage(Html.fromHtml(jsonObject.getString("error")));
                                quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       // finish();
                                        mostrarDialogInicial();
                                    }
                                });
                                quitDialog.show();
                            } else {
                                salidaList = new ArrayList<>();
                                try {
                                    for (int i = 1; i < jsonArray.length(); i++) {
                                        org.json.JSONObject apoyos = new org.json.JSONObject(jsonArray.get(i).toString());
                                        Salida salida = new Salida();
                                        salida.setSku(apoyos.getString("sku_ADO"));
                                        salida.setDescripcion(apoyos.getString("descripcion"));
                                        salida.setCantidadaApoyo(Double.parseDouble((apoyos.getString("unidades"))));
                                        salida.setUnidadMedida(apoyos.getString("um"));
                                        salida.setUbicacion(apoyos.getString("ubicacion"));
                                        salida.setEstatus(0);
                                        salidaList.add(salida);
                                    }
                                    salidaSegundaAdapter = new SalidaSegundaAdapter(getBaseContext(), salidaList);
                                    listView.setAdapter(salidaSegundaAdapter);
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            if (salidaList.get(position).getEstatus() < 1) {
                                                final int linea = position;
                                                final boolean[] manual = {false};
                                                final double canti = salidaList.get(linea).getCantidadaApoyo();
                                               // System.out.println("canti: " + canti);
                                                codigoUbicacion =  salidaList.get(linea).getUbicacion().replace(" |","").trim();
                                                //Toast.makeText(getApplicationContext(), "Piche la refac:."+salidaList.get(position).getSku(), Toast.LENGTH_LONG).show();
                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SalidaActivity.this);
                                                LayoutInflater li = LayoutInflater.from(SalidaActivity.this);
                                                View promptsView = li.inflate(R.layout.dialog_apoyo, null); //011030996,104K0201
                                                edtProductoSalida = (EditText) promptsView.findViewById(R.id.tv_scanCode);
                                                edtCantidadSalida = (EditText) promptsView.findViewById(R.id.tv_cantidad);
                                                manualApoyo = (Button) promptsView.findViewById(R.id.manualApoyo);
                                                manualApoyo.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        DecimalFormat det = new DecimalFormat("0.00");
                                                        if ((edtCantidadSalida.getText().toString().isEmpty())) {
                                                            Toast.makeText(getApplicationContext(), "La cantidad no puede ir vacia", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Double canti2 = Double.parseDouble(det.format(Double.parseDouble(edtCantidadSalida.getText().toString().replace(",","."))));
                                                           // System.out.println("Cantidad2: " + canti2 + " Canti" + canti);
                                                            //System.out.println(canti2 <= canti);
                                                            if (canti2 == null) {
                                                                Toast.makeText(getApplicationContext(), "La cantidad no puede ir vacia", Toast.LENGTH_LONG).show();
                                                            } else {
                                                                if (manual[0]) {
                                                                    if (canti2 <= canti) {
                                                                        manual[0] = false;
                                                                        manualApoyo.setText("MANUAL");
                                                                        edtCantidadSalida.setEnabled(false);
                                                                        edtCantidadSalida.setFocusable(true);
                                                                    } else {
                                                                        AlertDialog.Builder quitDialog
                                                                                = new AlertDialog.Builder(SalidaActivity.this);
                                                                        quitDialog.setTitle("Máxima cantidad");
                                                                        quitDialog.setCancelable(false);
                                                                        quitDialog.setMessage("No puedes autorizar mas refacciones de las solicitadas");
                                                                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        });
                                                                        quitDialog.show();
                                                                    }
                                                                } else {
                                                                    if (canti2 <= canti) {
                                                                        manual[0] = true;
                                                                        manualApoyo.setText("OK");
                                                                        edtCantidadSalida.setEnabled(true);
                                                                        edtCantidadSalida.setFocusable(true);
                                                                    } else {
                                                                        AlertDialog.Builder quitDialog
                                                                                = new AlertDialog.Builder(SalidaActivity.this);
                                                                        quitDialog.setTitle("Máxima cantidad");
                                                                        quitDialog.setCancelable(false);
                                                                        quitDialog.setMessage("No puedes autorizar mas refacciones de las solicitadas");
                                                                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        });
                                                                        quitDialog.show();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                                edtProductoSalida.setHint(salidaList.get(linea).getSku());
                                                //  System.out.println("---->Cantidad: "+ salidaList.get(linea).getCantidadaApoyo());
                                                edtProductoSalida.addTextChangedListener(new TextWatcher() {
                                                                                             String codigoProveedor = salidaList.get(linea).getSku();


                                                                                             @Override
                                                                                             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                                                             }

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
                                                                                                                 DecimalFormat formatter = new DecimalFormat("0.00");
                                                                                                                 System.out.println("CODPROV->" + codigoProveedor);
                                                                                                                 String codigoBueno = codigoProveedor + ","+ codigoUbicacion.replace("-","");
                                                                                                                 if (s.toString().equalsIgnoreCase(edtProductoSalida.getHint().toString()) || (s.toString().equalsIgnoreCase(codigoProveedor))|| (s.toString().equalsIgnoreCase(codigoBueno))) {
                                                                                                                     System.out.println("Cantidad INPUT: " + Double.parseDouble(formatter.format(Double.parseDouble(edtCantidadSalida.getText().toString()))) + " CANTIDAD RE:" + canti);
                                                                                                                     System.out.println(Double.parseDouble(formatter.format(Double.parseDouble(edtCantidadSalida.getText().toString()))) + "\n" + canti);
                                                                                                                     if (Double.parseDouble(formatter.format(Double.parseDouble(edtCantidadSalida.getText().toString()))) + 1 <= canti) {
                                                                                                                         String data = "";
                                                                                                                         edtProductoSalida.requestFocus();
                                                                                                                         Intent sendIntent = new Intent(RECEIVE_DATA);
                                                                                                                         sendIntent.putExtra("text", edtProductoSalida.getText().toString());
                                                                                                                         sendIntent.putExtra("activity", "salida");
                                                                                                                         sendIntent.putExtra("ubicacion",codigoUbicacion.replace("-",""));
                                                                                                                         sendBroadcast(sendIntent);
                                                                                                                     } else {
                                                                                                                         AlertDialog.Builder quitDialog
                                                                                                                                 = new AlertDialog.Builder(SalidaActivity.this);
                                                                                                                         quitDialog.setTitle("Máxima cantidad");
                                                                                                                         quitDialog.setCancelable(false);
                                                                                                                         quitDialog.setMessage("No puedes autorizar mas refacciones de las solicitadas");
                                                                                                                         quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                                                                                             @Override
                                                                                                                             public void onClick(DialogInterface dialog, int which) {
                                                                                                                             }
                                                                                                                         });
                                                                                                                         quitDialog.show();
                                                                                                                     }
                                                                                                                 } else {
                                                                                                                     AlertDialog.Builder quitDialog
                                                                                                                             = new AlertDialog.Builder(SalidaActivity.this);
                                                                                                                     quitDialog.setTitle("Código Incorrecto");
                                                                                                                     quitDialog.setCancelable(false);
                                                                                                                     quitDialog.setMessage("Verifica el código escaneado");
                                                                                                                     quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                                                                                         @Override
                                                                                                                         public void onClick(DialogInterface dialog, int which) {
                                                                                                                         }
                                                                                                                     });
                                                                                                                     quitDialog.show();
                                                                                                                 }
                                                                                                                 edtProductoSalida.setText("");
                                                                                                                 edtProductoSalida.requestFocus();
                                                                                                                 hiloEsperaEscaner = false;
                                                                                                             }
                                                                                                         }

                                                                                                                 .

                                                                                                                         start();
                                                                                                     }

                                                                                                 }
                                                                                             }


                                                                                             @Override
                                                                                             public void afterTextChanged(Editable s) {

                                                                                             }
                                                                                         }

                                                );
                                                //edtFolioSalida = (EditText) promptsView.findViewById(R.id.txtFolioSalida);
                                                alertDialogBuilder.setView(promptsView);
                                                alertDialogBuilder.setCancelable(false)
                                                        .setTitle("CONTEO AUTORIZACION")
                                                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog,
                                                                                        int which) {
                                                                        if ((edtCantidadSalida.getText().toString().isEmpty())) {
                                                                            Toast.makeText(getApplicationContext(), "La cantidad no puede ir vacia", Toast.LENGTH_LONG).show();
                                                                        } else {
                                                                           // System.out.println("Pre"+ salidaList.get(linea).getCantidadaApoyo()+"+-cant"+cantidadRequerida + "Cantidnoseque"+Double.parseDouble(edtCantidadSalida.getText().toString()));
                                                                           try{
                                                                               //BorrarIf
                                                                               if(Double.parseDouble(edtCantidadSalida.getText().toString()) <=salidaList.get(linea).getCantidadaApoyo() ){
                                                                                   //Este if y sus llaves
                                                                                   if (Double.parseDouble(edtCantidadSalida.getText().toString()) > 0) {
                                                                                       if (!manual[0]) {
                                                                                           salidaList.get(linea).setEstatus(1);
                                                                                           salidaList.get(linea).setAutorizado(Double.parseDouble(edtCantidadSalida.getText().toString()));
                                                                                           salidaSegundaAdapter.notifyDataSetChanged();
                                                                                       }
                                                                                       //borrar else
                                                                                       else{
                                                                                           salidaList.get(linea).setEstatus(1);
                                                                                           salidaList.get(linea).setAutorizado(Double.parseDouble(edtCantidadSalida.getText().toString()));
                                                                                           salidaSegundaAdapter.notifyDataSetChanged();
                                                                                       }
                                                                                       //borrar else
                                                                                   }
                                                                                   //El else de abajo borrar
                                                                               }else{
                                                                                   AlertDialog.Builder quitDialog
                                                                                           = new AlertDialog.Builder(SalidaActivity.this);
                                                                                   quitDialog.setTitle("Máxima cantidad");
                                                                                   quitDialog.setCancelable(false);
                                                                                   quitDialog.setMessage("No puedes autorizar más refacciones de las solicitadas");
                                                                                   quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                                                                                       @Override
                                                                                       public void onClick(DialogInterface dialog, int which) {
                                                                                       }
                                                                                   });
                                                                                   quitDialog.show();
                                                                               }
                                                                               //Este else
                                                                           }catch(Exception e){
                                                                               e.printStackTrace();
                                                                           }

                                                                        }
                                                                    }
                                                                }

                                                        )
                                                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog,
                                                                                        int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                }

                                                        );
                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                alertDialog.show();
                                            }
                                        }
                                    });
                                    btnResumen.setVisibility(View.VISIBLE);
                                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                        @Override
                                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                            if (String.valueOf(salidaSegundaAdapter.getItem(position).getEstatus()).equals("1")) {
                                                AlertDialog.Builder quitDialog
                                                        = new AlertDialog.Builder(SalidaActivity.this);
                                                quitDialog.setTitle("EDITAR");
                                                quitDialog.setCancelable(false);
                                                String pieza = salidaSegundaAdapter.getItem(position).getDescripcion();
                                                quitDialog.setMessage("¿Deséas editar la cantidad autorizada para:\n" + pieza + "?");
                                                quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        salidaSegundaAdapter.getItem(position).setEstatus(0);
                                                        salidaSegundaAdapter.getItem(position).setAutorizado(0);
                                                        salidaSegundaAdapter.notifyDataSetChanged();
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
                                } catch (Exception e) {
                                    System.out.println("El elemento solicitado");
                                    e.printStackTrace();
                                }

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR->" + error);
                progressDialog.dismiss();
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(SalidaActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setCancelable(false);
                quitDialog.setMessage("Ha ocurrido un error al recuperar los datos");
                quitDialog.setPositiveButton("REINTENTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        solicitarSalida(folio);
                    }
                });
                quitDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                quitDialog.show();
            }
        }

        )

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put(datosConfiguracion.getServicioRecepcion(), Datos);
                //params.put("", );
                params.put("", JSONarr.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        stringRequest.setRetryPolicy(new

                        DefaultRetryPolicy(DatosConfiguracion.TIEMPO_PETICION2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        queue.add(stringRequest);
    }

    public void setCodigoRecoleccion(String text) {
        edtProductoSalida.setText(text);
        edtProductoSalida.setFocusable(true);
        edtProductoSalida.requestFocus();
    }

    public void setCantidadRecoleccion(String text) {
        edtCantidadSalida.setText(text);
        edtProductoSalida.requestFocus();
        //imgClear.setVisibility(View.GONE);
        // check = true;
    }

   /* public String getCodigoProveedor(){
        return codigoProveedor;
    }*/

    public void focus() {
        edtProductoSalida.requestFocus();
    }

    public EditText getCantidadRecoleccion() {
        return edtCantidadSalida;
    }

    public EditText getCodigoRecoleccion() {
        return edtProductoSalida;
    }

    public void setMensaje(String titulo, String mensaje) {
        AlertDialog.Builder quitDialog
                = new AlertDialog.Builder(SalidaActivity.this);
        quitDialog.setTitle(titulo);
        quitDialog.setCancelable(false);
        quitDialog.setMessage(mensaje);
        quitDialog.setNeutralButton("ACEPTAR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }

    public void procesarSalida(final String Datos) {
        datosSalida = Datos;
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Procesando autorización", true);
        datosConfiguracion = new DatosConfiguracion(getBaseContext());
        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("DAORT->" + Datos);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioSalidas(),// + Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            response = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"), "UTF-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        System.out.println("TEXTOUNO->" + response);
                        try {
                            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
                            //System.out.println(jsonObject.toString());
String mensaje = "";
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
                                    = new AlertDialog.Builder(SalidaActivity.this);
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
                                    if(jsonObject.has("faltan")){
                                        try {
                                            org.json.JSONArray limpiandoJSON = new org.json.JSONArray(datosSalida);
                                            org.json.JSONArray auxiliarJSON = new org.json.JSONArray();
                                            System.out.println("LimpiandoJSON->"+limpiandoJSON.toString());
                                            org.json.JSONArray faltanJSON = new org.json.JSONArray(jsonObject.get("faltan").toString());
                                            System.out.println("Faltan->"+faltanJSON.toString());
                                            auxiliarJSON.put(limpiandoJSON.get(0));
                                            boolean entro = false;
                                            for (int i = 1; i < limpiandoJSON.length(); i++) {
                                                org.json.JSONObject sku = new org.json.JSONObject(limpiandoJSON.get(i).toString());
                                                entro = false;
                                                for (int j = 0; j < faltanJSON.length(); j++) {
                                                    if (sku.getString("sku_ADO").equalsIgnoreCase(faltanJSON.get(j).toString())) {
                                                        entro = true;
                                                        continue;
                                                    }
                                                }
                                                if(!entro) {
                                                    auxiliarJSON.put(limpiandoJSON.get(i));
                                                }
                                            }
                                            datosSalida = auxiliarJSON.toString();
                                            System.out.println("DatosSlaidaFaltan->"+datosSalida);
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
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
                                            procesarSalida(datosSalida);
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
                                            procesarSalida(datosSalida);
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


//                            if (!Boolean.parseBoolean(jsonObject.get("operacion").toString())) {
//                                AlertDialog.Builder quitDialog
//                                        = new AlertDialog.Builder(SalidaActivity.this);
//                                quitDialog.setTitle("ERROR");
//                                quitDialog.setMessage(Html.fromHtml(jsonObject.getString("error")));
//                                quitDialog.setNeutralButton("SALIR", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                                quitDialog.show();
//                            } else {
//                                if (Boolean.parseBoolean(jsonObject.get("errorlinea").toString())) {
//                                    final String erl = jsonObject.getString(jsonObject.get("msgErrorLinea").toString());
//                                    AlertDialog.Builder quitDialog
//                                            = new AlertDialog.Builder(SalidaActivity.this);
//                                    quitDialog.setTitle("Proceso Terminado");
//                                    quitDialog.setMessage(Html.fromHtml(jsonObject.getString(jsonObject.get("refaccionesOK").toString())));
//                                    quitDialog.setNeutralButton("MOSTRAR ERRORES", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            AlertDialog.Builder quitDialog
//                                                    = new AlertDialog.Builder(SalidaActivity.this);
//                                            quitDialog.setTitle("Proceso Terminado");
//                                            quitDialog.setMessage(Html.fromHtml(erl));
//                                            quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    finish();
//                                                }
//                                            });
//                                            quitDialog.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    finish();
//                                                }
//                                            });
//                                            quitDialog.show();
//                                        }
//                                    });
//                                    quitDialog.show();
//                                } else {
//                                    AlertDialog.Builder quitDialog
//                                            = new AlertDialog.Builder(SalidaActivity.this);
//                                    quitDialog.setTitle("Proceso Terminado");
//                                    quitDialog.setMessage("El proceso de salida se realizo con exito.");
//                                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            finish();
//                                        }
//                                    });
//                                    quitDialog.show();
//                                }
//                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            AlertDialog.Builder quitDialog
                                    = new AlertDialog.Builder(SalidaActivity.this);
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
                }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    System.out.println("ERROR->" + error.getMessage());
                }
                try {
                    progressDialog.dismiss();
                }catch(Exception e){
                    e.printStackTrace();
                }
                String errorStr = "Ha ocurrido un error. Inténtalo nuevamente";
                if(error.getMessage()!=null) {
                    try{
                    if (error.getMessage().contains("ConnectException") || error.getMessage().contains("SocketException")) {
                        errorStr = "Ha ocurrido un error con la conexión. Sin embargo el proceso en JDE puede seguir trabajando, verifique la salida para comprobar. ¿Deseas verificar el folio de salida?";
                        //procesarSalida(Datos);

                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(SalidaActivity.this);
                        quitDialog.setTitle("Verificación");
                        quitDialog.setMessage(errorStr);
                        quitDialog.setCancelable(false);
                        quitDialog.setPositiveButton("VERIFICAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JSONObject JSONobj = new JSONObject();
                                JSONobj.clear();
                                JSONArray JSONarr = new JSONArray();
                                JSONobj.put("paso", 3);
                                JSONobj.put("folio", folioFinal);
                                JSONobj.put("id_usuario", idUsuario);
                                JSONobj.put("almacen", nombreAlmacen);
                                JSONarr.add(JSONobj.clone());
                                for (Salida x : salidaList) {
                                    if (x.getEstatus() == 1) {
                                        JSONobj.clear();
                                        JSONobj.put("sku_ADO", x.getSku());
//                                        JSONobj.put("descripcion", x.getDescripcion());
                                        JSONobj.put("ubicacion", x.getUbicacion());
//                                        JSONobj.put("unidades", x.getCantidadaApoyo());
//                                        JSONobj.put("um", x.getUnidadMedida());
//                                        JSONobj.put("autorizada", x.getAutorizado());
                                        JSONarr.add(JSONobj.clone());

                                    }
                                }
                                String DatosVerificar = URLEncoder.encode(JSONarr.toString());
                                //procesarSalida(DatosVerificar);
                                procesarSalida(JSONarr.toString());
                            }
                        });
                        quitDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        quitDialog.show();


                    } else {
                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(SalidaActivity.this);
                        quitDialog.setTitle("ERROR");
                        quitDialog.setMessage(errorStr);
                        quitDialog.setCancelable(false);
                        quitDialog.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                procesarSalida(Datos);
                            }
                        });
                        quitDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        quitDialog.show();
                    }
                }catch(Exception e){

                    }
                }else{
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(SalidaActivity.this);
                    quitDialog.setTitle("ERROR");
                    quitDialog.setMessage(errorStr);
                    quitDialog.setCancelable(false);
                    quitDialog.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            procesarSalida(Datos);
                        }
                    });
                    quitDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    quitDialog.show();
                }

            }
        }

        )

        {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put(datosConfiguracion.getServicioRecepcion(), Datos);
                params.put("", Datos);
                //params.put("", JSONarr.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        stringRequest.setRetryPolicy(new

                        DefaultRetryPolicy(datosConfiguracion.getTiempoPeticion2(), DefaultRetryPolicy

                        .DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        queue.add(stringRequest);
    }
    public void verificarSalida(final String Datos) {
        progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Procesando autorización", true);
        datosConfiguracion = new DatosConfiguracion(getBaseContext());
        RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("DAORT->" + Datos);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioSalidas() + Datos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            response = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"), "UTF-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                        System.out.println("TEXTOUNO" + response);
                        try {
                            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonArray.get(0).toString());
                            //System.out.println(jsonObject.toString());

                            try {
                                operacion = Integer.parseInt(jsonObject.getString(("operacion")));
                            }catch(Exception e){
                                e.printStackTrace();
                                operacion = 3;
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
                                    = new AlertDialog.Builder(SalidaActivity.this);
                            switch (operacion) {
                                case 1:
                                    quitDialog.setTitle("PROCESO FINALIZADO");
                                    quitDialog.setMessage( Html.fromHtml(URLDecoder.decode(encabezado + rOK, "UTF-8")));
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
                                    quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(encabezado + rOK + rERROR, "UTF-8")));
                                    quitDialog.setCancelable(false);
                                    quitDialog.setNeutralButton("SALIR", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    quitDialog.show();
                                    break;
                                case 3:
                                    quitDialog.setTitle("ERROR");
                                    if(jsonObject.has("error")){
                                        quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(jsonObject.getString("error"),"UTF-8")));
                                    }else{
                                        quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(jsonObject.getString("errores"),"UTF-8")));
                                    }
                                    quitDialog.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    quitDialog.setCancelable(false);
                                    quitDialog.show();
                                    break;
                                case 4:
                                    quitDialog.setTitle("PROCESO FINALIZADO");
                                    quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(encabezado + rERROR + error,"UTF-8")));
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
                                    quitDialog.setMessage(Html.fromHtml(URLDecoder.decode(encabezado+ rOK +error,"UTF-8")));
                                    quitDialog.setNeutralButton("SALIR", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    quitDialog.show();
                                    break;
                            }


//                            if (!Boolean.parseBoolean(jsonObject.get("operacion").toString())) {
//                                AlertDialog.Builder quitDialog
//                                        = new AlertDialog.Builder(SalidaActivity.this);
//                                quitDialog.setTitle("ERROR");
//                                quitDialog.setMessage(Html.fromHtml(jsonObject.getString("error")));
//                                quitDialog.setNeutralButton("SALIR", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                                quitDialog.show();
//                            } else {
//                                if (Boolean.parseBoolean(jsonObject.get("errorlinea").toString())) {
//                                    final String erl = jsonObject.getString(jsonObject.get("msgErrorLinea").toString());
//                                    AlertDialog.Builder quitDialog
//                                            = new AlertDialog.Builder(SalidaActivity.this);
//                                    quitDialog.setTitle("Proceso Terminado");
//                                    quitDialog.setMessage(Html.fromHtml(jsonObject.getString(jsonObject.get("refaccionesOK").toString())));
//                                    quitDialog.setNeutralButton("MOSTRAR ERRORES", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            AlertDialog.Builder quitDialog
//                                                    = new AlertDialog.Builder(SalidaActivity.this);
//                                            quitDialog.setTitle("Proceso Terminado");
//                                            quitDialog.setMessage(Html.fromHtml(erl));
//                                            quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    finish();
//                                                }
//                                            });
//                                            quitDialog.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    finish();
//                                                }
//                                            });
//                                            quitDialog.show();
//                                        }
//                                    });
//                                    quitDialog.show();
//                                } else {
//                                    AlertDialog.Builder quitDialog
//                                            = new AlertDialog.Builder(SalidaActivity.this);
//                                    quitDialog.setTitle("Proceso Terminado");
//                                    quitDialog.setMessage("El proceso de salida se realizo con exito.");
//                                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            finish();
//                                        }
//                                    });
//                                    quitDialog.show();
//                                }
//                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            AlertDialog.Builder quitDialog
                                    = new AlertDialog.Builder(SalidaActivity.this);
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
                }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR->" + error.getMessage());
                progressDialog.dismiss();
                String errorStr = "Ha ocurrido un error. Inténtalo nuevamente";
                if(error.getMessage().contains("ConnectException")|| error.getMessage().contains("SocketException") ){
                    errorStr = "No se ha podido conectar con el servidor. Inténtalo nuevamente";
                }
                AlertDialog.Builder quitDialog
                        = new AlertDialog.Builder(SalidaActivity.this);
                quitDialog.setTitle("ERROR");
                quitDialog.setMessage(errorStr);
                quitDialog.setCancelable(false);
                quitDialog.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        verificarSalida(Datos);
                    }
                });
                quitDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                quitDialog.show();
            }
        }

        )

        {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        stringRequest.setRetryPolicy(new

                        DefaultRetryPolicy(datosConfiguracion.getTiempoPeticion2(), DefaultRetryPolicy

                        .DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        queue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        try{
            if (salidaList.isEmpty()) {
                finish();
            } else {
                boolean salida = false;
                for (Salida x : salidaList) {
                    if (x.getEstatus() == 1) {
                        salida = true;
                    }
                }
                if (salida) {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(SalidaActivity.this);
                    quitDialog.setTitle("ADVERTENCIA");
                    quitDialog.setCancelable(false);
                    quitDialog.setMessage("Existe un proceso iniciado, si sales se perderá");
                    quitDialog.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    quitDialog.setNegativeButton("NO SALIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    quitDialog.show();
                } else {
                    validarSalida();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            finish();
        }

    }


    public void validarSalida(){
        if(listView.getAdapter().getCount()>0){
            AlertDialog.Builder salidaUbicacion = new AlertDialog.Builder(this);
            salidaUbicacion.setCancelable(false);
            salidaUbicacion.setTitle("¡Atención!");
            salidaUbicacion.setMessage("¿Seguro que deseas salir?");
            salidaUbicacion.setNegativeButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            salidaUbicacion.setPositiveButton("No",null);
            salidaUbicacion.create();
            salidaUbicacion.show();
        }else{
            finish();
        }
    }
}

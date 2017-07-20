package com.example.lachewendy.rpl_androidv1;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OlvidePassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OlvidePassFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String respuesta;
    private long usuario;

    private RelativeLayout relativeNumeroEmpleado;
    private RelativeLayout relativePregunta;
    private RelativeLayout relativeCambiarPass;
    private Button btnAceptar;
    private Button btnAceptarRespuesta;
    private Button btnCancelar;
    private Button btnCancelarRespuesta;
    private Button btnCancelarPass;
    private Button btnConfirmarPass;
    private EditText edtNumeroUsuarioOlvida;
    private EditText edtRespuestaUsuarioOlvida;
    private EditText edtNuevoPass;
    private EditText edtConfirmarPass;
    private TextView txtUsuarioRespuesta;
    private TextView txtPreguntaRespuesta;
    private ProgressDialog progressDialog;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OlvidePassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OlvidePassFragment newInstance(String param1, String param2) {
        OlvidePassFragment fragment = new OlvidePassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OlvidePassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_olvide_pass, container, false);
        relativeNumeroEmpleado = (RelativeLayout) view.findViewById(R.id.relativeNumeroEmpleado);
        relativePregunta = (RelativeLayout)view.findViewById(R.id.relativePregunta);
        relativeCambiarPass = (RelativeLayout) view.findViewById(R.id.relativeCambiarPass);
        btnAceptarRespuesta = (Button) view.findViewById(R.id.btn_aceptarRespuesta);
        btnAceptar = (Button) view.findViewById(R.id.btn_aceptar);
        btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelarRespuesta = (Button) view.findViewById(R.id.btn_cancelarRespuesta);
        btnCancelarPass = (Button) view.findViewById(R.id.buttonCancelarPass);
        btnConfirmarPass = (Button) view.findViewById(R.id.btnConfirmarPass);
        edtNumeroUsuarioOlvida = (EditText) view.findViewById(R.id.txtNumeroUsuarioOlvida);
        edtRespuestaUsuarioOlvida = (EditText) view.findViewById(R.id.txtRespuestaUsuarioOlvida);
        edtNuevoPass = (EditText) view.findViewById(R.id.edtNuevoPass);
        edtConfirmarPass = (EditText) view.findViewById(R.id.edtConfirmarPass);
        txtUsuarioRespuesta = (TextView) view.findViewById(R.id.txtUsuario);
        txtPreguntaRespuesta = (TextView) view.findViewById(R.id.txtPregunta);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.relativeLogin.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction().remove(OlvidePassFragment.this).commit();
            }
        });

        btnCancelarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.relativeLogin.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction().remove(OlvidePassFragment.this).commit();
            }
        });

        btnCancelarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.relativeLogin.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction().remove(OlvidePassFragment.this).commit();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(edtNumeroUsuarioOlvida.getText().toString().isEmpty()){
                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(getContext());
                        quitDialog.setTitle("ERROR");
                        quitDialog.setMessage("El campo de usuario no debe estar vacío");
                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        quitDialog.show();
                    }else{
                        usuario =Integer.parseInt(edtNumeroUsuarioOlvida.getText().toString());
                        pedirPregunta(usuario, 1, "xx");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        btnAceptarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNumeroUsuarioOlvida.getText().toString().isEmpty()) {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(getContext());
                    quitDialog.setTitle("ERROR");
                    quitDialog.setMessage("El campo de respuesta no debe estar vacío");
                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    quitDialog.show();
                } else {
                    if ((edtRespuestaUsuarioOlvida.getText().toString()).equalsIgnoreCase(respuesta)) {
                        relativePregunta.setVisibility(View.GONE);
                        relativeCambiarPass.setVisibility(View.VISIBLE);
                    } else {
                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(getContext());
                        quitDialog.setTitle("ERROR");
                        quitDialog.setMessage("La respuesta no coincide. Intenta de nuevo");
                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                edtRespuestaUsuarioOlvida.setText("");
                            }
                        });

                        quitDialog.show();
                    }
                }


            }
        });

        btnConfirmarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoPass = edtNuevoPass.getText().toString();
                String confirmarPass = edtConfirmarPass.getText().toString();
                if (nuevoPass.isEmpty() || confirmarPass.isEmpty()) {
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(getContext());
                    quitDialog.setTitle("ERROR");
                    quitDialog.setMessage("Los campos no deben estar vacíos");
                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    quitDialog.show();
                } else {


                    if (nuevoPass.equals(confirmarPass)) {
                        System.out.println("Valido hasta aqui");
                        pedirPregunta(usuario, 2, confirmarPass);
                    } else {
                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(getContext());
                        quitDialog.setTitle("ERROR");
                        quitDialog.setMessage("Las contraseñas no coinciden. Vuelve a intentarlo");
                        quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                edtNuevoPass.setText("");
                                edtConfirmarPass.setText("");
                            }
                        });

                        quitDialog.show();
                    }
                }
            }
        });

        return view;
    }

    public void pedirPregunta(long numeroUsuario, final int paso, String nuevoPass){
        final org.json.simple.JSONObject JSONobj = new org.json.simple.JSONObject();
        JSONobj.clear();
        JSONArray JSONarr = new JSONArray();
        JSONobj.put("paso",paso);
        JSONobj.put("numeroEmpleado", numeroUsuario);
        JSONobj.put("nuevoPass", nuevoPass.toUpperCase());
        JSONarr.add(JSONobj.clone());
        final String Datos = URLEncoder.encode(JSONarr.toString());
        DatosConfiguracion datosConfiguracion = new DatosConfiguracion(getContext());
        progressDialog = ProgressDialog.show(getContext(), "Espere por Favor ...",	"Obteniendo validación...", true);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, datosConfiguracion.getEndpoint() + datosConfiguracion.getRecuperacionPass() + Datos ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println("RespuestaLogin->" + response);
                        try {
                            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
                            JSONObject pregunta = new JSONObject(jsonArray.getString(0));
                            if(paso==1){
                                if(pregunta.getString("operacion").equalsIgnoreCase("true")){
                                    System.out.println("Pregunta->" + pregunta.getString("pregunta"));
                                    txtPreguntaRespuesta.setText("" + pregunta.getString("pregunta"));
                                    txtUsuarioRespuesta.setText("Usuario: " + pregunta.getString("username"));
                                    respuesta = pregunta.getString("respuesta");
                                    relativePregunta.setVisibility(View.VISIBLE);
                                    relativeNumeroEmpleado.setVisibility(View.GONE);
                                }else{
                                    AlertDialog.Builder quitDialog
                                            = new AlertDialog.Builder(getContext());
                                    quitDialog.setTitle("ERROR");
                                    quitDialog.setMessage(""+pregunta.getString("error"));
                                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    quitDialog.show();
                                }

                            }if(paso==2){
                                AlertDialog.Builder quitDialog
                                        = new AlertDialog.Builder(getContext());
                                quitDialog.setTitle("Cambio exitoso");
                                quitDialog.setMessage(""+pregunta.getString("update"));
                                quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LoginActivity.relativeLogin.setVisibility(View.VISIBLE);
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(OlvidePassFragment.this).commit();
                                    }});

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
                        = new AlertDialog.Builder(getContext());
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(LoginActivity.tiempoPeticion, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }

    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }

    private SecretKey key;
    private Cipher cipher;
    private String algoritmo = "AES";
    private int keysize = 16;

    public void addKey() {
        byte[] valuebytes = "uny2_CB".getBytes();
        key = new SecretKeySpec(Arrays.copyOf(valuebytes, keysize), algoritmo);
    }

    public String encriptar(String texto) {
        addKey();
        String value = "";
        try {
            cipher = Cipher.getInstance(algoritmo);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] textobytes = texto.getBytes();
            byte[] cipherbytes = cipher.doFinal(textobytes);
            value = Base64.encodeToString(cipherbytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex.getMessage());
        } catch (NoSuchPaddingException ex) {
            System.err.println(ex.getMessage());
        } catch (InvalidKeyException ex) {
            System.err.println(ex.getMessage());
        } catch (IllegalBlockSizeException ex) {
            System.err.println(ex.getMessage());
        } catch (BadPaddingException ex) {
            System.err.println(ex.getMessage());
        }
        return value;
    }


}

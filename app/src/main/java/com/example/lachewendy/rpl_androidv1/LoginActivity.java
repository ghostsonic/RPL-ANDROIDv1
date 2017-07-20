package com.example.lachewendy.rpl_androidv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.uny2.clases.DatosConfiguracion;


import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
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


public class LoginActivity extends AppCompatActivity {
    private EditText password;
    private EditText usuario;
    private Button inicio;
    private String usuarioSt;
    private String passwordSt;
    static RelativeLayout relativeLogin;
    static final int tiempoPeticion = 35000;
    private ProgressDialog progressDialog;
    TextView txtOlvidastePass;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Seteando el titulo de la aplicacion en el ActionBar
            this.setTitle("LOGIN");
            setContentView(R.layout.activity_login);
            //asignacion de los elementos de el layout por medio de id
            relativeLogin = (RelativeLayout) findViewById(R.id.relativeLogin);
            password=(EditText)findViewById(R.id.txtPasssword);
            usuario=(EditText)findViewById(R.id.txtUsuario);
            usuario.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            inicio=(Button) findViewById(R.id.btnInicioSesion);
            txtOlvidastePass = (TextView) findViewById(R.id.txtOlvidastePass);
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setSubtitle(Html.fromHtml("<font color='#e8e8e8'>ALMACÉN DE REFACCIONES</font>"));

            sharedpreferences  = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, Context.MODE_PRIVATE);

            txtOlvidastePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    relativeLogin.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new OlvidePassFragment()).commit();
                    //  showDialog(LoginActivity.this, "Error de conexión al servidor");
                }
            });

            //evento onclickListener asignado al boton de inicio de sesion
            inicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passwordSt = password.getText().toString();
                    usuarioSt = usuario.getText().toString();
                    switch (v.getId()) {
                        case R.id.btnInicioSesion:
                            if (!passwordSt.isEmpty() && !usuarioSt.isEmpty()) {
                                loginApp(1);
                            } else {
                                Toast.makeText(getApplicationContext(), "Usuario y Password son requeridos.", Toast.LENGTH_LONG).show();
                            }
                           /* String usuario ="[{\"operacion\":\"true\",\"id_usuario\":\"3\",\"permisos\":\"1,1,1,1,1,1,1\",\"perfil\":\"ALMACENISTA\"}]";
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.add(usuario);
                            try {
                                System.out.println("JSONESTATUS->"+jsonArray.get(0));
                                org.json.JSONArray jsonRespuesta =  new org.json.JSONArray(jsonArray.get(0).toString());
                                System.out.println("JAONUNOIN"+jsonRespuesta.get(0).toString());
                                org.json.JSONObject jsonEstatus = new org.json.JSONObject(jsonRespuesta.get(0).toString());
                                System.out.println("ESTATUS"+jsonEstatus.getBoolean("operacion")); //estatus
                                if(jsonEstatus.getBoolean("operacion")){ //estatus
                                    Intent intent = new Intent().setClass(LoginActivity.this, MenuRPLActivity.class);
                                    intent.putExtra("USUARIO", usuarioSt);
                                    DatosConfiguracion.idUsuario = jsonEstatus.getString("id_usuario");
                                    intent.putExtra("ID_USUARIO",jsonEstatus.getString("id_usuario"));
                                    intent.putExtra("PERFIL",jsonEstatus.getString("perfil"));
                                    intent.putExtra("PERMISO", jsonEstatus.getString("permisos"));


                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                    editor.putString("usuario",usuarioSt );
                                    editor.putString("perfil",jsonEstatus.getString("perfil"));
                                    editor.putString("id", jsonEstatus.getString("id_usuario"));
                                    editor.putString("permisos", jsonEstatus.getString("permisos"));
                                    editor.commit();



                                    startActivity(intent);
                                    overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                                    finish();
                                }else{
                                    AlertDialog.Builder quitDialog
                                            = new AlertDialog.Builder(LoginActivity.this);
                                    quitDialog.setTitle("ERROR");
                                    quitDialog.setMessage(Html.fromHtml( jsonEstatus.getString("error")));
                                    quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }});

                                    quitDialog.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                            break;
                    }
                }
            });
        }catch (Exception ad){
            System.out.println(ad.getMessage());
            ad.printStackTrace();
        }
    }
    public void loginApp(int paso) {
       try {
           final JSONObject JSONobj = new JSONObject();
           JSONobj.clear();
           JSONArray JSONarr = new JSONArray();
           JSONobj.put("paso", paso);
           JSONobj.put("usuario", usuarioSt);
           JSONobj.put("password",passwordSt.toUpperCase());
           JSONobj.put("mac",getMacAddress(this));
           JSONarr.add(JSONobj.clone());
           System.out.println(""+JSONarr.toString());
           final String Datos = URLEncoder.encode(JSONarr.toString());
           DatosConfiguracion datosConfiguracion = new DatosConfiguracion(getBaseContext());
           progressDialog = ProgressDialog.show(this, "Espere por Favor ...",	"Obteniendo validación...", true);
           RequestQueue queue = Volley.newRequestQueue(this);
           System.out.println("------*>"+datosConfiguracion.getEndpoint() + datosConfiguracion.getConsultaAutentificacion() + Datos);
           StringRequest stringRequest = new StringRequest(Request.Method.GET, datosConfiguracion.getEndpoint() + datosConfiguracion.getConsultaAutentificacion() + Datos ,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           progressDialog.dismiss();
                           System.out.println("RespuestaLogin->" + response);
                           JSONArray jsonArray = new JSONArray();
                           jsonArray.add(response);
                           try {
                               System.out.println("JSONESTATUS->"+jsonArray.get(0));
                               org.json.JSONArray jsonRespuesta =  new org.json.JSONArray(jsonArray.get(0).toString());
                               System.out.println("JAONUNOIN"+jsonRespuesta.get(0).toString());
                               org.json.JSONObject jsonEstatus = new org.json.JSONObject(jsonRespuesta.get(0).toString());
                               System.out.println("ESTATUS"+jsonEstatus.getBoolean("operacion")); //estatus

                               if(jsonEstatus.getBoolean("operacion")){ //estatus
                                   Intent intent = new Intent().setClass(LoginActivity.this, MenuRPLActivity.class);
                                   intent.putExtra("USUARIO", usuarioSt);
                                   DatosConfiguracion.idUsuario = jsonEstatus.getString("id_usuario");
                                   intent.putExtra("ID_USUARIO",jsonEstatus.getString("id_usuario"));
                                   intent.putExtra("PERFIL",jsonEstatus.getString("perfil"));
                                   intent.putExtra("PERMISO",jsonEstatus.getString("permisos"));
                                   intent.putExtra("ALMACEN", jsonEstatus.getString("almacen"));

                                   SharedPreferences.Editor editor = sharedpreferences.edit();

                                   editor.putString("usuario", usuarioSt);
                                   editor.putString("perfil",jsonEstatus.getString("perfil"));
                                   editor.putString("id", jsonEstatus.getString("id_usuario"));
                                   editor.putString("permisos", jsonEstatus.getString("permisos"));
                                   editor.putString("almacen", jsonEstatus.getString("almacen"));
                                   editor.putString("numeroempleado", jsonEstatus.getString("num_empleado"));

                                   editor.commit();
                                   startActivity(intent);
                                   //overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                                   finish();
                               }else{
                                   try {
                                       if (jsonEstatus.getString("sesion_activa").equalsIgnoreCase("true")) {

                                           AlertDialog.Builder quitDialog
                                                   = new AlertDialog.Builder(LoginActivity.this);
                                           quitDialog.setTitle(Html.fromHtml("<font color='#FC1414'>" + (jsonEstatus.getString("error")) + "</font>"));
                                           quitDialog.setMessage(Html.fromHtml("<p>¿Deseas cerrar la otra sesión?</p></br><font color='#FC1414'>Importante! </font></br><p>Si se finaliza una sesion activa en otro dispositivo puede que los datos y procesos del otro dispositivo se pierdan.</p>"));
                                           quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   loginApp(2); //paso 2 para que se cierre sesion en el otro device.
                                               }
                                           });
                                           quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {

                                               }
                                           });
                                           quitDialog.show();
                                       } else {
                                           AlertDialog.Builder quitDialog
                                                   = new AlertDialog.Builder(LoginActivity.this);
                                           quitDialog.setTitle("ERROR");
                                           quitDialog.setMessage(Html.fromHtml(jsonEstatus.getString("error")));
                                           quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {

                                               }
                                           });

                                           quitDialog.show();
                                       }
                                   }catch (Exception ex){
                                       AlertDialog.Builder quitDialog
                                               = new AlertDialog.Builder(LoginActivity.this);
                                       quitDialog.setTitle("ERROR");
                                       quitDialog.setMessage(Html.fromHtml(jsonEstatus.getString("error")));
                                       quitDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {

                                           }
                                       });

                                       quitDialog.show();
                                   }

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
                           = new AlertDialog.Builder(LoginActivity.this);
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

    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            System.out.println("12345->" + sb.toString());
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }

    public void showDialog(Activity context, String msg){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.numero_empleado_dialog);


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_aceptar);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "";
        }
        return macAddress;
    }


    @Override
    public void onBackPressed() {

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

  /*  public String desencriptar(String texto) {
        String str = "";
        addKey();
        try {
            byte[] value = new BASE64Decoder().decodeBuffer(texto);
            cipher = Cipher.getInstance(algoritmo);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cipherbytes = cipher.doFinal(value);
            str = new String(cipherbytes);
        } catch (InvalidKeyException ex) {
            System.err.println(ex.getMessage());
        } catch (IllegalBlockSizeException ex) {
            System.err.println(ex.getMessage());
        } catch (BadPaddingException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex.getMessage());
        } catch (NoSuchPaddingException ex) {
            System.err.println(ex.getMessage());
        }
        return str;
    }*/

}

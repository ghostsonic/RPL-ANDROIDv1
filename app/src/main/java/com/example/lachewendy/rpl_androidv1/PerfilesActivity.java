package com.example.lachewendy.rpl_androidv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
import com.uny2.clases.PermisosAdapter;
import com.uny2.clases.Usuario;
import com.uny2.clases.UsuarioAdapter;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class PerfilesActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    DatosConfiguracion datosConfiguracion;

    UsuarioAdapter arrayAdapter;
    ListView listViewUsuarios;

    TextView txtBackToolbar;
    TextView txtTituloToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfiles);
        txtBackToolbar = (TextView) findViewById(R.id.txtBackToolbar);
        txtTituloToolbar = (TextView) findViewById(R.id.txtTituloToolbar);
        listViewUsuarios = (ListView) findViewById(R.id.listUsuario);

        txtTituloToolbar.setText("Perfiles");

        eventos();

        pedirUsuarios();
    }

    public void pedirUsuarios() {
        try {

                final JSONObject JSONobj = new JSONObject();
                JSONobj.clear();
                JSONArray JSONarr = new JSONArray();
                JSONobj.put("paso",1);
                JSONarr.add(JSONobj.clone());
                System.out.println(""+JSONarr.toString());
                String Datos = URLEncoder.encode(JSONarr.toString());
                datosConfiguracion = new DatosConfiguracion(getBaseContext());
                progressDialog = ProgressDialog.show(this, "Espere por Favor ...", "Obteniendo datos", true);
                RequestQueue queue = Volley.newRequestQueue(this);
                System.out.println("DAORT->" + Datos);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioUsuario() + Datos,//DatosConfiguracion.home+"/usuarios.php",//datosConfiguracion.getEndpoint() + datosConfiguracion.getServicioUsuario() + Datos,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                System.out.println("TEXTOPerfiles" + response);

                                Usuario usuario = new Usuario();
                                try{
                                    arrayAdapter = new UsuarioAdapter(getBaseContext(), usuario.toArrayListWS(response, 2));
                                    listViewUsuarios.setAdapter(arrayAdapter);
                                    listViewUsuarios.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    listViewUsuarios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                        @Override
                                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                            ListView listViewPermisos;
                                            TextView txtNombre;
                                            TextView txtUsuario;
                                            TextView txtPerfil;
                                            TextView txtNumeroEmpleado;
                                            TextView txtFechaCreacion;
                                            TextView txtModificador;
                                            PermisosAdapter permisosAdapter = new PermisosAdapter(PerfilesActivity.this,arrayAdapter.getItem(position).getPermisos());
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PerfilesActivity.this);
                                            LayoutInflater li = LayoutInflater.from(PerfilesActivity.this);
                                            View promptsView = li.inflate(R.layout.dialog_usuario, null);
                                            txtNombre = (TextView) promptsView.findViewById(R.id.txtNombre);
                                            txtUsuario = (TextView) promptsView.findViewById(R.id.txtUsuario);
                                            txtPerfil = (TextView) promptsView.findViewById(R.id.txtPerfil);
                                            txtNumeroEmpleado = (TextView) promptsView.findViewById(R.id.txtNumeroEmpleado);
                                            txtFechaCreacion = (TextView) promptsView.findViewById(R.id.txtFechaCreacion);
                                            txtModificador = (TextView) promptsView.findViewById(R.id.txtModificador);
                                            listViewPermisos = (ListView) promptsView.findViewById(R.id.listViewPermisos);

                                            txtNombre.setText(""+arrayAdapter.getItem(position).getNombre());
                                            txtUsuario.setText("Usuario: "+arrayAdapter.getItem(position).getNombreUsuario());
                                            txtPerfil.setText("Perfil: "+arrayAdapter.getItem(position).getPerfil());
                                            txtNumeroEmpleado.setText("Nùm. Empleado: "+arrayAdapter.getItem(position).getNumeroEmpleado());
                                            txtFechaCreacion.setText("Fecha Modificaciòn: "+arrayAdapter.getItem(position).getFechaCreacion());
                                            txtModificador.setText("Modificador: "+arrayAdapter.getItem(position).getEstatus());

                                            listViewPermisos.setAdapter(permisosAdapter);
                                            listViewPermisos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                            alertDialogBuilder.setView(promptsView);
                                            alertDialogBuilder.setCancelable(false)
                                                    //.setTitle("USUARIO: "+arrayAdapter.getItem(position).getNombreUsuario())
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();
                                            return false;
                                        }
                                    });

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
                                = new AlertDialog.Builder(PerfilesActivity.this);
                        quitDialog.setTitle("ERROR");
                        quitDialog.setMessage("Ha ocurrido un error. Vuelve a intentar");
                        quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pedirUsuarios();
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
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(35000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(stringRequest);

        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void eventos(){
        txtBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}

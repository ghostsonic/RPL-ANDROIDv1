package com.uny2.clases;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.RPLApplication;
import com.com.uny2.metodos.AlmacenesDbHelper;

import java.util.ArrayList;

/**
 * Created by La che Wendy on 17/02/2016.
 */
public class DatosConfiguracion {
 //  public static String endpoint ="http://172.29.15.13:9090/WSVSCB/webresources/";
//    public static String endpoint ="http://192.168.8.40:9090/WSVSCB/webresources/";
   public static String endpoint ="http://10.30.206.54:8085/WSVSCB/webresources/";
    private String consultaAutentificacion = "ServicioAutentificacion?parametros=";
    private String recuperacionPass = "ServicioRestaurarContrasena?parametros=";
    private String resultadoConteo ="resultadoConteo?mac=";
    //private String consultaConteo ="ServicioConteoCiclico?parametros=";
    private String consultaConteo ="ServicioConteoCiclico";
    private String servicioUsuario = "ServicioUsuarios?parametros=";

    //private String servicioRecoleccion = "ServicioRecoleccion?parametros=";
    private String servicioRecoleccion = "ServicioRecoleccion";
    //private String servicioRecepcion = "ServicioEntradas?parametros=";
    private String servicioRecepcion = "ServicioEntradas";
    //private String servicioUbicacion = "ServicioUbicacion?parametros=";
    private String servicioUbicacion = "ServicioUbicacion";
    //private String servicioSalidas = "ServicioSalidas?parametros=";
    private String servicioSalidas = "ServicioSalidas";

    public String getServicioSalidas() {
        return servicioSalidas;
    }

    public void setServicioSalidas(String servicioSalidas) {
        this.servicioSalidas = servicioSalidas;
    }

    //Temporales
    public static String home="http://nocturnapp.hol.es/";

    public static String PREFERENCIAS_LOGIN="loginPreferences";
    public static String PREFERENCIAS_RPL = "rpl";
    public static String PREFERENCIAS_SELECT = "seleccion";

    public static int TIEMPO_PETICION = 470000;
    public static int TIEMPO_PETICION2 = 900000;

    public static int getTiempoPeticion2() {
        return TIEMPO_PETICION2;
    }

    public static void setTiempoPeticion2(int tiempoPeticion2) {
        TIEMPO_PETICION2 = tiempoPeticion2;
    }

    public String getServicioUsuario() {
        return servicioUsuario;
    }

    public void setServicioUsuario(String servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }
//15024434
    public static String idUsuario;
    public static String almacen;
    public static String numEmpleado;
    public DatosConfiguracion() {
        super();
    }
    public DatosConfiguracion(Context context) {
        try {
            ArrayList<String> conf = traerDatosConfiguracion(context);
            System.out.println("TAMAÑO DE ARREGLO DE CONFIGURACION:"+conf.size());
            if(!conf.isEmpty()){
                this.endpoint=conf.get(0);
                this.consultaAutentificacion=conf.get(1);
                this.resultadoConteo=conf.get(2);
                this.consultaConteo = conf.get(3);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    public String getConsultaConteo() {
        return consultaConteo;
    }

    public void setConsultaConteo(String consultaConteo) {
        this.consultaConteo = consultaConteo;
    }

    public String getResultadoConteo() {
        return resultadoConteo;
    }

    public void setResultadoConteo(String resultadoConteo) {
        this.resultadoConteo = resultadoConteo;
    }


    public String getConsultaAutentificacion() {
        return consultaAutentificacion;
    }

    public void setConsultaAutentificacion(String consultaAutentificacion) {
        this.consultaAutentificacion = consultaAutentificacion;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getRecuperacionPass() {
        return recuperacionPass;
    }

    public void setRecuperacionPass(String recuperacionPassNumero) {
        this.recuperacionPass = recuperacionPassNumero;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public ArrayList<String> traerDatosConfiguracion(Context ct){
        ArrayList<String> aux = new ArrayList<String>();
        try {
            AlmacenesDbHelper admin = new AlmacenesDbHelper(ct);
            SQLiteDatabase bd = admin.getWritableDatabase();
            Cursor fila = bd.rawQuery("select _id, referencia,valor from CONFIGALMACENES", null);
            if (fila.moveToFirst()) {
                do{
                 aux.add(fila.getString(2));
                    System.out.println("RECUPERANDO--->>>>"+fila.getString(2));
                } while(fila.moveToNext());
            }
            bd.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return aux;
    }

    public String getServicioRecoleccion() {
        return servicioRecoleccion;
    }

    public String getServicioRecepcion() {
        return servicioRecepcion;
    }

    public void setServicioRecepcion(String servicioRecepcion) {
        this.servicioRecepcion = servicioRecepcion;
    }

    public String getServicioUbicacion() {
        return servicioUbicacion;
    }


    public static void setSharedPreferences(String pref, String key, String value) {

        SharedPreferences.Editor editor = RPLApplication.getInstance().getApplicationContext().getSharedPreferences(pref, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static String getPreferences(String pref, String key) {

        SharedPreferences prefs = RPLApplication.getInstance().getApplicationContext().getSharedPreferences(pref, Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }
}

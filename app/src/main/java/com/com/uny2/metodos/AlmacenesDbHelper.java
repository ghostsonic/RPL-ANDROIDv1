package com.com.uny2.metodos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.uny2.clases.ConteoCiclico;
import com.uny2.clases.ConteoCiclicoAdapter;
import com.uny2.clases.DatosConfiguracion;
import com.uny2.clases.Recoleccion;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import javax.xml.transform.sax.SAXSource;

/**
 * Created by La che Wendy on 10/02/2016.
 */
public class AlmacenesDbHelper extends SQLiteOpenHelper {
    private static int version = 1;
    private static String name = "AlmacenesDb";
    private static CursorFactory factory = null;
    private Context cont;

    public AlmacenesDbHelper(Context context) {
        super(context, name, factory, version);
        this.cont = context;


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db.isReadOnly()) {
            db = getWritableDatabase();
        } else {
            Log.i(this.getClass().toString(), "Creando base de datos");
            //Tabla SQLite para configuraciiones
            db.execSQL("CREATE TABLE CONFIGALMACENES(" +
                    " _id INTEGER PRIMARY KEY," +
                    " referencia TEXT NOT NULL, " +
                    " valor  TEXT NOT NULL)");
            db.execSQL("CREATE UNIQUE INDEX referencia ON CONFIGALMACENES(referencia ASC)");
            Log.i(this.getClass().toString(), "Tabla CONFIGALMACENES creada");
            //Tabla SQLite ConteoCiclico
            db.execSQL("CREATE TABLE CONTEOS_CICLICO(" +
                    " _id INTEGER NOT NULL," +
                    "idUsuario INTEGER NOT NULL," +
                    " sku TEXT NOT NULL, " +
                    " descripcion TEXT NOT NULL, " +
                    " ubicacion TEXT NOT NULL, " +
                    " cantidad TEXT default '0.0'," +
                    " estatus TEXT NOT NULL, " +
                    " confirmacion TEXT default 'false')");

            db.execSQL("CREATE TABLE RECOLECCION(" +
                    "_id INTEGER NOT NULL," +
                    "idUsuario INTEGER NOT NULL," +
                    "orden_servicio TEXT NOT NULL," +
                    "id_pegado TEXT NOT NULL," +
                    "fecha_pegado TEXT NOT NULL," +
                    "sku TEXT NOT NULL, " +
                    "descripcion TEXT NOT NULL, " +
                    "cantidad TEXT default '0.0'," +
                    "cantidad_recolectada TEXT default '0.0'," +
                    "unidad_medida TEXT NOT NULL," +
                    "ubicacion TEXT NOT NULL, " +
                    "estatus TEXT NOT NULL, " +
                    "confirmacion TEXT default 'false'," +
                    "faltante TEXT default '0.0'," +
                    "garantia TEXT default 'false')");

            db.execSQL("CREATE TABLE RECOLECCIONNUEVO(" +
                    "_id INTEGER NOT NULL," +
                    "idUsuario INTEGER NOT NULL," +
                    "orden_servicio TEXT NOT NULL," +
                    "id_pegado TEXT NOT NULL," +
                    "fecha_pegado TEXT NOT NULL," +
                    "sku TEXT NOT NULL, " +
                    "descripcion TEXT NOT NULL, " +
                    "cantidad TEXT default '0.0'," +
                    "cantidad_recolectada TEXT default '0.0'," +
                    "unidad_medida TEXT NOT NULL," +
                    "estatus TEXT NOT NULL, " +
                    "confirmacion TEXT default 'false'," +
                    "faltante TEXT default '0.0'," +
                    "garantia TEXT default 'false'," +
                    "ubicacionP TEXT," +
                    "cantidadP TEXT default '0.0'," +
                    "ubicacionC TEXT," +
                    "cantidadC TEXT default '0.0' )");
            Log.i(this.getClass().toString(), "Tabla CONTEOS_CICLICO creada");
            /*
            db.execSQL("INSERT INTO CONFIGALMACENES(_id, referencia,valor) VALUES(1,'endpoint','"+DatosConfiguracion.endpoint+"')");
            db.execSQL("INSERT INTO CONFIGALMACENES(_id, referencia,valor) VALUES(2,'consultaAutentificacion','consultaAutentificacion?parametros=')");
            db.execSQL("INSERT INTO CONFIGALMACENES(_id, referencia,valor) VALUES(3,'resultadoConteo','resultadoConteo?mac=')");
            db.execSQL("INSERT INTO CONFIGALMACENES(_id, referencia,valor) VALUES(4,'consultaConteo','ServicioConteoCiclico?parametros=')");
            */
            Log.i(this.getClass().toString(), "Base de datos creada");
            System.out.println(this.getClass().toString() + "Base de datos creada");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int conteoActivo() {
        SQLiteDatabase count = this.getWritableDatabase();
        Cursor fila = count.rawQuery("select * from CONTEOS_CICLICO where idUsuario=" + DatosConfiguracion.idUsuario, null);
        int conteoActivo = fila.getCount();
        return conteoActivo;
    }

    public int recoleccionActivo() {
        SQLiteDatabase count = this.getWritableDatabase();
        Cursor fila = count.rawQuery("select * from RECOLECCION where idUsuario=" + DatosConfiguracion.idUsuario, null);
        int conteoActivo = fila.getCount();
        return conteoActivo;
    }


    public String insertRecoleccion(String respuesta) {
        String respuestaID = "";
        try {
            JSONObject JSONobj = new JSONObject();
            JSONArray JSONarr = new JSONArray();
            SQLiteDatabase insert = this.getWritableDatabase();
            ArrayList<Recoleccion> conteoList = Recoleccion.toArrayListWS(respuesta, 1);
            String sqlite = "INSERT INTO RECOLECCION(_id,idUsuario, orden_servicio,id_pegado,fecha_pegado,sku,descripcion,cantidad,unidad_medida,ubicacion,estatus,confirmacion, garantia) VALUES";
            String sqliteInsert = "";
            ArrayList<Integer> filtro = filtroRecoleccionCiclo();
            if (filtro.size() == 0) {
                for (Recoleccion x : conteoList) {
                    sqliteInsert += "('" + x.getIdRow() + "','" + DatosConfiguracion.idUsuario + "','" + x.getOrdenServicio() + "','" + x.getIdPegado() + "','" + x.getFechaPegado()
                            + "','" + x.getSku() + "','" + x.getDescripcion() + "','" + x.getCantidad() + "','" + x.getUnidadMedida() + "','" + x.getUbicacion()
                            + "','" + x.getEstatus() + "','" + x.isConfirmacion() + "','" + x.isGarantia() + "'),";
                    JSONobj.clear();
                    JSONobj.put("_id", x.getIdRow());
                    JSONobj.put("idUsuario", DatosConfiguracion.idUsuario);
                    JSONobj.put("orden_servicio", x.getOrdenServicio());
                    JSONobj.put("id_pegado", x.getIdPegado());
                    JSONobj.put("fecha_pegado", x.getFechaPegado());
                    JSONobj.put("sku", x.getSku());
                    JSONobj.put("descripcion", x.getDescripcion());
                    JSONobj.put("cantidad", x.getCantidad());
                    JSONobj.put("unidad_medida", x.getUnidadMedida());
                    JSONobj.put("ubicacion", x.getUbicacion());
                    JSONobj.put("estatus", x.getEstatus());
                    JSONobj.put("confirmacion", x.isConfirmacion());
                    JSONobj.put("garantia", x.isGarantia());
                    JSONarr.add(JSONobj.clone());

                    System.out.println("142kelpersku " + x.getSku());
                    System.out.println("canti " + x.getCantidad());
                    System.out.println("reco " + x.getCantidadRecolectada());
                }
            } else {
                System.out.println("Tamaño de recuperacion de orden: " + filtro.size());
                for (Recoleccion x : conteoList) {
                    if (!filtro.contains(x.getIdPegado())) {
                        sqliteInsert += "('" + x.getIdRow() + "','" + DatosConfiguracion.idUsuario + "','" + x.getOrdenServicio() + "','" + x.getIdPegado() + "','" + x.getFechaPegado()
                                + "','" + x.getSku() + "','" + x.getDescripcion() + "','" + x.getCantidad() + "','" + x.getUnidadMedida() + "','" + x.getUbicacion()
                                + "','" + x.getEstatus() + "','" + x.isConfirmacion() + "'),";
                        JSONobj.clear();
                        JSONobj.put("_id", x.getIdRow());
                        JSONobj.put("idUsuario", DatosConfiguracion.idUsuario);
                        JSONobj.put("orden_servicio", x.getOrdenServicio());
                        JSONobj.put("id_pegado", x.getIdPegado());
                        JSONobj.put("fecha_pegado", x.getFechaPegado());
                        JSONobj.put("sku", x.getSku());
                        JSONobj.put("descripcion", x.getDescripcion());
                        JSONobj.put("cantidad", x.getCantidad());
                        JSONobj.put("unidad_medida", x.getUnidadMedida());
                        JSONobj.put("ubicacion", x.getUbicacion());
                        JSONobj.put("estatus", x.getEstatus());
                        JSONobj.put("confirmacion", x.isConfirmacion());
                        JSONobj.put("garantia", x.isGarantia());
                        JSONarr.add(JSONobj.clone());

                        System.out.println("sku169helper " + x.getSku());
                        System.out.println("canti " + x.getCantidad());
                        System.out.println("reco " + x.getCantidadRecolectada());
                    } else {
                        System.out.println("Disriminado-->>" + x.getOrdenServicio() + "--" + x.getIdPegado());
                        System.out.println("174helpersku " + x.getSku());
                        System.out.println("canti " + x.getCantidad());
                        System.out.println("reco " + x.getCantidadRecolectada());
                    }
                }
            }
            if (!sqliteInsert.equals("")) {
                sqliteInsert = sqliteInsert.substring(0, sqliteInsert.length() - 1);
                System.out.println("QUERY: --->>>" + sqlite + sqliteInsert);
                insert.execSQL(sqlite + sqliteInsert);
            }
            System.out.println("json antes de enviar a otro metodo: "+ JSONarr.toString());
            insert.close();
            //  respuestaID = JSONarr.toString();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return consultarOrdenRecoleccion();
    }


    /**
     * Sen envia la respuesta para procesarla y convertir un ArrayList, despues de se itera la lista para insertarlo en la base de datos
     * y para crear un json que sera enviado al metodo consultarOrdenRecoleccioNueva()
     * @param respuesta
     * @return
     */
    public String insertNuevaRecoleccion(String respuesta) {
        String respuestaID = "";
        try {
            JSONObject JSONobj = new JSONObject();
            JSONArray JSONarr = new JSONArray();
            SQLiteDatabase insert = this.getWritableDatabase();
            ArrayList<Recoleccion> conteoList = Recoleccion.toArrayListRWS(respuesta, 1);
            String sqlite = "INSERT INTO RECOLECCIONNUEVO(_id,idUsuario, orden_servicio,id_pegado,fecha_pegado,sku,descripcion,cantidad,unidad_medida,estatus,confirmacion, garantia, ubicacionP, cantidadP, ubicacionC, cantidadC) VALUES";
            String sqliteInsert = "";
            ArrayList<Integer> filtro = filtroRecoleccionNuevoCiclo();
            if (filtro.size() == 0) {
                for (Recoleccion x : conteoList) {
                    sqliteInsert += "('" + x.getIdRow() + "','" + DatosConfiguracion.idUsuario + "','" + x.getOrdenServicio() + "','" + x.getIdPegado() + "','" + x.getFechaPegado()
                            + "','" + x.getSku() + "','" + x.getDescripcion() + "','" + x.getCantidad() + "','" + x.getUnidadMedida()
                            + "','" + x.getEstatus() + "','" + x.isConfirmacion() + "','" + x.isGarantia() + "','"+x.getUbicacionP()+"','"+ x.getCantidadP()+ "','"+x.getUbicacionC()+"','"+x.getCantidadC() +"'),";
                    JSONobj.clear();
                    JSONobj.put("_id", x.getIdRow());
                    JSONobj.put("idUsuario", DatosConfiguracion.idUsuario);
                    JSONobj.put("orden_servicio", x.getOrdenServicio());
                    JSONobj.put("id_pegado", x.getIdPegado());
                    JSONobj.put("fecha_pegado", x.getFechaPegado());
                    JSONobj.put("sku", x.getSku());
                    JSONobj.put("descripcion", x.getDescripcion());
                    JSONobj.put("cantidad", x.getCantidad());
                    JSONobj.put("unidad_medida", x.getUnidadMedida());
                    JSONobj.put("estatus", x.getEstatus());
                    JSONobj.put("confirmacion", x.isConfirmacion());
                    JSONobj.put("garantia", x.isGarantia());
                    JSONobj.put("ubicacionP", x.getUbicacionP());
                    JSONobj.put("cantidadP", x.getCantidadP());
                    JSONobj.put("ubicacionC", x.getUbicacionC());
                    JSONobj.put("cantidadC", x.getCantidadC());

                    JSONarr.add(JSONobj.clone());

                    System.out.println("142kelperskuNueava " + x.getSku());
                    System.out.println("cantiNuea " + x.getCantidad());
                    System.out.println("recoNueva " + x.getCantidadRecolectada());
                }
            } else {
                System.out.println("Tamaño de recuperacion de ordenSeguroentra aqui?: " + filtro.size());
                for (Recoleccion x : conteoList) {
                    if (!filtro.contains(x.getIdPegado())) {
                        sqliteInsert += "('" + x.getIdRow() + "','" + DatosConfiguracion.idUsuario + "','" + x.getOrdenServicio() + "','" + x.getIdPegado() + "','" + x.getFechaPegado()
                                + "','" + x.getSku() + "','" + x.getDescripcion() + "','" + x.getCantidad() + "','" + x.getUnidadMedida() + "','" + x.getUbicacion()
                                + "','" + x.getEstatus() + "','" + x.isConfirmacion() + "'),";
                        JSONobj.clear();
                        JSONobj.put("_id", x.getIdRow());
                        JSONobj.put("idUsuario", DatosConfiguracion.idUsuario);
                        JSONobj.put("orden_servicio", x.getOrdenServicio());
                        JSONobj.put("id_pegado", x.getIdPegado());
                        JSONobj.put("fecha_pegado", x.getFechaPegado());
                        JSONobj.put("sku", x.getSku());
                        JSONobj.put("descripcion", x.getDescripcion());
                        JSONobj.put("cantidad", x.getCantidad());
                        JSONobj.put("unidad_medida", x.getUnidadMedida());
                        JSONobj.put("ubicacion", x.getUbicacion());
                        JSONobj.put("estatus", x.getEstatus());
                        JSONobj.put("confirmacion", x.isConfirmacion());
                        JSONobj.put("garantia", x.isGarantia());
                        JSONarr.add(JSONobj.clone());

                        System.out.println("sku169helperEntroNuevaRecoleccion " + x.getSku());
                        System.out.println("cantiNuevaReocleccion " + x.getCantidad());
                        System.out.println("recoNuevaRecoleccion " + x.getCantidadRecolectada());
                    } else {
                        System.out.println("Disriminado-->>" + x.getOrdenServicio() + "--" + x.getIdPegado());
                        System.out.println("174helpersku " + x.getSku());
                        System.out.println("canti " + x.getCantidad());
                        System.out.println("reco " + x.getCantidadRecolectada());
                    }
                }
            }
            if (!sqliteInsert.equals("")) {
                sqliteInsert = sqliteInsert.substring(0, sqliteInsert.length() - 1);
                System.out.println("QUERY: --->>>" + sqlite + sqliteInsert);
                insert.execSQL(sqlite + sqliteInsert);
            }
            System.out.println("json antes de enviar a otro metodo: "+ JSONarr.toString());
            insert.close();
            //  respuestaID = JSONarr.toString();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return consultarOrdenRecoleccionNueva();
    }

    public String consultarOrdenRecoleccion() {
        JSONArray JSONarr = null;
        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            Cursor fila = bd.rawQuery("select distinct orden_servicio,fecha_pegado,id_pegado  from Recoleccion where idUsuario=" + DatosConfiguracion.idUsuario, null); //where idUsuario = var
            JSONObject JSONobj = new JSONObject();
            JSONarr = new JSONArray();
            if (fila.moveToFirst()) {
                do {
                    JSONobj.clear();
                    JSONobj.put("orden_servicio", fila.getString(0));
                    JSONobj.put("fecha_pegado", fila.getString(1));
                    JSONobj.put("id_pegado", fila.getString(2));
                    JSONarr.add(JSONobj.clone());
                } while (fila.moveToNext());
            }
            System.out.println("ARRAY DE BASE-->>" + JSONarr.toString());
            bd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONarr.toString();
    }

    /**
     * Este metoddo hace el query para enviar en JSON la consulta a estos parametros y
     * el usuario escoja que orden va a procesar para posteriormente elegir las refacciones
     * @return JSON que obtiene la informacion de la consulta para mostrarle una primera lista.
     */
    public String consultarOrdenRecoleccionNueva() {
        JSONArray JSONarr = null;
        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            Cursor fila = bd.rawQuery("select distinct orden_servicio,fecha_pegado,id_pegado  from RECOLECCIONNUEVO where idUsuario=" + DatosConfiguracion.idUsuario, null); //where idUsuario = var
            JSONObject JSONobj = new JSONObject();
            JSONarr = new JSONArray();
            if (fila.moveToFirst()) {
                do {
                    JSONobj.clear();
                    JSONobj.put("orden_servicio", fila.getString(0));
                    JSONobj.put("fecha_pegado", fila.getString(1));
                    JSONobj.put("id_pegado", fila.getString(2));
                    JSONarr.add(JSONobj.clone());
                } while (fila.moveToNext());
            }
            System.out.println("ARRAY DE BASE-->>" + JSONarr.toString());
            bd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONarr.toString();
    }


    public String consultarOrdenRecoleccionActiva() {
        JSONArray JSONarr = null;
        String orden = "";
        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            Cursor fila = bd.rawQuery("select  orden_servicio from Recoleccion where cantidad_recolectada NOT LIKE '0.0' and idUsuario=" + DatosConfiguracion.idUsuario, null); //where idUsuario = var
            JSONObject JSONobj = new JSONObject();
            JSONarr = new JSONArray();
            if (fila.moveToFirst()) {
                do {
                    JSONobj.clear();
                    JSONobj.put("orden_servicio", fila.getString(0));
                    orden = fila.getString(0);
                    JSONarr.add(JSONobj.clone());
                } while (fila.moveToNext());
            }
            System.out.println("ARRAY DE BASE-->>" + JSONarr.toString());
            bd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return orden;
    }


    public ArrayList<Integer> filtroRecoleccionCiclo() {
        ArrayList<Integer> res = null;

        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            Cursor fila = bd.rawQuery("select distinct id_pegado  from Recoleccion where idUsuario=" + DatosConfiguracion.idUsuario, null); //where idUsuario = var
            res = new ArrayList<>();
            if (fila.moveToFirst()) {
                do {
                    res.add(Integer.parseInt(fila.getString(0)));
                } while (fila.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public ArrayList<Integer> filtroRecoleccionNuevoCiclo() {
        ArrayList<Integer> res = null;

        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            Cursor fila = bd.rawQuery("select distinct id_pegado  from RECOLECCIONNUEVO where idUsuario=" + DatosConfiguracion.idUsuario, null); //where idUsuario = var
            res = new ArrayList<>();
            if (fila.moveToFirst()) {
                do {
                    res.add(Integer.parseInt(fila.getString(0)));
                } while (fila.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public String obtenerDescripcionOrdenRecoleccion(String orden_servicio, int id_pegado){
        JSONArray JSONarr=null;
        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            Cursor fila = bd.rawQuery("select _id,sku,descripcion,cantidad,ubicacion,unidad_medida,estatus,confirmacion,orden_servicio,id_pegado,fecha_pegado, cantidad_recolectada  from Recoleccion where idUsuario=" + DatosConfiguracion.idUsuario + " and orden_servicio='" + orden_servicio + "' and id_pegado='" + id_pegado + "'", null); //where idUsuario = var
            JSONObject JSONobj = new JSONObject();
            JSONarr= new JSONArray();
            if (fila.moveToFirst()) {
                do{
                    JSONobj.clear();
                    JSONobj.put("_id", fila.getString(0));
                    JSONobj.put("sku", fila.getString(1));
                    JSONobj.put("descripcion", fila.getString(2));
                    JSONobj.put("cantidad", fila.getString(3));
                    JSONobj.put("ubicacion", fila.getString(4));
                    JSONobj.put("unidad_medida", fila.getString(5));
                    JSONobj.put("estatus", fila.getInt(6));
                    JSONobj.put("confirmacion", fila.getString(7));
                    JSONobj.put("orden_servicio", fila.getString(8));
                    JSONobj.put("id_pegado", fila.getString(9));
                    JSONobj.put("fecha_pegado", fila.getString(10));
                    JSONobj.put("cantidad_recolectada", fila.getString(11));
                    JSONarr.add(JSONobj.clone());
                } while(fila.moveToNext());
            }
            System.out.println("ARRAY DE BASE-*->>" + JSONarr.toString());
            bd.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  JSONarr.toString();
    }

    /**
     * Metodo que realizara la consulta para obtener los elementos que se encuentran en esa orden y crear el listado de refacciones. En este paso mostraremos
     * la lista de elmenetos para que seleccione los que va a recolectar.
     * se manda a llamar dos veces la primera para procesar y consultar la primer lista(la que el usuario selecciona) y la segunda sera con la segunda lista
     * la que el usuario escanea.
     * @param orden_servicio
     * @param id_pegado
     * @return
     */
    public String obtenerDescripcionOrdenRecoleccionNuevo(String orden_servicio, int id_pegado) {
        System.out.println("entra a obtenerDescripcionOrdenRecoleccion");
        JSONArray JSONarr = null;
        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            Cursor fila = bd.rawQuery("select _id,sku,descripcion,cantidad,unidad_medida,estatus,confirmacion,orden_servicio,id_pegado,fecha_pegado, cantidad_recolectada, garantia, ubicacionP, cantidadP, ubicacionC, cantidadC  from RecoleccionNuevo where idUsuario=" + DatosConfiguracion.idUsuario + " and orden_servicio='" + orden_servicio + "' and id_pegado='" + id_pegado + "'", null); //where idUsuario = var
            JSONObject JSONobj = new JSONObject();
            JSONarr = new JSONArray();
            if (fila.moveToFirst()) {
                do {
                    JSONobj.clear();
                    JSONobj.put("_id", fila.getString(0));
                    JSONobj.put("sku", fila.getString(1));
                    JSONobj.put("descripcion", fila.getString(2));
                    JSONobj.put("cantidad", fila.getString(3));
                    JSONobj.put("unidad_medida", fila.getString(4));
                    JSONobj.put("estatus", fila.getInt(5));
                    JSONobj.put("confirmacion", fila.getString(6));
                    JSONobj.put("orden_servicio", fila.getString(7));
                    JSONobj.put("id_pegado", fila.getString(8));
                    JSONobj.put("fecha_pegado", fila.getString(9));
                    JSONobj.put("cantidad_recolectada", fila.getString(10));
                    JSONobj.put("garantia", fila.getString(11));
                    JSONobj.put("ubicacionP", fila.getString(12));
                    JSONobj.put("cantidadP", fila.getString(13));
                    JSONobj.put("ubicacionC", fila.getString(14));
                    JSONobj.put("cantidadC", fila.getString(15));
                    JSONarr.add(JSONobj.clone());
                } while (fila.moveToNext());
            }
            System.out.println("ARRAY DE BASE-*-Para elementos de PRELISTA>>" + JSONarr.toString());
            bd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONarr.toString();
    }


 public String obtenerDescipcionOrdenRecoleccion(String orden_servicio, int id_pegado) {
        System.out.println("entra a obtenerDescripcionOrdenRecoleccion");
        JSONArray JSONarr = null;
        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            Cursor fila = bd.rawQuery("select _id,sku,descripcion,cantidad,ubicacion,unidad_medida,estatus,confirmacion,orden_servicio,id_pegado,fecha_pegado, cantidad_recolectada, garantia  from Recoleccion where idUsuario=" + DatosConfiguracion.idUsuario + " and orden_servicio='" + orden_servicio + "' and id_pegado='" + id_pegado + "'", null); //where idUsuario = var
            JSONObject JSONobj = new JSONObject();
            JSONarr = new JSONArray();
            if (fila.moveToFirst()) {
                do {
                    JSONobj.clear();
                    JSONobj.put("_id", fila.getString(0));
                    JSONobj.put("sku", fila.getString(1));
                    JSONobj.put("descripcion", fila.getString(2));
                    JSONobj.put("cantidad", fila.getString(3));
                    JSONobj.put("ubicacion", fila.getString(4));
                    JSONobj.put("unidad_medida", fila.getString(5));
                    JSONobj.put("estatus", fila.getInt(6));
                    JSONobj.put("confirmacion", fila.getString(7));
                    JSONobj.put("orden_servicio", fila.getString(8));
                    JSONobj.put("id_pegado", fila.getString(9));
                    JSONobj.put("fecha_pegado", fila.getString(10));
                    JSONobj.put("cantidad_recolectada", fila.getString(11));
                    JSONobj.put("garantia", fila.getString(12));
                    JSONarr.add(JSONobj.clone());
                } while (fila.moveToNext());
            }
            System.out.println("ARRAY DE BASE-*->>" + JSONarr.toString());
            bd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONarr.toString();
    }


    public String insertConteoCiclico(String respuesta) {
        String respuestaID = "";
        try {
            JSONObject JSONobj = new JSONObject();
            JSONArray JSONarr = new JSONArray();
            SQLiteDatabase insert = this.getWritableDatabase();
            ArrayList<ConteoCiclico> conteoList = ConteoCiclico.toArrayListWS(respuesta, 1);

            int i = 1;
            for (ConteoCiclico x : conteoList) {
                String sqlite = "INSERT INTO CONTEOS_CICLICO(_id,idUsuario, sku,descripcion,ubicacion,estatus) VALUES";
                sqlite += "('" + x.getIdRow() + "','" + DatosConfiguracion.idUsuario + "','" + x.getSku() + "','" + x.getDescripcion() + "','" + x.getUbicacion() + "','" + x.getEstatus() + "'),";
                JSONobj.clear();
                JSONobj.put("id", x.getIdRow());
                JSONobj.put("idUsuario", DatosConfiguracion.idUsuario);
                JSONobj.put("sku", x.getSku());
                JSONobj.put("descripcion", x.getDescripcion());
                JSONobj.put("estatus", x.getEstatus());
                JSONobj.put("ubicacion", x.getUbicacion());
                JSONarr.add(JSONobj.clone());
                sqlite = sqlite.substring(0, sqlite.length() - 1);
                System.out.println("QUERY: --->>>" + sqlite);
                insert.execSQL(sqlite);

                i++;
            }
            insert.close();
            respuestaID = JSONarr.toString();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return respuestaID;
    }


    public String insertConteoCiclicoActualizado(ArrayList<ConteoCiclico> actualizadoArrayList) {
        String respuestaID = "";
        try {
            JSONObject JSONobj = new JSONObject();
            JSONArray JSONarr = new JSONArray();
            SQLiteDatabase insert = this.getWritableDatabase();
           // ArrayList<ConteoCiclico> conteoList = ConteoCiclico.toArrayListWS(respuesta, 1);

            int i = 1;
            for (ConteoCiclico x : actualizadoArrayList) {
                String sqlite = "INSERT INTO CONTEOS_CICLICO(_id,idUsuario, sku,descripcion,ubicacion,estatus, cantidad, confirmacion) VALUES";
                sqlite += "('" + x.getIdRow() + "','" + DatosConfiguracion.idUsuario + "','" + x.getSku() + "','" + x.getDescripcion() + "','" + x.getUbicacion() + "','" + x.getEstatus() + "','" + x.getCantidad() + "','" + x.isConfirmacion() + "'),";
                JSONobj.clear();
                JSONobj.put("id", x.getIdRow());
                JSONobj.put("idUsuario", DatosConfiguracion.idUsuario);
                JSONobj.put("sku", x.getSku());
                JSONobj.put("descripcion", x.getDescripcion());
                JSONobj.put("estatus", x.getEstatus());
                JSONobj.put("ubicacion", x.getUbicacion());
                JSONobj.put("cantidad_contada",x.getCantidad());
                JSONobj.put("confirmacion",x.isConfirmacion());
                JSONarr.add(JSONobj.clone());
                sqlite = sqlite.substring(0, sqlite.length() - 1);
                System.out.println("QUERY: --->>>" + sqlite);
                insert.execSQL(sqlite);

                i++;
            }
            insert.close();
            respuestaID = JSONarr.toString();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return respuestaID;
    }






    public ArrayList<ConteoCiclico> actualizarConteo(String respuesta){
        ArrayList<ConteoCiclico> conteoActualizadoArray = new ArrayList<>();
        try{
            //boolean existe = false;
            ArrayList<ConteoCiclico> conteoRecibidoArray = ConteoCiclico.toArrayListWS(respuesta, 1);
            ArrayList<ConteoCiclico> conteoAlmacendoArray = ConteoCiclico.toArrayListWS(resgresarConteo(), 5);

            for (ConteoCiclico item: conteoRecibidoArray) {
                boolean existe = false;
                for (int i = 0; i < conteoAlmacendoArray.size(); i++) {
                    if(conteoAlmacendoArray.get(i).getSku().equalsIgnoreCase(item.getSku()) && conteoAlmacendoArray.get(i).getUbicacion().equalsIgnoreCase(item.getUbicacion()) ){
                        existe = true;
                       // if(!conteoActualizadoArray.contains(conteoAlmacendoArray.get(i))){
                            existe = true;
                            conteoActualizadoArray.add(conteoAlmacendoArray.get(i));
                       // }else{
                            break;
                       // }
                    }/*else {
                        existe = false;
                        conteoActualizadoArray.add(conteoRecibidoArray.get(i));
                    }*/
                }

                if(!existe){
                    conteoActualizadoArray.add(item);
                }


            }
            borrarConteo("CONTEOS_CICLICO");
            conteoActualizadoArray = ConteoCiclico.toArrayListWS(insertConteoCiclicoActualizado(conteoActualizadoArray),5);

            /*for (int i = 1; i < conteoAlmacendoArray.size(); i++) {
                existe = false;
                for (int j = 0; j < conteoRecibidoArray.size(); j++) {
                    if (conteoAlmacendoArray.get(i).getSku().equalsIgnoreCase(conteoAlmacendoArray.get(j).getSku().toString())) {
                        existe = true;
                        continue;
                    }
                }

                if(!existe) {
                    conteoActualizadoArray.add(limpiandoJSON.get(i));
                }
            }*/



        }catch(Exception e){
            e.printStackTrace();
        }
        return conteoActualizadoArray;
    }




    public String resgresarConteo() {
        JSONArray JSONarr = null;
        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            Cursor fila = bd.rawQuery("select  _id,sku,descripcion,estatus,ubicacion,cantidad,confirmacion,idUsuario from CONTEOS_CICLICO where idUsuario=" + DatosConfiguracion.idUsuario, null); //where idUsuario = var
            JSONObject JSONobj = new JSONObject();
            JSONarr = new JSONArray();
            if (fila.moveToFirst()) {
                do {
                    if (!Boolean.parseBoolean(fila.getString(6)) || fila.getString(6).equalsIgnoreCase("false")) {
                        JSONobj.clear();
                        JSONobj.put("id", fila.getString(0));
                        JSONobj.put("sku", fila.getString(1));
                        JSONobj.put("descripcion", fila.getString(2));
                        JSONobj.put("estatus", fila.getString(3));
                        JSONobj.put("ubicacion", fila.getString(4));
                        JSONobj.put("cantidad_contada", fila.getString(5));
                        JSONobj.put("confirmacion", fila.getString(6));
                        JSONobj.put("idUsuario", fila.getString(7));
                        JSONarr.add(JSONobj.clone());
                    } else {
                        System.out.println("DISCRIMINA" + fila.getPosition());
                    }
                } while (fila.moveToNext());
            }
            System.out.println("ARRAY DE REGRESO*-->>" + JSONarr.toString());
            bd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONarr.toString();
    }


    public String resgresarRecoleccion() {
        JSONArray JSONarr = null;
        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            ArrayList<Integer> filtro = filtroRecoleccionCiclo();
            Cursor fila = bd.rawQuery("select  _id, idUsuario,orden_servicio,id_pegado,fecha_pegado,sku,descripcion,cantidad,unidad_medida,ubicacion,estatus,confirmacion,cantidad_recolectada from RECOLECCION where idUsuario=" + DatosConfiguracion.idUsuario, null); //where idUsuario = var
            JSONObject JSONobj = new JSONObject();
            JSONarr = new JSONArray();
            if (fila.moveToFirst()) {
                do {
                    if (!Boolean.parseBoolean(fila.getString(11))) {
                        if (!filtro.contains(fila.getInt(3))) {
                            JSONobj.clear();
                            JSONobj.put("_id", fila.getString(0));
                            JSONobj.put("idUsuario", fila.getString(1));
                            JSONobj.put("orden_servicio", fila.getString(2));
                            JSONobj.put("id_pegado", fila.getString(3));
                            JSONobj.put("fecha_pegado", fila.getString(4));
                            JSONobj.put("sku", fila.getString(5));
                            JSONobj.put("descripcion", fila.getString(6));
                            JSONobj.put("cantidad", fila.getString(7));
                            JSONobj.put("unidad_medida", fila.getString(8));
                            JSONobj.put("ubicacion", fila.getString(9));
                            JSONobj.put("estatus", fila.getString(10));
                            JSONobj.put("confirmacion", fila.getString(11));
                            JSONobj.put("cantidad_recolectada", fila.getString(12));
                            JSONarr.add(JSONobj.clone());
                        }
                    } else {
                        System.out.println("DISCRIMINA" + fila.getPosition());
                    }
                } while (fila.moveToNext());
            }
            System.out.println("****ARRAY DE BASE-->>" + JSONarr.toString());
            bd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONarr.toString();
    }
    public String resgresarRecoleccionNuevo() {
        JSONArray JSONarr = null;
        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            ArrayList<Integer> filtro = filtroRecoleccionCiclo();
            Cursor fila = bd.rawQuery("select  _id, idUsuario,orden_servicio,id_pegado,fecha_pegado,sku,descripcion,cantidad,unidad_medida,estatus,confirmacion,cantidad_recolectada from RECOLECCIONNUEVO where idUsuario=" + DatosConfiguracion.idUsuario, null); //where idUsuario = var
            JSONObject JSONobj = new JSONObject();
            JSONarr = new JSONArray();
            if (fila.moveToFirst()) {
                do {
                    if (!Boolean.parseBoolean(fila.getString(11))) {
                        if (!filtro.contains(fila.getInt(3))) {
                            JSONobj.clear();
                            JSONobj.put("_id", fila.getString(0));
                            JSONobj.put("idUsuario", fila.getString(1));
                            JSONobj.put("orden_servicio", fila.getString(2));
                            JSONobj.put("id_pegado", fila.getString(3));
                            JSONobj.put("fecha_pegado", fila.getString(4));
                            JSONobj.put("sku", fila.getString(5));
                            JSONobj.put("descripcion", fila.getString(6));
                            JSONobj.put("cantidad", fila.getString(7));
                            JSONobj.put("unidad_medida", fila.getString(8));
                            JSONobj.put("estatus", fila.getString(9));
                            JSONobj.put("confirmacion", fila.getString(10));
                            JSONobj.put("cantidad_recolectada", fila.getString(11));
                            JSONarr.add(JSONobj.clone());
                        }
                    } else {
                        System.out.println("DISCRIMINA" + fila.getPosition());
                    }
                } while (fila.moveToNext());
            }
            System.out.println("****ARRAY DE BASE-->>" + JSONarr.toString());
            bd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONarr.toString();
    }

    public String resgresarRecoleccionActiva(String orden) {
        JSONArray JSONarr = null;
        try {
            SQLiteDatabase bd = this.getWritableDatabase();
            //ArrayList<Integer> filtro = filtroRecoleccionCiclo();
            Cursor fila = bd.rawQuery("select  _id, idUsuario,orden_servicio,id_pegado,fecha_pegado,sku,descripcion,cantidad,unidad_medida,ubicacion,estatus,confirmacion,cantidad_recolectada,faltante from RECOLECCION where orden_servicio LIKE '" + orden + "' and idUsuario=" + DatosConfiguracion.idUsuario, null); //where idUsuario = var
            JSONObject JSONobj = new JSONObject();
            JSONarr = new JSONArray();
            if (fila.moveToFirst()) {
                do {
                    if (!Boolean.parseBoolean(fila.getString(11))) {
                        // if (!filtro.contains(fila.getInt(3))) {
                        JSONobj.clear();
                        JSONobj.put("_id", fila.getString(0));
                        JSONobj.put("idUsuario", fila.getString(1));
                        JSONobj.put("orden_servicio", fila.getString(2));
                        JSONobj.put("id_pegado", fila.getString(3));
                        JSONobj.put("fecha_pegado", fila.getString(4));
                        JSONobj.put("sku", fila.getString(5));
                        JSONobj.put("descripcion", fila.getString(6));
                        JSONobj.put("cantidad", fila.getString(7));
                        JSONobj.put("unidad_medida", fila.getString(8));
                        JSONobj.put("ubicacion", fila.getString(9));
                        JSONobj.put("estatus", fila.getString(10));
                        JSONobj.put("confirmacion", fila.getString(11));
                        JSONobj.put("cantidad_recolectada", fila.getString(12));
                        JSONobj.put("faltante", fila.getString(13));
                        JSONarr.add(JSONobj.clone());
                        //}
                    } else {
                        System.out.println("DISCRIMINA" + fila.getPosition());
                    }
                } while (fila.moveToNext());
            }
            System.out.println("****ARRAY DE BASE-->>" + JSONarr.toString());
            bd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONarr.toString();
    }


    public boolean updateLineaRefaccion(String parametro, int id, int tipu) {
        SQLiteDatabase updateL = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int i = 0;
        if (tipu == 1) {
            System.out.println("Update de verificacion");
            contentValues.put("cantidad", parametro);
            contentValues.put("estatus", "1");
            i = updateL.update("CONTEOS_CICLICO", contentValues, "_id=" + id + " and idUsuario=" + DatosConfiguracion.idUsuario, null);
        } else if (tipu == 2) {
            System.out.println("Update de verificacion");
            contentValues.put("cantidad", parametro);
            contentValues.put("estatus", "0");
            i = updateL.update("CONTEOS_CICLICO", contentValues, "_id=" + id, null);
        } else if (tipu == 3) {
            System.out.println("Update de envio parsial");
            contentValues.put("confirmacion", parametro);
            i = updateL.update("CONTEOS_CICLICO", contentValues, "_id=" + id + " and idUsuario=" + DatosConfiguracion.idUsuario, null);
        }
        updateL.close();
        System.out.println("QUERY UPDATE:" + (i > 0));
        return i > 0;

    }


    public boolean updateLineaRecoleccion(String parametro, int id, int tipu) {
        SQLiteDatabase updateL = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int i = 0;
        if (tipu == 1) {
            System.out.println("Update de " + id + " verificacion " + parametro);
            contentValues.put("cantidad_recolectada", parametro);
            contentValues.put("estatus", "1");
            i = updateL.update("RECOLECCION", contentValues, "_id=" + id + " and idUsuario=" + DatosConfiguracion.idUsuario, null);
        } else if (tipu == 2) {
            System.out.println("Update de verificacion");
            contentValues.put("cantidad_recolectada", parametro);
            contentValues.put("estatus", "0");
            i = updateL.update("RECOLECCION", contentValues, "_id=" + id, null);
        } else if (tipu == 3) {
            System.out.println("Update de envio parsial");
            contentValues.put("confirmacion", parametro);
            i = updateL.update("RECOLECCION", contentValues, "_id=" + id + " and idUsuario=" + DatosConfiguracion.idUsuario, null);
        }
        updateL.close();
        System.out.println("QUERY UPDATE:" + (i > 0));
        return i > 0;

    }

    public boolean updateLineaRecoleccionNuevo(String parametro, int id, int tipu) {
        SQLiteDatabase updateL = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int i = 0;
        if (tipu == 1) {
            System.out.println("Update de " + id + " verificacion " + parametro);
            contentValues.put("cantidad_recolectada", parametro);
            contentValues.put("estatus", "1");
            i = updateL.update("RECOLECCIONNUEVO", contentValues, "_id=" + id + " and idUsuario=" + DatosConfiguracion.idUsuario, null);
        } else if (tipu == 2) {
            System.out.println("Update de verificacion");
            contentValues.put("cantidad_recolectada", parametro);
            contentValues.put("estatus", "0");
            i = updateL.update("RECOLECCIONNUEVO", contentValues, "_id=" + id, null);
        } else if (tipu == 3) {
            System.out.println("Update de envio parsial");
            contentValues.put("confirmacion", parametro);
            i = updateL.update("RECOLECCIONNUEVO", contentValues, "_id=" + id + " and idUsuario=" + DatosConfiguracion.idUsuario, null);
        }
        updateL.close();
        System.out.println("QUERY UPDATE:" + (i > 0));
        return i > 0;

    }

    public boolean updateLineaRecoleccion(String parametro, String faltante, int id, int tipu) {
        SQLiteDatabase updateL = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int i = 0;
        if (tipu == 1) {
            System.out.println("Update de" + id + " verificacion" + parametro);
            contentValues.put("cantidad_recolectada", parametro);
            contentValues.put("estatus", "1");
            contentValues.put("faltante", faltante);
            i = updateL.update("RECOLECCION", contentValues, "_id=" + id + " and idUsuario=" + DatosConfiguracion.idUsuario, null);
        } else if (tipu == 2) {
            System.out.println("Update de verificacion");
            contentValues.put("cantidad_recolectada", parametro);
            contentValues.put("estatus", "0");
            i = updateL.update("RECOLECCION", contentValues, "_id=" + id, null);
        } else if (tipu == 3) {
            System.out.println("Update de envio parsial");
            contentValues.put("confirmacion", parametro);
            i = updateL.update("RECOLECCION", contentValues, "_id=" + id + " and idUsuario=" + DatosConfiguracion.idUsuario, null);
        }
        updateL.close();
        System.out.println("QUERY UPDATE:" + (i > 0));
        return i > 0;

    }

    public boolean updateLineaRecoleccionNuevo(String parametro, String faltante, int id, int tipu) {
        SQLiteDatabase updateL = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int i = 0;
        if (tipu == 1) {
            System.out.println("Update de" + id + " verificacion" + parametro);
            contentValues.put("cantidad_recolectada", parametro);
            contentValues.put("estatus", "1");
            contentValues.put("faltante", faltante);
            i = updateL.update("RECOLECCIONNUEVO", contentValues, "_id=" + id + " and idUsuario=" + DatosConfiguracion.idUsuario, null);
        } else if (tipu == 2) {
            System.out.println("Update de verificacion");
            contentValues.put("cantidad_recolectada", parametro);
            contentValues.put("estatus", "0");
            i = updateL.update("RECOLECCIONNUEVO", contentValues, "_id=" + id, null);
        } else if (tipu == 3) {
            System.out.println("Update de envio parsial");
            contentValues.put("confirmacion", parametro);
            i = updateL.update("RECOLECCIONNUEVO", contentValues, "_id=" + id + " and idUsuario=" + DatosConfiguracion.idUsuario, null);
        }
        updateL.close();
        System.out.println("QUERY UPDATE:" + (i > 0));
        return i > 0;

    }
    public void consultarOrden() {

    }

    public int recoleccionActivado() {
        SQLiteDatabase count = this.getWritableDatabase();
        Cursor fila = count.rawQuery("SELECT cantidad_recolectada from RECOLECCIONNUEVO where cantidad_recolectada NOT LIKE '0.0' and  idUsuario=" + DatosConfiguracion.idUsuario, null);
        int conteoActivo = fila.getCount();
        return conteoActivo;
    }
 public int recoleccionActivadof() {
        SQLiteDatabase count = this.getWritableDatabase();
        Cursor fila = count.rawQuery("SELECT cantidad_recolectada from RECOLECCION where cantidad_recolectada NOT LIKE '0.0' and  idUsuario=" + DatosConfiguracion.idUsuario, null);
        int conteoActivo = fila.getCount();
        return conteoActivo;
    }

    public void borrarConteo(String table) {
        SQLiteDatabase delete = this.getWritableDatabase();
        delete.delete(table, null, null);
        delete.close();
    }
}

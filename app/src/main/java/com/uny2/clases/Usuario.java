package com.uny2.clases;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Juan on 06/04/2016.
 */
public class Usuario {

    private int id;
    private String nombre;
    private String perfil;
    private String fechaCreacion;
    private String fechaModificacion;
    private String modificador;
    private String creadoPor;
    private String nombreUsuario;
    private int numeroEmpleado;
    private String estatus;
    private ArrayList<Permisos> permisos;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String perfil, String estatus,int numeroEmpleado,
                   String fechaCreacion, String nombreUsuario,ArrayList<Permisos> permisos) {
        this.id = id;
        this.nombre = nombre;
        this.perfil = perfil;
        this.estatus = estatus;
        this.fechaCreacion = fechaCreacion;
        this.numeroEmpleado = numeroEmpleado;
        this.nombreUsuario = nombreUsuario;
        this.permisos = permisos;
    }

    public Usuario(int id, String nombre, String perfil, String fechaCreacion, String fechaModificacion, String modificador, String creadoPor, String nombreUsuario, int numeroEmpleado, ArrayList<Permisos> permisos) {
        this.id = id;
        this.nombre = nombre;
        this.perfil = perfil;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.modificador = modificador;
        this.creadoPor = creadoPor;
        this.nombreUsuario = nombreUsuario;
        this.numeroEmpleado = numeroEmpleado;
        this.permisos = permisos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getModificador() {
        return modificador;
    }

    public void setModificador(String modificador) {
        this.modificador = modificador;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(int numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public ArrayList<Permisos> getPermisos() {
        return permisos;
    }

    public void setPermisos(ArrayList<Permisos> permisos) {
        this.permisos = permisos;
    }


    public static ArrayList<Usuario> toArrayListWS(String json,int tipo){
        ArrayList<Usuario> arrayConteoCiclico = new ArrayList<>();
        try {
            org.json.JSONArray respuestaServicio = new org.json.JSONArray(json);
            for (int i = 0; i < respuestaServicio.length() ; i++) {

                org.json.JSONObject detallesRespuesta = new org.json.JSONObject(respuestaServicio.get(i).toString());
                if(tipo==1){
                    //Respuesta del Servicio inicial

                    //arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id_linea"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), detallesRespuesta.getString("estatus"), ubicacion.toString(),0.0,false));
                }else if(tipo==2){
                    //Base de datos
                    arrayConteoCiclico.add(new Usuario(i,detallesRespuesta.getString("nombre"),detallesRespuesta.getString("perfil"),detallesRespuesta.getString("modificador"),
                            detallesRespuesta.getInt("numero_empleado"),detallesRespuesta.getString("fecha_creacion"),detallesRespuesta.getString("username"),obtenerPermisosUsuario(detallesRespuesta.getString("permisos")))); // modificador * estatus

                }else if(tipo==3){
                    //Base de datos
                    //arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), detallesRespuesta.getString("estatus"),detallesRespuesta.getString("ubicacion"),detallesRespuesta.getDouble("cantidad_contada"),detallesRespuesta.getBoolean("confirmacion")));
                }else if(tipo==4){
                    //Respuesta de confirmacion de contado servicio
                   // arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id_linea"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"),"0",detallesRespuesta.getString("ubicacion"),Double.parseDouble(detallesRespuesta.getString("cantidad_contada")),Boolean.parseBoolean(detallesRespuesta.getString("confirmacion"))));
                    //System.out.println("Hola amigos");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayConteoCiclico;
    }


    public static ArrayList<Permisos> obtenerPermisosUsuario(String permiso) throws Exception{

        JSONArray permisosArray = new JSONArray(permiso);
        ArrayList<Permisos> permisosList = new ArrayList<>();
        for (int i = 0; i < permisosArray.length(); i++) {
            JSONObject elemento =new JSONObject(permisosArray.get(i).toString());
            permisosList.add(new Permisos(elemento.getInt("id_modulo"),elemento.getString("modulo"),
                    elemento.getString("fecha_registro"), elemento.getString("fecha_mod"),elemento.getString("modificador")));
        }
        return permisosList;
    }
}

class Permisos{
    private int id;
    private String modulo;
    private String fechaRegistro;
    private String fechaModificacion;
    private String modificadoPor;

    public Permisos() {
    }

    public Permisos(int id, String modulo, String fechaRegistro, String fechaModificacion, String modificadoPor) {
        this.id = id;
        this.modulo = modulo;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.modificadoPor = modificadoPor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }
}
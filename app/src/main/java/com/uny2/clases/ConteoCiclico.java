package com.uny2.clases;

import java.util.ArrayList;

/**
 * Created by La che Wendy on 23/02/2016.
 */
public class ConteoCiclico {
    //Variables de la clase
    private int idRow;
    private String descripcion;
    private String sku;
    private String estatus;
    private String ubicacion;
    private  double cantidad;
    private boolean confirmacion;
     //Get and Set de la Clase
    public int getIdRow() {
        return idRow;
    }
    public void setIdRow(int idRow) {
        this.idRow = idRow;
    }
    public boolean isConfirmacion() {
        return confirmacion;
    }
    public void setConfirmacion(boolean confirmacion) {
        this.confirmacion = confirmacion;
    }
    public double getCantidad() {
        return cantidad;
    }
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getEstatus() {
        return estatus;
    }
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    //Constructor vacio de la clase
    public ConteoCiclico() {
    }
    //Constructor de la clase
    public ConteoCiclico(int idRow, String descripcion, String sku, String estatus, String ubicacion, double cantidad, boolean confirmacion) {
        this.idRow = idRow;
        this.descripcion = descripcion;
        this.sku = sku;
        this.estatus = estatus;
        this.ubicacion = ubicacion;
        this.cantidad = cantidad;
        this.confirmacion = confirmacion;
    }

    public static ArrayList<ConteoCiclico> toArrayListWS(String json,int tipo){
        ArrayList<ConteoCiclico> arrayConteoCiclico = new ArrayList<>();
        try {
            org.json.JSONArray respuestaServicio = new org.json.JSONArray(json);
            for (int i = 0; i < respuestaServicio.length() ; i++) {

                org.json.JSONObject detallesRespuesta = new org.json.JSONObject(respuestaServicio.get(i).toString());
                if(tipo==1){
                //Respuesta del Servicio inicial
                StringBuilder  ubicacion= new StringBuilder();
                    System.out.println("Ubicacion->"+detallesRespuesta.getString("ubicacion"));
                /*if(detallesRespuesta.getString("ubicacion").length()>0) {
                    ubicacion.append(detallesRespuesta.getString("ubicacion").substring(0, 3));
                    ubicacion.append("-");
                    ubicacion.append(detallesRespuesta.getString("ubicacion").substring(3,4));
                    ubicacion.append("-");
                    ubicacion.append(detallesRespuesta.getString("ubicacion").substring(4, 6));
                    ubicacion.append("-");
                    ubicacion.append(detallesRespuesta.getString("ubicacion").substring(6, 8));
                }else{
                    ubicacion.append("No encontrada");
                }*/
                    arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id_linea"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), detallesRespuesta.getString("estatus"),detallesRespuesta.getString("ubicacion"),0.0,false));// ubicacion.toString(),0.0,false));
                }else if(tipo==2){
                    //Base de datos
                    arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), detallesRespuesta.getString("estatus"),detallesRespuesta.getString("ubicacion"),0.0,false));
                 }else if(tipo==3){
                    //Base de datos
                    arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), detallesRespuesta.getString("estatus"),detallesRespuesta.getString("ubicacion"),detallesRespuesta.getDouble("cantidad_contada"),detallesRespuesta.getBoolean("confirmacion")));
                }else if(tipo==4){
                    //Respuesta de confirmacion de contado servicio
                    arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id_linea"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"),"0",detallesRespuesta.getString("ubicacion"),Double.parseDouble(detallesRespuesta.getString("cantidad_contada")),Boolean.parseBoolean(detallesRespuesta.getString("confirmacion"))));
                    //System.out.println("Hola amigos");
                }
                else if(tipo==5){
                    arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"),detallesRespuesta.getString("estatus"),detallesRespuesta.getString("ubicacion"),Double.parseDouble(detallesRespuesta.getString("cantidad_contada")),Boolean.parseBoolean(detallesRespuesta.getString("confirmacion"))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayConteoCiclico;
    }




}

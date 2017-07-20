package com.uny2.clases;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by DanyCarreto on 13/04/16.
 */
public class Ubicacion {

    private int idRow;
    private String skuProveedor;
    private String skuADO;
    private String descripcion;
    private double unidades;
    private double unidadesContadas;
    private String unidadMedida;
    private String ubicacion;
    private boolean confirmacion;
    private int ubicada;
    private String estatus;

    public Ubicacion() {
    }


    public Ubicacion(int idRow, String descripcion, String skuProveedor, String ubicacion, double unidades, boolean confirmacion) {
        this.idRow = idRow;
        this.descripcion = descripcion;
        this.skuProveedor = skuProveedor;
        this.ubicacion = ubicacion;
        this.unidades = unidades;
        this.confirmacion = confirmacion;
    }

    public Ubicacion(int idRow, String skuProveedor, String skuADO, String descripcion, double unidades, String unidadMedida, String ubicacion, boolean confirmacion, int ubicada) {
        this.idRow = idRow;
        this.skuProveedor = skuProveedor;
        this.skuADO = skuADO;
        this.descripcion = descripcion;
        this.unidades = unidades;
        this.unidadMedida = unidadMedida;
        this.ubicacion = ubicacion;
        this.confirmacion = confirmacion;
        this.ubicada = ubicada;
    }

    public Ubicacion(String skuProveedor, String skuADO, String descripcion, double unidades, String unidadMedida, String ubicacion, boolean confirmacion, int ubicada) {
        this.skuProveedor = skuProveedor;
        this.skuADO = skuADO;
        this.descripcion = descripcion;
        this.unidades = unidades;
        this.unidadMedida = unidadMedida;
        this.ubicacion = ubicacion;
        this.confirmacion = confirmacion;
        this.ubicada = ubicada;
    }

    public Ubicacion(String skuProveedor, String skuADO, String descripcion, double unidades, String unidadMedida, boolean confirmacion, int ubicada) {
        this.skuProveedor = skuProveedor;
        this.skuADO = skuADO;
        this.descripcion = descripcion;
        this.unidades = unidades;
        this.unidadMedida = unidadMedida;
        this.confirmacion = confirmacion;
        this.ubicada = ubicada;
    }

    public Ubicacion(String skuProveedor, String skuADO, String descripcion, double unidades, String unidadMedida,String ubicacion,String estatus) {
        this.skuProveedor = skuProveedor;
        this.skuADO = skuADO;
        this.descripcion = descripcion;
        this.unidades = unidades;
        this.unidadMedida = unidadMedida;
        this.ubicacion = ubicacion;
        this.estatus = estatus;
     }
 public Ubicacion(String skuProveedor, String skuADO, String descripcion, double unidades, String unidadMedida,String ubicacion,Double unidadesContadas,String estatus) {
        this.skuProveedor = skuProveedor;
        this.skuADO = skuADO;
        this.descripcion = descripcion;
        this.unidades = unidades;
        this.unidadMedida = unidadMedida;
        this.ubicacion = ubicacion;
        this.unidadesContadas = unidadesContadas;
        this.estatus = estatus;
     }






    public static ArrayList<Ubicacion> toArrayListWS(String json,int tipo)throws JSONException{
        ArrayList<Ubicacion> arrayUbicacion = new ArrayList<>();
        try {
            org.json.JSONArray respuestaServicio = new org.json.JSONArray(json);
            for (int i = 1; i < respuestaServicio.length() ; i++) {

                org.json.JSONObject elementosUbicacion = new org.json.JSONObject(respuestaServicio.get(i).toString());
                if(tipo==1){
                    //Respuesta del Servicio inicial
                   /* StringBuilder  ubicacion= new StringBuilder();
                    if(detallesRespuesta.getString("ubicacion").length()>0) {
                        ubicacion.append(detallesRespuesta.getString("ubicacion").substring(0, 3));
                        ubicacion.append("-");
                        ubicacion.append(detallesRespuesta.getString("ubicacion").substring(3,4));
                        ubicacion.append("-");
                        ubicacion.append(detallesRespuesta.getString("ubicacion").substring(4, 6));
                        ubicacion.append("-");
                        ubicacion.append(detallesRespuesta.getString("ubicacion").substring(6, 8));
                    }else{
                        ubicacion.append("No encontrada");
                    }
                    arrayEntrada.add(new Entrada(detallesRespuesta.getInt("id_cb"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku_proveedor"), detallesRespuesta.getString("estatus"), ubicacion.toString(),0.0,false));
                */
                }else if(tipo==2){
                    //Base de datos
                    System.out.println("ReciboUbicacion->"+elementosUbicacion.getString("sku_proveedor")+elementosUbicacion.getString("sku_ADO")+elementosUbicacion.getString("descripcion")+elementosUbicacion.getDouble("unidades")+elementosUbicacion.getString("um")+elementosUbicacion.getString("ubicacion")+"0");
                    arrayUbicacion.add(new Ubicacion(elementosUbicacion.getString("sku_proveedor"),elementosUbicacion.getString("sku_ADO"),elementosUbicacion.getString("descripcion"),elementosUbicacion.getDouble("unidades"),elementosUbicacion.getString("um"),elementosUbicacion.getString("ubicacion"),"0"));
                }else if(tipo==3){
                    //Base de datos
                }else if(tipo==4){
                    //Respuesta de confirmacion de contado servicio
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayUbicacion;
    }


    public int getIdRow() {
        return idRow;
    }

    public void setIdRow(int idRow) {
        this.idRow = idRow;
    }

    public String getSkuProveedor() {
        return skuProveedor;
    }

    public void setSkuProveedor(String skuProveedor) {
        this.skuProveedor = skuProveedor;
    }

    public String getSkuADO() {
        return skuADO;
    }

    public void setSkuADO(String skuADO) {
        this.skuADO = skuADO;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getUnidades() {
        return unidades;
    }

    public void setUnidades(double unidades) {
        this.unidades = unidades;
    }


    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(boolean confirmacion) {
        this.confirmacion = confirmacion;
    }

    public int getUbicada() {
        return ubicada;
    }

    public void setUbicada(int ubicada) {
        this.ubicada = ubicada;
    }


    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public double getUnidadesContadas() {
        return unidadesContadas;
    }

    public void setUnidadesContadas(double unidadesContadas) {
        this.unidadesContadas = unidadesContadas;
    }
}

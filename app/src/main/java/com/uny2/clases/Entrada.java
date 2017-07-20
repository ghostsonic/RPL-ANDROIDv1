package com.uny2.clases;

import java.util.ArrayList;

/**
 * Created by Juan on 04/04/2016.
 */
public class Entrada {

    private int idRow;
    private String descripcion;
    private String skuProveedor;
    private String skuADO;
    private String estatus;
    private String unidadMedida;
    private  double unidades;
    private  double unidadesContadas;
    private boolean confirmacion;
    private String noLinea;


    public Entrada() {
    }

    public Entrada(int idRow, String descripcion, String skuProveedor, String estatus, String unidadMedida, double unidades, boolean confirmacion) {
        this.idRow = idRow;
        this.descripcion = descripcion;
        this.skuProveedor = skuProveedor;
        this.estatus = estatus;
        this.unidadMedida = unidadMedida;
        this.unidades = unidades;
        this.confirmacion = confirmacion;
    }

    public Entrada( String descripcion, String skuProveedor,String skuADO, String estatus, String unidadMedida, double unidades, boolean confirmacion) {
        this.descripcion = descripcion;
        this.skuProveedor = skuProveedor;
        this.skuADO = skuADO;
        this.estatus = estatus;
        this.unidadMedida = unidadMedida;
        this.unidades = unidades;
        this.confirmacion = confirmacion;
    }
    public Entrada( String descripcion, String skuProveedor,String skuADO, String estatus, String unidadMedida, double unidades,double unidadesContadas, boolean confirmacion) {
        this.descripcion = descripcion;
        this.skuProveedor = skuProveedor;
        this.skuADO = skuADO;
        this.estatus = estatus;
        this.unidadMedida = unidadMedida;
        this.unidades = unidades;
        this.unidadesContadas = unidadesContadas;
        this.confirmacion = confirmacion;
    }

    public Entrada( String descripcion, String skuProveedor,String skuADO, String estatus, String unidadMedida, double unidades,double unidadesContadas, boolean confirmacion, String noLinea) {
        this.descripcion = descripcion;
        this.skuProveedor = skuProveedor;
        this.skuADO = skuADO;
        this.estatus = estatus;
        this.unidadMedida = unidadMedida;
        this.unidades = unidades;
        this.unidadesContadas = unidadesContadas;
        this.confirmacion = confirmacion;
        this.noLinea = noLinea;
    }
    public int getIdRow() {
        return idRow;
    }

    public void setIdRow(int idRow) {
        this.idRow = idRow;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSkuProveedor() {
        return skuProveedor;
    }

    public void setSkuProveedor(String skuProveedor) {
        this.skuProveedor = skuProveedor;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public double getUnidades() {
        return unidades;
    }

    public void setUnidades(double unidades) {
        this.unidades = unidades;
    }

    public boolean isConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(boolean confirmacion) {
        this.confirmacion = confirmacion;
    }

    public String getNoLinea() {
        return noLinea;
    }

    public void setNoLinea(String noLinea) {
        this.noLinea = noLinea;
    }
//REvisar

    public static ArrayList<Entrada> toArrayListWS(String json,int tipo)throws Exception{
        ArrayList<Entrada> arrayEntrada = new ArrayList<>();

            org.json.JSONArray respuestaServicio = new org.json.JSONArray(json);
            for (int i = 1; i < respuestaServicio.length() ; i++) {

                org.json.JSONObject detallesRespuesta = new org.json.JSONObject(respuestaServicio.get(i).toString());
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
                    String aux = "";
                    if(detallesRespuesta.getString("sku_proveedor").contains("encontro")){
                        arrayEntrada.add(new Entrada(detallesRespuesta.getString("descripcion"), "SN/CP",detallesRespuesta.getString("sku_ADO"), detallesRespuesta.getString("estatus"),detallesRespuesta.getString("um"),detallesRespuesta.getDouble("unidades"),0.0,false, detallesRespuesta.getString("noLinea")));
                    }else{
                        arrayEntrada.add(new Entrada(detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku_proveedor"),detallesRespuesta.getString("sku_ADO"), detallesRespuesta.getString("estatus"),detallesRespuesta.getString("um"),detallesRespuesta.getDouble("unidades"),0.0,false, detallesRespuesta.getString("noLinea")));
                    }
                    //arrayEntrada.add(new Entrada(detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku_proveedor"),detallesRespuesta.getString("sku_ADO"), detallesRespuesta.getString("estatus"),detallesRespuesta.getString("um"),detallesRespuesta.getDouble("unidades"),0.0,false));
                }else if(tipo==3){
                    //Base de datos
                    arrayEntrada.add(new Entrada(detallesRespuesta.getInt("id_cb"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku_proveedor"), detallesRespuesta.getString("estatus"),detallesRespuesta.getString("um"),detallesRespuesta.getDouble("unidades"),detallesRespuesta.getBoolean("confirmacion")));
                }else if(tipo==4){
                    //Respuesta de confirmacion de contado servicio
                    arrayEntrada.add(new Entrada(detallesRespuesta.getInt("id_cb"),detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku_proveedor"),"0",detallesRespuesta.getString("um"),Double.parseDouble(detallesRespuesta.getString("unidades")),Boolean.parseBoolean(detallesRespuesta.getString("confirmacion"))));
                    //System.out.println("Hola amigos");
                }
            }

        return arrayEntrada;
    }

    public String getSkuADO() {
        return skuADO;
    }

    public void setSkuADO(String skuADO) {
        this.skuADO = skuADO;
    }

    public double getUnidadesContadas() {
        return unidadesContadas;
    }

    public void setUnidadesContadas(double unidadesContadas) {
        this.unidadesContadas = unidadesContadas;
    }
}

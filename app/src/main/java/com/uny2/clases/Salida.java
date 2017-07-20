package com.uny2.clases;

import java.util.ArrayList;

/**
 * Created by DanyCarreto on 19/10/2016.
 */
public class Salida {

    int idRow;
    String ordenServicio;
    int idPegado;
    String fechaPegado;
    String sku;
    String descripcion;
    double cantidad;
    double cantidadRecolectada;
    String unidadMedida;
    String ubicacion;
    int estatus;
    double faltante = 0;
    boolean confirmacion;
    double recolectado;



    double cantidadaApoyo;
    double autorizado;
    public Salida() {
    }

    public  Salida(int idRow, String ordenServicio, double cantidad, double cantidadaApoyo, double autorizado){
        this.idRow = idRow;
        this.ordenServicio = ordenServicio;
        this.cantidad = cantidad;
        this.cantidadaApoyo = cantidadaApoyo;
        this.autorizado = autorizado;

    }
    public Salida(String ordenServicio, int idPegado, String fechaPegado) {
        this.ordenServicio = ordenServicio;
        this.idPegado = idPegado;
        this.fechaPegado = fechaPegado;
    }


    public Salida(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, String unidadMedida, String ubicacion, int estatus, boolean confirmacion) {
        this.idRow = idRow;
        this.ordenServicio = ordenServicio;
        this.idPegado = idPegado;
        this.fechaPegado = fechaPegado;
        this.sku = sku;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.ubicacion = ubicacion;
        this.estatus = estatus;
        this.confirmacion = confirmacion;
    }
    public Salida(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad,double cantidadRecolectada, String unidadMedida, String ubicacion, int estatus, boolean confirmacion) {
        this.idRow = idRow;
        this.ordenServicio = ordenServicio;
        this.idPegado = idPegado;
        this.fechaPegado = fechaPegado;
        this.sku = sku;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.cantidadRecolectada = cantidadRecolectada;
        this.unidadMedida = unidadMedida;
        this.ubicacion = ubicacion;
        this.estatus = estatus;
        this.confirmacion = confirmacion;
    }

    public Salida(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad,double cantidadRecolectada, String unidadMedida, String ubicacion, int estatus,Double faltante, boolean confirmacion) {
        this.idRow = idRow;
        this.ordenServicio = ordenServicio;
        this.idPegado = idPegado;
        this.fechaPegado = fechaPegado;
        this.sku = sku;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.cantidadRecolectada = cantidadRecolectada;
        this.unidadMedida = unidadMedida;
        this.ubicacion = ubicacion;
        this.estatus = estatus;
        this.faltante = faltante;
        this.confirmacion = confirmacion;
    }

    public int getIdRow() {
        return idRow;
    }

    public void setIdRow(int idRow) {
        this.idRow = idRow;
    }

    public String getOrdenServicio() {
        return ordenServicio;
    }

    public void setOrdenServicio(String ordenServicio) {
        this.ordenServicio = ordenServicio;
    }

    public int getIdPegado() {
        return idPegado;
    }

    public void setIdPegado(int idPegado) {
        this.idPegado = idPegado;
    }

    public String getFechaPegado() {
        return fechaPegado;
    }

    public void setFechaPegado(String fechaPegado) {
        this.fechaPegado = fechaPegado;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getCantidadRecolectada() {
        return cantidadRecolectada;
    }

    public void setCantidadRecolectada(double cantidadRecolectada) {
        this.cantidadRecolectada = cantidadRecolectada;
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

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public double getFaltante() {
        return faltante;
    }

    public void setFaltante(double faltante) {
        this.faltante = faltante;
    }

    public boolean isConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(boolean confirmacion) {
        this.confirmacion = confirmacion;
    }

    public double getRecolectado() {
        return recolectado;
    }

    public void setRecolectado(double recolectado) {
        this.recolectado = recolectado;
    }

    public double getCantidadaApoyo() {
        return cantidadaApoyo;
    }

    public void setCantidadaApoyo(double cantidadaApoyo) {
        this.cantidadaApoyo = cantidadaApoyo;
    }

    public double getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(double autorizado) {
        this.autorizado = autorizado;
    }

    public static ArrayList<Salida> toArrayListWS(String json, int tipo) throws Exception{
        ArrayList<Salida> arrayConteoCiclico = new ArrayList<>();
        try {
            org.json.JSONArray respuestaServicio = new org.json.JSONArray(json);
            for (int i = 0; i < respuestaServicio.length(); i++) {

                org.json.JSONObject detallesRespuesta = new org.json.JSONObject(respuestaServicio.get(i).toString());
                if (tipo == 1) { //recibir
                    arrayConteoCiclico.add(new Salida(detallesRespuesta.getInt("id_linea"), detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("idp"), detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getDouble("cantidad"), detallesRespuesta.getString("um"), detallesRespuesta.getString("ubicacion"),detallesRespuesta.getInt("estatus"),   false));
                }else if (tipo == 2) { //insercion
                    //Base de datos
                    arrayConteoCiclico.add(new Salida(detallesRespuesta.getInt("_id"), detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("id_pegado"),
                            detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getDouble("cantidad"),detallesRespuesta.getDouble("cantidad_recolectada"),
                            detallesRespuesta.getString("unidad_medida"), detallesRespuesta.getString("ubicacion"),detallesRespuesta.getInt("estatus"), detallesRespuesta.getDouble("faltante"),  detallesRespuesta.getBoolean("confirmacion")));
                } else if (tipo == 3) { //lectura
                    //Base de datos
                    arrayConteoCiclico.add(new Salida(detallesRespuesta.getInt("_id"), detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("id_pegado"),
                            detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getDouble("cantidad"),
                            detallesRespuesta.getString("unidad_medida"), detallesRespuesta.getString("ubicacion"),detallesRespuesta.getInt("estatus"),   detallesRespuesta.getBoolean("confirmacion")));

                    // arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), detallesRespuesta.getString("estatus"), detallesRespuesta.getString("ubicacion"), detallesRespuesta.getDouble("cantidad_contada"), detallesRespuesta.getBoolean("confirmacion")));
                } else if (tipo == 4) { //respuesta
                    //Respuesta de confirmacion de contado servicio
                    //arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id_linea"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), "0", detallesRespuesta.getString("ubicacion"), Double.parseDouble(detallesRespuesta.getString("cantidad_contada")), Boolean.parseBoolean(detallesRespuesta.getString("confirmacion"))));
                    //System.out.println("Hola amigos");
                }
                else if (tipo ==5){
                    arrayConteoCiclico.add(new Salida(detallesRespuesta.getInt("_id"),
                            detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("id_pegado"),
                            detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"),Double.parseDouble( detallesRespuesta.getString("cantidad") ) ,
                            detallesRespuesta.getString("unidad_medida"), detallesRespuesta.getString("ubicacion"),detallesRespuesta.getInt("estatus"),   detallesRespuesta.getBoolean("confirmacion")));

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return arrayConteoCiclico;
    }

}

package com.uny2.clases;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by pc on 14/03/2016.
 */
public class Recoleccion {
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
    boolean isGarantia;
    String ordenGarantia;
    JSONArray codigoNuevoGarantia;

    double recolectado;

    //Estos e agrea
    String ubicacionP;
    double cantidadP;
    double cantidadC;
    String ubicacionC;
    ///

    public Recoleccion() {
    }

    public Recoleccion(String ordenServicio, int idPegado, String fechaPegado) {
        this.ordenServicio = ordenServicio;
        this.idPegado = idPegado;
        this.fechaPegado = fechaPegado;
    }


    public Recoleccion(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, String unidadMedida, String ubicacion, int estatus, boolean confirmacion) {
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

    public Recoleccion(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, double cantidadRecolectada, String unidadMedida, String ubicacion, int estatus, boolean confirmacion) {
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

    public Recoleccion(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, double cantidadRecolectada, String unidadMedida, String ubicacion, int estatus, boolean confirmacion, boolean isGarantia) {
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
        this.isGarantia = isGarantia;
    }

    public Recoleccion(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, double cantidadRecolectada, String unidadMedida, String ubicacion, int estatus, boolean confirmacion, boolean isGarantia, String ubicacionP, double cantidadP, String ubicacionC, double cantidadC ) {
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
        this.isGarantia = isGarantia;
        this.ubicacionC = ubicacionC;
        this.ubicacionP = ubicacionP;
        this.cantidadC = cantidadC;
        this.cantidadP = cantidadP;
    }

    public Recoleccion(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, double cantidadRecolectada, String unidadMedida, int estatus, boolean confirmacion, boolean isGarantia, String ubicacionP, double cantidadP, String ubicacionC, double cantidadC ) {
        this.idRow = idRow;
        this.ordenServicio = ordenServicio;
        this.idPegado = idPegado;
        this.fechaPegado = fechaPegado;
        this.sku = sku;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.cantidadRecolectada = cantidadRecolectada;
        this.unidadMedida = unidadMedida;
        this.estatus = estatus;
        this.confirmacion = confirmacion;
        this.isGarantia = isGarantia;
        this.ubicacionC = ubicacionC;
        this.ubicacionP = ubicacionP;
        this.cantidadC = cantidadC;
        this.cantidadP = cantidadP;
    }
    public Recoleccion(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, double cantidadRecolectada, String unidadMedida, String ubicacion, int estatus, Double faltante, boolean confirmacion) {
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
    public Recoleccion(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, double cantidadRecolectada, String unidadMedida, int estatus, Double faltante, boolean confirmacion,boolean isGarantia, String ubicacionP, double cantidadP, String ubicacionC, double cantidadC) {
        this.idRow = idRow;
        this.ordenServicio = ordenServicio;
        this.idPegado = idPegado;
        this.fechaPegado = fechaPegado;
        this.sku = sku;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.cantidadRecolectada = cantidadRecolectada;
        this.unidadMedida = unidadMedida;

        this.estatus = estatus;
        this.faltante = faltante;
        this.confirmacion = confirmacion;
        this.isGarantia = isGarantia;
        this.ubicacionC = ubicacionC;
        this.ubicacionP = ubicacionP;
        this.cantidadC = cantidadC;
        this.cantidadP = cantidadP;
    }

    public Recoleccion(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, double cantidadRecolectada, String unidadMedida, String ubicacion, int estatus, Double faltante, boolean confirmacion,boolean isGarantia, String ordenGarantia) {
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
        this.isGarantia = isGarantia;
        this.ordenGarantia = ordenGarantia;
    }

    public Recoleccion(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, String unidadMedida, String ubicacion, int estatus, boolean confirmacion, boolean isGarantia) {
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
        this.isGarantia = isGarantia;
    }

    public Recoleccion(int idRow, String ordenServicio, int idPegado, String fechaPegado, String sku, String descripcion, double cantidad, String unidadMedida,  int estatus, boolean confirmacion, boolean isGarantia, String ubicacionP, double cantidadP, String ubicacionC, double cantidadC) {
        this.idRow = idRow;
        this.ordenServicio = ordenServicio;
        this.idPegado = idPegado;
        this.fechaPegado = fechaPegado;
        this.sku = sku;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;

        this.estatus = estatus;
        this.confirmacion = confirmacion;
        this.isGarantia = isGarantia;
        this.ubicacionC = ubicacionC;
        this.ubicacionP = ubicacionP;
        this.cantidadC = cantidadC;
        this.cantidadP = cantidadP;
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

    public double getCantidadRecolectada() {
        return cantidadRecolectada;
    }

    public void setCantidadRecolectada(double cantidadRecolectada) {
        this.cantidadRecolectada = cantidadRecolectada;
    }

    public boolean isGarantia() {
        return isGarantia;
    }

    public void setIsGarantia(boolean isGarantia) {
        this.isGarantia = isGarantia;
    }

    public double getFaltante() {
        return faltante;
    }

    public void setFaltante(double faltante) {
        this.faltante = faltante;
    }

    public String getOrdenGarantia() {
        return ordenGarantia;
    }

    public void setOrdenGarantia(String ordenGarantia) {
        this.ordenGarantia = ordenGarantia;
    }

    public String getUbicacionC() {
        return ubicacionC;
    }

    public void setUbicacionC(String ubicacionC) {
        this.ubicacionC = ubicacionC;
    }

    public double getCantidadC() {
        return cantidadC;
    }

    public void setCantidadC(double cantidadC) {
        this.cantidadC = cantidadC;
    }

    public double getCantidadP() {
        return cantidadP;
    }

    public void setCantidadP(double cantidadP) {
        this.cantidadP = cantidadP;
    }

    public String getUbicacionP() {
        return ubicacionP;
    }

    public void setUbicacionP(String ubicacionP) {
        this.ubicacionP = ubicacionP;
    }

    public static ArrayList<Recoleccion> toArrayListWS(String json, int tipo) throws Exception {
        ArrayList<Recoleccion> arrayConteoCiclico = new ArrayList<>();
        try {
            org.json.JSONArray respuestaServicio = new org.json.JSONArray(json);
            for (int i = 0; i < respuestaServicio.length(); i++) {

                org.json.JSONObject detallesRespuesta = new org.json.JSONObject(respuestaServicio.get(i).toString());
                if (tipo == 1) { //recibir
                    arrayConteoCiclico.add(new Recoleccion(detallesRespuesta.getInt("id_linea"), detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("idp"), detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getDouble("cantidad"), detallesRespuesta.getString("um"), detallesRespuesta.getString("ubicacion"), detallesRespuesta.getInt("estatus"), false, detallesRespuesta.getBoolean("isGarantia")));
                } else if (tipo == 2) { //insercion
                    //Base de datos
                    arrayConteoCiclico.add(new Recoleccion(detallesRespuesta.getInt("_id"), detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("id_pegado"),
                            detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getDouble("cantidad"), detallesRespuesta.getDouble("cantidad_recolectada"),
                            detallesRespuesta.getString("unidad_medida"), detallesRespuesta.getString("ubicacion"), detallesRespuesta.getInt("estatus"), detallesRespuesta.getDouble("faltante"), detallesRespuesta.getBoolean("confirmacion")));
                } else if (tipo == 3) { //lectura
                    //Base de datos
                    arrayConteoCiclico.add(new Recoleccion(detallesRespuesta.getInt("_id"), detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("id_pegado"),
                            detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getDouble("cantidad"),
                            detallesRespuesta.getString("unidad_medida"), detallesRespuesta.getString("ubicacion"), detallesRespuesta.getInt("estatus"), detallesRespuesta.getBoolean("confirmacion")));

                    // arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), detallesRespuesta.getString("estatus"), detallesRespuesta.getString("ubicacion"), detallesRespuesta.getDouble("cantidad_contada"), detallesRespuesta.getBoolean("confirmacion")));
                } else if (tipo == 4) { //respuesta
                    //Respuesta de confirmacion de contado servicio
                    //arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id_linea"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), "0", detallesRespuesta.getString("ubicacion"), Double.parseDouble(detallesRespuesta.getString("cantidad_contada")), Boolean.parseBoolean(detallesRespuesta.getString("confirmacion"))));
                    //System.out.println("Hola amigos");
                } else if (tipo == 5) {
                    arrayConteoCiclico.add(new Recoleccion(detallesRespuesta.getInt("_id"),
                            detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("id_pegado"),
                            detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), Double.parseDouble(detallesRespuesta.getString("cantidad")),
                            detallesRespuesta.getString("unidad_medida"), detallesRespuesta.getString("ubicacion"), detallesRespuesta.getInt("estatus"), detallesRespuesta.getBoolean("confirmacion")));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayConteoCiclico;
    }

    //Para la nueva recoleccion
    public static ArrayList<Recoleccion> toArrayListRWS(String json, int tipo) throws Exception {
        ArrayList<Recoleccion> arrayConteoCiclico = new ArrayList<>();
        try {
            org.json.JSONArray respuestaServicio = new org.json.JSONArray(json);
            for (int i = 0; i < respuestaServicio.length(); i++) {

                org.json.JSONObject detallesRespuesta = new org.json.JSONObject(respuestaServicio.get(i).toString());
                if (tipo == 1) { //recibir
                    arrayConteoCiclico.add(new Recoleccion(detallesRespuesta.getInt("id_linea"), detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("idp"), detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getDouble("cantidad"), detallesRespuesta.getString("um"), detallesRespuesta.getInt("estatus"), false, detallesRespuesta.getBoolean("isGarantia"),
                             detallesRespuesta.getString("ubicacionP"), detallesRespuesta.getDouble("cantidadP"), detallesRespuesta.getString("ubicacionC"), detallesRespuesta.getDouble("cantidadC")));
                } else if (tipo == 2) { //insercion
                    //Base de datos
                    arrayConteoCiclico.add(new Recoleccion(detallesRespuesta.getInt("_id"), detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("id_pegado"),
                            detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getDouble("cantidad"), detallesRespuesta.getDouble("cantidad_recolectada"),
                            detallesRespuesta.getString("unidad_medida"), detallesRespuesta.getString("ubicacion"), detallesRespuesta.getInt("estatus"), detallesRespuesta.getDouble("faltante"), detallesRespuesta.getBoolean("confirmacion")));
                } else if (tipo == 3) { //lectura
                    //Base de datos
                    arrayConteoCiclico.add(new Recoleccion(detallesRespuesta.getInt("_id"), detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("id_pegado"),
                            detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getDouble("cantidad"),
                            detallesRespuesta.getString("unidad_medida"), detallesRespuesta.getString("ubicacion"), detallesRespuesta.getInt("estatus"), detallesRespuesta.getBoolean("confirmacion")));

                    // arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), detallesRespuesta.getString("estatus"), detallesRespuesta.getString("ubicacion"), detallesRespuesta.getDouble("cantidad_contada"), detallesRespuesta.getBoolean("confirmacion")));
                } else if (tipo == 4) { //respuesta
                    //Respuesta de confirmacion de contado servicio
                    //arrayConteoCiclico.add(new ConteoCiclico(detallesRespuesta.getInt("id_linea"), detallesRespuesta.getString("descripcion"), detallesRespuesta.getString("sku"), "0", detallesRespuesta.getString("ubicacion"), Double.parseDouble(detallesRespuesta.getString("cantidad_contada")), Boolean.parseBoolean(detallesRespuesta.getString("confirmacion"))));
                    //System.out.println("Hola amigos");
                } else if (tipo == 5) {
                    arrayConteoCiclico.add(new Recoleccion(detallesRespuesta.getInt("_id"),
                            detallesRespuesta.getString("orden_servicio"), detallesRespuesta.getInt("id_pegado"),
                            detallesRespuesta.getString("fecha_pegado"),
                            detallesRespuesta.getString("sku"), detallesRespuesta.getString("descripcion"), Double.parseDouble(detallesRespuesta.getString("cantidad")),
                            detallesRespuesta.getString("unidad_medida"),"", detallesRespuesta.getInt("estatus"), detallesRespuesta.getBoolean("confirmacion")));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayConteoCiclico;
    }
}

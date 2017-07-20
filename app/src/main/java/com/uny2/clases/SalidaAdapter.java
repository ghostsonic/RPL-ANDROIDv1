package com.uny2.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lachewendy.rpl_androidv1.R;

import java.util.ArrayList;

/**
 * Created by Juan on 20/04/2016.
 */
public class SalidaAdapter  extends ArrayAdapter<Salida> {

    ArrayList<Salida> arrayAdapter;
    Context context;
    ViewHolder viewHolder = null; // view lookup cache stored in tag
    boolean escaner = false;

    public SalidaAdapter(Context context, ArrayList<Salida> arrayAdapter) {
        super(context, R.layout.salida_list, arrayAdapter);
        this.arrayAdapter = arrayAdapter;
        this.context = context;
    }

    public SalidaAdapter(Context context, ArrayList<Salida> arrayAdapter, boolean escaner) {
        super(context, R.layout.salida_list, arrayAdapter);
        this.arrayAdapter = arrayAdapter;
        this.context = context;
        this.escaner = escaner;
    }

    private static class ViewHolder {
        TextView txtDescripcion;
        TextView txtUbicacion;
        TextView txtNumeroDePiezasSalida;
        TextView txtCantidadRecolectada;
        TextView txtTextoRecolectada;
        TextView txtUbicacionSalida;
        RelativeLayout linearItem;
        CheckBox chkSelect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Salida salida = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.salida_list, parent, false);
            viewHolder.chkSelect = (CheckBox) convertView.findViewById(R.id.chekListSalida);
            viewHolder.txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcionSalida);
            viewHolder.txtUbicacion = (TextView) convertView.findViewById(R.id.txtCodigoyUbicacionSalida);
            viewHolder.txtNumeroDePiezasSalida = (TextView) convertView.findViewById(R.id.txtNumeroDePiezasSalida);
            viewHolder.txtCantidadRecolectada = (TextView) convertView.findViewById(R.id.txtCantidadRecolectadaSalida);
            viewHolder.linearItem = (RelativeLayout) convertView.findViewById(R.id.linearItemSalida);
            viewHolder.txtTextoRecolectada = (TextView) convertView.findViewById(R.id.txtTextoRecolectada);
            viewHolder.txtUbicacionSalida = (TextView) convertView.findViewById(R.id.txtUbicacionSalida);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (salida.getEstatus() == 1) {
            viewHolder.chkSelect.setChecked(true);

            if (salida.getCantidad() > salida.getCantidadRecolectada()) {
                viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.colorSelectIncomplete));
            }
            if (salida.getCantidad() == salida.getCantidadRecolectada()) {
                viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.colorSelectComplete));
            }
           /* if(salida.getCantidad()<salida.getCantidadRecolectada()){
                viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.colorSelectSemicomplete));
            }*/

        } else {
            viewHolder.chkSelect.setChecked(false);
            viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.transparente));
        }

        if (salida.getEstatus() == 2) {
            viewHolder.chkSelect.setChecked(true);
            viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.seleccion_transparente));
//            viewHolder.chkSelect.setBackgroundColor(context.getColor(R.color.colorAccent));
        }


        if (escaner) {
            viewHolder.txtDescripcion.setText(salida.getDescripcion());
            viewHolder.txtNumeroDePiezasSalida.setText(String.format("%.1f", salida.getCantidad()) + "");
            viewHolder.txtTextoRecolectada.setVisibility(View.GONE);
            viewHolder.txtCantidadRecolectada.setVisibility(View.GONE);
            viewHolder.txtUbicacionSalida.setText("" + salida.getUbicacion());
            //viewHolder.txtNumeroDePiezasRecoleccion.setVisibility(View.GONE);
            viewHolder.txtUbicacion.setText(salida.getSku());
        } else {
            viewHolder.txtDescripcion.setText(salida.getDescripcion());
            viewHolder.txtCantidadRecolectada.setText(String.format("%.1f",salida.getCantidadRecolectada()));
            viewHolder.txtNumeroDePiezasSalida.setText(String.format("%.1f", salida.getCantidad()) + "");
            viewHolder.txtUbicacion.setText(salida.getSku());
            viewHolder.txtUbicacionSalida.setText("" + salida.getUbicacion());
        }

        if (salida.getFaltante() > 0) {
            viewHolder.txtNumeroDePiezasSalida.setText(String.format("%.1f", salida.getFaltante()));
        } else {
            viewHolder.txtNumeroDePiezasSalida.setText(String.format("%.1f", salida.getCantidad()) + "");
        }

        return convertView;
    }

    public void check(boolean check) {
        if (!viewHolder.chkSelect.isChecked()) {
            viewHolder.chkSelect.setChecked(check);
        } else {
            viewHolder.chkSelect.setChecked(false);
        }
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

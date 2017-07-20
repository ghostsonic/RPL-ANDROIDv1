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
 * Created by Juan on 19/10/2016.
 */
public class SalidaSegundaAdapter extends ArrayAdapter<Salida> {

    ArrayList<Salida> arrayAdapter;
    Context context;
    ViewHolder viewHolder = null; // view lookup cache stored in tag
    boolean escaner = false;

    public SalidaSegundaAdapter(Context context, ArrayList<Salida> arrayAdapter) {
        super(context, R.layout.salida_list, arrayAdapter);
        this.arrayAdapter = arrayAdapter;
        this.context = context;
    }

    public SalidaSegundaAdapter(Context context, ArrayList<Salida> arrayAdapter, boolean escaner) {
        super(context, R.layout.salida_list, arrayAdapter);
        this.arrayAdapter = arrayAdapter;
        this.context = context;
        this.escaner = escaner;
    }

    private static class ViewHolder {
       TextView tvUbicacion;
        TextView tvAutorizada;
        TextView tvCantidadApoyo;
        TextView tvDescripcion;
        TextView tvCodigo;
        TextView tvUm;
        RelativeLayout linea;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Salida salida = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.salida_segunda_list, parent, false);
            viewHolder.tvAutorizada = (TextView) convertView.findViewById(R.id.tv_autorizada);
            viewHolder.tvCantidadApoyo = (TextView) convertView.findViewById(R.id.tv_apoyo_salida);
            viewHolder.tvCodigo = (TextView) convertView.findViewById(R.id.tv_codigo_salida);
            viewHolder.tvDescripcion = (TextView) convertView.findViewById(R.id.tv_descripcion_salida);
            viewHolder.tvUbicacion = (TextView) convertView.findViewById(R.id.tv_ubica);
            viewHolder.tvUm=(TextView) convertView.findViewById(R.id.tv_um);
            viewHolder.linea = (RelativeLayout) convertView.findViewById(R.id.lineaApoyo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvDescripcion.setText(salida.getDescripcion());
        viewHolder.tvCodigo.setText(salida.getSku());
        viewHolder.tvAutorizada.setText(""+salida.getAutorizado());
        viewHolder.tvUbicacion.setText(salida.getUbicacion());
        viewHolder.tvUm.setText(salida.getUnidadMedida());
        if(salida.getCantidadaApoyo() > 0){
            viewHolder.tvCantidadApoyo.setText(""+salida.getCantidadaApoyo());
        }else{
            viewHolder.tvCantidadApoyo.setText("0.0");
        }
        if(salida.getEstatus()==1){
            viewHolder.linea.setBackgroundColor(context.getResources().getColor(R.color.apoyoCompleto));
        }else{
            viewHolder.linea.setBackgroundColor(context.getResources().getColor(R.color.transparente));
        }
        return convertView;
    }



}

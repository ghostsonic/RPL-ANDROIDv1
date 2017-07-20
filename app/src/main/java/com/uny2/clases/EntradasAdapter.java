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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Juan on 04/04/2016.
 */
public class EntradasAdapter extends ArrayAdapter<Entrada> {

    private ArrayList<Entrada> arrayAdapter;
    private int tamanoLetra;
    private Context context;
    private ViewHolder viewHolder = null;

    public EntradasAdapter(Context context, ArrayList<Entrada> arrayAdapter) {
        super(context, R.layout.entrada_list, arrayAdapter);
        this.context = context;
        this.arrayAdapter = arrayAdapter;
        tamanoLetra = 0;
    }

    private static class ViewHolder {
        private TextView txtCodigoyUbicacion;
        private TextView txtDescripcion;
        private TextView txtConteoActual;
        private TextView txtContidadContada;
        private TextView txtUnidadMedida;
        private RelativeLayout linearItem;
        private CheckBox chkSelect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Entrada entrada = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.entrada_list, parent, false);
            viewHolder.chkSelect = (CheckBox) convertView.findViewById(R.id.chekList);
            viewHolder.txtCodigoyUbicacion = (TextView) convertView.findViewById(R.id.txtCodigoyUbicacion);
            viewHolder.txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
            viewHolder.txtConteoActual = (TextView) convertView.findViewById(R.id.txtNumeroDePiezas);
            viewHolder.linearItem = (RelativeLayout) convertView.findViewById(R.id.linearItem);
            viewHolder.txtUnidadMedida = (TextView) convertView.findViewById(R.id.txtUnidadMedida);
            viewHolder.txtContidadContada = (TextView) convertView.findViewById(R.id.txtCantidadRecolectada);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (tamanoLetra > 0) {
            viewHolder.txtDescripcion.setTextSize(tamanoLetra);
        }
        if (entrada.getEstatus().equalsIgnoreCase("1")) {
            viewHolder.chkSelect.setChecked(true);
            viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.colorSelectDisabled));
        } else {
            viewHolder.chkSelect.setChecked(false);
            viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.transparente));
        }

        if(entrada.getUnidadesContadas()>0){
            viewHolder.txtContidadContada.setText(String.format("%.1f", entrada.getUnidadesContadas()));
        }else{
            viewHolder.txtContidadContada.setText("0");
        }
        viewHolder.txtCodigoyUbicacion.setText(entrada.getSkuADO());
        viewHolder.txtDescripcion.setText(entrada.getDescripcion());
        viewHolder.txtConteoActual.setText(""+String.format("%.1f", entrada.getUnidades()));
        viewHolder.txtUnidadMedida.setText(entrada.getUnidadMedida());
        return convertView;
    }

    @Override
    public int getCount(){
        return arrayAdapter.size();
    }


}
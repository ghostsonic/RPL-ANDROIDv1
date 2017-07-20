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
 * Created by DanyCarreto on 13/04/16.
 */
public class UbicacionAdapter extends ArrayAdapter<Ubicacion> {

    ArrayList<Ubicacion> arrayAdapter;
    Context context;
    public UbicacionAdapter(Context context, ArrayList<Ubicacion> arrayAdapter) {
        super(context, R.layout.ubicacion_list,arrayAdapter);
        this.context = context;
        this.arrayAdapter = arrayAdapter;
    }

    private static class ViewHolder {
        TextView txtCodigoyUbicacion;
        TextView txtUbicacion;
        TextView txtDescripcion;
        TextView txtConteoActual;
        TextView txtUnidadMedida;
        TextView txtCantidadRecolectadaUbicacion;
        RelativeLayout linearItem;
        CheckBox chkSelect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ubicacion ubicacion = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.ubicacion_list, parent, false);
            viewHolder.chkSelect = (CheckBox) convertView.findViewById(R.id.chekList);
            viewHolder.txtCodigoyUbicacion = (TextView) convertView.findViewById(R.id.txtCodigoyUbicacion);
            viewHolder.txtUbicacion = (TextView) convertView.findViewById(R.id.txtUbicacion);
            viewHolder.txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
            viewHolder.txtConteoActual = (TextView) convertView.findViewById(R.id.txtNumeroDePiezas);
            viewHolder.linearItem = (RelativeLayout) convertView.findViewById(R.id.linearItem);
            viewHolder.txtUnidadMedida = (TextView) convertView.findViewById(R.id.txtUnidadMedida);
            viewHolder.txtCantidadRecolectadaUbicacion = (TextView) convertView.findViewById(R.id.txtCantidadRecolectadaUbicacion);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (ubicacion.getEstatus().equalsIgnoreCase("1")) {
            viewHolder.chkSelect.setChecked(true);
            viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.colorSelectDisabled));
        } else {
            viewHolder.chkSelect.setChecked(false);
            viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.transparente));
        }
        if(ubicacion.getUnidadesContadas()>0){
            viewHolder.txtCantidadRecolectadaUbicacion.setText(Double.toString(ubicacion.getUnidadesContadas()));
        }else{
            viewHolder.txtCantidadRecolectadaUbicacion.setText(Double.toString(0));
        }
        viewHolder.txtCodigoyUbicacion.setText("Código: "+ubicacion.getSkuADO());
        viewHolder.txtUbicacion.setText("Ubicación: "+ubicacion.getUbicacion());
        viewHolder.txtDescripcion.setText(ubicacion.getDescripcion());
        viewHolder.txtConteoActual.setText(Double.toString(ubicacion.getUnidades()));
        viewHolder.txtUnidadMedida.setText(ubicacion.getUnidadMedida());
        return convertView;
    }

    @Override
    public int getCount() {
        return arrayAdapter.size();
    }
}

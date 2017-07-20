package com.uny2.clases;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lachewendy.rpl_androidv1.R;
import com.uny2.clases.ConteoCiclico;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by La che Wendy on 23/02/2016.
 */
public class ConteoCiclicoAdapter extends ArrayAdapter<ConteoCiclico>{

    ArrayList<ConteoCiclico> arrayAdapter;
    int tamanoLetra;
    Context context;
    public ConteoCiclicoAdapter(Context context, ArrayList<ConteoCiclico> arrayAdapter) {
        super(context, R.layout.conteo_ciclico_list,arrayAdapter);
        this.context = context;
        this.arrayAdapter = arrayAdapter;
        tamanoLetra = 0;
    }

    public ConteoCiclicoAdapter(Context context, ArrayList<ConteoCiclico> arrayAdapter, int tamanoLetra){
        super(context,R.layout.conteo_ciclico_list,arrayAdapter);
        this.arrayAdapter = arrayAdapter;
        this.tamanoLetra = tamanoLetra;
    }


    private static class ViewHolder {
        TextView txtCodigoyUbicacion;
        TextView txtDescripcion;
        TextView txtConteoActual;
        RelativeLayout linearItem;
        CheckBox chkSelect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ConteoCiclico conteoCiclico = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.conteo_ciclico_list, parent, false);
            viewHolder.chkSelect = (CheckBox) convertView.findViewById(R.id.chekList);
            viewHolder.txtCodigoyUbicacion = (TextView) convertView.findViewById(R.id.txtCodigoyUbicacion);
            viewHolder.txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
            viewHolder.txtConteoActual = (TextView) convertView.findViewById(R.id.txtNumeroDePiezas);
            viewHolder.linearItem = (RelativeLayout) convertView.findViewById(R.id.linearItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(tamanoLetra > 0 ){
            viewHolder.txtDescripcion.setTextSize(tamanoLetra);
        }
        //System.out.println("ESTATUS"+conteoCiclico.getEstatus());
        if(conteoCiclico.getEstatus().equalsIgnoreCase("1")){
                viewHolder.chkSelect.setChecked(true);
                viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.colorSelectDisabled));
        }else{
            viewHolder.chkSelect.setChecked(false);
            viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.transparente));
        }
        if(conteoCiclico.getCantidad() == 0 ){
            viewHolder.txtConteoActual.setText(conteoCiclico.getCantidad()+"");
        }

        // Populate the data into the template view using the data object
        viewHolder.txtCodigoyUbicacion.setText(conteoCiclico.getSku()+" / "+conteoCiclico.getUbicacion());
        viewHolder.txtDescripcion.setText(conteoCiclico.getDescripcion());

        viewHolder.txtConteoActual.setText(String.format("%.1f", conteoCiclico.getCantidad()));
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getPosition(ConteoCiclico item) {
        return super.getPosition(item);
    }
}

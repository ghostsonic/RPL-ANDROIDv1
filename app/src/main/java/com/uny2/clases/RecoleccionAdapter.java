package com.uny2.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lachewendy.rpl_androidv1.R;

import java.util.ArrayList;

/**
 * Created by pc on 14/03/2016.
 */
public class RecoleccionAdapter extends ArrayAdapter<Recoleccion> {

    ArrayList<Recoleccion> arrayAdapter;
    Context context;
    ViewHolder viewHolder = null; // view lookup cache stored in tag
    boolean escaner = false;

    public RecoleccionAdapter(Context context, ArrayList<Recoleccion> arrayAdapter ) {
        super(context, R.layout.recoleccion_list,arrayAdapter);
        this.arrayAdapter = arrayAdapter;
        this.context = context;
    }
    public RecoleccionAdapter(Context context, ArrayList<Recoleccion> arrayAdapter, boolean escaner ) {
        super(context, R.layout.recoleccion_list,arrayAdapter);
        this.arrayAdapter = arrayAdapter;
        this.context = context;
        this.escaner = escaner;
    }

    private static class ViewHolder {
        TextView txtDescripcion;
        TextView txtUbicacion;
        TextView txtNumeroDePiezasRecoleccion;
        TextView txtCantidadRecolectada;
        TextView txtTextoRecolectada;
        TextView txtUbicacionRecoleccion;
        RelativeLayout linearItem;
        CheckBox chkSelect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Recoleccion recoleccion = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.recoleccion_list, parent, false);
            viewHolder.chkSelect = (CheckBox) convertView.findViewById(R.id.chekListRecoleccion);
            viewHolder.txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcionRecoleccion);
            viewHolder.txtUbicacion = (TextView) convertView.findViewById(R.id.txtCodigoyUbicacionRecoleccion);
            viewHolder.txtNumeroDePiezasRecoleccion = (TextView) convertView.findViewById(R.id.txtNumeroDePiezasRecoleccion);
            viewHolder.txtCantidadRecolectada = (TextView) convertView.findViewById(R.id.txtCantidadRecolectadaRecoleccion);
            viewHolder.linearItem = (RelativeLayout) convertView.findViewById(R.id.linearItemRecoleccion);
            viewHolder.txtTextoRecolectada = (TextView) convertView.findViewById(R.id.txtTextoRecolectada);
            viewHolder.txtUbicacionRecoleccion = (TextView) convertView.findViewById(R.id.txtUbicacionRecoleccion);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if(recoleccion.getEstatus()==1){
            viewHolder.chkSelect.setChecked(true);

           // if(recoleccion.getCantidad()>recoleccion.getCantidadRecolectada()){
            if(recoleccion.getFaltante()>0){
                viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.colorSelectIncomplete));
            }
            if(recoleccion.getCantidad()== 0){
                viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.colorSelectComplete));
            }
           /* if(recoleccion.getCantidad()<recoleccion.getCantidadRecolectada()){
                viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.colorSelectSemicomplete));
            }*/

        }else{
            viewHolder.chkSelect.setChecked(false);
            viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.transparente));
        }

        if(recoleccion.getEstatus()==2){
            viewHolder.chkSelect.setChecked(true);
            viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.seleccion_transparente));
//            viewHolder.chkSelect.setBackgroundColor(context.getColor(R.color.colorAccent));
        }


        if(escaner){
            viewHolder.txtDescripcion.setText(recoleccion.getDescripcion());
            viewHolder.txtNumeroDePiezasRecoleccion.setText(String.format("%.1f", recoleccion.getCantidad() ) + "");
            viewHolder.txtTextoRecolectada.setVisibility(View.GONE);
            viewHolder.txtCantidadRecolectada.setVisibility(View.GONE);
            viewHolder.txtUbicacionRecoleccion.setText(""+recoleccion.getUbicacion());
            //viewHolder.txtNumeroDePiezasRecoleccion.setVisibility(View.GONE);
            viewHolder.txtUbicacion.setText(recoleccion.getSku());
        }else{
            viewHolder.txtDescripcion.setText(recoleccion.getDescripcion());
            viewHolder.txtCantidadRecolectada.setText(""+recoleccion.getCantidadRecolectada());
            viewHolder.txtNumeroDePiezasRecoleccion.setText(String.format("%.1f", recoleccion.getCantidad()) + "");
            viewHolder.txtUbicacion.setText(recoleccion.getSku());
            viewHolder.txtUbicacionRecoleccion.setText("" + recoleccion.getUbicacion());
        }

        if (recoleccion.getFaltante() > 0) {
            viewHolder.txtNumeroDePiezasRecoleccion.setText(String.format("%.1f", recoleccion.getFaltante()));
        }else{
            viewHolder.txtNumeroDePiezasRecoleccion.setText(String.format("%.1f", recoleccion.getCantidad()) + "");
        }

        return convertView;
    }

    public void check(boolean check){
        if(!viewHolder.chkSelect.isChecked()){
            viewHolder.chkSelect.setChecked(check);
        }else{
            viewHolder.chkSelect.setChecked(false);
        }
    }

}

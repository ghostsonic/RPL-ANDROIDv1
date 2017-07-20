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
 * Created by Juan on 28/01/2017.
 */
public class RecoleccionNuevoAdapter  extends ArrayAdapter<Recoleccion> {

    ArrayList<Recoleccion> arrayAdapter;
    Context context;
    ViewHolder viewHolder = null; // view lookup cache stored in tag
    boolean escaner = false;

    public RecoleccionNuevoAdapter(Context context, ArrayList<Recoleccion> arrayAdapter ) {
        super(context, R.layout.recoleccion_nuevo_list,arrayAdapter);
        this.arrayAdapter = arrayAdapter;
        this.context = context;
    }
    public RecoleccionNuevoAdapter(Context context, ArrayList<Recoleccion> arrayAdapter, boolean escaner ) {
        super(context, R.layout.recoleccion_nuevo_list,arrayAdapter);
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
        TextView txtUbicacionPropiedad;
        TextView txtUbicacionConsigna;
        TextView txtCantidadPropiedad;
        TextView txtCantidadConsigna;
        RelativeLayout linearItem;
        CheckBox chkSelect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Recoleccion recoleccion = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.recoleccion_nuevo_list, parent, false);
            viewHolder.chkSelect = (CheckBox) convertView.findViewById(R.id.chekListRecoleccion);
            viewHolder.txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcionRecoleccion);
            viewHolder.txtUbicacion = (TextView) convertView.findViewById(R.id.txtCodigoyUbicacionRecoleccion);
            viewHolder.txtNumeroDePiezasRecoleccion = (TextView) convertView.findViewById(R.id.txtNumeroDePiezasRecoleccion);
            viewHolder.txtCantidadRecolectada = (TextView) convertView.findViewById(R.id.txtCantidadRecolectadaRecoleccion);
            viewHolder.linearItem = (RelativeLayout) convertView.findViewById(R.id.linearItemRecoleccion);
            viewHolder.txtTextoRecolectada = (TextView) convertView.findViewById(R.id.txtTextoRecolectada);
            viewHolder.txtUbicacionRecoleccion = (TextView) convertView.findViewById(R.id.txtUbicacionRecoleccion);
            viewHolder.txtUbicacionPropiedad = (TextView) convertView.findViewById(R.id.txtUbicacionPropiedad);
            viewHolder.txtUbicacionConsigna = (TextView)convertView.findViewById(R.id.txtUbicacionConsigna);
            viewHolder.txtCantidadPropiedad = (TextView) convertView.findViewById(R.id.txtCantidadUbicacionPropiedad);
            viewHolder.txtCantidadConsigna = (TextView) convertView.findViewById(R.id.txtCantidadUbicacionConsigna);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        System.out.println("Ubicacion Propiedad->"+recoleccion.getUbicacionP());
        System.out.println("Ubicacion Consigna->"+recoleccion.getUbicacionC());
        System.out.println("CAntidad Propiedad->"+recoleccion.getCantidadP());
        System.out.println("CAntidad Consigna->"+recoleccion.getCantidadC());

        viewHolder.txtUbicacionConsigna.setText(recoleccion.getUbicacionC());
        viewHolder.txtCantidadConsigna.setText(Double.toString(recoleccion.getCantidadC()));

        viewHolder.txtUbicacionPropiedad.setText(recoleccion.getUbicacionP());
        viewHolder.txtCantidadPropiedad.setText(Double.toString(recoleccion.getCantidadP()));
        /*if(recoleccion.getCantidadC() > 0){
        //if(!recoleccion.getUbicacionC().equals("")){
            //viewHolder.txtUbicacionConsigna.setText(recoleccion.getUbicacionC());
            //viewHolder.txtCantidadConsigna.setText(Double.toString(recoleccion.getCantidadC()));
            viewHolder.txtUbicacionConsigna.setTextColor(context.getResources().getColor(R.color.color_consigna));
            viewHolder.txtCantidadConsigna.setTextColor(context.getResources().getColor(R.color.color_consigna));//>
        }else{
           // viewHolder.txtUbicacionConsigna.setVisibility(View.GONE);
           // viewHolder.txtCantidadConsigna.setVisibility(View.GONE);
            viewHolder.txtUbicacionConsigna.setTextColor(context.getResources().getColor(R.color.transparente));//>
            viewHolder.txtCantidadConsigna.setTextColor(context.getResources().getColor(R.color.transparente));//>
        }

        if(recoleccion.getCantidadP() > 0){
        //if(!recoleccion.getCantidadP().equals("")){
            viewHolder.txtUbicacionPropiedad.setText(recoleccion.getUbicacionP());
            viewHolder.txtCantidadPropiedad.setText(Double.toString(recoleccion.getCantidadP()));
            viewHolder.txtUbicacionPropiedad.setTextColor(context.getResources().getColor(R.color.color_propiedad));//>
        }else{
           // viewHolder.txtUbicacionPropiedad.setVisibility(View.GONE);
           // viewHolder.txtCantidadPropiedad.setVisibility(View.GONE);
            viewHolder.txtUbicacionPropiedad.setTextColor(context.getResources().getColor(R.color.transparente));//>
            viewHolder.txtCantidadPropiedad.setTextColor(context.getResources().getColor(R.color.transparente));//>
        }
        if(recoleccion.getUbicacionC().equals("*") && recoleccion.getUbicacionP().equals("*")){
            viewHolder.txtUbicacionPropiedad.setText("Sin ubicación");
            viewHolder.txtUbicacionPropiedad.setTextColor(context.getResources().getColor(R.color.negro)); //>
        }*/

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
            viewHolder.txtNumeroDePiezasRecoleccion.setText(String.format("%.1f", recoleccion.getCantidad()) + " " + recoleccion.getUnidadMedida());
            viewHolder.txtTextoRecolectada.setVisibility(View.GONE);
            viewHolder.txtCantidadRecolectada.setVisibility(View.GONE);
            viewHolder.txtUbicacionRecoleccion.setText(""+recoleccion.getUbicacionP());
            //viewHolder.txtNumeroDePiezasRecoleccion.setVisibility(View.GONE);
            viewHolder.txtUbicacion.setText(recoleccion.getSku());
            if(!recoleccion.getUbicacionP().equals("*")){
                viewHolder.txtUbicacionPropiedad.setText("" + recoleccion.getUbicacionP());
                viewHolder.txtCantidadPropiedad.setText(""+recoleccion.getCantidadP());
                viewHolder.txtUbicacionPropiedad.setTextColor(context.getResources().getColor(R.color.color_propiedad));
                //viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.color_propiedad));
            } else
            if(!recoleccion.getUbicacionC().equals("*")){
                //viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.color_consigna));
                viewHolder.txtUbicacionConsigna.setText("" + recoleccion.getUbicacionC());
                viewHolder.txtCantidadConsigna.setText(""+recoleccion.getCantidadC());
                viewHolder.txtUbicacionConsigna.setTextColor(context.getResources().getColor(R.color.color_consigna));
            }
            else{
              viewHolder.txtUbicacionPropiedad.setText("Sin ubicacion");
                viewHolder.txtUbicacionPropiedad.setTextColor(context.getResources().getColor(R.color.negro));
            }

        }else{
            viewHolder.txtDescripcion.setText(recoleccion.getDescripcion());
            viewHolder.txtCantidadRecolectada.setText(""+String.format("%.1f", recoleccion.getCantidadRecolectada())+" "+recoleccion.getUnidadMedida());
            viewHolder.txtNumeroDePiezasRecoleccion.setText(String.format("%.1f", recoleccion.getCantidad()) + " " + recoleccion.getUnidadMedida());
            viewHolder.txtUbicacion.setText(recoleccion.getSku());
            //viewHolder.txtUbicacionPropiedad.setText("" + recoleccion.getUbicacionP());
            /*if(!recoleccion.getUbicacionP().equals("*")){
                viewHolder.txtUbicacionPropiedad.setText("" + recoleccion.getUbicacionP());
                viewHolder.txtCantidadPropiedad.setText(""+recoleccion.getCantidadP());
                // viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.color_propiedad));
                viewHolder.txtUbicacionPropiedad.setTextColor(context.getResources().getColor(R.color.color_propiedad));
            }else
            if(!recoleccion.getUbicacionC().equals("*")){
                viewHolder.txtUbicacionConsigna.setText("" + recoleccion.getUbicacionC());
                viewHolder.txtCantidadConsigna.setText(""+recoleccion.getCantidadC());
                //viewHolder.linearItem.setBackgroundColor(context.getResources().getColor(R.color.color_consigna));
                viewHolder.txtUbicacionConsigna.setTextColor(context.getResources().getColor(R.color.color_consigna));
            }

            else{
                viewHolder.txtUbicacionPropiedad.setText("Sin ubicacion");
                viewHolder.txtUbicacionPropiedad.setTextColor(context.getResources().getColor(R.color.negro));
            }*/

        }

        if (recoleccion.getFaltante() > 0) {
            viewHolder.txtNumeroDePiezasRecoleccion.setText(Double.toString(recoleccion.getFaltante())+ " "+recoleccion.getUnidadMedida());
        }else{
            viewHolder.txtNumeroDePiezasRecoleccion.setText(String.format("%.1f", recoleccion.getCantidad()) + " "+recoleccion.getUnidadMedida());
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

    @Override
    public int getCount() {
        return arrayAdapter.size();
    }
}

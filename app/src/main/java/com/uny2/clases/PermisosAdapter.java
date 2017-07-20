package com.uny2.clases;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lachewendy.rpl_androidv1.R;

import java.util.ArrayList;

/**
 * Created by DanyCarreto on 10/04/16.
 */
public class PermisosAdapter extends ArrayAdapter<Permisos> {

    ArrayList<Permisos> arrayAdapter;
    int tamanoLetra;
    Context context;
    public PermisosAdapter(Context context, ArrayList<Permisos> arrayAdapter) {
        super(context, R.layout.usuario_list,arrayAdapter);
        this.context = context;
        this.arrayAdapter = arrayAdapter;
        tamanoLetra = 0;
    }

    public PermisosAdapter(Context context, ArrayList<Permisos> arrayAdapter, int tamanoLetra){
        super(context,R.layout.usuario_list,arrayAdapter);
        this.arrayAdapter = arrayAdapter;
        this.tamanoLetra = tamanoLetra;
    }


    private static class ViewHolder {
        TextView txtCodigoyUbicacion;
        TextView txtDescripcion;
        ImageView imgHabilitado,icon_permiso;
        RelativeLayout linearItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Permisos permiso = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.usuario_list, parent, false);
            viewHolder.txtCodigoyUbicacion = (TextView) convertView.findViewById(R.id.txtCodigoyUbicacion);
            viewHolder.txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
            viewHolder.linearItem = (RelativeLayout) convertView.findViewById(R.id.linearItem);
            viewHolder.imgHabilitado = (ImageView) convertView.findViewById(R.id.txtNumeroDePiezas);
            viewHolder.icon_permiso = (ImageView) convertView.findViewById(R.id.chekList);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // Populate the data into the template view using the data object
        viewHolder.txtCodigoyUbicacion.setText(permiso.getModificadoPor());
        viewHolder.txtDescripcion.setText(permiso.getModulo());
        viewHolder.icon_permiso.setImageResource(R.drawable.listuser);
        // Return the completed view to render on screen
        return convertView;
    }

}

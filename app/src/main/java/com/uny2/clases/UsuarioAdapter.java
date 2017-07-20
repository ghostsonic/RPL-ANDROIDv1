package com.uny2.clases;

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
 * Created by Juan on 06/04/2016.
 */
public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    ArrayList<Usuario> arrayAdapter;
    int tamanoLetra;
    Context context;
    public UsuarioAdapter(Context context, ArrayList<Usuario> arrayAdapter) {
        super(context, R.layout.usuario_list,arrayAdapter);
        this.context = context;
        this.arrayAdapter = arrayAdapter;
        tamanoLetra = 0;
    }

    public UsuarioAdapter(Context context, ArrayList<Usuario> arrayAdapter, int tamanoLetra){
        super(context,R.layout.usuario_list,arrayAdapter);
        this.arrayAdapter = arrayAdapter;
        this.tamanoLetra = tamanoLetra;
    }


    private static class ViewHolder {
        TextView txtCodigoyUbicacion;
        TextView txtDescripcion;
        ImageView imgHabilitado;
        RelativeLayout linearItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Usuario usuario = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.usuario_list, parent, false);
            viewHolder.txtCodigoyUbicacion = (TextView) convertView.findViewById(R.id.txtCodigoyUbicacion);
            viewHolder.txtDescripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
            viewHolder.imgHabilitado = (ImageView) convertView.findViewById(R.id.txtNumeroDePiezas);
            viewHolder.linearItem = (RelativeLayout) convertView.findViewById(R.id.linearItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(usuario.getEstatus().equalsIgnoreCase("ACTIVO")){
            viewHolder.imgHabilitado.setImageResource(R.drawable.punto_verde);
        }else if(usuario.getEstatus().equalsIgnoreCase("Bloqueado")){
            viewHolder.imgHabilitado.setImageResource(R.drawable.punto_amarillo);
        }else if(usuario.getEstatus().equalsIgnoreCase("eliminado")){
            viewHolder.imgHabilitado.setImageResource(R.drawable.punto_rojo);
        }
        // Populate the data into the template view using the data object
        viewHolder.txtCodigoyUbicacion.setText(usuario.getNombre());
        viewHolder.txtDescripcion.setText(usuario.getPerfil());
        // Return the completed view to render on screen
        return convertView;
    }

}

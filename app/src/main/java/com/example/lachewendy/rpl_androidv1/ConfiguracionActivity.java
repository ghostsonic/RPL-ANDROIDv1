package com.example.lachewendy.rpl_androidv1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.uny2.clases.DatosConfiguracion;

import static com.example.lachewendy.rpl_androidv1.R.color.colorPrimary;

public class ConfiguracionActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText endpointED,autenticationED,consultaConteo,respuestaConteo;
    private ImageButton btnEndp,btnAuten,btnConsConte,btnResConte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_configuracion);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            this.setTitle("CONFIGURACIÓN");
            endpointED = (EditText) findViewById(R.id.txtEndpoint);
            autenticationED = (EditText) findViewById(R.id.txtAut);
            consultaConteo = (EditText) findViewById(R.id.txtConsultaConteo);
            respuestaConteo = (EditText) findViewById(R.id.txtRespConteo);
            btnEndp = (ImageButton) findViewById(R.id.btnEndpoin);
            btnAuten = (ImageButton) findViewById(R.id.btnAut);
            btnConsConte=(ImageButton) findViewById(R.id.btnConsulConte);
            btnResConte=(ImageButton) findViewById(R.id.btnRespConteo);
            btnEndp.setOnClickListener(this);
            btnAuten.setOnClickListener(this);
            btnResConte.setOnClickListener(this);
            btnConsConte.setOnClickListener(this);
            DatosConfiguracion datos = new DatosConfiguracion(getBaseContext());
            endpointED.setText(datos.getEndpoint());
            endpointED.setEnabled(false);
            autenticationED.setText(datos.getConsultaAutentificacion());
            autenticationED.setEnabled(false);
            consultaConteo.setText(datos.getConsultaConteo());
            consultaConteo.setEnabled(false);
            respuestaConteo.setText(datos.getResultadoConteo());
            respuestaConteo.setEnabled(false);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.btnEndpoin:
                 if(!endpointED.isEnabled()){
                     edConfig("Endpoint");
                 }else{
                     endpointED.setEnabled(false);
                     btnEndp.setBackground(getResources().getDrawable(R.drawable.ed));
                 }
                 break;
             case R.id.btnAut:
                 if(!autenticationED.isEnabled()){
                     edConfig("Autenticacion");
                 }else{
                     autenticationED.setEnabled(false);
                     btnAuten.setBackground(getResources().getDrawable(R.drawable.ed));
                 }
                 break;
             default:
                 break;
         }
    }

    private void edConfig(final String tpc){
        AlertDialog.Builder quitDialog
                = new AlertDialog.Builder(this);
        quitDialog.setTitle("¿Deseas editar esta configuración\n("+tpc+")?");

        quitDialog.setPositiveButton("SI", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                         if(tpc.equals("Endpoint")){
                             endpointED.setEnabled(true);
                             btnEndp.setBackground(getResources().getDrawable(R.drawable.oked));
                         }
                         if(tpc.equals("Autenticacion")){
                            autenticationED.setEnabled(true);
                            btnAuten.setBackground(getResources().getDrawable(R.drawable.oked));
                         }
            }});

        quitDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }
}

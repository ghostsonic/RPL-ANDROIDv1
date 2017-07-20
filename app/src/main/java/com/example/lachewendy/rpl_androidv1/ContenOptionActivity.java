package com.example.lachewendy.rpl_androidv1;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import static com.example.lachewendy.rpl_androidv1.R.color.colorPrimary;

public class ContenOptionActivity extends AppCompatActivity implements View.OnClickListener  {
    private FrameLayout optFm1;
    private EditText txtCodigoEntrada;
    private FrameLayout optFm2;
    private FrameLayout optFm3;
    private FrameLayout optFm4;
    private FrameLayout optFm5;
    private FrameLayout optFm6;
    private FrameLayout optFm7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        String opt = getIntent().getExtras().getString("Option");
        setContentView(R.layout.activity_conten_option);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       // Window window = this.getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.setStatusBarColor(this.getResources().getColor(colorPrimary));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
            optFm1 = (FrameLayout) findViewById(R.id.fmEntradas);
            optFm3 = (FrameLayout) findViewById(R.id.fmRecoleccion);
            optFm2 = (FrameLayout) findViewById(R.id.fmUbicacion);
            optFm4 = (FrameLayout) findViewById(R.id.fmConteo);
            optFm5 = (FrameLayout) findViewById(R.id.fmPerfiles);
            optFm6 = (FrameLayout) findViewById(R.id.fmConfig);
            optFm7 = (FrameLayout) findViewById(R.id.fmConfigSuper);
            if(opt.equals("1")){
                this.setTitle("ENTRADAS");
                optFm2.setVisibility(View.GONE);
                optFm3.setVisibility(View.GONE);
                optFm4.setVisibility(View.GONE);
                optFm5.setVisibility(View.GONE);
                optFm6.setVisibility(View.GONE);
                optFm7.setVisibility(View.GONE);
                txtCodigoEntrada = (EditText) findViewById(R.id.txtCodigoEntrada);
                txtCodigoEntrada.setEnabled(false);
            }else if(opt.equals("2")){
                this.setTitle("UBICACIÓN");
                optFm1.setVisibility(View.GONE);
                optFm3.setVisibility(View.GONE);
                optFm4.setVisibility(View.GONE);
                optFm5.setVisibility(View.GONE);
                optFm6.setVisibility(View.GONE);
                optFm7.setVisibility(View.GONE);
            }else if(opt.equals("3")){
                this.setTitle("RECOLECCIÓN");
                optFm2.setVisibility(View.GONE);
                optFm1.setVisibility(View.GONE);
                optFm4.setVisibility(View.GONE);
                optFm5.setVisibility(View.GONE);
                optFm6.setVisibility(View.GONE);
                optFm7.setVisibility(View.GONE);
            }else if(opt.equals("4")){
                this.setTitle("CONTEO CÍCLICO");
                optFm2.setVisibility(View.GONE);
                optFm3.setVisibility(View.GONE);
                optFm1.setVisibility(View.GONE);
                optFm5.setVisibility(View.GONE);
                optFm6.setVisibility(View.GONE);
                optFm7.setVisibility(View.GONE);
            }else if(opt.equals("5")){
                this.setTitle("PERFILES");
                optFm2.setVisibility(View.GONE);
                optFm3.setVisibility(View.GONE);
                optFm4.setVisibility(View.GONE);
                optFm1.setVisibility(View.GONE);
                optFm6.setVisibility(View.GONE);
                optFm7.setVisibility(View.GONE);
            }else if(opt.equals("6")){
                this.setTitle("CONFIGURACION");
                optFm2.setVisibility(View.GONE);
                optFm3.setVisibility(View.GONE);
                optFm4.setVisibility(View.GONE);
                optFm5.setVisibility(View.GONE);
                optFm1.setVisibility(View.GONE);
                optFm7.setVisibility(View.GONE);
            }else if(opt.equals("7")){
                this.setTitle("CONFIGURACION\nSUPERVISORES");
                optFm2.setVisibility(View.GONE);
                optFm3.setVisibility(View.GONE);
                optFm4.setVisibility(View.GONE);
                optFm5.setVisibility(View.GONE);
                optFm6.setVisibility(View.GONE);
                optFm1.setVisibility(View.GONE);
            }
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
            //@Override
           // public void onClick(View view) {
          //      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();
            //}
        //});
        }catch (Exception ad){
            System.out.println(ad.getMessage());
            ad.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }
    private int isOnOff=0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.activarScannEntradas:
                txtCodigoEntrada = (EditText) findViewById(R.id.txtCodigoEntrada);
                if(isOnOff==0){
                    txtCodigoEntrada.setEnabled(true);
                    txtCodigoEntrada.setText("");
                    txtCodigoEntrada.setHint("DIGITE CODIGO MANUAL...");
                    item.setIcon(android.R.drawable.button_onoff_indicator_off);
                    isOnOff=1;
                    return true;
                }else{
                    txtCodigoEntrada.setEnabled(false);
                    txtCodigoEntrada.setText("");
                    txtCodigoEntrada.setHint("ESCANEAR CODIGO...");
                    item.setIcon(android.R.drawable.button_onoff_indicator_on);
                    isOnOff=0;
                    return true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.switch_entradas, menu);
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }
        return super.onCreateOptionsMenu(menu);
    }
}

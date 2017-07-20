package com.example.lachewendy.rpl_androidv1;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.com.uny2.metodos.AlmacenesDbHelper;
import com.uny2.clases.DatosConfiguracion;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.lachewendy.rpl_androidv1.R.color.colorPrimary;

public class ScreenSplashActivity extends AppCompatActivity {
    private static final long SPLASH_SCREEN_DELAY = 3000;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //Obtencion de y asignacion de caracteristicas de la actividad
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setStatusBarColor(this.getResources().getColor(colorPrimary));
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_screen_splash);
            //TimerTask para las tareas que se realizaran desues del intervalo de tiempo transcurrido
            AlmacenesDbHelper dbHelper = new AlmacenesDbHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    SharedPreferences loginPrefs = getSharedPreferences(DatosConfiguracion.PREFERENCIAS_LOGIN, MODE_PRIVATE);
                    if(!loginPrefs.getString("usuario","").isEmpty()){
                        startActivity(new Intent().setClass(
                                ScreenSplashActivity.this,MenuRPLActivity.class));
                        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                        finish();
                    }else{
                        startActivity(new Intent().setClass(
                                ScreenSplashActivity.this,LoginActivity.class));
                        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                        finish();
                    }

                }
            };
            //Creacion y asignacion del timer que ejecutrara la tarea de task despues de 3 seg.
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
        }catch (Exception ad){
            System.out.println(ad.getMessage());
            ad.printStackTrace();
        }
    }
}

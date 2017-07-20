package com.com.uny2.metodos;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.lachewendy.rpl_androidv1.R;

/**
 * Created by Juan on 13/06/2016.
 */
public class Animacion {
    Context context;
    private Button button = null;
    private Animation fadeIn = null;
    private Animation fadeOut = null;
    public boolean isAnimate = false;
    // Listeners que detectan el fin de la animación
    private LocalFadeInAnimationListener myFadeInAnimationListener = new LocalFadeInAnimationListener();
    private LocalFadeOutAnimationListener myFadeOutAnimationListener = new LocalFadeOutAnimationListener();


    public Animacion(Context context, Button button){
        this.context = context;
        this.button = button;
    }

    private void launchOutAnimation() {
        button.startAnimation(fadeOut);
    }

    private void launchInAnimation() {
        button.startAnimation(fadeIn);
    }

    public void stopAnimations(){
        fadeIn.cancel();
        fadeOut.cancel();
        fadeIn.setAnimationListener(null);
        fadeOut.setAnimationListener(null);
        isAnimate = false;
    }

    public void starAnimations(){
        isAnimate = true;
        runAnimations();
    }

    public boolean isAnimated(){
        return isAnimate;
    }
    private void runAnimations() {
        //uso de las animaciones
        fadeIn = AnimationUtils.loadAnimation(this.context, R.anim.fadein);
        fadeIn.setAnimationListener(myFadeInAnimationListener );
        fadeOut = AnimationUtils.loadAnimation(this.context, R.anim.fadeout);
        fadeOut.setAnimationListener(myFadeOutAnimationListener );
        // And start
        launchInAnimation();
    }

    // Runnable que arranca la animación
    private Runnable mLaunchFadeOutAnimation = new Runnable() {
        public void run() {
            launchOutAnimation();
        }
    };

    private Runnable mLaunchFadeInAnimation = new Runnable() {
        public void run() {
            launchInAnimation();
        }
    };

    /**
     * Listener para la animacion del Fadeout
     *
     * @author moi
     *
     */
    private class LocalFadeInAnimationListener implements Animation.AnimationListener {
        public void onAnimationEnd(Animation animation) {
            button.post(mLaunchFadeOutAnimation);
        }
        public void onAnimationRepeat(Animation animation){
        }
        public void onAnimationStart(Animation animation) {
        }
    };

    /**
     * Listener de animación para el Fadein
     */
    private class LocalFadeOutAnimationListener implements Animation.AnimationListener {
        public void onAnimationEnd(Animation animation) {
            button.post(mLaunchFadeInAnimation);
        }
        public void onAnimationRepeat(Animation animation) {
        }
        public void onAnimationStart(Animation animation) {
        }
    };



}

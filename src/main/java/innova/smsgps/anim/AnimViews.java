package innova.smsgps.anim;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by USUARIO on 21/05/2016.
 */
public class AnimViews
{
    /**
     * Simple enumerable para las propiedades de animaciones..
     */
    public enum PROPIEDADES
    {
        TRANSLATION_Y   ("translationY"),
        TRANSLATION_X   ("translationX"),
        ROTATION        ("rotation"),
        ROTATION_X      ("rotationX"),
        ROTATION_Y      ("rotationY"),
        SCALE_X         ("scaleX"),
        SCALE_Y         ("scaleY"),
        PIVOT_X         ("pivotX"),
        PIVOT_Y         ("pivotY"),
        ALPHA           ("alpha");

        private String s;

        PROPIEDADES(String s)
        {
            this.s = s;
        }
        public String getString()
        {
            return s;
        }
    }

    /**
     * @param view View
     * @param isMostrar boolean
     * @param context Context
     */
    public void deslizadoVertical(View view,  Context context , boolean isMostrar)
    {
        String tipoAnimacion        = PROPIEDADES.TRANSLATION_Y.getString();
        int heigth                  = -getDimensionesWindow(context)[1];    // arriba hacia abajo = (-altura)
        view.setVisibility(View.VISIBLE);

        ObjectAnimator anim = ObjectAnimator.ofFloat(view, tipoAnimacion, 0f, heigth);
        if (!isMostrar) // Comprobamos si debemos ocultar o no..
            anim =  ObjectAnimator.ofFloat(view, tipoAnimacion, heigth, 0f);

        anim.setDuration(500).start();
        anim.start();
    }
    /**
     * Retorna simple array de enteros con las dimesiones de la pantalla (W/H)..
     * para la impresión de boletos zonales..
     * @param context Context
     * @return int[]
     * <ul>
     *     <li>   int[2] La función retornó las dimensiones de la pantalla correctamente..</li>
     * </ul>
     */
    private int[] getDimensionesWindow(Context context)
    {
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return new int[]{display.widthPixels, display.heightPixels};
    }


}


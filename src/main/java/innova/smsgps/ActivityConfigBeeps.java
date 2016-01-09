package innova.smsgps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import innova.smsgps.enums.IDSP1;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityConfigBeeps extends BaseActivity{

    Intent intent ;
    // Spinner pulsaciones..
    Spinner spinner1, spinner2, spinner3;
    List<String> listOpciones = new ArrayList<String>();
    IndicesBeep indicesBeep     = IndicesBeep.SINVALOR;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_config_beeps);

        spinner1    = (Spinner)findViewById(R.id.spinner1);
        spinner2    = (Spinner)findViewById(R.id.spinner2);
        spinner3    = (Spinner)findViewById(R.id.spinner3);
        cargarOpciones();
    }

    private void cargarOpciones()
    {

//        listOpciones.add(indicesBeep.getDescripcion());
        listOpciones.add(IndicesBeep.BEEP1.descripcion);
        listOpciones.add(IndicesBeep.BEEP2.descripcion);
        listOpciones.add(IndicesBeep.BEEP3.descripcion);
        ArrayAdapter<String> datosSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOpciones);
        datosSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(datosSpinner);
        spinner2.setAdapter(datosSpinner);
        spinner3.setAdapter(datosSpinner);

        spinner1.setSelection(getUltimaPosicion(datosSpinner, IDSP1.BEEP1));
        spinner2.setSelection(getUltimaPosicion(datosSpinner, IDSP1.BEEP2));
        spinner3.setSelection(getUltimaPosicion(datosSpinner, IDSP1.BEEP3));

    }




    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnGuardar:
                managerInfoMovil.setSpf1(IDSP1.BEEP1, getCodigoBeep(spinner1));
                managerInfoMovil.setSpf1(IDSP1.BEEP2, getCodigoBeep(spinner2));
                managerInfoMovil.setSpf1(IDSP1.BEEP3, getCodigoBeep(spinner3));

                managerUtils.imprimirToast(this, "Guardado con éxito." );
                break;

        }

        if (intent != null)
            startActivityForResult(intent, 1);
    }

    /**
     * Devolverá el código del beep en tipo de dato Int
     * @param spinner View
     * @return int
     */
    public int getCodigoBeep(Spinner spinner)
    {
        IndicesBeep indicesBeep = IndicesBeep.SINVALOR;
        return indicesBeep.getCodigo(spinner.getSelectedItem().toString());
    }

    /**
     * Simple función que retornará la ultima posición del array adapter
     * en el q se dejo..
     * @param datos Arrayadapter Spinner
     * @param idsp1 IDSP1
     * @return int
     */
    public int getUltimaPosicion(ArrayAdapter<String> datos, IDSP1 idsp1)
    {
        if (datos != null)
            return datos.getPosition(indicesBeep.getDescripcion(managerInfoMovil.getSPF1(idsp1)));

        return 0;
    }



    /**
     * Simple Enumerable interno para los
     * indices de serializados iguales al servidor Hp-IDSP1(enum)..
     */
    public enum IndicesBeep
    {
        BEEP1            (1, 1, "Asalto"),    // SIN USO
        BEEP2            (1, 2, "Accidente"),
        BEEP3            (1, 3, "Emergencia"),

        SINVALOR            (-1, 0, "Sin valor");



        int     indice;
        int     codigo;
        String  descripcion;

        /**
         * Simple constructor que recibira un parámetro entero.
         * @param codigo int valor que contendra los enumerables
         * @param descripcion String que servira de info y para casos especiales..
         */
        IndicesBeep(int indice, int codigo, String descripcion)
        {
            this.indice         = indice;
            this.codigo         = codigo;
            this.descripcion    = descripcion;
        }

        /**
         * Simples funciónes que retornara el valor de algún
         * enumerable.
         **/
        public int getIndiceInt() {
            return indice;
        }
        public String getIndiceStr()
        {
            return String.valueOf(indice);
        }

        public int getCodigoInt()
        {
            return codigo;
        }
        public String getCodigoStr()
        {
            return String.valueOf(codigo);
        }
        public String getDescripcion()
        {
            return descripcion;
        }

        /**
         * Simple función que recorrerá los valores de
         * los enumerables y retornará un idsp1
         * @param indice int se comparará con el Indice de los enumerables.
         * @param codigo int se comparará con el Código de los enumerables.
         * @param isPrincipal boolean indicará que tipo de enumerable regresar (Tipo o SubIndice)
         * @return
         * <ul>
         *     <li> WS_INDICE   : Indices..  </li>
         *     <li> SINVALOR    : -1    </li>
         * </ul>
         **/
        public IndicesBeep getIndices(int indice, int codigo, boolean isPrincipal)
        {
            for(IndicesBeep mIndice : IndicesBeep.values())
            {
                // Comprobamos si se requiere principal para setear el código a 0
                if (isPrincipal)
                    codigo = 0;

                // De está forma solo obtendremos los indices o subindices..
                if (indice == mIndice.getIndiceInt() && mIndice.getCodigoInt() == codigo)
                    return mIndice;
            }
            return SINVALOR;
        }

        /**
         * Simple función que recorrerá los valores de
         * los enumerables y retornará un idsp1
         * @return
         * <ul>
         *     <li> WS_INDICE   : Indices..  </li>
         *     <li> SINVALOR    : -1    </li>
         * </ul>
         **/
        public int getCodigo(String descripcion)
        {
            for(IndicesBeep mIndice : IndicesBeep.values())
            {

                // De está forma solo obtendremos los indices o subindices..
                if (descripcion.equals(mIndice.getDescripcion()))
                    return mIndice.getCodigoInt();
            }
            return SINVALOR.getCodigoInt();
        }

        /**
         * Retorna una descripción de los ultimos cambios
         * @param codigo int infomovil
         * @return String descripción
         */
        public String getDescripcion(int codigo)
        {
            for(IndicesBeep mIndice : IndicesBeep.values())
            {
                // De está forma solo obtendremos los indices o subindices..
                if (mIndice.getCodigoInt() == codigo)
                    return mIndice.getDescripcion();
            }
            return SINVALOR.getDescripcion();
        }
    }

    @Override
    public void listenerTimer() {

    }
}




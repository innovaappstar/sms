package innova.smsgps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import innova.smsgps.enums.IDSP2;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityListaContactos extends BaseActivity implements View.OnClickListener {

    Intent intent ;
    /**
     * DIÁLOGO DE EDICION
     **/
    Dialog dialog;
    EditText editDialogo;
    String contactoTemp = "";
    /**
     * Vistas editables
     **/
    EditText editContacto01;
    EditText editContacto02;
    EditText editContacto03;
    EditText editContacto04;
    int idEditContacto = 0;




    public static final int REQUEST_CONTACTOS = 1;
    public static final String ID_PETICION = "PETICION";
    public static final String COD_PETICION = "COD_PETICION";
    public static final int ID_EDITAR_CONTACTO= 2;
    public static final int ID_INSERTAR_CONTACTO = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_lista_contactos_apolo);
        editContacto01 = (EditText)findViewById(R.id.editContacto01);
        editContacto02 = (EditText)findViewById(R.id.editContacto02);
        editContacto03 = (EditText)findViewById(R.id.editContacto03);
        editContacto04 = (EditText)findViewById(R.id.editContacto04);
        cargarContactos();

    }
    //region ciclos
    @Override
    public void onResume()
    {
        super.onResume();

    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //endregion
    @Override
    public void listenerTimer() {

    }

    /**
     * Simple método para cargar de texto los edittext
     **/
    private void cargarContactos()
    {
        editContacto01.setText(managerInfoMovil.getSPF2(IDSP2.NUMERO01));
        editContacto02.setText(managerInfoMovil.getSPF2(IDSP2.NUMERO02));
        editContacto03.setText(managerInfoMovil.getSPF2(IDSP2.NUMERO03));
        editContacto04.setText(managerInfoMovil.getSPF2(IDSP2.NUMERO04));
    }


    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.btnAceptar:
                guardarNumeros();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
//                imprimitToast("Aceptar..");
                break;
            case R.id.contenedor_contactos:
                managerUtils.imprimirToast(getApplicationContext(), "contactos...");
                break;
            case R.id.inputContacto01:
                mostrarPrimerDialogo();
                contactoTemp = editContacto01.getText().toString();
                idEditContacto = 1;
                break;
            case R.id.inputContacto02:
                mostrarPrimerDialogo();
                contactoTemp = editContacto02.getText().toString();
                idEditContacto = 2;
                break;
            case R.id.inputContacto03:
                mostrarPrimerDialogo();
                contactoTemp = editContacto03.getText().toString();
                idEditContacto = 3;
                break;
            case R.id.inputContacto04:
                mostrarPrimerDialogo();
                contactoTemp = editContacto04.getText().toString();
                idEditContacto = 4;
                break;
            case R.id.contenedor_automatico:
//                managerUtils.imprimirToast(getApplicationContext(), "Automatico..");
                cerrarDialogo();
                mostrarSegundoDialogo(contactoTemp);
                break;
            case R.id.contenedor_manual:
//                managerUtils.imprimirToast(getApplicationContext(), "Manual..");
                cerrarDialogo();
                mostrarTercerDialogo();
                break;
            case R.id.btnSustituirContacto:
                Intent intent=new Intent(this, ActivityConfigListaContactos.class);
                startActivityForResult(intent, REQUEST_CONTACTOS);
                cerrarDialogo();
                break;
            case R.id.btnEliminarContacto:
                cerrarDialogo();
                getInputEditContactoX().setText("");
                break;
            case R.id.btnSustituirContactoManualmente:
                if (editDialogo.getText().toString().length() > 6)
                {
                    cerrarDialogo();
                    getInputEditContactoX().setText(editDialogo.getText().toString());
                }
                break;
            case R.id.btnEliminarContactoManualmente:
                editDialogo.setText("");
                cerrarDialogo();
                getInputEditContactoX().setText("");
                break;

        }

        //startActivity(intent);
    }

    private void mostrarPrimerDialogo()
    {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_pop_up_contacts_manually_automatically);
        ((RelativeLayout)dialog.findViewById(R.id.contenedor_automatico)).setOnClickListener(this);
        ((RelativeLayout)dialog.findViewById(R.id.contenedor_manual)).setOnClickListener(this);

        dialog.show();
    }
    private void mostrarSegundoDialogo(String NombreContacto)
    {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_pop_up_contacts_gestion);
        String datos = "Ha seleccionado al contacto \n" + NombreContacto + "\nQue acción desea realizar?.";
        ((TextView)dialog.findViewById(R.id.txtDetalleAplicativo)).setText(datos);
        ((Button)dialog.findViewById(R.id.btnSustituirContacto)).setOnClickListener(this);
        ((Button)dialog.findViewById(R.id.btnEliminarContacto)).setOnClickListener(this);

        dialog.show();
    }
    private void mostrarTercerDialogo()
    {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_pop_up_contacts_gestion_manually);
        editDialogo     = (EditText)dialog.findViewById(R.id.editContacto);
        editDialogo.setText(getInputEditContactoX().getText());
        Button btnAceptar   = (Button) dialog.findViewById(R.id.btnSustituirContactoManualmente);
        Button btnEliminar  = (Button) dialog.findViewById(R.id.btnEliminarContactoManualmente);
        btnAceptar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        if (getInputEditContactoX().getText().length() < 1)
            btnAceptar.setText("Aceptar");
        else
            btnAceptar.setText("Sustituir");

        dialog.show();
    }

    // Call Back method  to get the Message form other Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case REQUEST_CONTACTOS:
                if (resultCode != Activity.RESULT_OK)
                {

                    return;
                } else
                {
//                    mostrarListaContactos();
                    String numero = data.getStringExtra("MESSAGE");
                    managerUtils.imprimirToast(getApplicationContext(), numero);
                    getInputEditContactoX().setText(numero);

                }
                break;
        }
    }
    /**
     * Simple metodo para cerrar el diálogo una vez haya dejado de usarse...
     **/
    private void cerrarDialogo()
    {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    /**
     * Simple función que retornara el edittext seleccionado
     * para su gestion ...
     **/
    public EditText getInputEditContactoX()
    {
        EditText[] editArray = new EditText[]{editContacto01, editContacto02, editContacto03, editContacto04};
        for (int i = 0; i < editArray.length; i++)
        {
            if (idEditContacto == i + 1)
            {
//                editArray[i].setText(numero);
                return editArray[i];
            }
        }
        return editContacto01;  // NO TIENE PORQUE ENTRAR AQUI.,..
    }

    /**
     * Simple método para guardar los numeros que se encuentran en los edittext...
     **/
    private void guardarNumeros() {
        managerInfoMovil.setSpf2(IDSP2.NUMERO01, editContacto01.getText().toString());
        managerInfoMovil.setSpf2(IDSP2.NUMERO02, editContacto02.getText().toString());
        managerInfoMovil.setSpf2(IDSP2.NUMERO03, editContacto03.getText().toString());
        managerInfoMovil.setSpf2(IDSP2.NUMERO04, editContacto04.getText().toString());
    }


}




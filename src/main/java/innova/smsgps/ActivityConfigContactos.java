package innova.smsgps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import innova.smsgps.enums.IDSP2;
import innova.smsgps.infomovil.ManagerInfoMovil;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityConfigContactos extends Activity {
    EditText editContacto01;
    EditText editContacto02;
    EditText editContacto03;
    EditText editContacto04;
    Button   btnListar01;
    Button   btnListar02;
    Button   btnListar03;
    Button   btnListar04;


    RelativeLayout contentInputs;
    // Info
    ManagerInfoMovil managerInfoMovil = null;
    public static final int REQUEST_CONTACTOS = 1;
    // Indicador de contacto a agregar o eliminar
    int mIndicadorContacto  = 0;
    String TEXTO_LIMPIAR    = "Limpiar";
    String TEXTO_LISTAR     = "Listar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.config_lista_contactos);
        editContacto01 = (EditText) findViewById(R.id.editContacto01);
        editContacto02 = (EditText) findViewById(R.id.editContacto02);
        editContacto03 = (EditText) findViewById(R.id.editContacto03);
        editContacto04 = (EditText) findViewById(R.id.editContacto04);

        btnListar01     = (Button) findViewById(R.id.btnListar01);
        btnListar02     = (Button) findViewById(R.id.btnListar02);
        btnListar03     = (Button) findViewById(R.id.btnListar03);
        btnListar04     = (Button) findViewById(R.id.btnListar04);

        contentInputs   = (RelativeLayout)findViewById(R.id.contentContactosInput);

        managerInfoMovil = new ManagerInfoMovil(getApplicationContext());

    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case REQUEST_CONTACTOS:
                if (resultCode != Activity.RESULT_OK)
                {
                    // SI OCURRIO ALGÚN ERROR o Cancelo acción
                    //imprimitToast("RESULT error ");
                    return;
                } else
                {
                    String message = data.getStringExtra("MESSAGE");
                    //imprimitToast("RESULT " + message);
                    agregarNumero(false, message);
                    //Toast.makeText(this, "LEYENDO TARJETA", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * Simple método que agregará el número
     * de celular al input text dependiendo
     * del indicadorContacto.
     * @param number String que se insertará.
     */
    private void agregarNumero(boolean isLimpiar, String number)
    {
        String TextoButton = TEXTO_LIMPIAR;
        if (isLimpiar)
            TextoButton = TEXTO_LISTAR;

        switch (mIndicadorContacto)
        {
            case 1: // CONTACTO 01
                editContacto01.setText(number);
                btnListar01.setText(TextoButton);
                break;
            case 2: // CONTACTO 02
                editContacto02.setText(number);
                btnListar02.setText(TextoButton);
                break;
            case 3: // CONTACTO 03
                editContacto03.setText(number);
                btnListar03.setText(TextoButton);
                break;
            case 4: // CONTACTO 04
                editContacto04.setText(number);
                btnListar04.setText(TextoButton);
                break;
        }
    }

    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */

    public void onClickConfig(View view) {
        switch (view.getId()) {
            case R.id.btnNextContactos:
                if (editContacto01.getText().length() < 9 || editContacto02.getText().length() < 9 ||
                        editContacto03.getText().length() < 9 || editContacto04.getText().length() < 9) {
                    imprimitToast("Números incompletos!");
                    return;
                }
                String contacto01 = editContacto01.getText().toString();
                String contacto02 = editContacto02.getText().toString();
                String contacto03 = editContacto03.getText().toString();
                String contacto04 = editContacto04.getText().toString();

                managerInfoMovil.setSpf2(IDSP2.NUMERO01, contacto01);
                managerInfoMovil.setSpf2(IDSP2.NUMERO02, contacto02);
                managerInfoMovil.setSpf2(IDSP2.NUMERO03, contacto03);
                managerInfoMovil.setSpf2(IDSP2.NUMERO04, contacto04);
                imprimitToast("Se guardó correctamente!");
                // SALTAR A LA OTRA ACTIVIDAD
                Intent i = new Intent(ActivityConfigContactos.this, ActivityLoginFb.class);
                startActivity(i);
                finish();
                return;
            case R.id.btnListar01:
                mIndicadorContacto = 1;
                break;
            case R.id.btnListar02:
                mIndicadorContacto = 2;
                break;
            case R.id.btnListar03:
                mIndicadorContacto = 3;
                break;
            case R.id.btnListar04:
                mIndicadorContacto = 4;
                break;
        }
        // Comprobamos que el edittext no este agregado
        if(!isLimpiar(view))
        {
            Intent intent=new Intent(ActivityConfigContactos.this, ActivityConfigListaContactos.class);
            startActivityForResult(intent, REQUEST_CONTACTOS);
        }

    }

    /**
     * Simple booleano que nos indicará que acción
     * realizar.
     */
    private boolean isLimpiar (View view)
    {
        if (((Button)view).getText().toString().equals(TEXTO_LIMPIAR))
        {
            agregarNumero(true, "");
            return true;
        }
        return false;
    }


    private void imprimitToast(String data)
    {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}

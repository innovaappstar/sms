package innova.smsgps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import innova.smsgps.application.Globals;
import innova.smsgps.enums.ACTIONS;
import innova.smsgps.enums.IDSP2;


/**
 * Created by USUARIO on 10/11/2015.
 * Esta actividad solo servira para los casos
 * en los que se requiera recuperar la sesión de facebook... (1 o mas veces)
 */
public class ActivityAutenticationFacebook extends BaseActivity{


    /**Nombre de la key para el guardado de instancias. SavedInstance- OnRestoreInstance.*/
    private final String PENDING_ACTION_BUNDLE_KEY = "innova.smsgps:PendingAction";

    /**Enumrable para las acciones*/
    private ACTIONS pendingAction = ACTIONS.NONE;

    /**
     * Clase para guardar y restaurar las sesiones
     */
    private UiLifecycleHelper uiHelper;

    /**Class GraphUser para obtener sus datos.*/
    private GraphUser user;

    innova.smsgps.beans.Session sessionbeans = new innova.smsgps.beans.Session();
    /**Vistas simples*/
    private LoginButton btnLogin;

    /**Cambios de estados o sesiones, muestra Toast de:  'no se le concedio permisos' */
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != ACTIONS.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                        exception instanceof FacebookAuthorizationException)) {
            new AlertDialog.Builder(ActivityAutenticationFacebook.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = ACTIONS.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
//            handlePendingAction();
        }
        updateUI();
    }
    //region guardado y lectura de instancias guardadas.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, null);
        if (requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
//                String result=data.getStringExtra("result");
//                startActivity(new Intent(this, ActivityBienvenidaInfoAplicativo.class));
//                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                // se canceló por alguna razón...
            }
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = ACTIONS.valueOf(name);
        }

        setContentView(R.layout.activity_autentication_facebook);

        btnLogin = (LoginButton) findViewById(R.id.login_button);
        btnLogin.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                ActivityAutenticationFacebook.this.user = user;
                updateUI();
            }
        });

    }
    //region FACEBOOK
    /**
     * Callback para las sesiones.
     */
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            sessionbeans.setSession(session);
            Globals.setSession(session);
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();

    }
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void listenerTimer() {

    }

    /**Actualizaci�n de detalles de vista (Nombres , etc)*/
    private void updateUI() {
        sessionbeans.getSession().getActiveSession();

        if (user != null)
        {

//            managerUtils.imprimirToast(this, user.toString());
//            managerUtils.imprimirLog("Obteniendo datos de facebook ...\n" + user.toString());
            // enviamos un setResult
//            pictureProfile.setProfileId(user.getId());
//            uiHelper.onPause();
//            uiHelper.onDestroy();
            Globals.getInfoMovil().setSpf2(IDSP2.IDFACEBOOK, String.valueOf(user.getId()));
            startActivity(new Intent(ActivityAutenticationFacebook.this, ActivityMenu.class));
            finish();
        } else
        {
//            pictureProfile.setProfileId(null);
            managerUtils.imprimirLog("no se obtuvieron datos... --- datos de usuario null");
        }
    }


    /**Comprobamos si tenemos permiso de publicar */
    private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }
    //endregion

    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view) {
        switch (view.getId())
        {
            default:
                break;
        }
    }



    /**

     {
     "id":"483620641820436",
     "first_name":"Kenny",
     "timezone":-5,
     "verified":true,
     "name":"Kenny Baltazar Alanoca"
     ,"locale":"es_ES",
     "link":"https:\/\/www.facebook.com\/app_scoped_user_id\/483620641820436\/",
     "last_name":"Baltazar Alanoca",
     "gender":"male",
     "updated_time":"2015-12-05T00:28:36+0000"
     }
     */

}




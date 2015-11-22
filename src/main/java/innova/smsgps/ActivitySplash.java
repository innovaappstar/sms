package innova.smsgps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import innova.smsgps.application.Globals;
import innova.smsgps.enums.ACTIONS;

/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivitySplash extends Activity {

    /**Permisos de la api de Facebook (Publicar)*/
    private static final String PERMISOS = "publish_actions";

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
            new AlertDialog.Builder(ActivitySplash.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = ACTIONS.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            handlePendingAction();
        }
        updateUI();
    }
    /**
     * Comprueba si la session esta activa.
     */
    private void performPublish(ACTIONS action, boolean allowNoSession) {
        Session session = Session.getActiveSession();
        if (session != null) {
            pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
                handlePendingAction();
                return;
            } else if (session.isOpened()) {
                // We need to get new permissions, then complete the action when we get called back.
                session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISOS));
                //request.setPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_work_history"));

                return;
            }
        }

        if (allowNoSession) {
            pendingAction = action;
            handlePendingAction();
        }
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = ACTIONS.valueOf(name);
        }

        setContentView(R.layout.activity_splash);


        // SETEAMOS VISTAS
        btnLogin = (LoginButton) findViewById(R.id.login_button);
        btnLogin.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                ActivitySplash.this.user = user;
                updateUI();
                // It's possible that we were waiting for this.user to be populated in order to post a
                // status update.
                handlePendingAction();
            }
        });

    }

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
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    /**Manejado para acciones pendientes.*/

    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {
        ACTIONS previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = ACTIONS.NONE;

        switch (previouslyPendingAction) {
            case POST_PHOTO:
                postPhoto();
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }
    /**M�todo post status*/
    private void postStatusUpdate()
    {
        if (hasPublishPermission())
        {

        } else {
            pendingAction = ACTIONS.POST_STATUS_UPDATE;
        }
    }
    /**Actualizaci�n de detalles de vista (Nombres , etc)*/
    private void updateUI() {
        sessionbeans.getSession().getActiveSession();
        boolean enableButtons = (sessionbeans.getSession() != null && sessionbeans.getSession().isOpened());


        if (user != null) {
//            pictureProfile.setProfileId(user.getId());
            imprimitToast("Te damos la bienvenida \n" + user.getFirstName() + "\n a AlertaSms");
            startActivity(new Intent(this, ActivityBienvenidaInfoAplicativo.class));
            finish();
        } else {
//            pictureProfile.setProfileId(null);
        }
    }

    /**M�todo Post Image*/
    private void postPhoto() {
        if (hasPublishPermission()) {


        } else {
            pendingAction = ACTIONS.POST_PHOTO;
        }
    }

    /**Comprobamos si tenemos permiso de publicar */
    private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }

    /**
     * Evento que se ejecutará al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgSplashFacebook:
                imprimitToast("Autenticando...");
                break;
            case R.id.btnAceptar:
                imprimitToast("Aceptar..");
                break;
        }
    }
    private void imprimitToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}




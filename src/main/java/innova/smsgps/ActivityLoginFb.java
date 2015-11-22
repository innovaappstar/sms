package innova.smsgps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import innova.smsgps.application.Globals;
import innova.smsgps.enums.ACTIONS;

/**
 * Created by USUARIO on 10/11/2015.
 */



/**
 * Created by USUARIO on 08/11/2015.
 */
public class ActivityLoginFb extends Activity
{

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

    /**Vistas simples*/
    private LoginButton btnLogin;
    private ProfilePictureView pictureProfile;
    private TextView txtDetalleUsuario;
    innova.smsgps.beans.Session sessionbeans = new innova.smsgps.beans.Session();


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

    /**Cambios de estados o sesiones, muestra Toast de:  'no se le concedio permisos' */
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != ACTIONS.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                        exception instanceof FacebookAuthorizationException)) {
            new AlertDialog.Builder(ActivityLoginFb.this)
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


    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
        AppEventsLogger.activateApp(this);

        updateUI();
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

        setContentView(R.layout.fragment_fb);
        // SETEAMOS VISTAS
        btnLogin = (LoginButton) findViewById(R.id.login_button);
        pictureProfile = (ProfilePictureView)findViewById(R.id.profilePicture);

        txtDetalleUsuario = (TextView)findViewById(R.id.txtUsuario);


        btnLogin.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                ActivityLoginFb.this.user = user;
                updateUI();
                // It's possible that we were waiting for this.user to be populated in order to post a
                // status update.
                handlePendingAction();
            }
        });

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
    //endregion



    public void onClickConfig(View view)
    {
        switch (view.getId())
        {
            case R.id.postStatus:
                onClickPostStatusUpdate();
                break;
            case R.id.postPhoto:
                onClickPostPhoto();
                break;
            case R.id.btnNextProfile:
                // SALTAR A SIGUIENTE VENTANA
                Intent intent=new Intent(this, ActivityMenu.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**M�todo para comprobar si tenemos sesi�n activa y podemos publicar*/
    private void onClickPostStatusUpdate() {
        performPublish(ACTIONS.POST_STATUS_UPDATE, false);
    }

    /**M�todo post status*/
    private void postStatusUpdate() {
        if (hasPublishPermission()) {


        } else {
            pendingAction = ACTIONS.POST_STATUS_UPDATE;
        }
    }

    /**M�todo para comprobar si tenemos sesi�n activa y podemos publicar*/
    private void onClickPostPhoto() {
        performPublish(ACTIONS.POST_PHOTO, false);
    }

    /**M�todo Post Image*/
    private void postPhoto() {
        if (hasPublishPermission()) {

            Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon);
            Request request = Request.newUploadPhotoRequest(Session.getActiveSession(), image, new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    //showPublishResult(getString(R.string.photo_post), response.getGraphObject(), response.getError());
                    Toast.makeText(ActivityLoginFb.this, "Completado", Toast.LENGTH_SHORT).show();
                }
            });
            request.executeAsync();


        } else {
            pendingAction = ACTIONS.POST_PHOTO;
        }
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

    /**Actualizaci�n de detalles de vista (Nombres , etc)*/
    private void updateUI() {
        sessionbeans.getSession().getActiveSession();
        boolean enableButtons = (sessionbeans.getSession() != null && sessionbeans.getSession().isOpened());


        ((Button)findViewById(R.id.postStatus)).setEnabled(enableButtons);
        ((Button)findViewById(R.id.postPhoto)).setEnabled(enableButtons);

        if (enableButtons && user != null) {
            pictureProfile.setProfileId(user.getId());
            txtDetalleUsuario.setText("Te damos la bienvenida \n" + user.getFirstName() + "\n a AlertaSms");
            pictureProfile.setVisibility(View.VISIBLE);
            txtDetalleUsuario.setVisibility(View.VISIBLE);

        } else {
            pictureProfile.setProfileId(null);
            txtDetalleUsuario.setText(null);
        }
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

    /**Comprobamos si tenemos permiso de publicar */
    private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }

}

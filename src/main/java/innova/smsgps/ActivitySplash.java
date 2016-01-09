package innova.smsgps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import innova.smsgps.enums.IDSP2;
import innova.smsgps.task.UpLoginUsuario;
import innova.smsgps.task.UpLoginUsuario.LoginUsuarioCallback;
import innova.smsgps.utils.ManagerUtils;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivitySplash extends BaseActivity implements LoginUsuarioCallback{

    /**
     * Instancias HTTP y utils
     **/
    UpLoginUsuario objUpLoginUsuario = null;
    ManagerUtils managerUtils = null;

    /**
     * Matriz de valores...
     **/
    String[] params = new String[8];


    /**
     * Casos de actions php
     **/
    private static String caseLoginAPP          = "1";
    private static String caseLoginFacebook     = "2";

    /**
     * Valores estáticos...
     **/
    private static String passwordDefault   = "12345678910";


    /**
     * Vistas editables...
     **/
    EditText editNick ;
    EditText editPassword;



    /**Permisos de la api de Facebook (Publicar)*/
    //private static final String[] PERMISOS = "publish_actions";
    private static final String[] PERMISOS = new String[]{"publish_actions", "email" , "user_birthday", "user_location"};

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
//    /**
//     * Comprueba si la session esta activa.
//     */
//    private void performPublish(ACTIONS action, boolean allowNoSession)
//    {
//        Session session = Session.getActiveSession();
//        if (session != null) {
//            pendingAction = action;
//            if (hasPublishPermission()) {
//                // We can do the action right away.
//                handlePendingAction();
//                return;
//            } else if (session.isOpened()) {
//                // We need to get new permissions, then complete the action when we get called back.
//                session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISOS));
//                //request.setPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_work_history"));
//
//                return;
//            }
//        }
//
//        if (allowNoSession) {
//            pendingAction = action;
//            handlePendingAction();
//        }
//    }
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
                startActivity(new Intent(this, ActivityBienvenidaInfoAplicativo.class));
                finish();
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

        setContentView(R.layout.activity_splash_apolo);


        // SETEAMOS VISTAS
        editNick        = (EditText)findViewById(R.id.editNick);
        editPassword    = (EditText)findViewById(R.id.editPassword);

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

        // COMPROBAMOS EL FLAG DEL SPLASH
//        if (Globals.getInfoMovil().getSPF1(IDSP1.FLAGSPLASH) == 1)
//        {
//            startActivity(new Intent(this, ActivityMenu.class));
//            finish();
//        }

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
        objUpLoginUsuario   = null;
        objUpLoginUsuario   = new UpLoginUsuario(this);
        managerUtils        = null;
        managerUtils        = new ManagerUtils();

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

        if (user != null) {
//            pictureProfile.setProfileId(user.getId());
            Globals.getInfoMovil().setSpf2(IDSP2.IDFACEBOOK, String.valueOf(user.getId()));
            //imprimitToast("Te damos la bienvenida \n" + user + "\n a AlertaSms");
            params = new String[8];

            params[0] = caseLoginFacebook;
            params[1] = user.getId();
            params[2] = passwordDefault;
            params[3] = user.getFirstName();
            params[4] = user.getLastName();
            params[5] = user.asMap().get("gender").toString();
            params[6] = managerUtils.getCountry(this);
            params[7] = user.getId();

            runAsyntask(params);
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
    //endregion

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
            case R.id.inputTextCrearCuenta:
                Intent i = new Intent(this, ActivityRegistroUsuario.class);
                startActivityForResult(i, 1);
                break;
            case R.id.btnAceptar:
                iniciarAutenticacion();
//                imprimitToast("Aceptar..");
                break;
        }
    }


    /**
     * Simple método para iniciar el proceso de Autenticación..
     * @param
     **/
    private void iniciarAutenticacion ()
    {
        params = new String[3];
        params[0] = caseLoginAPP;
        params[1] = editNick.getText().toString();
        params[2] = editPassword.getText().toString();

        runAsyntask(params);
    }

    private void runAsyntask(String[] params)
    {
        objUpLoginUsuario.cancel(true);
        objUpLoginUsuario = null;
        objUpLoginUsuario = new UpLoginUsuario(this);
        objUpLoginUsuario.execute(params);

//        if (objUpLoginUsuario.getStatus()  != AsyncTask.Status.FINISHED)
//        {
//            // Cancel the task
//            objUpLoginUsuario.cancel(true);
//        }
//
//        if (objUpLoginUsuario.getStatus() == AsyncTask.Status.FINISHED)
//        {
//            objUpLoginUsuario = new UpLoginUsuario(this);
////            // Execute the task
////            AsyncTask task = new UpLoginUsuario(this).execute(params);
//        }
//
//        if (objUpLoginUsuario.getStatus() != AsyncTask.Status.RUNNING) {
//            objUpLoginUsuario.execute(params);
//        }

    }

    private void imprimitToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    /**
     * Valores de retorno
     * <ul>
     *     <li> 1 : login correcto </li>
     *     <li> 0 : login incorrecto </li>
     *     <li> 2 : Registro de usuario correcto con facebook</li>
     *     <li> -1 : Ocurrió un error insertar registro de usuario con facebook </li>
     * </ul>
     **/
    @Override
    public void LoginUsuarioExecute(int resultado)
    {
        if ( resultado == 1 || resultado == 2)    // LOGIN CORRECTO
        {
            // guardamos el nickusuario para mas adelante enviarlo en sus alertas
            managerInfoMovil.setSpf2(IDSP2.NICKUSUARIO, params[1]);
            startActivity(new Intent(this, ActivityBienvenidaInfoAplicativo.class));
            finish();
        }else
        {
            imprimitToast("Autenticación fallida.");
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




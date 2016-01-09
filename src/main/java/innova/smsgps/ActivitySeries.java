package innova.smsgps;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivitySeries extends BaseActivity{

    Intent intent ;
    WebView myWebView ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_series);


        myWebView = (WebView) this.findViewById(R.id.myWebView);
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // Handle the error
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        if (savedInstanceState != null)
            myWebView.restoreState(savedInstanceState);
        else
        {
            String src = "http://hqq.tv/player/embed_player.php?vid=AN1AYKUMOMSG";
            iniciarWeb(src);
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        myWebView.saveState(outState);
    }
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

    @Override
    public void listenerTimer() {

    }

    /**
     * Simple método para mostrar el contenido html dentro
     * de un webview..
     **/
    private void iniciarWeb(String src)
    {
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        managerUtils.imprimirToast(this, metrics.heightPixels + "|" + metrics.widthPixels);

        // WebViewの設定
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT > 7)
            settings.setPluginState(WebSettings.PluginState.ON);

        String html = "";
        html += "<html><body bgcolor=\"#000000\">";
//        html += "<h1><center>Bienvenido</center></h1>";
//        <IFRAME SRC="http://vidxtreme.to/embed-n7mvxqrvv9dg.html" FRAMEBORDER=0 MARGINWIDTH=0 MARGINHEIGHT=0 SCROLLING=NO WIDTH=640 HEIGHT=360 allowfullscreen></IFRAME>
//            html += "<iframe src=\""+ src +"\" width=\"100%\" height=\"100%\" frameborder=\"0\" scrolling=\"no\" allowfullscreen> </iframe>";
//        html += "<iframe src=\""+ src +"\" width=\"100%\" height=\"100%\" frameborder=\"0\" allowfullscreen> </iframe>";
//        html += "<iframe src=\"http://hqq.tv/player/embed_player.php?vid=AN1AYKUMOMSG\" width=\"100%\" height=\"100%\" scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" allowfullscreen=\"true\"></iframe>";
        html += "<iframe src=\"http://hqq.tv/player/embed_player.php?vid=AN1AYKUMOMSG\" width=\"100%\" height=\"100%\" scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" allowfullscreen=\"true\"></iframe>";

        //        html += "<iframe align=\"left\" width=\"900\" height=\"700\" frameborder=\"0\" hspace=\"0\" marginheight=\"0\" marginwidth=\"0\" vspace=\"0\" src=\"http://peliculasaudiolatino.com/show/powvideo.php?url=7gj6zaw66oit\"></iframe>";

        html += "</body></html>";
        myWebView.loadData(html, "text/html", "UTF-8");


    }


    private void initWebView(String src) {

        // WebViewの設定
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT > 7)
            settings.setPluginState(WebSettings.PluginState.ON);

        String html = "";
        html += "<html><body>";
        html += "<iframe src=\""+ src +"\" width=\"100%\" height=\"100%\" frameborder=\"0\" scrolling=\"no\"> </iframe>";
//        html += "<iframe src=\"http://streamplay.to/embed-017x4y1nypfh-740x360.html\" width=\"560\" height=\"315\" frameborder=\"0\" scrolling=\"no\"> </iframe>";
        //        html += "<iframe align=\"left\" width=\"900\" height=\"700\" frameborder=\"0\" hspace=\"0\" marginheight=\"0\" marginwidth=\"0\" vspace=\"0\" src=\"http://peliculasaudiolatino.com/show/powvideo.php?url=7gj6zaw66oit\"></iframe>";

        html += "</body></html>";


        myWebView.loadData(html, "text/html", null);
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
            case R.id.btnAceptar:
//                imprimitToast("Aceptar..");
                return;
            case R.id.buttonClick:
                String src = "http://hqq.tv/player/embed_player.php?vid=AN1AYKUMOMSG";
//                initWebView(src);
                iniciarWeb(src);
                break;
            case R.id.contenedor_add_facebook:
                intent = new Intent(this, ActivityFacebookAccount.class);
                break;
            case R.id.contenedor_mapa_alertas:
                intent = new Intent(this, ActivityMapaAlertas.class);
                break;
        }
    }


}




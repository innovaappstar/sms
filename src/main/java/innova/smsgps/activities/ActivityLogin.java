package innova.smsgps.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import innova.smsgps.R;


/**
 * Created by USUARIO on 10/11/2015.
 */
public class ActivityLogin extends BaseActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

    }


    @Override
    public void listenerTimer()
    {
    }


    /**
     * evento onClick
     * @param view View
     */
    public void onClick(View view)
    {
    }


}




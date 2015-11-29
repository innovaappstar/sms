package innova.smsgps;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import innova.smsgps.adapters.AdaptadorPaginasConfiguracion;


/**
 * Created by innovaapps on 30/10/2015.
 */
public class ManagerFragmentsConfiguracion extends FragmentActivity
{
    /**
     * EL WIDGET VIEW PAGER (VISTA)
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.manager_fragments_sms);
        // INICIAMOS LAS INSTANCIAS
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(new AdaptadorPaginasConfiguracion(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // UNIMO TABS AL VIEW PAGER
        tabsStrip.setViewPager(mViewPager);
        tabsStrip.setVisibility(View.GONE);
    }


    /**
     * Evento que se ejecutarA al dar click
     * en los botones.
     * @param view View que identificaremos ..
     */
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnAceptar:
                imprimitToast("agregar...");
                break;
            case R.id.txtLeerMas:
                imprimitToast("leer mAs...");
                break;

        }
    }

    //region BOTON BACK
    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0)
        {
            super.onBackPressed();
        }else
        {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() -1);
        }
    }
    //endregion
    private void imprimitToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
}

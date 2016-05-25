package innova.smsgps.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by USUARIO on 21/05/2016.
 * Listener de editText's
 */
public class EditTextListener extends EditText
{

    private static EditTextListenerCallback editTextListenerCallback;

    public interface EditTextListenerCallback
    {
        void onAfterTextChanged(EditText editText, String texto, int tamanio);
    }

    public static void setOnTextListener(EditTextListenerCallback editTextListenerCallback)
    {
        EditTextListener.editTextListenerCallback = editTextListenerCallback;
    }

    //region ciclos default
    public EditTextListener(Context context) {
        super(context);
        this.textChangedListener();
    }

    public EditTextListener(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.textChangedListener();
    }

    public EditTextListener(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.textChangedListener();
    }

    public EditTextListener(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.textChangedListener();
    }
    //endregion

    public void textChangedListener()
    {
        this.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String texto = EditTextListener.this.getText().toString();
                int tamanio = EditTextListener.this.getText().toString().length();
                if (editTextListenerCallback != null)
                    editTextListenerCallback.onAfterTextChanged(EditTextListener.this, texto, tamanio);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }


    /*
    public void textOnTouchListener(OnTouchListener onTouchListener)
    {
        this.setOnTouchListener(onTouchListener);
        this.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (editTextListenerCallback != null)
                    editTextListenerCallback.onTouchListener(v, event);
                return true;
            }
        });
    }
    */

}

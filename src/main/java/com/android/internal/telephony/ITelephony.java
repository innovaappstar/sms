package com.android.internal.telephony;

/**
 * Created by USUARIO on 15/01/2016.
 */
public interface ITelephony
{
    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
}

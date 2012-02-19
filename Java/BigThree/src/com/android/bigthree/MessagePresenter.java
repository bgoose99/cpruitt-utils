package com.android.bigthree;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class MessagePresenter
{
    /**
     * Show a simple error dialog with an OK button.
     * 
     * @param context
     * @param title
     * @param message
     */
    public static void showErrorDialog( Context context, String title,
            String message )
    {
        AlertDialog dialog = new AlertDialog.Builder( context ).create();
        dialog.setTitle( title );
        dialog.setMessage( message );
        dialog.setButton( "OK", new DialogInterface.OnClickListener()
        {
            public void onClick( DialogInterface dialog, int which )
            {
                dialog.dismiss();
            }
        } );
        dialog.show();
    }

    /**
     * Show a simple toast message with length = Toast.LENGTH_SHORT.
     * 
     * @param context
     * @param message
     */
    public static void showToastMessage( Context context, String message )
    {
        showToastMessage( context, message, Toast.LENGTH_SHORT );
    }

    /**
     * Show a simple toast message with custom length.
     * 
     * @param context
     * @param message
     * @param duration
     */
    public static void showToastMessage( Context context, String message,
            int duration )
    {
        Toast toast = Toast.makeText( context, message, duration );
        toast.show();
    }
}

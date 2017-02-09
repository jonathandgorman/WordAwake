package com.sonido.sonido;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SpinnerDialogFragment extends DialogFragment{

    Dialog spinnerDialog;

    public SpinnerDialogFragment(){};

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        spinnerDialog = new ProgressDialog(getActivity());
        this.setStyle(STYLE_NO_TITLE, getTheme()); // You can use styles or inflate a view
        spinnerDialog.setCancelable(false);

        return spinnerDialog;
    }

}


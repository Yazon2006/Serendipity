package com.example.bel.softwarefactory.utils;

import android.app.Dialog;
import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.bel.softwarefactory.R;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class AlertDialogHelper {

    @RootContext
    void setContext(Context context){
        this.context = context;
    }

    protected Context context;

    private MaterialDialog dialog;

    public Dialog showMessage(String message, MaterialDialog.ButtonCallback clickListener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        dialog = builder.title(context.getString(R.string.Attention)).content(message).positiveText(android.R.string.ok).callback(clickListener).build();
        return showDialog(dialog);
    }

    public Dialog showMessage(String message) {
        return showMessage(message, null);
    }

    public Dialog showMessage(int messageResId, MaterialDialog.ButtonCallback clickListener) {
        return showMessage(
                context.getString(messageResId),
                clickListener
        );
    }

    public Dialog showMessage(int messageResId) {
        return showMessage(messageResId, null);
    }

    public Dialog showError(String message, MaterialDialog.ButtonCallback clickListener) {
        return showMessage(message, clickListener);
    }

    public Dialog showError(int messageId) {
        return showMessage(messageId);
    }

    public Dialog showError(String message) {
        return showError(message, null);
    }

    public Dialog showError(Exception e, MaterialDialog.ButtonCallback clickListener) {
        return showError(e.getMessage(), clickListener);
    }

    public Dialog showError(Exception e) {
        return showError(e, null);
    }

    public void dismissDialog() {
        if (dialog == null)
            return;

        dialog.dismiss();
        dialog = null;
    }

    public Dialog showDialog(MaterialDialog dialog) {
        dismissDialog();

        this.dialog = dialog;
        dialog.show();
        return dialog;
    }

}

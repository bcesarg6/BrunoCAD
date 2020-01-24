package com.example.brunocad.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.brunocad.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogAjuda extends AlertDialog.Builder {

    private AlertDialog dialog;

    @Override
    public AlertDialog create() {
        this.dialog = super.create();
        return this.dialog;
    }

    public DialogAjuda(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_ajuda, null);
        setCancelable(true);

        ButterKnife.bind(this, view);

        setView(view);
    }

    @OnClick(R.id.btn_fechar)
    void fecharDialog(){
        this.dialog.dismiss();
    }
}
